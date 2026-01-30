package org.ayed.tda.aestrella;

import org.ayed.tda.colaPrioridad.ColaPrioridad;
import org.ayed.tda.comparador.*;
import org.ayed.tda.conjunto.Conjunto;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.grafo.Grafo;
import org.ayed.tda.lista.Pila;
import java.util.Map;


/**
 * Implementación del algoritmo A* para hallar el camino mínimo entre dos vértices de un grafo.
 * 
 * @param <T> Tipo de dato utilizado para representar los vértices.
 */
public class AEstrella<T> {
    
    private final Grafo<T> grafo;
    private final Heuristica<T> heuristica;
    private final Comparador<CostosVertice<T>> comparadorCostos;
    private ColaPrioridad<CostosVertice<T>> setAbierto;
    private Conjunto<T> setCerrado;
    private Diccionario<T, CostosVertice<T>> costos;     //Estructura auxiliar para trabajar con los costos de los vertices.
    private T destino; 


    /**
     * Constructor de clase AEstrella a partir de un grafo y una heurística.
     *
     * @param grafo      Grafo sobre el cual se ejecutarán las búsquedas.
     * @param heuristica Heurística utilizada para estimar la distancia al destino.
     */
    public AEstrella(Grafo<T> grafo, Heuristica<T> heuristica) {
        this.grafo = grafo;
        this.heuristica = heuristica;
        this.comparadorCostos = new ComparadorVertice<T>(); 
        this.setAbierto = new ColaPrioridad<CostosVertice<T>>(this.comparadorCostos);
        this.setCerrado = new Conjunto<T>(100);            
        this.costos = new Diccionario<T, CostosVertice<T>>(100);   //Falta ver dimensiones del grafo
        this.destino = null;
    }

    /**
     * Ejecuta el algoritmo A* para hallar el camino mínimo entre un vértice origen
     * y un vértice destino.
     * 
     * @param origen    Vértice inicial.
     * @param destino   Vértice objetivo.
     * @return          Objeto tipo pila con los vértices del camino mínimo de origen a destino.
     *                  Null si no existe camino.
     * @throws ExcepcionAEstrella  si los vertices origen o destino no existen o son invalidos.
     *                             si no existe un camino minimo entre ellos.
     */
    public Pila<T> buscarCaminoMinimo (T origen, T destino){

        if (origen == null || destino == null || !grafo.tieneVertice(origen) || !grafo.tieneVertice(destino)) {
            throw new ExcepcionAEstrella("Origen y/o destino inválidos.");
        }

        inicializarBusqueda(origen, destino);
        boolean destinoEncontrado = false;
        
        while (!setAbierto.vacio() && !destinoEncontrado) {
            
            CostosVertice<T> costoActual = setAbierto.eliminar();
            T verticePrometedor = costoActual.obtenerVertice();
            
            if (verticePrometedor.equals(destino)) {
                destinoEncontrado = true;
            }
            else if (!setCerrado.contiene(verticePrometedor)) {               

                setCerrado.agregar(verticePrometedor); 
                explorarVecinos(verticePrometedor, costoActual);
            }
        }
        if (destinoEncontrado) {
            return reconstruirCamino();
        } else {
            throw new ExcepcionAEstrella("No existe camino entre origen y destino.");
        }
    }


    /**
     * Recorre los vecinos de un vértice y actualiza los costos
     * correspondientes en caso de encontrar rutas más económicas.
     *
     * @param vertice      Vértice actualmente explorado.
     * @param costoActual  Costos asociados al vértice explorado.
     */
    private void explorarVecinos(T vertice, CostosVertice<T> costoActual) {
       
        int costoReal = costoActual.obtenerCostoReal();
        Map<T, Integer> vecinos = grafo.obtenerAdyacentes(vertice);

        for (T vecino : vecinos.keySet()) {
            if (!setCerrado.contiene(vecino)) {
                int pesoArista = grafo.obtenerArista(vertice, vecino); 
                int nuevoCostoReal = costoReal + pesoArista;
                procesarVecino(vecino, vertice, nuevoCostoReal);
            }
        }
    }

    /**
    * Actualiza los costos y el padre de un vecino si se encuentra un camino más económico.
    * Crea un NUEVO objeto CostosVertice cada vez para evitar problemas con el heap.
    *
    * @param vecino         Vértice vecino a actualizar.
    * @param padre          Vértice padre desde el que se llega al vecino.
    * @param nuevoCostoReal Costo real acumulado hasta este vecino.
    */
    private void procesarVecino(T vecino, T padre, int nuevoCostoReal) {
        
        CostosVertice<T> costoVecino = costos.obtenerValor(vecino);
        
        // Solo procesamos si es un camino nuevo o mejor que el anterior
        if (costoVecino == null || nuevoCostoReal < costoVecino.obtenerCostoReal()) {
            int costoHeuristica = heuristica.calcularPuntaje(vecino, destino);
            int nuevoCostoTotal = nuevoCostoReal + costoHeuristica;

            // SIEMPRE crear un nuevo objeto para evitar problemas con el heap
            CostosVertice<T> nuevoCosto = new CostosVertice<T>(vecino, nuevoCostoReal, nuevoCostoTotal, padre);
            costos.agregar(vecino, nuevoCosto);  // Actualiza el diccionario
            setAbierto.agregar(nuevoCosto);       // Agrega a la cola (puede haber duplicados)
        }
    }

    /**
     * Inicializa las estructuras para comenzar la búsqueda A*: 
     *      - Resetea las 2 estructuras principales (Set abierto y cerrado)
     *      - Resetea la estructura auxiliar costos.
     *      - Agrega el origen al set abierto.
     * 
     * NOTA:  la clase AEstrella se instancia una sola vez por GPS (Clase que la usa).
     *        Elegi "resetear" los sets cerrado y abierto, y el diccionario de costos
     *        mediante este metodo, para que en cada llamado de la busqueda no se arrastre 
     *        datos anteriores, sin la necesidad de crear el objeto AEstrella desde cero. 
     *
     * @param origen  Vértice inicial de la búsqueda.
     * @param destino Vértice objetivo de la búsqueda.
     */
    private void inicializarBusqueda(T origen, T destino) {

        this.destino = destino;
        this.costos = new Diccionario<T, CostosVertice<T>>(100);
        this.setCerrado = new Conjunto<T>(100);
        this.setAbierto = new ColaPrioridad<CostosVertice<T>>(this.comparadorCostos);
        
        int heuristicaOrigen = heuristica.calcularPuntaje(origen, destino); 
        CostosVertice<T> costoOrigen = new CostosVertice<>(origen, 0, heuristicaOrigen, null);   
        
        costos.agregar(origen, costoOrigen);
        setAbierto.agregar(costoOrigen);
    }

    /**
     * Reconstruye el camino mínimo desde el destino hasta el origen utilizando
     * los padres registrados en el diccionario de costos. 
     * Utiliza una pila para conservar el orden real de recorrido.
     *
     * @return  Pila de vértices que representan el camino mínimo de origen a destino.
     */
    private Pila<T> reconstruirCamino() {
        
        Pila<T> camino = new Pila<T>();
        T actual = destino;
        while (actual != null) {
            camino.agregar(actual);
            actual = costos.obtenerValor(actual).obtenerPadre(); 
        }
        return camino;
    }
}
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
    
    private Grafo<T> mapa;
    private Heuristica<T> heuristica;
    private Comparador<CostosVertice<T>> comparadorCostos;
    private ColaPrioridad<CostosVertice<T>> setAbierto;
    private Conjunto<T> setCerrado;
    private Diccionario<T, CostosVertice<T>> costos;


    /**
     * Constructor de clase AEstrella a partir de un grafo y una heurística.
     *
     * @param grafo      Grafo sobre el cual se ejecutarán las búsquedas.
     * @param heuristica Heurística utilizada para estimar la distancia al destino.
     */
    public AEstrella(Grafo<T> grafo, Heuristica<T> heuristica) {
        this.mapa = grafo;
        this.heuristica = heuristica;
        this.comparadorCostos = new ComparadorVertice<T>(); 
        this.setAbierto = new ColaPrioridad<CostosVertice<T>>(this.comparadorCostos);
        this.setCerrado = new Conjunto<T>(100);            
        this.costos = new Diccionario<T, CostosVertice<T>>(100);   //Falta ver dimensiones del grafo/mapa
    }

    /**
     * Ejecuta el algoritmo A* para hallar el camino mínimo entre un vértice origen
     * y un vértice destino.
     * 
     * @param origen    Vértice inicial.
     * @param destino   Vértice objetivo.
     * @return          Un Objeto tipo pila con los vértices del camino mínimo de origen a destino.
     *                  Null si no existe camino.
     * @throws ExcepcionAEstrella  si los vertices origen o destino no existen.
     *                             o si no existe un camino minimo entre ellos.
     */
    public Pila<T> buscarCaminoMinimo (T origen, T destino){

        if (origen == null || destino == null) {
            throw new ExcepcionAEstrella("Origen o destino inválido.");
        }

        inicializarBusqueda(origen, destino);

        Pila<T> caminoMinimo = new Pila<T>();
        boolean destinoEncontrado = false;
        
        while (!setAbierto.vacio() && !destinoEncontrado) {
            
            CostosVertice<T> costoActual = setAbierto.eliminar();
            T verticePrometedor = costoActual.obtenerVertice();
            
            if (verticePrometedor.equals(destino)) {
                destinoEncontrado = true;
            }
            else if (!setCerrado.contiene(verticePrometedor)) {               

                setCerrado.agregar(verticePrometedor); 
                explorarVecinos(verticePrometedor, costoActual, destino);
            }
        }
        if (destinoEncontrado) {
            caminoMinimo = reconstruirCamino(destino);
            return caminoMinimo;
        } else {
            throw new ExcepcionAEstrella("No existe camino entre origen y destino.");
        }
    }


    /**
     * Recorre los vecinos de un vértice y actualiza los costos
     * correspondientes en caso de encontrar rutas más económicas.
     *
     * @param vertice      Vértice actualmente expandido.
     * @param costoActual  Costos asociados al vértice expandido.
     * @param destino      Vértice objetivo de la búsqueda.
     */
    private void explorarVecinos(T vertice, CostosVertice<T> costoActual, T destino) {
        
        Map<T, Integer> adyacentes = mapa.obtenerAdyacentes(vertice);
        int costoReal = costoActual.obtenerCostoReal();

        for (T vecino : adyacentes.keySet()) {
            if (!setCerrado.contiene(vecino)) {
                int pesoArista = adyacentes.get(vecino);
                int nuevoCostoReal = costoReal + pesoArista;
                actualizarVecino(vecino, vertice, nuevoCostoReal, destino);
            }
        }
    }

    /**
    * Actualiza los costos y el padre de un vecino si se encuentra un camino más económico.
    *
    * @param vecino         Vértice vecino a actualizar.
    * @param padre          Vértice padre desde el que se llega al vecino.
    * @param nuevoCostoReal Costo real acumulado hasta este vecino.
    * @param destino        Vértice objetivo de la búsqueda.
    */
    private void actualizarVecino(T vecino, T padre, int nuevoCostoReal, T destino) {
        
        CostosVertice<T> costoExistente = costos.obtenerValor(vecino);
        if (costoExistente == null || nuevoCostoReal < costoExistente.obtenerCostoReal()) {
            int costoHeuristica = heuristica.calcularPuntaje(vecino, destino);
            int nuevoCostoTotal = nuevoCostoReal + costoHeuristica;

            if (costoExistente == null) {
                CostosVertice<T> nuevoCosto = new CostosVertice<>(vecino, nuevoCostoReal, nuevoCostoTotal, padre);
                costos.agregar(vecino, nuevoCosto);
                setAbierto.agregar(nuevoCosto);
            } else {
                costoExistente.actualizar(nuevoCostoReal, nuevoCostoTotal, padre);
                setAbierto.agregar(costoExistente);
            }
        }
    }

    /**
     * Inicializa las estructuras para comenzar la búsqueda A*.
     * 
     * NOTA:  la clase AEstrella se instancia una sola vez por GPS (Clase que la usa) debido
     *        a que la busqueda se realiza constantemente y es muy costoso crear nuevas instancias
     *        cada vez.
     *        Por este motivo elegi "resetear" los sets cerrado y abierto y el diccionario de costos
     *        mediante este metodo, para que en cada llamado de la busqueda no se arrastre 
     *        datos anteriores, sin la necesidad de crear el objeto AEstrella desde cero. 
     *
     * @param origen  Vértice inicial de la búsqueda.
     * @param destino Vértice objetivo de la búsqueda.
     */
    private void inicializarBusqueda(T origen, T destino) {

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
     * Utiliza una pila para conservar el orden real de recorrido
     *
     * @param destino  Vértice donde termina el camino.
     * @return         Pila de vértices que representan el camino mínimo de origen a destino.
     */
    private Pila<T> reconstruirCamino(T destino) {
        
        Pila<T> camino = new Pila<T>();
        T actual = destino;
        while (actual != null) {
            camino.agregar(actual);
            CostosVertice<T> datos = costos.obtenerValor(actual);
            if (datos != null) {
                actual = datos.obtenerPadre();
            } else {
                actual = null;
            }       
        }
        return camino;
    }
}
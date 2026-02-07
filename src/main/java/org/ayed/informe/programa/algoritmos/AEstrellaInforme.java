package org.ayed.informe.programa.algoritmos;

import org.ayed.tda.grafo.Grafo;
import org.ayed.tda.aestrella.*;
import org.ayed.tda.colaPrioridad.ColaPrioridad;
import org.ayed.tda.comparador.ComparadorVertice;
import org.ayed.tda.conjunto.Conjunto;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.gta.mapa.Nodo;
import org.ayed.informe.programa.*;

import java.util.Map;

/*
 * Versión de A* modificada para uso exclusivo del programa de complejidad algoritmica.
 */
public class AEstrellaInforme {
   
    private final Grafo<Nodo> grafo;
    private final Heuristica<Nodo> heuristica;
    private ColaPrioridad<CostosVertice<Nodo>> setAbierto;
    private Conjunto<Nodo> setCerrado;
    private Diccionario<Nodo, CostosVertice<Nodo>> costos;
    private Nodo destino;

    /**
     * Constructor de clase AEstrella a partir de un grafo y una heurística.
     *
     * @param grafo      Grafo sobre el cual se ejecutarán las búsquedas.
     * @param heuristica Heurística utilizada para estimar la distancia al destino.
     */
    public AEstrellaInforme(Grafo<Nodo> grafo, Heuristica<Nodo> heuristica) {
        this.grafo = grafo;
        this.heuristica = heuristica;
    }

    /**
     * Ejecuta el algoritmo A* para hallar el camino mínimo entre un vértice origen
     * y un vértice destino. 
     * 
     * @param origen     Vértice inicial.
     * @param destino    Vértice objetivo.
     * @param mediciones Objeto tipo Metrica con las mediciones obtenidas.
     */
  
    public void ejecutar(Nodo origen, Nodo destino, Metricas mediciones) {    
      
        int capacidad = grafo.obtenerVertices().size() + 1;
        this.destino = destino;
        this.costos = new Diccionario<>(capacidad);
        this.setCerrado = new Conjunto<>(capacidad);
        this.setAbierto = new ColaPrioridad<>(new ComparadorVertice<Nodo>());
        
        long inicio = System.nanoTime();

        int heuristicaOrigen = heuristica.calcularPuntaje(origen, destino); 
        CostosVertice<Nodo> costoOrigen = new CostosVertice<>(origen, 0, heuristicaOrigen, null);   
        
        costos.agregar(origen, costoOrigen);
        setAbierto.agregar(costoOrigen);
        mediciones.seInserto();
        
        boolean destinoEncontrado = false;

        while (!setAbierto.vacio() && !destinoEncontrado) {
            
            CostosVertice<Nodo> costoActual = setAbierto.eliminar();
            mediciones.seExtrajo();
            Nodo verticePrometedor = costoActual.obtenerVertice();
            if (setCerrado.contiene(verticePrometedor)) {
                continue;
            }

            setCerrado.agregar(verticePrometedor);
            mediciones.seExpandio();
            if (verticePrometedor.equals(destino)) {
                destinoEncontrado = true;
            } else {
                explorarVecinos(verticePrometedor, costoActual, mediciones);
            }
        }        
        
        long fin = System.nanoTime();
        mediciones.setearTiempoNs(fin - inicio);

        if (destinoEncontrado) {
            mediciones.setearCostoTotal(costos.obtenerValor(destino).obtenerCostoReal());
        } else {
            mediciones.setearCostoTotal(-1);
        }
    }

    /**
     * Recorre los vecinos de un vértice y actualiza los costos
     * correspondientes en caso de encontrar rutas más económicas.
     *
     * @param vertice      Vértice actualmente explorado.
     * @param costoActual  Costos asociados al vértice explorado.
     * @param mediciones Objeto tipo Metrica con las mediciones obtenidas.
     */
    private void explorarVecinos(Nodo vertice, CostosVertice<Nodo> costoActual, Metricas mediciones) {
        
        int costoReal = costoActual.obtenerCostoReal();
        Map<Nodo, Integer> vecinos = grafo.obtenerAdyacentes(vertice);
        
        for (Nodo vecino : vecinos.keySet()) {
            mediciones.seRelajo(); 
            if (!setCerrado.contiene(vecino)) {
                int pesoArista = grafo.obtenerArista(vertice, vecino);
                int nuevoCostoReal = costoReal + pesoArista;
                procesarVecino(vecino, vertice, nuevoCostoReal, mediciones);
            }
        }
    }

    /**
    * Actualiza los costos y el padre de un vecino si se encuentra un camino más económico.
    * 
    * NOTA: Esto ajusta la complejidad del algoritmo de O((V+E)logV) a O((V+E)logE), 
    * ya que el tamaño de la cola de prioridad queda acotado por el número de aristas (E) 
    * en lugar del de vértices (V). 
    * En el contexto de nuestro programa, el impacto en el rendimiento es despreciable.
    * 
    * @param vecino         Vértice vecino a actualizar.
    * @param padre          Vértice padre desde el que se llega al vecino.
    * @param nuevoCostoReal Costo real acumulado hasta este vecino.
    * @param mediciones Objeto tipo Metrica con las mediciones obtenidas.
    */
    private void procesarVecino(Nodo vecino, Nodo padre, int nuevoCostoReal, Metricas mediciones) {
        
        CostosVertice<Nodo> costoVecino = costos.obtenerValor(vecino);
        
        if (costoVecino == null || nuevoCostoReal < costoVecino.obtenerCostoReal()) {
            int costoHeuristica = heuristica.calcularPuntaje(vecino, destino);
            int nuevoCostoTotal = nuevoCostoReal + costoHeuristica;

            CostosVertice<Nodo> nuevoCosto = new CostosVertice<Nodo>(vecino, nuevoCostoReal, nuevoCostoTotal, padre);
            costos.agregar(vecino, nuevoCosto);  
            setAbierto.agregar(nuevoCosto);
            mediciones.seInserto();
        }
    }
}
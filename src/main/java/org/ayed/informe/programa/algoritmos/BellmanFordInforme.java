package org.ayed.informe.programa.algoritmos;

import org.ayed.gta.mapa.Nodo;
import org.ayed.informe.programa.Metricas;
import org.ayed.tda.grafo.Grafo;
import org.ayed.tda.diccionario.Diccionario;
import java.util.Map;
import java.util.Set;

/**
 * Implementacion del algoritmo Bellman-Ford para caminos mínimos.
 * Tiene contadores en las expansiones de nodos y relajaciones de aristas para
 * analizar su eficiencia.
 * 
 * La complejidad algoritmica de esta implementacion es O(V*E).
 */
public class BellmanFordInforme {

    public void ejecutar(Grafo<Nodo> grafo, Nodo origen, Nodo destino, Metricas mediciones) {
        
        Set<Nodo> nodos = grafo.obtenerVertices();
        int cantidadNodos = nodos.size();
        Diccionario<Nodo, Integer> distancias = new Diccionario<>(cantidadNodos);
        
        for (Nodo nodo : nodos) {
            distancias.agregar(nodo, Integer.MAX_VALUE);
        }
        distancias.agregar(origen, 0);

        long inicio = System.nanoTime();
        for (int i = 1; i < cantidadNodos; i++) {
            
            for (Nodo u : nodos) {

                mediciones.seExpandio();    //Expansiones: (V-1)*V
                Map<Nodo, Integer> adyacentes = grafo.obtenerAdyacentes(u);   
                
                for (Nodo v : adyacentes.keySet()) {
                    mediciones.seRelajo();  //Relajaciones: (V-1)*2E
                    
                    Integer distanciaU = distancias.obtenerValor(u);
                    
                    if (distanciaU != null && distanciaU != Integer.MAX_VALUE) {
                        int pesoUV = adyacentes.get(v);
                        Integer distanciaV = distancias.obtenerValor(v);
                        
                        if (distanciaV != null && distanciaU + pesoUV < distanciaV) {
                            distancias.agregar(v, distanciaU + pesoUV);
                        }
                    }
                }
            }
        }

        long fin = System.nanoTime();
        mediciones.setearTiempoNs(fin - inicio);
        Integer costoFinal = distancias.obtenerValor(destino);

        if (costoFinal == null || costoFinal == Integer.MAX_VALUE) {
            mediciones.setearCostoTotal(-1);
        }
        else {
            mediciones.setearCostoTotal(costoFinal);
        }
    }
}
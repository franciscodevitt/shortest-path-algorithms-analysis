package org.ayed.tda.grafo;

/**
 * Interfaz para una función heuristica,
 * para el algoritmo A*.
 *
 * @param <T> Tipo de dato del vertice
 */
public interface Heuristica<T> {
    /**
     * Calcula el puntaje para ir de origen a destino
     *
     * @param origen  Vertice origen
     * @param destino Vertice destino
     * @return el puntaje
     */
    int calcularPuntaje(T origen, T destino);
}

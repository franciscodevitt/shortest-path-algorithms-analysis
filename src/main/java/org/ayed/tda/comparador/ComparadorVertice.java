package org.ayed.tda.comparador;

import org.ayed.tda.aestrella.*;

/**
 * Implementación de la interfaz comparador para vertices.
 *
 * @param <T> Tipo de dato utilizado para representar los vértices.
 * @param CostosVertice<T> Estructura auxiliar que guarda los costos de un vertice. 
 */
public class ComparadorVertice<T> implements Comparador<CostosVertice<T>> {

    /**
     * Compara dos vertices segun el costo estimado total almacenado en el
     * objeto costosVertice correspondiente a cada uno.
     *
     * @param <T>  Tipo de dato utilizado para representar los vértices.
     * @return    -1 si el vertice a tiene menor costo que el vertice b.
     *             1 si el vertice b tiene menor costo que el vertice a.
     *             0 si ambos vertices tienen igual costo.
     * 
     * @throws  IllegalArgumentException si recibe null para alguno de los vertices.
     */
    @Override
    public int comparar(CostosVertice<T> a, CostosVertice<T> b) {
        
        if (a == null || b == null) {
            throw new IllegalArgumentException("No se pueden comparar los vertices.");
        }

        int resultado;
        if (a.obtenerCostoTotal() < b.obtenerCostoTotal()) {
            resultado = -1;
        } else if (a.obtenerCostoTotal() > b.obtenerCostoTotal()) {
            resultado = 1;
        } else {
            resultado = 0;
        }

        return resultado;
    }
}

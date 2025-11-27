package org.ayed.tda.comparador;

import org.ayed.tda.aestrella.*;

/*
 * Compara dos nodos según el costo estimado total almacenado en el Diccionario de Estado.
 */
public class ComparadorVertice<T> implements Comparador<CostosVertice<T>> {

    @Override
    public int comparar(CostosVertice<T> a, CostosVertice<T> b) {
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

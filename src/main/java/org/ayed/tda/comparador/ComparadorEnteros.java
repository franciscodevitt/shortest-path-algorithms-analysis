package org.ayed.tda.comparador;

public class ComparadorEnteros implements Comparador<Integer> {

    @Override
    public int comparar(Integer a, Integer b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("no se pueden comparar null");
        }
        if (a < b) return -1;
        if (a > b) return 1;
        return 0;
    }
    
    
}

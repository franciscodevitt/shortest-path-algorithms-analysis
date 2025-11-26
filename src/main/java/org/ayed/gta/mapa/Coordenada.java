package org.ayed.gta.mapa;

import java.util.Objects;

/**
 * Clase que representa las posiciones dentro de a una grilla.
 */
public class Coordenada {
    
    private final int x;
    private final int y;
    
    /** Constructor
     * @param f el numero de fila dentro de la grilla.
     * @param c el numero de columna dentro de la grilla.
     * 
     */
    public Coordenada(int f, int c) { 
        this.x = c; 
        this.y = f; 
    }
    
    /**
     *  Getters para conocer las coordenadas
     */
    public int obtenerX(){ return x; }
    public int obtenerY(){ return y; }


    /**
     * Metodo que implementa la relación de equivalencia lógica entre objetos Coordenada. 
     * Dos objetos Coordenada son considerados iguales si y solo si poseen los mismos 
     * valores en sus atributos x e y.
     * 
     * @param objeto  El objeto con el cual se compara esta Coordenada.
     * @return        True si objeto es una instancia de Coordenada y sus 
     *                atributos son identicaos. 
     *                False en caso contrario.
     * 
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Coordenada coordenada = (Coordenada) objeto;
        return x == coordenada.x && y == coordenada.y;
    }


    /**
     * Metodo que implementa la funcion de hash para poder usar una Coordenada
     * como llave en una tabla hash.
     * Dos Coordenadas lógicamente iguales segun equals() deben producir el mismo valor de hash.
     * 
     * @return Un numero entero correspondiente al valor hash calculado para esta coordenada.
     * 
     * @see #equals(Object)    
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);   
    }

}

package org.ayed.gta.mapa;

/**
 * Clase que representa un Nodo del mapa de la ciudad.
 * NOTA: SOLO para las clases Mapa y Grafo. 
 *       NO confundir con el Nodo de los diccionarios.
 */
public class Nodo {
    private final Coordenada posicion; 
    private final String tipoTerreno;
    private final boolean trafico;

    /**
     * Constructor de Nodo.
     * @param posicion    La coordenada del Nodo.
     * @param tipoTerreno String que representa el tipo de terreno.
     * @param trafico     Si tienen o no trafico 
     */
    public Nodo(Coordenada posicion, String tipoTerreno, boolean trafico) {
        this.posicion = posicion;
        this.tipoTerreno = tipoTerreno;
        this.trafico = trafico;
    }

    //Getters
    
    /**
     * @return las coordenadas del nodo.
     */
    public Coordenada obtenerCoordenada() { 
        return this.posicion; 
    }
    
    /**
     * @return el tipo de terreno del nodo.
     */
    public String obtenerTerreno() { 
        return this.tipoTerreno; 
    }

    /**
     * @return True si este nodo representa un terreno transitable con trafico.
     *         False en el caso contrario.
     */
    public boolean tieneTrafico() {
        return this.trafico;
    }

    /**
     * Metodo que implementa la relación de equivalencia lógica entre objetos Nodo. 
     * Dos objetos Nodo son considerados iguales si y solo si poseen los mismos 
     * valores en su atributo posicion (Coordenada)
     *
     * 
     * @param objeto  El objeto con el cual se compara este Nodo.
     * @return        True si objeto es una instancia de Nodo y sus  
     *                posiciones son identicas. 
     *                False en caso contrario.
     * 
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Nodo nodo = (Nodo) objeto;
        return this.posicion.equals(nodo.obtenerCoordenada());
    }

    /**
     * Metodo que implementa la funcion de hash para poder usar un Nodo
     * como llave en una tabla hash.
     * Dos Nodos lógicamente iguales segun equals() deben producir el mismo valor de hash.
     * 
     * @return Un numero entero correspondiente al valor hash calculado para este Nodo.
     * 
     * @see #equals(Object)    
     */
    @Override
    public int hashCode() {
        return this.posicion.hashCode();
    }
}

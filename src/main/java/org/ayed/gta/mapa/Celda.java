package org.ayed.gta.mapa;

/**
 * Clase que representa una celda del mapa de la ciudad.
 */
public class Celda {
    private final Coordenada posicion; 
    private final String tipoTerreno;
    private final boolean trafico;

    /**
     * Constructor de Celda.
     * @param posicion La coordenada inmutable de la Celda.
     * @param tipoTerreno String que representa el tipo de terreno.
     * @param trafico Si tienen o no trafico 
     */
    public Celda(Coordenada posicion, String tipoTerreno, boolean trafico) {
        this.posicion = posicion;
        this.tipoTerreno = tipoTerreno;
        this.trafico = trafico;
    }

    //Getters
    
    /**
     * @return las coordenadas de la celda.
     */
    public Coordenada obtenerCoordenada() { 
        return this.posicion; 
    }
    
    /**
     * @return el tipo de terreno de la celda.
     */
    public String obtenerTerreno() { 
        return this.tipoTerreno; 
    }

    /**
     * @return True si esta celda representa un terreno transitable con trafico.
     *         False en el caso contrario.
     */
    public boolean tieneTrafico() {
        return this.trafico;
    }

    /**
     * Metodo que implementa la relación de equivalencia lógica entre objetos Celda. 
     * Dos objetos Celda son considerados iguales si y solo si poseen los mismos 
     * valores en su atributo posicion (Coordenada)
     *
     * 
     * @param objeto  El objeto con el cual se compara esta Celda.
     * @return        True si objeto es una instancia de Celda y sus  
     *                posiciones son identicas. 
     *                False en caso contrario.
     * 
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Celda celda = (Celda) objeto;
        return this.posicion.equals(celda.obtenerCoordenada());
    }

    /**
     * Metodo que implementa la funcion de hash para poder usar una Celda
     * como llave en una tabla hash.
     * Dos Celdas lógicamente iguales segun equals() deben producir el mismo valor de hash.
     * 
     * @return Un numero entero correspondiente al valor hash calculado para esta Celda.
     * 
     * @see #equals(Object)    
     */
    @Override
    public int hashCode() {
        return this.posicion.hashCode();
    }
}

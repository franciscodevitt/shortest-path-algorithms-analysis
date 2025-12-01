package org.ayed.tda.aestrella;

/**
 * Clase auxiliar que representa el estado temporal de un vertice durante la búsqueda A*.
 * Contiene el costo real (G), costo estimado total (F) y el vertice padre.
 * 
 * @param <T> Tipo de dato utilizado para representar los vértices.
 */
public class CostosVertice<T> {

    private final T vertice;
    private int costoReal;        
    private int costoTotal;    //costoReal + la distancia estimada calculada con la heurisitca
    private T padre;

    public CostosVertice(T vertice, int costoReal, int costoTotal, T padre) {
        this.vertice = vertice;
        this.costoReal = costoReal;
        this.costoTotal = costoTotal;
        this.padre = padre;
    }

    // Getters 
    public T obtenerVertice() { return vertice; }
    public int obtenerCostoReal() { return costoReal; }
    public int obtenerCostoTotal() { return costoTotal; }
    public T obtenerPadre() { return padre; }

    /**
     * Actualiza los valores del vertice.
     * @param nuevoCostoReal  Costo real actualizado desde origen hasta este vertice.
     * @param nuevoCostoTotal Costo total actualizado desde origen hasta el destino.
     * @param nuevoPadre      Nuevo vertice padre de este vertice.
     */
    public void actualizar(int nuevoCostoReal, int nuevoCostoTotal, T nuevoPadre) {
        this.costoReal = nuevoCostoReal;
        this.costoTotal = nuevoCostoTotal;
        this.padre = nuevoPadre;
    }

}

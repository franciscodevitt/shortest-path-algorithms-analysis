package org.ayed.tda.aestrella;


/**
 * Estado temporal de un nodo durante la búsqueda A*.
 * Contiene el costo real (G), estimado total (F) y el nodo padre.
 * 
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
     * Actualiza los valores del nodo.
     */
    public void actualizar(int nuevoCostoReal, int nuevoCostoTotal, T nuevoPadre) {
        this.costoReal = nuevoCostoReal;
        this.costoTotal = nuevoCostoTotal;
        this.padre = nuevoPadre;
    }

}

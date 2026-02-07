package org.ayed.informe.programa;

import java.util.Locale;

/**
 * Clase para almacenar las mediciones obtenidas de las ejecución de los algoritmos de
 * caminos minimos.
 * Su uso es exclusivo de PruebaRendimiento.java.
 */
public class Metricas {
    private final String algoritmo;
    private int v = 0;                // Nodos del grafo
    private int e = 0;                // Aristas del grafo
    private long extracciones = 0;    // Total de nodos extraídos de la cola
    private long inserciones = 0;     // Total de nodos agregados a la cola 
    private long expansiones = 0;     // Nodos procesados 
    private long relajaciones = 0;    // Aristas inspeccionadas
    private long tiempoNs = 0;        // Nanosegundos
    private int costoTotal = 0;       // Solo para corroborar
    private int totalEjecuciones = 0; // Cantidad de ejecuciones realizadas hasta el momento

    private static final double NANO_A_MILISEGUNDOS = 1_000_000.0;   
    
    /**
     * Constructor
     * @param algoritmo nombre del algoritmo de camino minimo que se ejecutara en el grafo.
     * @param v         cantidad de nodos del grafo.
     * @param e         cantidad de aristas del grafo.
     */
    public Metricas(String algoritmo, int v, int e) {
        this.algoritmo = algoritmo;
        this.v = v;
        this.e = e;
    }

    /**
     * Calcula las operaciones totales de la cola de prioridad.
     */
    public long obtenerOperacionesCola() {
        return extracciones + inserciones;
    }

    /**
     * Calcula las operaciones totales del algoritmo.
     */
    public long obtenerOperacionesTotales() {
        return expansiones + relajaciones + obtenerOperacionesCola();
    }

    /**
     * acumula metricas.
     * @param otra la metrica de la nueva ejecución del algoritmo
     */
    public void sumarRendimiento(Metricas otra) {

        if (this.v == 0) {
            this.v = otra.v;
            this.e = otra.e;
        }
        this.expansiones += otra.expansiones;
        this.relajaciones += otra.relajaciones;
        this.extracciones += otra.extracciones;
        this.inserciones += otra.inserciones;
        this.tiempoNs += otra.tiempoNs;
        this.costoTotal = otra.costoTotal; 
        this.totalEjecuciones++;
    }

    /**
    * Genera la fila de datos formateada para el CSV.
    * @param  mapaNombre Nombre del mapa para la primera columna.
    * @return String con los datos separados por coma. 
    *         (Mapa, Tamaño, Nodos, Aristas, Algoritmo, Expansiones, Relajaciones,
    *         Operaciones totales, Tiempo, Costo)
    */
    public String filaCsv(String mapaNombre) {
        
        double promedioMs = 0.0;
        long promedioExp = 0;
        long promedioRel = 0;
        long promedioOp = 0;

        if (totalEjecuciones != 0) {
            promedioMs = (tiempoNs / (double)totalEjecuciones) / NANO_A_MILISEGUNDOS;
            promedioExp = expansiones / totalEjecuciones;
            promedioRel = relajaciones / totalEjecuciones;
            promedioOp = obtenerOperacionesTotales() / totalEjecuciones;
        }

        return String.format(Locale.US, "%s,%d,%d,%d,%s,%d,%d,%d,%.4f,%d",
                            mapaNombre, 
                            this.v + this.e, 
                            this.v, 
                            this.e, 
                            this.algoritmo, 
                            promedioExp, 
                            promedioRel, 
                            promedioOp, 
                            promedioMs, 
                            this.costoTotal);
    }

    /*
    //getters
    public long expansiones() { return expansiones; }
    public long relajaciones() { return relajaciones; }
    public long operacionesCola() { return extracciones + inserciones; }
    public long operacionesTotales() { return expansiones + relajaciones + operacionesCola(); }
    */

    public long obtenerTiempoNs() { return tiempoNs; }
    public int obtenerCostoTotal() { return costoTotal; }
    
    /**
     * Cuenta la expansion de un nodo
     */
    public void seExpandio() { this.expansiones++; }
    
    /**
     * Cuenta la extracción de un nodo de la cola de prioridad
     */
    public void seExtrajo() { this.extracciones++; }
    
    /**
     * Cuenta la inserción de un nodo a la cola de prioridad
     */
    public void seInserto() { this.inserciones++; } 
    
    /**
     * Cuenta la relajacion de una arista
     */
    public void seRelajo() { this.relajaciones++; }

    /**
     * Guarda el tiempo que tardo en ejecutarse el algoritmo
     * @param t Tiempo total 
     */
    public void setearTiempoNs(long t) { this.tiempoNs = t; }

    /**
     * Guarda el costo del camino minimo obtenido.
     * Se utiliza principalmete para corroborar que todas las ejecuciones
     * hayan obtenido el mismo camino, o en defecto uno de igual costo.
     */
    public void setearCostoTotal(int c) { this.costoTotal = c; }
}
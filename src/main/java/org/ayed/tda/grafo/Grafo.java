package org.ayed.tda.grafo;

import java.util.*;

/**
 * Esta implementación de Grafo representa un grafo no
 * dirigido.
 * Admite tipo genérico para indicarle el tipo de dato
 * de los vertices. Esto permite agregar datos/comportamiento
 * a los vertices (por ejemplo, si queremos buscar un AEM en
 * un contexto de red social), ademas de permitir la
 * implementación del algoritmo A*, donde la heuristica se
 * define a partir de los vertices
 */
public class Grafo<T> {
    protected final Map<T, Map<T, Integer>> adyacencias;
    protected static final int INFINITO = 99999; // Ajustar si es necesario
    protected static final int SIN_PESO = 1;     // Ajustar si es necesario

    /**
     * Constructor de Grafo
     */
    public Grafo() {
        adyacencias = new HashMap<>();
    }

    /**
     * Constructor de copia de Grafo
     *SS
     * @param grafo Grafo a copiar
     *              No puede ser nulo
     * @throws ExcepcionGrafo si el grafo es nulo
     */
    public Grafo(Grafo<T> grafo) {
        if (grafo == null) {
            throw new ExcepcionGrafo("El grafo no puede ser nulo.");
        }
        adyacencias = new HashMap<>();
        for (T vertice : grafo.adyacencias.keySet()) {
            // copia las adyacencias del otro grafo
            adyacencias.put(vertice, new HashMap<>(grafo.obtenerAdyacentes(vertice)));
        }
    }

    /**
     * Agrega un vertice no existente al grafo
     *
     * @param vertice Vertice a agregar
     *                No puede ser nulo
     *                No puede existir previamente en el grafo
     * @throws ExcepcionGrafo si el vertice es nulo,
     *                        o si ya existia en el grafo
     */
    public void agregarVertice(T vertice) {
        if (vertice == null) {
            throw new ExcepcionGrafo("El vértice no puede ser nulo.");
        }
        if (adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice ya existe.");
        }
        adyacencias.put(vertice, new HashMap<>());
    }

    /**
     * Elimina un vertice del grafo, junto con todas las
     * adyacencias
     *
     * @param vertice Vertice a eliminar
     *                Debe existir en el grafo
     * @throws ExcepcionGrafo si el vertice no existe en el grafo
     */
    public void eliminarVertice(T vertice) {
        if (!adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice no existe.");
        }
        adyacencias.remove(vertice);
        for (Map<T, Integer> adyacente : adyacencias.values()) {
            adyacente.remove(vertice);
        }
    }

    /**
     * Agrega una arista al grafo. Sobreescribe una arista anterior,
     * si existia
     *
     * @param origen  Vertice origen
     *                Debe existir en el grafo
     * @param destino Vertice destino
     *                Debe existir en el grafo
     * @throws ExcepcionGrafo si alguno de los dos vertices no
     *                        existe en el grafo
     */
    public void agregarArista(T origen, T destino, int peso) {
        if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
            throw new ExcepcionGrafo("La arista no es válida.");
        }
        obtenerAdyacentes(origen).put(destino, peso);
        obtenerAdyacentes(destino).put(origen, peso);
    }

    /**
     * Agrega una arista con peso por defecto
     */
    public void agregarArista(T origen, T destino) {
        agregarArista(origen, destino, SIN_PESO);
    }

    /**
     * Elimina una arista del grafo. Si no existe, no hace nada
     *
     * @param origen  Vertice origen
     *                Debe existir en el grafo
     * @param destino Vértice destino
     *                Debe existir en el grafo.
     * @throws ExcepcionGrafo si alguno de los dos vertices no
     *                        existe en el grafo.
     */
    public void eliminarArista(T origen, T destino) {
        if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
            throw new ExcepcionGrafo("La arista no es válida.");
        }
        obtenerAdyacentes(origen).remove(destino);
        obtenerAdyacentes(destino).remove(origen);
    }

    /**
     * Obtiene una arista existente del grafo
     *
     * @param origen  Vertice origen
     *                Debe existir en el grafo
     * @param destino Vértice destino
     *                Debe existir en el grafo
     * @throws ExcepcionGrafo si alguno de los dos vertices no
     *                        existe en el grafo, o si la arista
     *                        no existe en el grafo
     */
    public int obtenerArista(T origen, T destino) {
        if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
            throw new ExcepcionGrafo("La arista no es válida.");
        }
        if (!obtenerAdyacentes(origen).containsKey(destino)) {
            throw new ExcepcionGrafo("La arista no existe.");
        }
        return obtenerAdyacentes(origen).get(destino);
    }

    /**
     * Obtiene las adyacencias de un vertice existente del grafo
     *
     * @param vertice Vertice a obtener
     *                Debe existir en el grafo
     * @throws ExcepcionGrafo si el vertice no existe en el grafo
     */
    public Map<T, Integer> obtenerAdyacentes(T vertice) {
        if (!adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice no existe.");
        }
        return adyacencias.get(vertice);
    }

    /** Obtiene todos los vertices del grafo */
    public Set<T> obtenerVertices() {
        return adyacencias.keySet();
    }

    /** Indica si un vertice existe en el grafo */
    public boolean tieneVertice(T vertice) {
        return adyacencias.containsKey(vertice);
    }

    /** Indica si existe una arista entre dos vertices */
    public boolean tieneArista(T origen, T destino) {
        if (!tieneVertice(origen) || !tieneVertice(destino)) return false;
        return adyacencias.get(origen).containsKey(destino);
    }

    /** Obtiene el grado (cantidad de adyacentes) de un vertice */
    public int grado(T vertice) {
        return obtenerAdyacentes(vertice).size();
    }
}


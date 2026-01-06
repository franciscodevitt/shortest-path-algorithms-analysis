package org.ayed.tda.lista;

public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidadDatos;

    /**
     * Constructor de Cola.
     */
    public Cola() {
        // Implementar
    }

    /**
     * Constructor de copia de Cola
     *
     * @param cola Cola a copiar
     *             No puede ser nula
     * @throws ExcepcionLista si la cola es nula
     *
    public Cola(Cola<T> cola) {
        // Implementar
    }

    /**
     * Agrega el dato al final de la cola
     *
     * @param dato Dato a agregar
     */
    public void agregar(T dato) {
        // Implementar
    }

    /**
     * Elimina el siguiente dato de la cola (FIFO)
     *
     * @return el siguiente dato de la cola
     * @throws ExcepcionLista si la cola está vacía
     */
    public T eliminar() {
        // Implementar
        return (T) new Object();
    }

    /**
     * Obtiene el siguiente dato de la cola (FIFO)
     *
     * @return el siguiente dato de la cola
     * @throws ExcepcionLista si la cola está vacia
     */
    public T siguiente() {
        // Implementar
        return (T) new Object();
    }

    /**
     * Obtiene el tamaño de la cola
     *
     * @return el tamaño de la cola
     */
    public int tamanio() {
        // Implementar
        return 0;
    }

    /**
     * Evalua si la cola está vacia
     *
     * @return true si la cola está vacia
     */
    public boolean vacio() {
        // Implementar
        return true;
    }
}

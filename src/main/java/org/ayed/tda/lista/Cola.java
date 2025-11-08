package org.ayed.tda.lista;

public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidadDatos;

    /**
     * Constructor de Cola.
     */
    public Cola() {
        primero = null;
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Cola.
     *
     * @param cola Cola a copiar.
     *             No puede ser nula.
     * @throws ExcepcionLista si la cola es nula.
     */
    public Cola(Cola<T> cola) {
        if(cola == null){
            throw new ExcepcionLista("Cola nula.");
        }
        primero = null;
        ultimo = null;
        cantidadDatos = 0;

        Nodo<T> actual = cola.primero;
        while (actual != null) {
            agregar(actual.dato);
            actual = actual.siguiente;
        }
    }

    /**
     * Agrega el dato al final de la cola.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<T>(dato,ultimo,null);
        if(vacio()){
            primero = nuevo;
            ultimo = nuevo;
        }
        else{
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        cantidadDatos++;
    }

    /**
     * Elimina el siguiente dato de la cola (FIFO).
     *
     * @return el siguiente dato de la cola.
     * @throws ExcepcionLista si la cola está vacía.
     */
    public T eliminar() {
        if(vacio()){
            throw new ExcepcionLista("Cola vacia.");
        }
        T dato = primero.dato;
        if(primero.siguiente != null){
            primero = primero.siguiente;
            primero.anterior = null;
        }
        else{
            primero = null;
            ultimo = null;
        }
        return dato;
    }

    /**
     * Obtiene el siguiente dato de la cola (FIFO).
     *
     * @return el siguiente dato de la cola.
     * @throws ExcepcionLista si la cola está vacía.
     */
    public T siguiente() {
        if(vacio()){
            throw new ExcepcionLista("Cola vacia.");
        }
        T dato = primero.dato;
        return dato;
    }

    /**
     * Obtiene el tamaño de la cola.
     *
     * @return el tamaño de la cola.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la cola está vacía.
     *
     * @return true si la cola está vacía.
     */
    public boolean vacio() {
        return primero == null;
    }
}

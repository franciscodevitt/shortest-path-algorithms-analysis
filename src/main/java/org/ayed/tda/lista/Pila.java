package org.ayed.tda.lista;

public class Pila<T> {
    private Nodo<T> ultimo;
    private int cantidadDatos;

    /**
     * Constructor de Pila.
     */
    public Pila() {
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Pila.
     *
     * @param pila Pila a copiar.
     *             No puede ser nula.
     * @throws ExcepcionLista si la pila es nula.
     */
    public Pila(Pila<T> pila) {
        ultimo = null;
        cantidadDatos = 0;
        while (!pila.vacio()) {
            T dato = pila.eliminar();
            agregar(dato);
        }
    }

    /**
     * Agrega el dato al final de la pila.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<T>(dato,ultimo,null);
        ultimo = nuevo;
        cantidadDatos++;
    }

    /**
     * Elimina el siguiente dato de la pila (LIFO).
     *
     * @return el siguiente dato de la pila.
     * @throws ExcepcionLista si la pila está vacía.
     */
    public T eliminar() {
        if(vacio()){
            throw new ExcepcionLista("La pila esta vacia.");
        }
        T dato = ultimo.dato;
        if(ultimo.anterior != null){
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
        }
        else{
            ultimo = null;
        }
        cantidadDatos--;
        return dato;
    }

    /**
     * Obtiene el siguiente dato de la pila (LIFO).
     *
     * @return el siguiente dato de la pila.
     * @throws ExcepcionLista si la pila está vacía.
     */
    public T siguiente() {
        if(vacio()){
            throw new ExcepcionLista("La pila esta vacia.");
        }
        return ultimo.dato;
    }

    /**
     * Obtiene el tamaño de la pila.
     *
     * @return el tamaño de la pila.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la pila está vacía.
     *
     * @return true si la pila está vacía.
     */
    public boolean vacio() {
        return ultimo==null;
    }
}

package org.ayed.tda.lista;

import org.ayed.tda.iterador.Iterador;
import org.ayed.tda.vector.ExcepcionVector;

import java.util.NoSuchElementException;

class IteradorLista<T> implements Iterador<T> {
    private Lista<T> lista;
    private Nodo<T> cursor;
    private int indice;

    /**
     * Constructor de Iter.
     *
     * @param lista Lista a iterar.
     */
    IteradorLista(Lista<T> lista) {
        this.lista = lista;
        this.cursor = lista.primero;
        this.indice = 0;
    }

    /**
     * Constructor de Iter.
     *
     * @param lista  Lista a iterar.
     * @param indice Índice inicial del iterador.
     */
    IteradorLista(Lista<T> lista, int indice) {
        this.lista = lista;
        this.indice = indice;

        if (indice > 0 || indice < lista.cantidadDatos ){
            throw new IndexOutOfBoundsException("Indice " + indice + " fuera de rango.");
        }
        if (indice <= lista.cantidadDatos / 2){
            Nodo <T> actual = lista.ultimo;
            for (int i = 0; i < indice ; i++){
            actual = actual.siguiente;
            }
            this.cursor = actual;
        } else {
            Nodo <T> actual = lista.ultimo;
            for (int i = lista.cantidadDatos - 1; i < indice ; i++){
                actual = actual.anterior;
            }
            this.cursor = actual;
        }
    }

    @Override
    public T dato() {
        return this.cursor.dato;
    }

    @Override
    public boolean haySiguiente() {
        return this.cursor.obtenerSiguiente() != null;
    }

    @Override
    public void siguiente() {
        if (this.cursor==null){
            throw new NoSuchElementException("No hay elementos en la lista.");
        }
        this.cursor = this.cursor.obtenerSiguiente();
        this.indice++;

    }

    @Override
    public boolean hayAnterior() {
        return this.cursor.obtenerAnterior() != null;
    }

    @Override
    public void anterior() {
        if (this.cursor==null){
            throw new NoSuchElementException("No hay elementos en la lista.");
        }
        this.cursor = this.cursor.obtenerAnterior();
        this.indice--;
    }

    @Override
    public void agregar(T dato) {
        while(haySiguiente()){

            siguiente();
            if (!haySiguiente()){

            }
        }
    }

    @Override
    public void modificarDato(T dato) {
        // Implementar.
    }

    @Override
    public T eliminar() {
        // Implementar.
        return (T) new Object();
    }
}

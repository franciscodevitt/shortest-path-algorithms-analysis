package org.ayed.tda.lista;

import org.ayed.tda.iterador.ExcepcionNoHayDato;
import org.ayed.tda.iterador.Iterador;


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
        this.indice = -1;
    }

    /**
     * Constructor de Iter.
     *
     * @param lista  Lista a iterar.
     * @param indice Índice inicial del iterador.
     */
    IteradorLista(Lista<T> lista, int indice) {
        if (indice < 0 || indice > lista.cantidadDatos)
            throw new ExcepcionLista("Índice inválido.");

        this.lista = lista;

        if (indice == lista.cantidadDatos) {
            this.cursor = null; // final
        } else {
            this.cursor = lista.obtenerNodo(indice);
        }

        this.indice = indice;
    }

    @Override
    public T dato() {
        if (cursor == null)
            throw new ExcepcionNoHayDato("No hay dato (iterador al final).");

        return cursor.dato;
    }

    @Override
    public boolean haySiguiente() {
        return cursor != null && (indice < lista.cantidadDatos - 1);
    }

    @Override
    public void siguiente() {
        if (!haySiguiente())
            throw new ExcepcionNoHayDato("No hay siguiente.");

        cursor = cursor.siguiente;
        indice++;
    }

    @Override
    public boolean hayAnterior() {
        if (cursor == null)
            return lista.ultimo != null; // si está al final, puede retroceder si hay algo

        return cursor.anterior != null;
    }

    @Override
    public void anterior() {
        if (!hayAnterior())
            throw new ExcepcionNoHayDato("No hay anterior.");

        if (cursor == null) {
            // si estamos al final, saltamos al último nodo
            cursor = lista.ultimo;
            indice = lista.cantidadDatos - 1;
        } else {
            cursor = cursor.anterior;
            indice--;
        }
    }

    @Override
    public void agregar(T dato) {
        // insertar antes del cursor
        if (cursor == null) {
            // agregar al final
            lista.agregar(dato);
            cursor = null;
            indice = lista.cantidadDatos; // final
        } else{
            lista.agregar(dato, indice);
            indice++; // mueve el cursor a la siguiente posición
        }
    }

    @Override
    public void modificarDato(T dato) {
        if (cursor == null)
            throw new ExcepcionNoHayDato("No hay dato.");

        cursor.dato = dato;
    }

    @Override
    public T eliminar() {
        if (cursor == null)
            throw new ExcepcionNoHayDato("No hay dato a eliminar.");

        T valor = cursor.dato;
        Nodo<T> ant = cursor.anterior;
        Nodo<T> sig = cursor.siguiente;

        // Caso único elemento
        if (lista.cantidadDatos == 1) {
            lista.primero = null;
            lista.ultimo = null;
            cursor = null;
            lista.cantidadDatos--;
            indice = 0;
            return valor;
        }

        // Caso general
        if (ant != null) ant.siguiente = sig;
        else lista.primero = sig;

        if (sig != null) sig.anterior = ant;
        else lista.ultimo = ant;

        // Cursor queda en el siguiente
        cursor = sig;

        lista.cantidadDatos--;
        return valor;
    }
}

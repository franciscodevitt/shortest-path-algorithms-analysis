package org.ayed.tda.lista;

import org.ayed.tda.iterador.Iterador;
import org.ayed.tda.vector.ExcepcionVector;

public class Lista<T> {
    Nodo<T> primero;
    Nodo<T> ultimo;
    int cantidadDatos;

    /**
     * Constructor de Lista.
     */
    public Lista() {
        primero = null;
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Lista.
     *
     * @param lista Lista a copiar.
     *              No puede ser nula.
     * @throws ExcepcionLista si la lista es nula.
     */
    public Lista(Lista<T> lista) {
        if (lista == null)
            throw new ExcepcionLista("Lista nula.");

        primero = null;
        ultimo = null;
        cantidadDatos = 0;

        Nodo<T> act = lista.primero;
        while (act != null) {
            agregar(act.dato);
            act = act.siguiente;
        }
    }

    /**
     * Agrega un dato al final de la lista.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (vacio()){
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.anterior = ultimo;
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        cantidadDatos++;
    }

    /**
     * Agrega un dato a la lista en el índice indicado.
     * <p>
     * Ejemplo:
     * <pre>
     * {@code
     * >> 0 -> 1 -> 5 -> 3
     * agregar(4, 1);
     * >> 0 -> 4 -> 1 -> 5 -> 3
     * }
     * </pre>
     *
     * @param dato   Dato a agregar.
     * @param indice Índice en el que se inserta el dato.
     *               No puede ser negativo.
     *               No puede ser mayor que el tamaño de la lista.
     * @throws ExcepcionLista si el índice no es válido.
     */
    public void agregar(T dato, int indice) {
        if (indice < 0 || indice > cantidadDatos) {
        throw new ExcepcionLista("Índice inválido");
        }

        boolean terminado = false;

        if (!terminado && indice == cantidadDatos) { // final
            agregar(dato);
            terminado = true;
        }

        if (!terminado && indice == 0) { // principio
            Nodo<T> nuevo = new Nodo<>(dato, null, primero);
            if (primero != null) {
                primero.anterior = nuevo;
            }
            primero = nuevo;
            cantidadDatos++;
            terminado = true;
        }

        if (!terminado) { // medio
            Nodo<T> actual = primero;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }

            Nodo<T> nuevo = new Nodo<>(dato, actual.anterior, actual);
            actual.anterior.siguiente = nuevo;
            actual.anterior = nuevo;
            cantidadDatos++;
            terminado = true;
        }
    }

    /**
     * Elimina el último dato de la lista
     *
     * @return el dato eliminado.
     * @throws ExcepcionLista si la lista está vacía.
     */
    public T eliminar() {
        if (vacio()) {
        throw new ExcepcionLista("Lista vacía");
        }

        T dato = ultimo.dato;

        if (cantidadDatos == 1) {
            primero = null;
            ultimo = null;
        } else {
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
        }

        cantidadDatos--;

        return dato;
    }

    /**
     * Elimina el dato de la lista en el índice indicado por parámetro.
     * <p>
     * Ejemplo:
     * <pre>
     * {@code
     * >> 0 -> 1 -> 5 -> 3
     * eliminar(1);
     * >> 0 -> 5 -> 3
     * }
     * </pre>
     *
     * @param indice Índice del dato a eliminar.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño de la lista.
     * @return el dato eliminado.
     */
    public T eliminar(int indice) {
        if (indice < 0 || indice >= cantidadDatos) {
        throw new ExcepcionLista("Índice inválido.");
        }

        T dato = null;

        if (indice == cantidadDatos - 1) { // último → usar eliminar()
            dato = eliminar();
        } 
        else if (indice == 0) { // primero
            dato = primero.dato;
            primero = primero.siguiente;

            if (primero != null) {
                primero.anterior = null;
            }

            cantidadDatos--;
        }
        else { // medio
            Nodo<T> actual = obtenerNodo(indice);
            dato = actual.dato;

            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;

            cantidadDatos--;
        }

        return dato;
    }

    /**
     * Obtiene el dato de la lista en el índice indicado.
     *
     * @param indice Índice del dato a obtener.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño de la lista.
     * @return el dato en el índice indicado.
     * @throws ExcepcionLista si el índice no es válido.
     */
    public T dato(int indice) {
        
        if (indice < 0 || indice >= cantidadDatos)
            throw new ExcepcionLista("Índice inválido.");

        return obtenerNodo(indice).dato;
    }

    /**
     * Modifica el dato de la lista en el índice indicado
     * por el dato indicado por parámetro.
     *
     * @param indice Índice del dato a modificar.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño de la lista.
     * @throws ExcepcionLista si el índice no es válido.
     */
    public void modificarDato(T dato, int indice) {
        if (indice < 0 || indice >= cantidadDatos)
            throw new ExcepcionLista("Índice inválido.");

        obtenerNodo(indice).dato = dato;
    }

    /**
     * Obtiene el tamaño de la lista.
     *
     * @return el tamaño de la lista.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la lista está vacía.
     *
     * @return true si la lista está vacía.
     */
    public boolean vacio() {
        return cantidadDatos == 0;
    }

    /**
     * Obtiene un iterador bidireccional posicionado
     * en el primer dato de la lista.
     *
     * @return el iterador.
     * @see Iterador
     */
    public Iterador<T> iterador() {
        return new IteradorLista<>(this);
    }

    /**
     * Obtiene un iterador bidireccional posicionado
     * en el índice indicado por parámetro.
     *
     * @param indice Índice del nodo inicial del iterador.
     *               No puede ser negativo.
     *               No puede ser mayor que el tamaño de la lista.
     * @return el iterador.
     * @throws ExcepcionLista si el índice no es válido.
     * @see Iterador
     */
    public Iterador<T> iterador(int indice) {
        if (indice < 0 || indice > cantidadDatos)
            throw new ExcepcionLista("Índice inválido.");

        return new IteradorLista<>(this, indice);
    }

    /**
     * Verifica que el indice sea valido
     *
     * @param indice Índice del dato a modificar.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño del vector.
     * @return true -> indice valido
     *         false -> indice invalido
     */
    public boolean indiceValido(int indice) {
        return indice >= 0 && indice < cantidadDatos;
    }

    Nodo<T> obtenerNodo(int indice) {

        // elegir camino más corto
        if (indice < cantidadDatos / 2) {
            Nodo<T> act = primero;
            for (int i = 0; i < indice; i++) act = act.siguiente;
            return act;
        } else {
            Nodo<T> act = ultimo;
            for (int i = cantidadDatos - 1; i > indice; i--) act = act.anterior;
            return act;
        }
    }


}

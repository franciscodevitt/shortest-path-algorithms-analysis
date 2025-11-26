package org.ayed.tda.vector;

import java.util.concurrent.ExecutionException;

public class Vector<T> {
    private T[] datos;
    private int tamanioFisico;
    private int tamanioLogico;

    /**
     * Constructor de Vector.
     */
    @SuppressWarnings("unchecked")
    public Vector() {
        this.datos = (T[]) new Object [0];
        this.tamanioFisico = 0;
        this.tamanioLogico = 0;
    }

    /**
     * Constructor de copia de Vector.
     *
     * @param vector Vector a copiar.
     *               No puede ser nulo.
     * @throws ExcepcionVector si el vector es nulo.
     */
    @SuppressWarnings("unchecked")
    public Vector(Vector<T> vector) {
        if ( vector == null) {
            throw new ExcepcionVector ("El vector no puede ser nulo");
        } else {
            this.tamanioFisico = vector.tamanioMaximo();
            this.tamanioLogico = vector.tamanio();
            this.datos = (T[]) new Object [this.tamanioFisico];
            System.arraycopy(vector.datos, 0, this.datos, 0, tamanioFisico);
        }
    }

    /**
     * Agrega un dato al final del vector.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        if (lleno()) {
            redimensionar(); // si está lleno, redimensionamos
        }
        this.datos[tamanioLogico] = dato; // agregamos el elemento
        tamanioLogico++;
    }

    /**
     * Agrega un dato al vector en el índice indicado.
     * <p>
     * Ejemplo:
     * <pre>
     * {@code
     * >> [1, 3, 2, 7, 0]
     * agregar(9, 2);
     * >> [1, 3, 9, 2, 7, 0]
     * }
     * </pre>
     *
     * @param dato   Dato a agregar.
     * @param indice Índice en el que se inserta el dato.
     *               No puede ser negativo.
     *               No puede ser mayor que el tamaño del vector.
     * @throws ExcepcionVector si el índice no es válido.
     */
    public void agregar(T dato, int indice) {
        if ( indice < 0 || indice > tamanioLogico){
            throw new ExcepcionVector ("Indice invalido");
        }else {
            redimensionar();
            System.arraycopy(datos, indice, datos, indice+1, tamanioLogico - indice);
            datos[indice] = dato;
            tamanioLogico++;

        }
    }

    /**
     * Elimina el último dato del vector.
     *
     * @return el dato eliminado.
     * @throws ExcepcionVector si el vector está vacío.
     */
    public T eliminar() {
        T auxiliar = null;
        if (vacio()){
            throw new ExcepcionVector ("Vector vacío");
        } else if ( indiceValido(tamanioLogico) ){
            auxiliar = datos[tamanioLogico - 1];
            datos[--tamanioLogico] = null;
            redimensionar();
        }
        return auxiliar;
    }

    /**
     * Elimina el dato del vector en el índice indicado.
     * <p>
     * Ejemplo:
     * <pre>
     * {@code
     * >> [1, 3, 2, 7, 0]
     * eliminar(1);
     * >> [1, 2, 7, 0]
     * }
     * </pre>
     *
     * @param indice Índice del dato a eliminar.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño del vector.
     * @return el dato eliminado.
     * @throws ExcepcionVector si el vector está vacío,
     *                         o si el índice no es válido.
     */
    public T eliminar(int indice) {
        if (vacio()){
            throw new ExcepcionVector(("Vector vacìo"));
        } else if (!indiceValido(indice) || indice == tamanioLogico){
            throw new ExcepcionVector("Indice invalido");
        }
        T datoEliminado =  this.datos[indice];


        for (int i = indice ; i < tamanioLogico - 1  ; i++){
            this.datos[i] = this.datos[i+1];
        }
        tamanioLogico--;
        this.datos[tamanioLogico] = null;
        redimensionar();

        return datoEliminado;
    }

    /**
     * Obtiene el dato del vector en el índice indicado.
     *
     * @param indice Índice del dato a obtener.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño del vector.
     * @return el dato en el índice indicado.
     * @throws ExcepcionVector si el índice no es válido. Si esta vacio.
     */
    public T dato(int indice) {
        if ( !indiceValido(indice) || indice == tamanioLogico){
            throw new ExcepcionVector ("Indice invalido");
        } else if (vacio()) {
            throw new ExcepcionVector("el vector no puede estar vacio");
        }
        {
            return this.datos[indice];
        }

    }

    /**
     * Modifica el dato del vector en el índice indicado
     * por el dato indicado por parámetro.
     *
     * @param indice Índice del dato a modificar.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño del vector.
     * @throws ExcepcionVector si el índice no es válido.
     */
    public void modificarDato(T dato, int indice) {

        if ( !indiceValido(indice) || indice == tamanioLogico){
            throw new ExcepcionVector ("Indice invalido");
        }else if (vacio()){
            throw new ExcepcionVector ("No se puede modificar un vector vacio");
        } else {
            this.datos[indice] = dato;
        }



    }

    /**
     * Obtiene el tamaño del vector.
     *
     * @return el tamaño del vector.
     */
    public int tamanio() {
        return tamanioLogico;

    }

    /**
     * Obtiene el tamaño máximo del vector.
     * <p>
     * NOTA: Este método es únicamente para probar
     * el TDA.
     *
     * @return el tamaño máximo del vector.
     */
    public int tamanioMaximo() {
        return tamanioFisico;
    }

    /**
     * Evalúa si el vector está vacío.
     *
     * @return true si el vector está vacío.
     */
    public boolean vacio() {
        boolean estaVacio = (tamanioLogico == 0)? true: false;
        return estaVacio;
    }

    /**
     * Evalúa si el vector está lleno.
     *
     * @return true si el vector está lleno.
     */
    public boolean lleno() {
        return tamanioLogico == tamanioFisico;
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
        boolean valido = (indice >= 0 && indice <= tamanioLogico)? true: false;
        return valido;
    }

    /**
     * Redimenciona el vector en caso de ser necesario
     *
     *
     */
    @SuppressWarnings("unchecked")
    public void redimensionar() {
        T[] nuevoArray;
        int nuevaCapacidad = tamanioFisico;


        // Aumentar capacidad si está lleno
        if (lleno()) {
            nuevaCapacidad = Math.max(1, tamanioFisico * 2);
            nuevoArray = (T[]) new Object[nuevaCapacidad];
        }
        // Reducir cuando tamaño lógico <= mitad
        else if (tamanioLogico <= tamanioFisico / 2 && tamanioFisico > 1) {
            nuevaCapacidad = Math.max(1, tamanioFisico / 2);
            nuevoArray = (T[]) new Object[nuevaCapacidad];
        }
        else {
            return;
        }

        System.arraycopy(datos, 0, nuevoArray, 0, tamanioLogico);

        this.datos = nuevoArray;
        this.tamanioFisico = nuevaCapacidad;
    }
}

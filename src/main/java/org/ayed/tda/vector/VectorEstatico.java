package org.ayed.tda.vector;

public class VectorEstatico<T> {
    private T[] datos;
    private int tamanioFisico;
    private int tamanioLogico;

    /**
     * Constructor de Vector Estático.
     *
     * @param tamanio Tamaño físico del vector.
     *                No puede ser negativo.
     * @throws ExcepcionVector si el tamaño físico es negativo.
     */
    @SuppressWarnings("unchecked")
    public VectorEstatico(int tamanio) {
        if ( tamanio >= 0){
            this.datos = (T[]) new Object [tamanio];
            this.tamanioFisico = tamanio;
            this.tamanioLogico = 0;
        } else {
            throw new ExcepcionVector ("El tamaño fisico no puede ser negativo");
        }
    }

    /**
     * Constructor de copia de Vector Estático.
     *
     * @param vectorEstatico Vector a copiar.
     *                       No puede ser nulo.
     * @throws ExcepcionVector si el vector es nulo.
     */
    @SuppressWarnings("unchecked")
    public VectorEstatico(VectorEstatico<T> vectorEstatico) {
        if ( vectorEstatico == null) {
            throw new ExcepcionVector ("El vector no puede ser nulo");
        } else {
            this.tamanioFisico = vectorEstatico.tamanioMaximo();
            this.tamanioLogico = 0;
            this.datos = (T[]) new Object [this.tamanioFisico];
            for (int i = 0; i < vectorEstatico.tamanio(); i++) {
                agregar(vectorEstatico.dato(i));
            }
        }
    }

    /**
     * Agrega un dato al final del vector.
     *
     * @param dato Dato a agregar.
     * @throws ExcepcionVector si el vector está lleno.
     */
    public void agregar(T dato) {
        if (lleno()) {
            throw new ExcepcionVector("El vector está lleno");
        } else {
            this.datos[tamanioLogico++] = dato;

        }
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
     * @throws ExcepcionVector si el vector está lleno,
     *                         o si el índice no es válido.
     */
    public void agregar(T dato, int indice) {
        if (!indiceValido(indice)){
            throw new ExcepcionVector ("Indice invalido");
        }else if (lleno()){
            throw new ExcepcionVector ("Vector lleno");
        }else{
            for (int i = tamanioLogico; i > indice; i--) {
                datos[i] = datos[i - 1];
            }
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
        T auxiliar;
        if (vacio()){
            throw new ExcepcionVector ("Vector vacío");
        }else {
            auxiliar = datos[tamanioLogico - 1];
            datos[--tamanioLogico] = null;
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
        T auxiliar;
        if (!indiceValido(indice) ){
            throw new ExcepcionVector ("Indice invalido");
        }else if (vacio()){
            throw new ExcepcionVector ("vector vacio");
        } else {
            auxiliar =  this.datos[indice];
            for (int i = indice; i < tamanioLogico - 1; i++) {
                datos[i] = datos[i + 1];
            }
            datos[--tamanioLogico] = null;
        }
        return auxiliar;
    }

    /**
     * Obtiene el dato del vector en el índice indicado.
     *
     * @param indice Índice del dato a obtener.
     *               No puede ser negativo.
     *               No puede ser mayor o igual que el tamaño del vector.
     * @return el dato en el índice indicado.
     * @throws ExcepcionVector si el índice no es válido.
     */
    public T dato(int indice) {
        if (!indiceValido(indice) ){
            throw new ExcepcionVector ("Indice invalido");
        }else {
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
        if (!indiceValido(indice)){
            throw new ExcepcionVector ("Indice invalido");
        }else {
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
     * Obtiene el tamaño máximo de datos del vector.
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
        return tamanioLogico == 0;
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
        boolean valido = (indice >= 0 && indice < tamanioLogico)? true: false;
        return valido;
    }
}

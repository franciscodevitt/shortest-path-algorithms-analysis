package org.ayed.tda.matriz;

//import org.ayed.tda.lista.*;

/**
 * TDA que representa una Matriz de tamaño fijo (estática).
 * Permite almacenar elementos de cualquier tipo en una estructura bidimensional. 
 *
 * @param <T> Tipo de dato almacenado en cada celda.
 */
public class Matriz<T> {

    private final int filas;
    private final int columnas;
    private T[][] datos;


    /**
     * Constructor. Crea una matriz genérica vacia de tamaño fijo.
     *
     * @param filas       cantidad positiva de filas. 
     * @param columnas    cantidad positiva de columnas .
     * 
     * @throws ExcepcionMatriz si las dimensiones no son validas.
     */
    @SuppressWarnings("unchecked")
    public Matriz(int filas, int columnas) {
        if (filas <= 0 || columnas <= 0) {
            throw new ExcepcionMatriz("Dimensiones inválidas. Deben ser numeros enteros positivos.");
        } else {
            this.filas = filas;
            this.columnas = columnas;
            this.datos = (T[][]) new Object[filas][columnas];
        }
    }

    /**
     * Constructor de copia de Matriz
     *
     * @param matriz  Matriz a copiar. No puede ser nula.
     *                   
     * @throws ExcepcionMatriz si la matriz es nula
     */
    @SuppressWarnings("unchecked")
    public Matriz(Matriz<T> matriz) {
        if (matriz == null) {
            throw new ExcepcionMatriz("La matriz no puede ser nula");
        } else {
            this.filas = matriz.cantidadFilas();
            this.columnas = matriz.cantidadColumnas();
            this.datos = (T[][]) new Object[this.filas][this.columnas];
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < columnas; c++) {
                    agregarEntrada(f, c, matriz.obtenerEntrada(f, c));
                }
            }
        }
    }

    /**
     * Clona a la matriz.
     * Para modificar sin afectar la original.
     * 
     * @return una copia de la matriz.
     */
    public Matriz<T> copiar() {
        Matriz<T> nueva = new Matriz<>(filas, columnas);
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                nueva.datos[f][c] = this.datos[f][c];
            }
        }
        return nueva;
    }

    /**
     * Para conocer la cantidad de filas.
     * 
     * @return la cantidad de filas.
     */  
    public int cantidadFilas() {
        return filas;
    }

    /**
     * Para conocer la cantidad de columnas.
     * 
     * @return la cantidad de columnas.
     */  
    public int cantidadColumnas() {
        return columnas;
    }

    /**
     * Para conocer el dato de una entrada de la matriz.
     * 
     * @param fila El numero de fila de la entrada a obtener.
     * @param col  El numero de columna de la entrada a obtener.
     *             En conjunto tienen que indicar una entrada valida de la matriz.
     * 
     * @throws ExcepcionMatriz si fila y col no indican una entrada valida.
     * 
     * @return El dato guardado en la entrada solicitada. null si está vacia.
     */   
    public T obtenerEntrada (int fila, int col) {
        if (!esEntradaValida(fila, col)) {
            throw new ExcepcionMatriz("No se puede obtener. Entrada inválida");
        }
        return datos[fila][col];
    }

    /**
     * Agrega un dato en la entrada indicada.
     * 
     * @param fila  El numero de fila de la entrada a agregar.
     * @param col   El numero de columna de la entrada a agregar.
     *              En conjunto tienen que indicar una entrada valida de la matriz.
     * @param dato El dato a agregar.
     * 
     * @throws ExcepcionMatriz si fila y col no indican una entrada valida.
     * @throws ExcepcionMatriz si la entrada ya tiene un dato preexistente.
     */   
    public void agregarEntrada(int fila, int col, T dato) {
        if (!esEntradaValida(fila, col)) {
            throw new ExcepcionMatriz("No se puede agregar. Entrada inválida");
        } 
        else if (!esEntradaVacia(fila, col)){
            throw new ExcepcionMatriz("No se puede agregar. Dato preexistente.");
        } else {
            datos[fila][col] = dato;
        }
    }

    /**
     * Reemplaza el dato de la entrada indicada.
     * 
     * @param fila  El numero de fila de la entrada a modificar.
     * @param col   El numero de columna de la entrada a modificar.
     *              En conjunto tienen que indicar una entrada valida de la matriz.
     * @param dato El dato por el cual se va a reemplazar el dato previo.
     * 
     * @throws ExcepcionMatriz si fila y col no indican una entrada valida.
     */
    public void modificarEntrada(int fila, int col, T dato) {
        if (!esEntradaValida(fila, col)) {
            throw new ExcepcionMatriz("No se puede modificar. Entrada inválida");
        } 
        else {
            datos[fila][col] = dato;
        }
    }

    /**
     * Elimina el dato de la entrada indicada.
     * 
     * @param fila  El numero de fila de la entrada a eliminar.
     * @param col   El numero de columna de la entrada a eliminar.
     *              En conjunto tienen que indicar una entrada valida de la matriz.
     * 
     * @throws ExcepcionMatriz si fila y col no indican una entrada valida.
     * @throws ExcepcionMatriz si no hay ningun dato en la entrada previamente.
     * 
     * @return el dato eliminado.
     */
    public T eliminar(int fila, int col) {

        T datoEliminado = datos[fila][col];

        if (!esEntradaValida(fila, col)) {
            throw new ExcepcionMatriz("No se puede eliminar. Entrada inválida");
        } 
        else if (esEntradaVacia(fila, col)) {
            throw new ExcepcionMatriz("No se puede eliminar. Entrada vacía.");
        } 
        else {
            datos[fila][col] = null;
        }
        return datoEliminado;
    }

    /**
     * Verifica que fila y col indiquen una entrada valida de la matriz.
     * 
     * @param fila  El numero de fila de la entrada a consultar.
     * @param col   El numero de columna de la entrada a consultar.
     * 
     * @return True si es una entrada valida. False si no lo es.
     */
    public boolean esEntradaValida(int fila, int col) {
        return (fila >= 0 && fila < filas && col >= 0 && col < columnas);
    }

    /**
     * Verifica que la entrada indicada no guarda ningun dato.
     * 
     * @param fila  El numero de fila de la entrada a consultar.
     * @param col   El numero de columna de la entrada a consultar.
     * 
     * @return True si es una entrada vacia. False si no lo es.
     */
    public boolean esEntradaVacia(int fila, int col) {
        if (!esEntradaValida(fila, col)) {
            throw new ExcepcionMatriz("No se puede consultar. Entrada inválida");
        }
        return datos[fila][col] == null;
    }

}
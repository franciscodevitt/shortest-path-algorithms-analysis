package org.ayed.tda.matriz;

public class ExcepcionMatriz extends RuntimeException {
    /**
     * Constructor de ExcepcionMatriz.
     * Esta clase es para el uso exclusivo de Matriz.
     *
     * @param mensaje Mensaje de error.
     * @see Matriz
     */
    public ExcepcionMatriz(String mensaje) {
        super(mensaje);
    }
}

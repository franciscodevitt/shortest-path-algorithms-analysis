package org.ayed.gta;

public class ExcepcionGaraje extends RuntimeException {
    /**
     * Constructor de ExcepcionGaraje.
     * Esta clase es para el uso exclusivo de Garaje.
     *
     * @param mensaje Mensaje de error.
     * @see Garaje
     */
    public ExcepcionGaraje(String mensaje) {
        super(mensaje);
    }
}

package org.ayed.gta.mapa;

public class ExcepcionMapa extends RuntimeException {
    /**
     * Constructor de ExcepcionMapa.
     * Esta clase es para el uso exclusivo de Mapa.
     *
     * @param mensaje Mensaje de error.
     * @see Mapa
     */
    public ExcepcionMapa(String mensaje) {
        super(mensaje);
    }
}

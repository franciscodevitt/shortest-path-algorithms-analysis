package org.ayed.tda.aestrella;

public class ExcepcionAEstrella extends RuntimeException {
    /**
     * Constructor de ExcepcionAEstrella.
     *
     * @param mensaje Mensaje de error.
     * @see AEstrella
     */
    public ExcepcionAEstrella(String mensaje) {
        super(mensaje);
    }
}


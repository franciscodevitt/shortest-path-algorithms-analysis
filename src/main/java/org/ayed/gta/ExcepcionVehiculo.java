package org.ayed.gta;

public class ExcepcionVehiculo extends RuntimeException {
    /**
     * Constructor de ExcepcionGaraje.
     * Esta clase es para el uso exclusivo de Garaje.
     *
     * @param mensaje Mensaje de error.
     * @see Vehiculo
     */
    public ExcepcionVehiculo(String mensaje) {
        super(mensaje);
    }
}

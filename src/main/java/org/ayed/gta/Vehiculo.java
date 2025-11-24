package org.ayed.gta;

public class Vehiculo {

    private String nombre;
    private TipoVehiculo tipo;
    private int precio;
    private int capacidadGasolina;

    public static final int PRECIO_RUEDA_MOTO = 30;
    public static final int PRECIO_RUEDA_AUTO = 50;
    public static final int PRECIO_LITRO = 1;

    // constructor del vehiculo
    public Vehiculo(String nombre, TipoVehiculo tipo, int precio, int capacidadGasolina) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre del vehiculo no puede ser vacio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("el tipo no puede ser nulo");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("el precio tiene que ser mayor a 0");
        }
        if (capacidadGasolina < 0) {
            throw new IllegalArgumentException("la capacidad de gasolina no puede ser negativa");
        }

        this.nombre = nombre.trim();
        this.tipo = tipo;
        this.precio = precio;
        this.capacidadGasolina = capacidadGasolina;
    }

    // getters
    public String getNombre() { return nombre; }
    public TipoVehiculo getTipo() { return tipo; }
    public int getPrecio() { return precio; }
    public int getCapacidadGasolina() { return capacidadGasolina; }

    // devuelve 4 si es auto o 2 si es moto
    public int ruedas() {
        return (tipo == TipoVehiculo.AUTO) ? 4 : 2;
    }

    // calcula el costo de mantenimiento del vehiculo
    public int obtenerCostoPorVehiculo() {
        int costoRueda = (tipo == TipoVehiculo.AUTO) ? PRECIO_RUEDA_AUTO : PRECIO_RUEDA_MOTO;
        return costoRueda * ruedas() + PRECIO_LITRO * capacidadGasolina;
    }

    // devuelve el precio del vehiculo
    public int obtenerPrecioPorVehiculo() {
        return this.precio;
    }

    // devuelve una version corta para mostrar en consola
    public String obtenerVehiculo() {
        return tipo + ": " + nombre;
    }

    // devuelve una linea lista para exportar al csv
    // formato: nombre,precio,tipo,ruedas,capacidadGasolina
    public String obtenerVehiculoParaExportar() {
        return nombre + "," + precio + "," + tipo + "," + ruedas() + "," + capacidadGasolina;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", precio=" + precio +
                ", capacidadGasolina=" + capacidadGasolina +
                ", costoDiario=" + obtenerCostoPorVehiculo() +
                '}';
    }
}

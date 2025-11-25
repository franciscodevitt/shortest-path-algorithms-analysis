package org.ayed.gta;

public class Moto extends Vehiculo {

    public Moto(String nombre, int precio, int capacidadGasolina, int velocidadMaxima) {
        super(nombre, TipoVehiculo.MOTO, precio, capacidadGasolina, velocidadMaxima);
    }

    @Override
    public int ruedas() {
        return 2;
    }

    @Override
    protected int costoPorKilometraje() {
        // Para motos, el costo aumenta en $10 por cada 500 km
        int bloques = this.kilometraje / 500;
        return bloques * 10;
    }
}

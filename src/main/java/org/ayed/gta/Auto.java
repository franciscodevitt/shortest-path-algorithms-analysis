package org.ayed.gta;

public class Auto extends Vehiculo {

    public Auto(String nombre, int precio, int capacidadGasolina, int velocidadMaxima) {
        super(nombre, TipoVehiculo.AUTO, precio, capacidadGasolina, velocidadMaxima);
    }

    @Override
    public int ruedas() {
        return 4;
    }

    @Override
    protected int costoPorKilometraje() {
        // Para autos, el costo aumenta en $10 por cada 1000 km
        int bloques = this.kilometraje / 1000;
        return bloques * 10;
    }
}

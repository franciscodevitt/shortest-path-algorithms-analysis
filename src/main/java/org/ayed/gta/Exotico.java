package org.ayed.gta;

public class Exotico extends Vehiculo {

    private int cantidadRuedas;

    public Exotico(String nombre, int precio, int capacidadGasolina, int velocidadMaxima, int cantidadRuedas) {
        super(nombre, TipoVehiculo.EXOTICO, precio, capacidadGasolina, velocidadMaxima);
        if (cantidadRuedas <= 0) {
            throw new IllegalArgumentException("un vehiculo exotico debe tener al menos una rueda");
        }
        this.cantidadRuedas = cantidadRuedas;
    }

    @Override
    public int ruedas() {
        return this.cantidadRuedas;
    }

    @Override
    protected int costoRuedas() {
        // cada rueda cuesta 10, más 10 por cada rueda anterior
        // 10 + 20 + 30 + ... + 10 * n = 10 * (1 + 2 + ... + n) = 10 * n*(n+1)/2
        int n = this.cantidadRuedas;
        return 10 * n * (n + 1) / 2;
    }

    @Override
    protected int costoPorKilometraje() {
        // Para exóticos, no aumenta por km. En cambio se agrega una cantidad fija de $100.
        return 100;
    }
}

package org.ayed.gta;


import org.ayed.tda.vector.ExcepcionVector;

public class Vehiculo {

    private String nombre;
    private TipoVehiculo tipo;
    private int precio;
    private int capacidadGasolina;


    public static final int PRECIO_RUEDA_MOTO = 30;
    public static final int PRECIO_RUEDA_AUTO = 50;
    public static final int PRECIO_LITRO = 1;

    /**
     * cosntructor del vehiculo
     * @param nombre
     * @param tipo
     * @param precio
     * @param capacidadGasolina
     */
    public Vehiculo(String nombre, TipoVehiculo tipo, int precio, int capacidadGasolina) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.capacidadGasolina = capacidadGasolina;
    }
    /**
     * @return nombre
     */
    public String obtenerNombreVehiculo(){
        return this.nombre;

    }
    /**
     * @return costo de mantenimiento de cada vehiculo
     */
    public int obtenerCostoPorVehiculo(){
        int costo;
        switch (this.tipo){
            case AUTO:
                costo = PRECIO_RUEDA_AUTO * 4 + PRECIO_LITRO * this.capacidadGasolina;
                break;
            case MOTO:
                costo = PRECIO_RUEDA_MOTO * 2 + PRECIO_LITRO * this.capacidadGasolina;
                break;
            default:
                throw new ExcepcionVector("Error al calcular costo.");
        }
        return costo;
    }
    /**
     * @return precio de cada vehiculo
     */
    public int obtenerPrecioPorVehiculo(){
        return this.precio;
    }
    /**
     * @return  vehiculo foramteado para mostrar por consola
     */
    public String obtenerVehiculo(){
        return this.tipo + ": " + this.nombre + "\n";
    }
    /**
     * @return vehiculo formato para exportar csv
     */
    public String obtenerVehiculoParaExportar(){
        //NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA
        String string = null;
        switch (this.tipo){
            case AUTO:
                string = this.nombre + "," + String.valueOf(this.precio) +"," + String.valueOf(this.tipo) +",4," + String.valueOf(this.capacidadGasolina);
                break;
            case MOTO:
                string = this.nombre + "," + String.valueOf(this.precio) +"," + String.valueOf(this.tipo) +",2," + String.valueOf(this.capacidadGasolina);
                break;

        }
        return string;
    }

}

package org.ayed.gta;

import org.ayed.tda.vector.Vector;

import java.io.*;


public class Garaje {
    private int capacidad;
    private Vector flota;
    private int creditos;


    private static final String RUTA = "garaje.csv";
    //ORDE DE DATOS DEL GARAJE IMPORTADO
    // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA
    public static final int NOMBRE = 0;
    public static final int PRECIO = 1;
    public static final int TIPO_VEHICULO = 2;
    public static final int CAPACIDAD_GASAOLINA = 4;
    /**
     * Constructor del garaje
     */

    public Garaje(){
        this.capacidad = 5;
        this.flota = new Vector<>();
        this.creditos = 0;
    }
    /**
     * Agrega un vehiculo al vector flota.
     *
     * @param vehiculo
     * @throws ExcepcionGaraje si la flota esta repleta.
     */
    public void agregarVehiculo(Vehiculo vehiculo) {

        if (this.flota.tamanio() < this.capacidad){
            this.flota.agregar(vehiculo);
            System.out.println("se ha ingresado " + vehiculo.obtenerVehiculo());
        }else if (this.flota.tamanio() == this.capacidad) {
            mejorarGaraje();
            this.flota.agregar(vehiculo);
            System.out.println("se ha ingresado " + vehiculo.obtenerVehiculo());
        }else {
            throw new ExcepcionGaraje("Flota repleta.");
        }

    }
    /**
     * Elimina un vehiculo de la flota, y se lo informa al usuario.
     *
     * @param nombre
     * @throws ExcepcionGaraje si el vehiuclo no existe.
     */

    public void eliminarVehiculo(String nombre) {

        int auxiliar = buscarVehiculoEnFlota(nombre);
        if (auxiliar >= 0) {
            this.flota.eliminar(auxiliar);
            System.out.println("se ha eliminado: " + auxiliar);
        } else {
            throw new ExcepcionGaraje("Vehiculo inexistente.");
        }

    }
    /**
     * Agrega los creditos indicados.
     *
     * @param creditos
     *
     * @throws ExcepcionGaraje si no fue posible agregar los creditos
     */

    public void agregarCreditos(int creditos) {

        if (creditos >= 0) {
            this.creditos = creditos;
        } else {
            throw new ExcepcionGaraje("no fue posible suar los creditos");
        }

    }
    /**
     * mejora el garaje de ser posbile.
     *
     *
     * @throws ExcepcionGaraje si no fue posible mejorar el garaje
     */
    public void mejorarGaraje() {

        if (this.creditos >= 50){
            this.capacidad++;
            this.creditos = this.creditos-50;
        }else {
            throw new ExcepcionGaraje("No es posible mejorar el garaje");
        }


    }
    /**
     * @return el valor total del garaje
     *
     */

    public int obtenerValorTotal() {
        int auxiliar = 0;
        for (int i = 0 ; i < this.flota.tamanio() ; i++){
            Vehiculo vehiculo = (Vehiculo) this.flota.dato(i);
            auxiliar += vehiculo.obtenerPrecioPorVehiculo();

        }
        return auxiliar;
    }
    /**
     * @return el costo de mantemiento total del garaje
     */
    public int obtenerCostoMantenimiento() {
        int auxiliar = 0;
        for (int i = 0 ; i < this.flota.tamanio() ; i++){
            Vehiculo vehiculo = (Vehiculo) this.flota.dato(i);
            auxiliar += vehiculo.obtenerCostoPorVehiculo();
        }
        return auxiliar;

    }
    /**
     * busca un vehiculo en especifico
     *
     * @param nombre
     * @return indice del vehiculo indicado
     */

    public int buscarVehiculoEnFlota(String nombre){
        int auxiliar = -1;
        for (int i = 0 ; i < this.flota.tamanio() ; i++){
            Vehiculo vehiculo = (Vehiculo) this.flota.dato(i);
            if (vehiculo.obtenerNombreVehiculo().equals(nombre)){
                auxiliar = i;
            }
        }


        return auxiliar;
    }
    /**
     * imprime en consola todos los vehiculos de la flota
     *

     */
    public void listarVehiculos(){
        for (int i = 0 ; i < this.flota.tamanio() ; i++){
            Vehiculo vehiculo = (Vehiculo) this.flota.dato(i);
            String stringVehiculo = vehiculo.obtenerVehiculo();
            System.out.println(stringVehiculo);
        }
    }
    /**
     * Exporta el garaje a un archivo csv
     */

    public  void exportarGaraje() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA))) {
            String linea = String.valueOf(this.capacidad)+","+ String.valueOf(this.creditos);
            writer.println(linea);
            for (int i = 0 ; i < this.flota.tamanio() ; i++) {
                Vehiculo vehiculoParaExportar = (Vehiculo) this.flota.dato(i);
                writer.println(vehiculoParaExportar.obtenerVehiculoParaExportar());
            }
        } catch (IOException e) {
            System.out.println("No se pudo exportar el garaje" + e.getMessage());
        }
    }

    /**
     * Importa el garaje a un archivo csv
     */
    public void importarGaraje(){
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            if ((linea = reader.readLine()) != null){
                String [] parte= linea.split(",");

                this.creditos = Integer.parseInt(parte[0]);
                this.capacidad = Integer.parseInt(parte[1]);
            }
            while ((linea = reader.readLine()) != null) {
                //NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA

                String [] parte = linea.split(",");

                String nombre = parte[NOMBRE];
                TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(parte[TIPO_VEHICULO]);
                int precio = Integer.parseInt(parte[PRECIO]);
                int capacidadGasolina = Integer.parseInt(parte[CAPACIDAD_GASAOLINA]);


                Vehiculo vehiculo = new Vehiculo(nombre, tipoVehiculo, precio, capacidadGasolina);
                agregarVehiculo(vehiculo);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer la clasificación: " + e.getMessage());
        }
    }
}

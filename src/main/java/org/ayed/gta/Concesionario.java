package org.ayed.gta;

import org.ayed.tda.vector.Vector;
import java.io.*;

public class Concesionario {

    private Vector<Vehiculo> vehiculos;

    // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA, GASOLINA_ACTUAL, KILOMETRAJE
    public static final int NOMBRE = 0;
    public static final int PRECIO = 1;
    public static final int TIPO_VEHICULO = 2;
    public static final int CAPACIDAD_GASOLINA = 4;
    public static final int GASOLINA_ACTUAL = 5;
    public static final int KILOMETRAJE = 6;
    public static final int VELOCIDAD_MAXIMA = 7;

    public Concesionario() {
        this.vehiculos = new Vector<>();
    }

    private void buscar(String s){
        String filtro = s.toLowerCase();
        int max = vehiculos.tamanio();
        boolean encontrado = false;

        for (int i = 0; i < max; i++) {
            Vehiculo v = vehiculos.dato(i);
            if (v.obtenerNombreVehiculo().toLowerCase().contains(filtro)) {
                System.out.println(v.obtenerVehiculo());
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron vehículos con coincidencias en el nombre/marca.");
        }
    }

    public void buscarPorNombre(String nombre) {
        buscar(nombre);
    }

    public void buscarPorMarca(String marca) {
        buscar(marca);
    }

    public void comprarVehiculo(String nombreExacto, Garaje garaje) {
        int max = vehiculos.tamanio();
        int i = 0;
        boolean encontrado = false;

        while (i < max && !encontrado) {
            Vehiculo v = vehiculos.dato(i);
            if (v.obtenerNombreVehiculo().equals(nombreExacto)) {
                encontrado = true;
                if (garaje.getDinero() >= v.obtenerPrecioPorVehiculo()) {
                    System.out.println("Vehículo comprado: " + v.getNombre() + ", Precio: $" + v.getPrecio());
                    garaje.agregarVehiculo(v); // se compra → se elimina del concesionario
                    vehiculos.eliminar(i);
                } else {
                    throw new IllegalArgumentException("No tienes suficiente dinero para comprar el vehículo: " + nombreExacto);
                }
            }
            i++;
        }
        if (!encontrado) {
            throw new IllegalArgumentException("No se encontró el vehículo con nombre exacto: " + nombreExacto);
        }

    }

    public void listarVehiculos() {
        System.out.println("===== Vehículos en CONCESIONARIO (" + vehiculos.tamanio() + ") =====");
        if (vehiculos.vacio()) System.out.println("(vacío)");
        for (int i = 0; i < vehiculos.tamanio(); i++) {
            System.out.println(vehiculos.dato(i).obtenerVehiculo());
        }
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.agregar(vehiculo);
    }

    // -------- MÉTODOS DE EXPORT/IMPORT --------

    /**
     * Exporta el concesionario a un archivo CSV con formato:
     * #CONCESIONARIO
     * NOMBRE,PRECIO,TIPO,CANTIDAD_RUEDAS,CAPACIDAD_GASOLINA,GASOLINA_ACTUAL,KILOMETRAJE
     * ...
     */

    public void exportarConcesionario(String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println("#CONCESIONARIO");
            for (int i = 0; i < vehiculos.tamanio(); i++) {
                writer.println(vehiculos.dato(i).obtenerVehiculoParaExportar());
            }
        } catch (IOException e) {

            throw new ExcepcionGaraje("No se pudo exportar el concesionario: " + e.getMessage());
        }
    }

    /**
     * Importa un concesionario desde un archivo CSV.
     * Formato esperado:
     * #CONCESIONARIO
     * NOMBRE,PRECIO,TIPO,CANTIDAD_RUEDAS,CAPACIDAD_GASOLINA,GASOLINA_ACTUAL,KILOMETRAJE
     * ...
     */
    public void importarConcesionario(String ruta) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            // Reiniciar estructura
            this.vehiculos = new Vector<>();

            String linea;
            boolean enConcesionario = false;

            // Leer el archivo
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Detectar sección
                if (linea.startsWith("#")) {
                    if (linea.equalsIgnoreCase("#CONCESIONARIO")) {
                        enConcesionario = true;
                    } else {
                        enConcesionario = false;
                    }
                    continue;
                }

                // Parsear vehículo si estamos en la sección correcta
                if (enConcesionario) {
                    Vehiculo v = parsearVehiculoDesdeCsv(linea);
                    vehiculos.agregar(v);
                }
            }

        } catch (IOException e) {
            throw new ExcepcionGaraje("No se pudo leer el concesionario: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new ExcepcionGaraje("Formato inválido en CSV: " + e.getMessage());
        }
    }

    /**
     * Parsea una línea CSV y crea un Vehiculo correspondiente.
     * Formato: nombre,precio,tipo,ruedas,capacidadGasolina,gasolinaActual,kilometraje
     */
    private Vehiculo parsearVehiculoDesdeCsv(String linea) {
        // formato: nombre, precio, tipo, ruedas, capacidadGasolina, gasolinaActual, kilometraje
        String[] parte = linea.split(",");

        String nombre = parte[NOMBRE].trim();
        int precio = Integer.parseInt(parte[PRECIO].trim());
        TipoVehiculo tipo = TipoVehiculo.valueOf(parte[TIPO_VEHICULO].trim().toUpperCase());
        int ruedas = Integer.parseInt(parte[3].trim());
        int capacidadGasolina = Integer.parseInt(parte[CAPACIDAD_GASOLINA].trim());
        int gasolinaActual = Integer.parseInt(parte[GASOLINA_ACTUAL].trim());
        int kilometraje = Integer.parseInt(parte[KILOMETRAJE].trim());
        int velocidadMaxima = Integer.parseInt(parte[VELOCIDAD_MAXIMA].trim());

        Vehiculo vehiculo;
        switch (tipo) {
            case AUTO:
                vehiculo = new Auto(nombre, precio, capacidadGasolina, velocidadMaxima);
                break;
            case MOTO:
                vehiculo = new Moto(nombre, precio, capacidadGasolina, velocidadMaxima);
                break;
            case EXOTICO:
                vehiculo = new Exotico(nombre, precio, capacidadGasolina, velocidadMaxima, ruedas);
                break;
            default:
                throw new ExcepcionGaraje("Tipo de vehiculo desconocido: " + tipo);
        }

        // restaurar estado
        vehiculo.cargarCombustible(gasolinaActual);
        vehiculo.sumarKilometros(kilometraje);
        return vehiculo;

    }

    // ---- GETTERS ----
    public Vector<Vehiculo> getVehiculos() {
        return vehiculos;
    }
    
    public int getCantidadVehiculos() {
        return vehiculos.tamanio();
    }
    
    public Vehiculo getVehiculo(int indice) {
        return vehiculos.dato(indice);
    }
    
    // ---- SETTERS ----
    public void setVehiculos(Vector<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

}

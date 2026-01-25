package org.ayed.gta;

import org.ayed.tda.vector.Vector;
import java.io.*;

public class Concesionario {

    private Vector<Vehiculo> vehiculos;

    private void buscar(String s){
        String filtro = s.toLowerCase();
        int max = vehiculos.tamanio();
        boolean encontrado = false;

        for (int i = 0; i < max; i++) {
            Vehiculo v = vehiculos.dato(i);
            if (v.getNombre().toLowerCase().contains(filtro)) {
                System.out.println(v.getNombre() + ", Precio: $" + v.getPrecio());
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

    public Vehiculo comprarVehiculo(String nombreExacto) {
        int max = vehiculos.tamanio();

        for (int i = 0; i < max; i++) {
            Vehiculo v = vehiculos.dato(i);
            if (v.getNombre().equals(nombreExacto)) {
                return vehiculos.eliminar(i); // se compra → se elimina del concesionario
            }
        }

        throw new IllegalArgumentException("No existe un vehículo con nombre: " + nombreExacto);
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
    
    // exportar vehículos del concesionario a CSV
    public void exportarConcesionario(String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println("#CONCESIONARIO");
            for (int i = 0; i < vehiculos.tamanio(); i++) {
                writer.println(vehiculos.dato(i).obtenerVehiculoParaExportar());
            }
        } catch (IOException e) {
            throw new ExcepcionVehiculo("No se pudo exportar el concesionario: " + e.getMessage());
        }
    }
    
    // importar vehículos del concesionario desde CSV
    public void importarConcesionario(String ruta) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            this.vehiculos = new Vector<>();
            
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                
                Vehiculo v = parsearVehiculoDesdeCsv(linea);
                vehiculos.agregar(v);
            }
        } catch (IOException e) {
            throw new ExcepcionVehiculo("No se pudo leer el concesionario: " + e.getMessage());
        }
    }
    
    private Vehiculo parsearVehiculoDesdeCsv(String linea) {
        // formato: nombre,precio,tipo,ruedas,capacidadGasolina
        String[] parte = linea.split(",");
        
        String nombre = parte[0].trim();
        int precio = Integer.parseInt(parte[1].trim());
        TipoVehiculo tipo = TipoVehiculo.valueOf(parte[2].trim().toUpperCase());
        int ruedas = Integer.parseInt(parte[3].trim());
        int capacidadGasolina = Integer.parseInt(parte[4].trim());
        
        int velocidadMaxima = 100; // valor por defecto
        
        switch (tipo) {
            case AUTO:
                return new Auto(nombre, precio, capacidadGasolina, velocidadMaxima);
            case MOTO:
                return new Moto(nombre, precio, capacidadGasolina, velocidadMaxima);
            case EXOTICO:
                return new Exotico(nombre, precio, capacidadGasolina, velocidadMaxima, ruedas);
            default:
                throw new ExcepcionVehiculo("Tipo de vehiculo desconocido: " + tipo);
        }
    }
}

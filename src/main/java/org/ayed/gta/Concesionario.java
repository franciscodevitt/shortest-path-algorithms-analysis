package org.ayed.gta;

import org.ayed.tda.vector.Vector;

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
    
    
}

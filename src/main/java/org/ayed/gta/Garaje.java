// interfaz esperada para Cola (para que el garage la use despues)
// guia para cuando se suba el TDA real
// ---------------------------------------------------------
// interface Cola<T> {
//     void encolar(T x);
//     T desencolar();
//     T frente();          // opcional
//     boolean vacia();
//     int tamaño();       // opcional
// }


package org.ayed.gta;

import org.ayed.tda.vector.Vector;
import org.ayed.tda.lista.Cola;
import java.io.*;

public class Garaje {
    private int capacidad;                       // lugares dentro del garaje
    private int creditos;                        // moneda para mejorar el garaje
    private Vector<Vehiculo> vehiculosEnGaraje;  // vehículos utilizables (adentro)
    private Cola<Vehiculo> zonaDeEspera;         // cola FIFO real de vehículos que no entraron

    private static final String RUTA = "garaje.csv";

    
    // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA
    public static final int NOMBRE = 0;
    public static final int PRECIO = 1;
    public static final int TIPO_VEHICULO = 2;
    public static final int CAPACIDAD_GASOLINA = 4;

    public Garaje() {
        this.capacidad = 5;
        this.creditos = 0;
        this.vehiculosEnGaraje = new Vector<>();
        this.zonaDeEspera = new Cola<>();

    }

    // -----------------------------
    // operaciones principales
    // -----------------------------

    /**
     * si hay lugar, entra al garaje. Si no, va a la zona de espera (FIFO) y no se puede usar.
     */
    public void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculosEnGaraje.tamanio() < capacidad) {
            vehiculosEnGaraje.agregar(vehiculo);
            System.out.println("Ingresó al garaje: " + vehiculo.obtenerVehiculo());
        } else {
            zonaDeEspera.agregar(vehiculo); // FIFO real de tu Cola
            System.out.println("Garaje lleno. Agregado a zona de espera: " + vehiculo.obtenerVehiculo());
        }
    }

    /**
     * elimina por nombre desde el GARAGE (no de la espera).
     * si hay en espera, ingresa automaticamente el primero (FIFO).
     */
    public void eliminarVehiculo(String nombre) {
        int idx = buscarVehiculoEnGaraje(nombre);
        if (idx < 0) throw new ExcepcionGaraje("Vehículo inexistente en el garaje.");
        Vehiculo eliminado = vehiculosEnGaraje.eliminar(idx);
        System.out.println("Eliminado del garaje: " + eliminado.obtenerVehiculo());

     // si se libera un espacio en el garaje, el primer vehiculo en espera entra automaticamente
     // TODO: cuando haya una Cola real, cambiar eliminar(0) por desencolar()
        if (!zonaDeEspera.vacio() && vehiculosEnGaraje.tamanio() < capacidad) {
            Vehiculo siguiente = zonaDeEspera.eliminar();   // FIFO real
            vehiculosEnGaraje.agregar(siguiente);
            System.out.println("Ingresó desde zona de espera: " + siguiente.obtenerVehiculo());
        }

    }

    /**
     * suma creditos (no reemplaza).
     */
    public void agregarCreditos(int creditos) {
        if (creditos < 0) throw new ExcepcionGaraje("No se pueden agregar créditos negativos.");
        this.creditos += creditos;
    }

    /**
     * cuesta 50 creditos, +1 capacidad, y sube los primeros de la espera hasta llenar.
     */
    public void mejorarGaraje() {
        if (this.creditos < 50) throw new ExcepcionGaraje("Créditos insuficientes para mejorar.");
        this.creditos -= 50;
        this.capacidad++;

        while (vehiculosEnGaraje.tamanio() < capacidad && !zonaDeEspera.vacio()) {
            Vehiculo siguiente = zonaDeEspera.eliminar();
            vehiculosEnGaraje.agregar(siguiente);
            System.out.println("Ingresó desde espera por mejora: " + siguiente.obtenerVehiculo());
        }
    }

    /**
     * suma el precio de los vehiculos en el GARAGE (los de espera no cuentan).
     */
    public int obtenerValorTotal() {
        int total = 0;
        for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
            Vehiculo v = vehiculosEnGaraje.dato(i);
            total += v.obtenerPrecioPorVehiculo();
        }
        return total;
    }

    /**
     * suma el costo diario de los que estan en el GARAGE (los de espera no generan costo).
     */
    public int obtenerCostoMantenimiento() {
        int total = 0;
        for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
            Vehiculo v = vehiculosEnGaraje.dato(i);
            total += v.obtenerCostoPorVehiculo();
        }
        return total;
    }

    // -----------------------------
    // utilidades
    // -----------------------------

    /**
     * busca por nombre en el GARAGE (case-insensitive). devuelve indice o -1.
     */
    public int buscarVehiculoEnGaraje(String nombre) {
        for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
            Vehiculo v = vehiculosEnGaraje.dato(i);
            if (v.obtenerNombreVehiculo().equalsIgnoreCase(nombre)) return i;
        }
        return -1;
    }

    /**
     * muestra vehículos del garaje y de la espera por separado.
     */
    public void listarVehiculos() {
        System.out.println("===== Vehículos en GARAGE (" + vehiculosEnGaraje.tamanio() + "/" + capacidad + ") =====");
        if (vehiculosEnGaraje.vacio()) System.out.println("(vacío)");
        for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
            System.out.println(vehiculosEnGaraje.dato(i).obtenerVehiculo());
        }

        System.out.println("===== Zona de ESPERA (" + zonaDeEspera.tamanio() + ") =====");
        if (zonaDeEspera.vacio()) {
            System.out.println("(sin espera)");
        } else {
            int n = zonaDeEspera.tamanio();
            for (int i = 0; i < n; i++) {
                Vehiculo v = zonaDeEspera.eliminar();              // sale el primero
                System.out.println("[espera " + (i + 1) + "] " + v.obtenerVehiculo());
                zonaDeEspera.agregar(v);                            // lo vuelvo a poner al final
            }
        }
    }

    public int getCreditos() { return this.creditos; }
    public int getCapacidad() { return this.capacidad; }
    public Vector<Vehiculo> getVehiculosEnGaraje() { return vehiculosEnGaraje; }
    public Cola<Vehiculo> getZonaDeEspera() { return zonaDeEspera; }

    // -----------------------------
    // CSV con dos secciones
    // -----------------------------

    /**
     * formato:
     *   CAPACIDAD,CREDITOS
     *   #GARAJE
     *   NOMBRE,PRECIO,TIPO,CANTIDAD_RUEDAS,CAPACIDAD_GASOLINA
     *   ...
     *   #ESPERA
     *   NOMBRE,PRECIO,TIPO,CANTIDAD_RUEDAS,CAPACIDAD_GASOLINA
     *   ...
     */
    public void exportarGaraje() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA))) {
            // meta
            writer.println(capacidad + "," + creditos);

            // garaje
            writer.println("#GARAJE");
            for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
                writer.println(vehiculosEnGaraje.dato(i).obtenerVehiculoParaExportar());
            }

            // espera
            writer.println("#ESPERA");
            int n = zonaDeEspera.tamanio();
            for (int i = 0; i < n; i++) {
                Vehiculo v = zonaDeEspera.eliminar();             // saco el primero
                writer.println(v.obtenerVehiculoParaExportar());  // lo escribo en el CSV
                zonaDeEspera.agregar(v);                          // lo vuelvo a encolar
            }
        } catch (IOException e) {
            throw new ExcepcionGaraje("No se pudo exportar el garaje: " + e.getMessage());
        }
    }


    public void importarGaraje() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA))) {

            // ---- 1) Leer la primera linea: meta ----
            String linea = reader.readLine();
            if (linea != null) {
                String[] meta = linea.split(",");
                this.capacidad = Integer.parseInt(meta[0].trim());
                this.creditos  = Integer.parseInt(meta[1].trim());
            }

            // ---- 2) Reiniciar estructuras ----
            this.vehiculosEnGaraje = new Vector<>();
            this.zonaDeEspera = new Cola<>(); // tu Cola real

            // ---- 3) Variable para saber en que sección estamos ----
            String seccionActual = null;

            // ---- 4) Leer el resto del archivo ----
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Detectar secciones
                if (linea.startsWith("#")) {
                    if (linea.equalsIgnoreCase("#GARAJE")) {
                        seccionActual = "GARAJE";
                    } else if (linea.equalsIgnoreCase("#ESPERA")) {
                        seccionActual = "ESPERA";
                    } else {
                        seccionActual = null; // secciones desconocidas
                    }
                    continue;
                }

                // ---- 5) Parsear vehiculo (una linea normal) ----
                Vehiculo v = parsearVehiculoDesdeCsv(linea);

                // ---- 6) Guardar en la estructura correspondiente ----
                if ("GARAJE".equals(seccionActual)) {
                    vehiculosEnGaraje.agregar(v);
                } else if ("ESPERA".equals(seccionActual)) {
                    zonaDeEspera.agregar(v);
                }
            }

            // ---- 7) Ajustar si habia mas autos que cap ----
            while (vehiculosEnGaraje.tamanio() > capacidad) {
                Vehiculo v = vehiculosEnGaraje.eliminar(vehiculosEnGaraje.tamanio() - 1);
                zonaDeEspera.agregar(v);
            }

        } catch (IOException e) {
            throw new ExcepcionGaraje("No se pudo leer el garaje: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new ExcepcionGaraje("Formato inválido en CSV: " + e.getMessage());
        }
    }



    
    private Vehiculo parsearVehiculoDesdeCsv(String linea) {
        // formato: nombre, precio, tipo, ruedas, capacidadGasolina
        String[] parte = linea.split(",");

        String nombre = parte[NOMBRE].trim();
        int precio = Integer.parseInt(parte[PRECIO].trim());
        TipoVehiculo tipo = TipoVehiculo.valueOf(parte[TIPO_VEHICULO].trim().toUpperCase());
        int ruedas = Integer.parseInt(parte[3].trim());
        int capacidadGasolina = Integer.parseInt(parte[CAPACIDAD_GASOLINA].trim());

        // por ahora ponemos una velocidad max por defecto, se puede ajustar despues
        int velocidadMaxima = 100;

        switch (tipo) {
            case AUTO:
                return new Auto(nombre, precio, capacidadGasolina, velocidadMaxima);
            case MOTO:
                return new Moto(nombre, precio, capacidadGasolina, velocidadMaxima);
            case EXOTICO:
                return new Exotico(nombre, precio, capacidadGasolina, velocidadMaxima, ruedas);
            default:
                throw new ExcepcionGaraje("Tipo de vehiculo desconocido: " + tipo);
        }
    }

}

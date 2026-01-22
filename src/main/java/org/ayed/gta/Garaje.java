// interfaz esperada para Cola (para que el garage la use despues)
// guia para cuando se suba el TDA real
// ---------------------------------------------------------
// interface Cola<T> {
//     void encolar(T x);
//     T desencolar();
//     T frente();          // opcional
//     boolean vacia();
//     int tamanio();       // opcional
// }


//TODO: Cargar todos los vehiculos con gasolina
// Cargar un vehiculo X cantidad de gasolina
// Cargar por completo un vehiculo.


package org.ayed.gta;

import org.ayed.tda.vector.Vector;
import org.ayed.tda.lista.Cola;
import java.io.*;

public class Garaje {
    private int capacidad;                       // lugares dentro del garaje
    private int creditos;                        // moneda para mejorar el garaje
    private Vector<Vehiculo> vehiculosEnGaraje;  // vehículos utilizables (adentro)
    private Cola<Vehiculo> zonaDeEspera;         // cola FIFO real de vehículos que no entraron

    private int dinero;

    
    // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA, GASOLINA_ACTUAL, KILOMETRAJE
    public static final int NOMBRE = 0;
    public static final int PRECIO = 1;
    public static final int TIPO_VEHICULO = 2;
    public static final int CAPACIDAD_GASOLINA = 4;
    public static final int GASOLINA_ACTUAL = 5;
    public static final int KILOMETRAJE = 6;

    public Garaje() {
        this.capacidad = 5;
        this.creditos = 0;
        this.vehiculosEnGaraje = new Vector<>();
        this.zonaDeEspera = new Cola<>();
        this.dinero = 0;

    }

    // -----------------------------
    // operaciones principales
    // -----------------------------

    /**
     * si hay lugar, entra al garaje. Si no, va a la zona de espera (FIFO) y NO se puede usar.
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
     * vende un vehículo por su nombre, agrega el dinero al garaje.
     * si hay en espera, ingresa automaticamente el primero (FIFO).
     */
    public void venderVehiculo(String nombre) {
        int idx = buscarVehiculoEnGaraje(nombre);
        if (idx < 0) throw new ExcepcionGaraje("Vehículo inexistente en el garaje.");
        Vehiculo vendido = vehiculosEnGaraje.eliminar(idx);
        int precioVenta = vendido.obtenerPrecioPorVehiculo();
        this.dinero += precioVenta;
        System.out.println("Vehículo vendido: " + vendido.obtenerVehiculo() + " por $" + precioVenta);

     // si se libera un espacio en el garaje, el primer vehiculo en espera entra automaticamente.
        if (!zonaDeEspera.vacio() && vehiculosEnGaraje.tamanio() < capacidad) {
            Vehiculo siguiente = zonaDeEspera.eliminar();   // FIFO real
            vehiculosEnGaraje.agregar(siguiente);
            System.out.println("Ingresó desde zona de espera: " + siguiente.obtenerVehiculo());
        }
    }

    /**
     * carga gasolina a todos los vehículos en el GARAGE hasta su capacidad máxima.
     */
    public void cargarTodosLosVehiculos() {
        if (vehiculosEnGaraje.vacio()) {
            System.out.println("No hay vehículos en el garaje para cargar.");
            return;
        }
        
        for (int i = 0; i < vehiculosEnGaraje.tamanio(); i++) {
            Vehiculo v = vehiculosEnGaraje.dato(i);
            int litrosCargados = v.cargarAlMaximo();
            System.out.println(v.obtenerVehiculo() + " cargado: +" + litrosCargados + " litros");
        }
    }

    /**
     * carga una cantidad específica de gasolina a un vehículo en el GARAGE.
     */
    public void cargarVehiculo(String nombre, int litros) {
        int idx = buscarVehiculoEnGaraje(nombre);
        if (idx < 0) throw new ExcepcionGaraje("Vehículo inexistente en el garaje.");
        
        Vehiculo v = vehiculosEnGaraje.dato(idx);
        int litrosCargados = v.cargarCombustible(litros);
        System.out.println(v.obtenerVehiculo() + " cargado: +" + litrosCargados + " litros");
    }

    /**
     * carga un vehículo al máximo de su capacidad.
     */
    public void cargarVehiculoAlMaximo(String nombre) {
        int idx = buscarVehiculoEnGaraje(nombre);
        if (idx < 0) throw new ExcepcionGaraje("Vehículo inexistente en el garaje.");
        
        Vehiculo v = vehiculosEnGaraje.dato(idx);
        int litrosCargados = v.cargarAlMaximo();
        System.out.println(v.obtenerVehiculo() + " cargado al máximo: +" + litrosCargados + " litros");
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
                Vehiculo v = zonaDeEspera.eliminar();              // saco el primero
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
    public void exportarGaraje(String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            // meta
            writer.println(capacidad + "," + creditos + "," + dinero);

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


    public void importarGaraje(String ruta) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {

            // ---- 1) Leer la primera línea: meta ----
            String linea = reader.readLine();
            if (linea != null) {
                String[] meta = linea.split(",");
                this.capacidad = Integer.parseInt(meta[0].trim());
                this.creditos  = Integer.parseInt(meta[1].trim());
                this.dinero    = Integer.parseInt(meta[2].trim());
            }

            // ---- 2) Reiniciar estructuras ----
            this.vehiculosEnGaraje = new Vector<>();
            this.zonaDeEspera = new Cola<>(); // tu Cola real

            // ---- 3) Variable para saber en qué sección estamos ----
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

                // ---- 5) Parsear vehículo (una línea normal) ----
                Vehiculo v = parsearVehiculoDesdeCsv(linea);

                // ---- 6) Guardar en la estructura correspondiente ----
                if ("GARAJE".equals(seccionActual)) {
                    vehiculosEnGaraje.agregar(v);
                } else if ("ESPERA".equals(seccionActual)) {
                    zonaDeEspera.agregar(v);
                }
            }

            // ---- 7) Ajustar si había más autos que capacidad ----
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
        // formato: nombre, precio, tipo, ruedas, capacidadGasolina, gasolinaActual, kilometraje
        String[] parte = linea.split(",");

        String nombre = parte[NOMBRE].trim();
        int precio = Integer.parseInt(parte[PRECIO].trim());
        TipoVehiculo tipo = TipoVehiculo.valueOf(parte[TIPO_VEHICULO].trim().toUpperCase());
        int ruedas = Integer.parseInt(parte[3].trim());
        int capacidadGasolina = Integer.parseInt(parte[CAPACIDAD_GASOLINA].trim());
        int gasolinaActual = Integer.parseInt(parte[GASOLINA_ACTUAL].trim());
        int kilometraje = Integer.parseInt(parte[KILOMETRAJE].trim());

        // por ahora ponemos una velocidad maxima por defecto, se puede ajustar después
        int velocidadMaxima = 100;

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

}

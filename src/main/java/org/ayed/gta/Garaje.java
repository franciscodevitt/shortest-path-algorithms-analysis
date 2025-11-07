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


package org.ayed.gta;

import org.ayed.tda.vector.Vector;

import java.io.*;

public class Garaje {
    private int capacidad;                       // lugares dentro del garaje
    private int creditos;                        // moneda para mejorar el garaje
    private Vector<Vehiculo> vehiculosEnGaraje;  // vehículos utilizables (adentro)
    // TODO: cuando esté el TDA Cola real, reemplazar este Vector por Cola<Vehiculo
    private Vector<Vehiculo> zonaDeEspera;       // cola FIFO de vehículos que no entraron (no utilizables)

    private static final String RUTA = "garaje.csv";

    
    // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA
    public static final int NOMBRE = 0;
    public static final int PRECIO = 1;
    public static final int TIPO_VEHICULO = 2;
    public static final int CAPACIDAD_GASAOLINA = 4;

    public Garaje() {
        this.capacidad = 5;
        this.creditos = 0;
        this.vehiculosEnGaraje = new Vector<>();
        this.zonaDeEspera = new Vector<>();
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
        	// TODO: cuando haya una Cola real, cambiar agregar() por encolar()
            zonaDeEspera.agregar(vehiculo); // al final de la cola
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

     // si se libera un espacio en el garaje, el primer vehiculo en espera entra automaticamente.
     // TODO: cuando haya una Cola real, cambiar eliminar(0) por desencolar()
        if (!zonaDeEspera.vacio() && vehiculosEnGaraje.tamanio() < capacidad) {
            Vehiculo siguiente = zonaDeEspera.eliminar(0);
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
            Vehiculo siguiente = zonaDeEspera.eliminar(0);
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
        if (zonaDeEspera.vacio()) System.out.println("(sin espera)");
        for (int i = 0; i < zonaDeEspera.tamanio(); i++) {
            System.out.println("[espera " + (i + 1) + "] " + zonaDeEspera.dato(i).obtenerVehiculo());
        }
    }

    public int getCreditos() { return this.creditos; }
    public int getCapacidad() { return this.capacidad; }
    public Vector<Vehiculo> getVehiculosEnGaraje() { return vehiculosEnGaraje; }
    public Vector<Vehiculo> getZonaDeEspera() { return zonaDeEspera; }

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
            for (int i = 0; i < zonaDeEspera.tamanio(); i++) {
                writer.println(zonaDeEspera.dato(i).obtenerVehiculoParaExportar());
            }
        } catch (IOException e) {
            throw new ExcepcionGaraje("No se pudo exportar el garaje: " + e.getMessage());
        }
    }

    public void importarGaraje() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA))) {
            String linea = reader.readLine();
            if (linea != null) {
                String[] meta = linea.split(",");
                this.capacidad = Integer.parseInt(meta[0].trim());
                this.creditos  = Integer.parseInt(meta[1].trim());
            }

            // reset listas
            this.vehiculosEnGaraje = new Vector<>();
            this.zonaDeEspera = new Vector<>();

            // leer secciones
            leerSeccion(reader, "#GARAJE", vehiculosEnGaraje);
            leerSeccion(reader, "#ESPERA", zonaDeEspera);

         // si el archivo CSV contiene mas vehiculos de los que entran en el garaje,
         // los vehiculos excedentes se mueven automaticamente a la zona de espera
         // para mantenerlos registrados sin superar la capacidad maxima.

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

    private static void leerSeccion(BufferedReader br, String marca, Vector<Vehiculo> destino) throws IOException {
        String linea;

   
        // avanza en el archivo hasta encontrar la sección indicada (#GARAJE o #ESPERA)
        while ((linea = br.readLine()) != null && !linea.startsWith(marca)) { }

        if (linea == null) return; // seccion no encontrada - no hay nada que leer

        br.mark(1 << 20);
        while ((linea = br.readLine()) != null && !linea.startsWith("#")) {
            if (linea.trim().isEmpty()) { br.mark(1 << 20); continue; }

            // NOMBRE, PRECIO, TIPO, CANTIDAD_RUEDAS, CAPACIDAD_GASOLINA
            String[] parte = linea.split(",");
            String nombre = parte[NOMBRE].trim();
            int precio = Integer.parseInt(parte[PRECIO].trim());
            TipoVehiculo tipo = TipoVehiculo.valueOf(parte[TIPO_VEHICULO].trim().toUpperCase());
            int capacidadGasolina = Integer.parseInt(parte[CAPACIDAD_GASAOLINA].trim());

            Vehiculo vehiculo = new Vehiculo(nombre, tipo, precio, capacidadGasolina);
            destino.agregar(vehiculo);

            br.mark(1 << 20);
        }
        if (linea != null && linea.startsWith("#")) br.reset();
    }
}

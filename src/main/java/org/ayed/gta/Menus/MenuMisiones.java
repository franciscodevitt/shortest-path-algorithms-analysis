package org.ayed.gta.Menus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.TipoVehiculo;
import org.ayed.gta.Vehiculo;
import org.ayed.gta.mapa.Mapa;
import org.ayed.gta.ui.MisionView;
import org.ayed.tda.vector.Vector;
import org.ayed.gta.ExcepcionGaraje;
import org.ayed.gta.Exotico;

// Menu misiones

// Seleccionar dificultad:
// 	1. Facil, 	1000 segundos, $1000, 10creditos
// 	2. Normal	500 segundos, $2500, 20creditos
// 	3. Dificil	250 segundos, $7500, 50creditos
// 	0. Volver al menu
// --------------------------------
// 	-Vehiculo 1 Gasolina: X -
// 	-Vehiculo 2 Gasolina: X -
// 	-Vehiculo 3 Gasolina: X -
// 	-Vehiculo 4 Gasolina: X -
// 	-Vehiculo 5 Gasolina: X -
// 	Elegir vehiculo("NO" para cancelar):
// 	(Si mision dificil y no hay exoticos tirar exepcion)
// ---------------------------------
// 	Recargar Gasolina? (S/N)("NO" para cancelar):
// --------- Empieza mision ----------


public class MenuMisiones {

    private Garaje garaje;
    private Scanner scanner;

    // Opciones del menú
    private static final int FACIL = 1;
    private static final int NORMAL = 2;
    private static final int DIFICIL = 3;
    private static final int VOLVER_AL_MENU = 0;

    public MenuMisiones(Garaje garaje) {
        this.garaje = garaje;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        // Elegir dificultad
        opcion = menuDificultad();
        opcionDificultad(opcion);

    }

    private int menuDificultad(){
        limpiarConsola();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          DIFICULTAD                  ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Fácil 1000 segundos, $1000, 10c   ║");
        System.out.println("║ 2. Normal 500 segundos, $2500, 20c   ║");
        System.out.println("║ 3. Difícil 250 segundos, $7500, 50c  ║");
        System.out.println("║ 0. Volver al menú                    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Ingrese una opción válida: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }
    private void opcionDificultad(int opcion){
        switch (opcion) {
            case FACIL:
                System.out.println("Misión Fácil seleccionada.");
                pausar();
                seleccionarVehiculo(FACIL);
                break;
            case NORMAL:
                System.out.println("Misión Normal seleccionada.");
                pausar();
                seleccionarVehiculo(NORMAL);
                break;
            case DIFICIL:
                // Validar que haya al menos un vehículo exótico
                if (!hayVehiculoExotico()) {
                    System.out.println("¡Error! Para misiones difíciles necesitas al menos 1 vehículo EXÓTICO.");
                    pausar();
                    // Reiniciar el menú
                    iniciar();
                } else {
                    System.out.println("Misión Difícil seleccionada.");
                    pausar();
                    seleccionarVehiculo(DIFICIL);
                }
                break;
            case VOLVER_AL_MENU:
                System.out.println("Volviendo al menú principal.");
                break;
            default:
                System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                pausar();
        }
    }

    /**
     * Verifica si hay al menos un vehículo exótico en el garaje.
     */
    private boolean hayVehiculoExotico() {
        for (int i = 0; i < garaje.getVehiculosEnGaraje().tamanio(); i++) {
            Vehiculo v = garaje.getVehiculosEnGaraje().dato(i);
            if (v instanceof Exotico) {
                return true;
            }
        }
        return false;
    }

    /**
     * Muestra los vehículos disponibles según la dificultad y permite al usuario seleccionar uno.
     * Para FÁCIL y NORMAL: muestra todos los vehículos del garaje.
     * Para DIFÍCIL: muestra solo los vehículos exóticos.
     */
    private void seleccionarVehiculo(int dificultad) {
        limpiarConsola();
        
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      SELECCIONAR VEHÍCULO            ║");
        System.out.println("╠══════════════════════════════════════╣");
        
        int contador = 0;
        
        // Mostrar vehículos según dificultad
        for (int i = 0; i < garaje.getVehiculosEnGaraje().tamanio(); i++) {
            Vehiculo v = garaje.getVehiculosEnGaraje().dato(i);
            
            // Para DIFÍCIL solo mostrar exóticos
            if (dificultad == DIFICIL && !(v instanceof Exotico)) {
                continue;
            }
            
            contador++;
            String info = contador + ". " + v.obtenerNombreVehiculo() + " Gasolina: " + 
                         v.getGasolinaActual() + "/" + v.getCapacidadGasolina();
            System.out.println("║ " + String.format("%-35s", info) + "  ║");
        }
        
        System.out.println("║ 0. Cancelar misión                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Seleccione un vehículo: ");
        
        // Validar entrada
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Ingrese un número válido: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        // Procesar selección
        if (opcion == 0) {
            System.out.println("Misión cancelada.");
            pausar();
            iniciar();
        } else if (opcion < 1 || opcion > contador) {
            System.out.println("Opción inválida.");
            pausar();
            seleccionarVehiculo(dificultad);
        } else {
            // Buscar el vehículo seleccionado (el n-ésimo que cumple la condición)
            int seleccionContador = 0;
            for (int i = 0; i < garaje.getVehiculosEnGaraje().tamanio(); i++) {
                Vehiculo v = garaje.getVehiculosEnGaraje().dato(i);
                
                // Para DIFÍCIL solo contar exóticos
                if (dificultad == DIFICIL && !(v instanceof Exotico)) {
                    continue;
                }
                
                seleccionContador++;
                if (seleccionContador == opcion) {
                    System.out.println("Vehículo seleccionado: " + v.obtenerVehiculo());
                    System.out.println("Gasolina actual: " + v.getGasolinaActual() + " / " + v.getCapacidadGasolina() + " litros");
                    pausar();
                    empezarMision(v, dificultad);
                    return;
                }
            }
        }
    }

    private void empezarMision(Vehiculo vehiculoSeleccionado, int dificultad) {
        limpiarConsola();
        System.out.println("Gasolina actual del vehículo: " + vehiculoSeleccionado.getGasolinaActual() + " / " + vehiculoSeleccionado.getCapacidadGasolina() + " litros");
        System.out.print("Recargar Gasolina? (S/N)(\"NO\" para cancelar):");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        if(respuesta.equals("NO")){
            System.out.println("Misión cancelada.");
            pausar();
            iniciar();
            return;
        } else if(respuesta.equals("S")){
            try {
                garaje.cargarVehiculoAlMaximo(vehiculoSeleccionado.getNombre());
                System.out.println("Vehículo recargado al máximo.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error al recargar combustible: " + e.getMessage());
                pausar();
                iniciar();
                return;
            }
        }
        
        // Preparar la misión
        String rutaMapa = mapaAleatorio();
        try{
            Mapa mapa = new Mapa(rutaMapa);
            System.out.println("\n¡Iniciando interfaz gráfica!");
            System.out.println("Mapa: " + rutaMapa);
            System.out.println("Vehículo: " + vehiculoSeleccionado.getNombre());
            System.out.println("Gasolina: " + vehiculoSeleccionado.getGasolinaActual() + " litros");
            System.out.println("\nControles:");
            System.out.println("- Flechas o WASD para mover");
            System.out.println("- ESC para salir");
            System.out.println("\n");
            
            // Crear la vista de la misión
            MisionView vista = new MisionView(mapa, vehiculoSeleccionado, obtenerTiempoLimite(dificultad));
            
            // Mostrar la ventana en el hilo de JavaFX
            javafx.application.Platform.runLater(() -> {
                vista.mostrarVentana();
            });


            
            // ESPERAR a que el usuario cierre la ventana
            try {
                Thread.sleep(5000); // 5 segundos
            } catch (InterruptedException e) {
                // no pasa nada
            }
            pausar();
            System.out.println("\n¡Interfaz cerrada!");
            
            // Procesar resultados de la misión
            terminoPartida(dificultad, mapa);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    private String mapaAleatorio(){
        String rutaMapas = "data/ciudades/";
        File carpeta = new File(rutaMapas);
        File[] archivos = carpeta.listFiles();
        if (archivos != null && archivos.length > 0) {
            int indiceAleatorio = (int) (Math.random() * archivos.length);
            return archivos[indiceAleatorio].getName();
        }
        return rutaMapas +"mapa1.txt";
    }

    private double obtenerTiempoLimite(int dificultad) {
        switch (dificultad) {
            case FACIL:
                return 1000.0;
            case NORMAL:
                return 500.0;
            case DIFICIL:
                return 250.0;
            default:
                throw new IllegalArgumentException("Dificultad inválida.");
        }
    }

    private void terminoPartida(int dificultad, Mapa mapa){
        System.out.println("--- RESULTADOS DE LA MISIÓN ---");
        System.out.println("¿Llegó al destino? " + (mapa.llegoDestino() ? "Sí" : "No"));
        System.out.println("Recompensa:");
        if(mapa.llegoDestino() && mapa.seConsiguioRecompensaExtra()){
            obtenerRecompensa(dificultad);
            System.out.println("Recompensa extra obtenida:");
            int chance = (int)(Math.random() * 100) + 1; // Genera un número entre 1 y 100
            if(chance <= 20){
                Vehiculo exotico = obtenerExotico();
                garaje.agregarExotico(exotico);
                System.out.println("¡Felicidades! Has obtenido un vehículo exótico como recompensa extra: " + exotico.obtenerVehiculo());
            }else if(chance <= 55){
                int creditosExtra = obtenerCreditos();
                garaje.agregarCreditos(creditosExtra);
                System.out.println("Créditos extra obtenidos: " + creditosExtra);
            }else{
                int dineroExtra = obtenerDinero();
                garaje.agregarDinero(dineroExtra);
                System.out.println("Dinero extra obtenido: $" + dineroExtra);
            }
        }else if(mapa.llegoDestino()){
            obtenerRecompensa(dificultad);
        }else{
            System.out.println("No se obtuvo la recompensa por no llegar al destino.");
        }
        garaje.avanzarDia();
        garaje.cobrarMantenimientoDiario();
        System.out.println("Avanzando al día " + garaje.getDia() + ". Mantenimiento diario cobrado: $" + garaje.obtenerCostoMantenimiento());
        System.out.println("Dinero actual: $" + garaje.getDinero());
    }

    private void obtenerRecompensa(int dificultad){
        int dinero;
        int creditos;
        switch (dificultad) {
            case FACIL:
                creditos = 10;
                dinero = 1000;
                garaje.agregarCreditos(creditos);
                garaje.agregarDinero(dinero);
                break;
            case NORMAL:
                creditos = 20;
                dinero = 2500;
                garaje.agregarCreditos(creditos);
                garaje.agregarDinero(dinero);
                break;
            case DIFICIL:
                creditos = 50;
                dinero = 7500;
                garaje.agregarCreditos(creditos);
                garaje.agregarDinero(dinero);
                break;
            default:
                throw new IllegalArgumentException("Dificultad inválida.");
        }
        System.out.println("Créditos obtenidos: " + creditos);
        System.out.println("Dinero obtenido: $" + dinero);
    }

    private int obtenerCreditos(){
        return (int)(Math.random() * 21) + 20; // Genera un número entre 20 y 40
    }

    private int obtenerDinero(){
        return (int)(Math.random() * 251) + 500; // Genera un número entre 500 y 750
    }

    private Vehiculo obtenerExotico(){
        String ruta = "data/vehiculos/exoticos.csv";
        Vector <Vehiculo> exoticos = new Vector<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(ruta))){
            String linea;
            while ((linea = reader.readLine()) != null){
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                Vehiculo v = parsearExotico(linea);
                exoticos.agregar(v);
            }
            int chance = (int)(Math.random() * exoticos.tamanio()); // Genera un número entre 0 y el tamaño de exoticos
            return exoticos.dato(chance);
        }catch (IOException e) {
            throw new ExcepcionGaraje("No se pudo leer el garaje: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new ExcepcionGaraje("Formato inválido en CSV: " + e.getMessage());
        }

    }

    private Vehiculo parsearExotico(String linea){
        // formato: nombre, precio, tipo, ruedas, capacidadGasolina, gasolinaActual, kilometraje
        String[] parte = linea.split(",");

        String nombre = parte[garaje.NOMBRE].trim();
        int precio = Integer.parseInt(parte[garaje.PRECIO].trim());
        TipoVehiculo tipo = TipoVehiculo.valueOf(parte[garaje.TIPO_VEHICULO].trim().toUpperCase());
        int ruedas = Integer.parseInt(parte[garaje.RUEDAS].trim());
        int capacidadGasolina = Integer.parseInt(parte[garaje.CAPACIDAD_GASOLINA].trim());
        int gasolinaActual = Integer.parseInt(parte[garaje.GASOLINA_ACTUAL].trim());
        int kilometraje = Integer.parseInt(parte[garaje.KILOMETRAJE].trim());
        int velocidadMaxima = Integer.parseInt(parte[garaje.VELOCIDAD_MAXIMA].trim());

        Vehiculo exotico = new Exotico(nombre, precio, capacidadGasolina, velocidadMaxima, ruedas);
        exotico.cargarCombustible(gasolinaActual);
        exotico.sumarKilometros(kilometraje);
        return exotico;
    }




    /* ===================== UTILIDADES ===================== */

    private void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

}

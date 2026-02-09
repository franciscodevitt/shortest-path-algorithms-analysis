package org.ayed.gta.Menus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.Concesionario;
import org.ayed.tda.vector.Vector;

public class MenuInicio {

    private Scanner scanner;

    // Opciones del menú
    private final int NUEVA_PARTIDA = 1;
    private final int CARGAR_PARTIDA = 2;
    private final int CREDITOS = 3;
    private final int SALIR = 0;

    public MenuInicio() {
        this.scanner = new Scanner(System.in);
    }

    /* ===================== LOOP PRINCIPAL ===================== */

    public void iniciar() {
        int opcion;
        do {
            opcion = mostrarMenu();
            ejecutarOpcion(opcion);
        } while (opcion != SALIR);
    }

    /* ===================== MENU ===================== */

    private int mostrarMenu() {
        limpiarConsola();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║            GTA - GARAGE              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Nueva Partida                     ║");
        System.out.println("║ 2. Cargar Partida                    ║");
        System.out.println("║ 3. Créditos                          ║");
        System.out.println("║ 0. Salir del juego                   ║");
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

    /* ===================== ACCIONES ===================== */

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case NUEVA_PARTIDA:
                nuevaPartida();
                break;
            case CARGAR_PARTIDA:
                cargarPartida();
                break;
            case CREDITOS:
                mostrarCreditos();
                break;
            case SALIR:
                System.out.println("Saliendo del juego...");
                break;
            default:
                System.out.println("Opción inválida.");
                pausar();
        }
    }

    /* ===================== CASOS DE USO ===================== */

    private void nuevaPartida(){
        String nombrePartida = "";
        boolean partidaValida = false;

        while (!partidaValida) {
            System.out.print("Ingrese nombre de la nueva partida (o 'cancelar' para volver): ");
            nombrePartida = scanner.nextLine().trim();

            // Opción para cancelar
            if (nombrePartida.equalsIgnoreCase("cancelar")) {
                System.out.println("Creación de partida cancelada.");
                pausar();
                return;
            }

            // Validar que no esté vacío
            if (nombrePartida.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                pausar();
                continue;
            }

            // Verificar si la carpeta ya existe
            String ruta = "data/guardados/" + nombrePartida;
            if (Files.exists(Paths.get(ruta))) {
                System.out.println("¡Error! La partida '" + nombrePartida + "' ya existe.");
                System.out.print("¿Desea intentar con otro nombre? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s") && !respuesta.equals("si")) {
                    System.out.println("Creación de partida cancelada.");
                    pausar();
                    return;
                }
                // Si responde sí, vuelve al inicio del loop
                continue;
            }

            partidaValida = true;
        }

        // Crear la estructura de carpetas y archivos
        Garaje garaje = new Garaje();
        Concesionario concesionario = new Concesionario();
        String ruta = "data/guardados/" + nombrePartida;

        try {
            // Crear carpeta
            Files.createDirectories(Paths.get(ruta));

            // Cargar datos por defecto
            garaje.importarGaraje("data/default/default_garaje.csv");
            concesionario.importarConcesionario("data/default/default_concesionario.csv");

            // Guardar los archivos de la partida
            garaje.exportarGaraje(ruta + "/garaje.csv");
            concesionario.exportarConcesionario(ruta + "/concesionario.csv");

            System.out.println("Nueva partida creada exitosamente: " + nombrePartida);
            System.out.println("Garaje y concesionario inicializados.");
            pausar();

            MenuPrincipal menuPrincipal = new MenuPrincipal(garaje, concesionario, ruta);
            menuPrincipal.iniciar();

        } catch (IOException e) {
            System.out.println("Error al crear la partida: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error al inicializar la partida: " + e.getMessage());
            e.printStackTrace();
        }

        pausar();
    }

    private void cargarPartida() {
        try {
            // Obtener la carpeta de guardados
            Path guardsPath = Paths.get("data/guardados");
            
            // Validar que la carpeta existe
            if (!Files.exists(guardsPath)) {
                System.out.println("No hay partidas guardadas aún.");
                pausar();
                return;
            }

            // Listar carpetas (subdirectorios) usando Vector del TDA
            Vector<String> partidas = new Vector<>();
            File guardsFolder = guardsPath.toFile();
            File[] carpetas = guardsFolder.listFiles(File::isDirectory);

            if (carpetas == null || carpetas.length == 0) {
                System.out.println("No hay partidas guardadas aún.");
                pausar();
                return;
            }

            // Agregar nombres de partidas al Vector
            for (File carpeta : carpetas) {
                partidas.agregar(carpeta.getName());
            }

            // Mostrar listado de partidas
            limpiarConsola();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          PARTIDAS GUARDADAS          ║");
            System.out.println("╠══════════════════════════════════════╣");
            
            for (int i = 0; i < partidas.tamanio(); i++) {
                String nombrePartida = partidas.dato(i);
                System.out.println("║ " + (i + 1) + ". " + nombrePartida.substring(0, Math.min(35, nombrePartida.length())));
            }
            
            System.out.println("║ 0. Cancelar                          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Seleccione una partida: ");

            // Validar entrada del usuario
            while (!scanner.hasNextInt()) {
                scanner.next();
                System.out.print("Ingrese un número válido: ");
            }
            int opcion = scanner.nextInt();
            scanner.nextLine();

            // Validar opción
            if (opcion == 0) {
                System.out.println("Carga de partida cancelada.");
                pausar();
                return;
            }

            if (opcion < 1 || opcion > partidas.tamanio()) {
                System.out.println("Opción inválida.");
                pausar();
                return;
            }

            // Obtener nombre de la partida seleccionada
            String nombrePartidaSeleccionada = partidas.dato(opcion - 1);
            String rutaPartida = "data/guardados/" + nombrePartidaSeleccionada;

            // Cargar Garaje y Concesionario
            Garaje garaje = new Garaje();
            Concesionario concesionario = new Concesionario();

            try {
                garaje.importarGaraje(rutaPartida + "/garaje.csv");
                concesionario.importarConcesionario(rutaPartida + "/concesionario.csv");

                System.out.println("Partida cargada: " + nombrePartidaSeleccionada);
                pausar();

                // Lanzar el menú principal
                MenuPrincipal menuPrincipal = new MenuPrincipal(garaje, concesionario, rutaPartida);
                menuPrincipal.iniciar();

            } catch (Exception e) {
                System.out.println("Error al cargar los archivos de la partida: " + e.getMessage());
                e.printStackTrace();
                pausar();
            }

        } catch (Exception e) {
            System.out.println("Error al acceder a las partidas guardadas: " + e.getMessage());
            e.printStackTrace();
            pausar();
        }
    }

    private void mostrarCreditos() {
        limpiarConsola();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║              CRÉDITOS                ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Proyecto AyED - GTA Garage           ║");
        System.out.println("║ Desarrollado por:                    ║");
        System.out.println("║                                      ║");
        System.out.println("║                                      ║");
        System.out.println("║ Año: 2026                            ║");
        System.out.println("╚══════════════════════════════════════╝");
        pausar();
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

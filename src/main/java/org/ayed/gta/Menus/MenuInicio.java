package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.Concesionario;

public class MenuInicio {

    private Scanner scanner;

    // Opciones del menú
    private static final int NUEVA_PARTIDA = 1;
    private static final int CARGAR_PARTIDA = 2;
    private static final int CREDITOS = 3;
    private static final int SALIR = 0;

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

    private void nuevaPartida() {
        System.out.print("Ingrese nombre de la nueva partida: ");
        String nombrePartida = scanner.nextLine();

        // TODO:
        // 1. Crear carpeta en /guardados/nombrePartida
        // 2. Inicializar Garaje con valores iniciales
        // 3. Inicializar Concesionario
        Garaje garaje = new Garaje();
        Concesionario concesionario = new Concesionario();

        // TODO:
        // MenuGaraje menuGaraje = new MenuGaraje(garaje);
        // menuGaraje.iniciar();

        System.out.println("Nueva partida creada: " + nombrePartida);
        pausar();
    }

    private void cargarPartida() {
        // TODO:
        // 1. Mostrar listado de carpetas guardadas
        // 2. Pedir nombre de partida
        // 3. Cargar Garaje y Concesionario desde archivos

        System.out.println("Función cargar partida (pendiente)");
        pausar();
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

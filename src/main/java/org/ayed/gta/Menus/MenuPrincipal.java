package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.Concesionario;

public class MenuPrincipal {

    private Garaje garaje;
    private Concesionario concesionario;
    private String ruta;
    private Scanner scanner;

    // Opciones del menú
    private static final int MENU_GARAJE = 1;
    private static final int MENU_CONCESIONARIO = 2;
    private static final int MENU_MISIONES = 3;
    private static final int GUARDAR_PARTIDA = 4;
    private static final int SALIR_PARTIDA = 5;

    public MenuPrincipal(Garaje garaje, Concesionario concesionario, String ruta) {
        this.garaje = garaje;
        this.concesionario = concesionario;
        this.scanner = new Scanner(System.in);
        this.ruta = ruta;
    }

    /* ===================== LOOP PRINCIPAL ===================== */

    public void iniciar() {
        int opcion;
        do {
            opcion = mostrarMenu();
            ejecutarOpcion(opcion);
        } while (opcion != SALIR_PARTIDA);
    }

    /* ===================== MENU ===================== */

    private int mostrarMenu() {
        limpiarConsola();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          MENÚ PRINCIPAL              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Garaje                            ║");
        System.out.println("║ 2. Concesionario                     ║");
        System.out.println("║ 3. Empezar misión                    ║");
        System.out.println("║ 4. Guardar partida                   ║");
        System.out.println("║ 5. Salir de la partida               ║");
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
            case MENU_GARAJE:
                abrirMenuGaraje();
                break;
            case MENU_CONCESIONARIO:
                abrirMenuConcesionario();
                break;
            case MENU_MISIONES:
                abrirMenuMisiones();
                break;
            case GUARDAR_PARTIDA:
                guardarPartida();
                break;
            case SALIR_PARTIDA:
                salirPartida();
                break;
            default:
                System.out.println("Opción inválida.");
                pausar();
        }
    }

    /* ===================== SUBMENÚS ===================== */

    private void abrirMenuGaraje() {
        MenuGaraje menuGaraje = new MenuGaraje(garaje, concesionario);
        menuGaraje.iniciar();
    }

    private void abrirMenuConcesionario() {
        MenuConcesionario menu = new MenuConcesionario(concesionario, garaje);
        menu.iniciarConcesionario();
        pausar();
    }

    private void abrirMenuMisiones() {
        MenuMisiones menu = new MenuMisiones(garaje);
        menu.iniciar();
        pausar();
    }

    /* ===================== PARTIDA ===================== */

    private void guardarPartida() {
        try {
            garaje.exportarGaraje(ruta + "/garaje.csv");
            concesionario.exportarConcesionario(ruta + "/concesionario.csv");

            System.out.println("Partida guardada correctamente");
        } catch (Exception e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
        pausar();
    }

    private void salirPartida() {
        System.out.print("¿Desea guardar la partida antes de salir? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        if (respuesta.equals("S")) {
            guardarPartida();
        }

        System.out.println("Saliendo de la partida...");
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

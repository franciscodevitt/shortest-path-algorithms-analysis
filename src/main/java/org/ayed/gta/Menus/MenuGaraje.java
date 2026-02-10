package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Concesionario;
import org.ayed.gta.Garaje;

public class MenuGaraje {

    private Garaje garaje;
    private Concesionario concesionario;
    private Scanner scanner;

    // Opciones del menú
    private final int MOSTRAR_VEHICULOS = 1;
    private final int VENDER_VEHICULO = 2;
    private final int MEJORAR_GARAJE = 3;
    private final int COMPRAR_CREDITOS = 4;
    private final int MOSTRAR_VALOR = 5;
    private final int MOSTRAR_COSTO = 6;
    private final int CARGAR_TODOS_VEHICULOS = 7;
    private final int CARGAR_VEHICULO = 8;
    private final int CARGAR_VEHICULO_AL_MAXIMO = 9;
    private final int SALIR = 0;
    
    public MenuGaraje(Garaje garaje, Concesionario concesionario) {
        this.garaje = garaje;
        this.concesionario = concesionario;
        this.scanner = new Scanner(System.in);
    }

    /* ===================== LOOP PRINCIPAL ===================== */

    public void iniciar() {
        int opcion;
        do {
            opcion = menuPrincipal();
            try {
                ejecutarOpcion(opcion);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            pausar();
        } while (opcion != SALIR);
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case MOSTRAR_VEHICULOS:
                garaje.listarVehiculos();
                break;
            case VENDER_VEHICULO:
                garaje.venderVehiculo(ingresarNombre(), concesionario);
                break;
            case MEJORAR_GARAJE:
                garaje.mejorarGaraje();
                break;
            case COMPRAR_CREDITOS:
                garaje.comprarCreditos(ingresarCreditos());
                break;
            case MOSTRAR_VALOR:
                System.out.println("Valor total del garaje: $" + garaje.obtenerValorTotal());
                break;
            case MOSTRAR_COSTO:
                System.out.println("Costo de mantenimiento: $" + garaje.obtenerCostoMantenimiento());
                break;
            case CARGAR_TODOS_VEHICULOS:
                garaje.cargarTodosLosVehiculos();
                break;
            case CARGAR_VEHICULO:
                garaje.cargarVehiculo(ingresarNombre(), ingresarLitros());
                break;
            case CARGAR_VEHICULO_AL_MAXIMO:
                garaje.cargarVehiculoAlMaximo(ingresarNombre());
                break;
            case SALIR:
                System.out.println("Saliendo del garaje...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    /* ===================== MENÚ ===================== */

    private int menuPrincipal() {
        limpiarConsola();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          MENÚ GARAJE                 ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Mostrar vehículos                 ║");
        System.out.println("║ 2. Vender vehículo                   ║");
        System.out.println("║ 3. Mejorar garaje                    ║");
        System.out.println("║ 4. Comprar créditos                  ║");
        System.out.println("║ 5. Mostrar valor total               ║");
        System.out.println("║ 6. Mostrar costo mantenimiento       ║");
        System.out.println("║ 7. Cargar todos los vehículos        ║");
        System.out.println("║ 8. Cargar combustible a vehículo     ║");
        System.out.println("║ 9. Cargar vehículo al máximo         ║");
        System.out.println("║ 0. Salir                             ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ " + String.format("%-35s", "Dinero: $" + garaje.getDinero()) + "  ║");
        System.out.println("║ " + String.format("%-35s", "Créditos: " + garaje.getCreditos()) + "  ║");
        System.out.println("║ " + String.format("%-35s", "Día: " + garaje.getDia()) + "  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Ingrese un número válido: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    /* ===================== INPUT USUARIO ===================== */

    private String ingresarNombre() {
        System.out.print("Ingrese nombre del vehículo: ");
        return scanner.nextLine();
    }

    private int ingresarCreditos() {
        return ingresarEntero("Ingrese cantidad de créditos: ");
    }

    private int ingresarLitros() {
        return ingresarEntero("Ingrese cantidad de litros: ");
    }

    private int ingresarEntero(String mensaje) {
        String input;
        do {
            System.out.print(mensaje);
            input = scanner.nextLine();
        } while (!esValido(input));
        return Integer.parseInt(input);
    }

    private boolean esValido(String valor) {
        try {
            int n = Integer.parseInt(valor);
            if (n > 0) return true;
            System.out.println("Debe ser positivo.");
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
        }
        return false;
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

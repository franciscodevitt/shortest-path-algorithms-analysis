package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.Moto;
import org.ayed.gta.TipoVehiculo;
import org.ayed.gta.Vehiculo;

public class MenuGaraje {

    private Garaje garaje;
    private Scanner scanner;

    // Opciones del menú
    private static final int AGREGAR_VEHICULO = 1;
    private static final int MOSTRAR_VEHICULOS = 2;
    private static final int ELIMINAR_VEHICULO = 3;
    private static final int MEJORAR_GARAJE = 4;
    private static final int AGREGAR_CREDITOS = 5;
    private static final int MOSTRAR_VALOR = 6;
    private static final int MOSTRAR_COSTO = 7;
    private static final int EXPORTAR_GARAJE = 8;
    private static final int IMPORTAR_GARAJE = 9;
    private static final int SALIR = 0;

    public MenuGaraje(Garaje garaje) {
        this.garaje = garaje;
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
            case AGREGAR_VEHICULO:
                garaje.agregarVehiculo(nuevoVehiculo());
                break;
            case MOSTRAR_VEHICULOS:
                garaje.listarVehiculos();
                break;
            case ELIMINAR_VEHICULO:
                garaje.eliminarVehiculo(ingresarNombre());
                break;
            case MEJORAR_GARAJE:
                garaje.mejorarGaraje();
                break;
            case AGREGAR_CREDITOS:
                garaje.agregarCreditos(ingresarCreditos());
                break;
            case MOSTRAR_VALOR:
                System.out.println("Valor total del garaje: $" + garaje.obtenerValorTotal());
                break;
            case MOSTRAR_COSTO:
                System.out.println("Costo de mantenimiento: $" + garaje.obtenerCostoMantenimiento());
                break;
            case EXPORTAR_GARAJE:
                garaje.exportarGaraje();
                break;
            case IMPORTAR_GARAJE:
                garaje.importarGaraje();
                break;
            case SALIR:
                System.out.println("Saliendo del programa...");
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
        System.out.println("║ 1. Agregar vehículo                  ║");
        System.out.println("║ 2. Mostrar vehículos                 ║");
        System.out.println("║ 3. Eliminar vehículo                 ║");
        System.out.println("║ 4. Mejorar garaje                    ║");
        System.out.println("║ 5. Agregar créditos                  ║");
        System.out.println("║ 6. Mostrar valor total               ║");
        System.out.println("║ 7. Mostrar costo mantenimiento       ║");
        System.out.println("║ 8. Exportar garaje                   ║");
        System.out.println("║ 9. Importar garaje                   ║");
        System.out.println("║ 0. Salir                             ║");
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

    private Vehiculo nuevoVehiculo() {
        String nombre = ingresarNombre();
        TipoVehiculo tipo = TipoVehiculo.valueOf(ingresarTipoVehiculo());
        int precio = ingresarPrecio();
        int capacidad = ingresarCapacidadGasolina();

        return new Moto(nombre, 12, precio, capacidad);
    }

    private String ingresarNombre() {
        System.out.print("Ingrese nombre del vehículo: ");
        return scanner.nextLine();
    }

    private String ingresarTipoVehiculo() {
        String tipo;
        do {
            System.out.print("Ingrese tipo (AUTO/MOTO): ");
            tipo = scanner.nextLine().toUpperCase();
        } while (!tipo.equals("AUTO") && !tipo.equals("MOTO"));
        return tipo;
    }

    private int ingresarPrecio() {
        return ingresarEntero("Ingrese el precio: ");
    }

    private int ingresarCapacidadGasolina() {
        return ingresarEntero("Ingrese capacidad de gasolina: ");
    }

    private int ingresarCreditos() {
        return ingresarEntero("Ingrese cantidad de créditos: ");
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

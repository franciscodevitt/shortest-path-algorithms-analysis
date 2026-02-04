package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Concesionario;
import org.ayed.gta.Garaje;

public class MenuConcesionario {
    private Scanner scanner;
    private Concesionario concesionario;
    private Garaje garaje;

    private final int BUSCAR_VEHICULOS_NOMBRE = 1;
    private final int BUSCAR_VEHICULOS_MARCA  = 2;
    private final int LISTAR_VEHICULOS        = 3;
    private final int COMPRAR_VEHICULO        = 4;
    private final int VOLVER                  = 0;

    public MenuConcesionario(Concesionario concesionario, Garaje garaje) {
        this.scanner = new Scanner(System.in);
        this.concesionario = concesionario;
        this.garaje = garaje;
    }
    
    /**
     * Imprime el menú del concesionario y solicita una opción.
     * @return opción del menú.
     */
    public int menuConcesionario() {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                    CONCESIONARIO                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║     1. Buscar vehículos por nombre.                   ║");
        System.out.println("║     2. Buscar vehículos por marca.                    ║");
        System.out.println("║     3. Listar todos los vehículos disponibles.        ║");
        System.out.println("║     4. Comprar un vehículo (por nombre exacto).       ║");
        System.out.println("║     0. Volver al menú principal.                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║ " + String.format("%-35s", "Dinero: $" + garaje.getDinero()) + "                   ║");
        System.out.println("║ " + String.format("%-35s", "Créditos: " + garaje.getCreditos()) + "                   ║");
        System.out.println("║ " + String.format("%-35s", "Día: " + garaje.getDia()) + "                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un número.");
            scanner.next(); 
            System.out.print("Seleccione una opción: ");
        }

        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpiar salto de línea
        return opcion;
    }

    

    /**
     * Inicia la interacción con el concesionario
     * Llama al menú del concesionario
     */
    public void iniciarConcesionario() {
        int opcion;
        do {
            limpiarConsola();
            opcion = menuConcesionario();

            try {
                switch (opcion) {
                    case BUSCAR_VEHICULOS_NOMBRE:
                        String nombre = ingresarNombre();
                        concesionario.buscarPorNombre(nombre);
                        break;

                    case BUSCAR_VEHICULOS_MARCA:
                        String marca = ingresarMarca();
                        concesionario.buscarPorMarca(marca);
                        break;

                    case LISTAR_VEHICULOS:
                        concesionario.listarVehiculos();
                        break;
                    case COMPRAR_VEHICULO:
                        String nombreExacto = ingresarNombreExacto();
                        concesionario.comprarVehiculo(nombreExacto, garaje);
                        System.out.println("Vehículo comprado.");
                        break;

                    case VOLVER:
                        break;

                    default:
                        System.out.println("Ingrese una opción válida.");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (opcion != VOLVER) pausar();

        } while (opcion != VOLVER);
    }

    private void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    private String ingresarNombre() {
        String s;
        do {
            System.out.print("Ingrese el nombre del vehículo: ");
            s = scanner.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    private String ingresarMarca() {
        String s;
        do {
            System.out.print("Ingrese la marca: ");
            s = scanner.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    private String ingresarNombreExacto() {
        System.out.print("Ingrese el nombre exacto del vehículo a comprar: ");
        return scanner.nextLine().trim();
    }



}

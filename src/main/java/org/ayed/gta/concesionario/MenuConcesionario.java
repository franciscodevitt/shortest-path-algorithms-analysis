package org.ayed.gta.concesionario;

import java.util.Scanner;

import org.ayed.gta.Vehiculo;

public class MenuConcesionario {
    private Scanner scanner;
    private Concesionario concesionario;

    public MenuConcesionario(Concesionario concesionario) {
        this.scanner = new Scanner(System.in);
        this.concesionario = concesionario;
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
        System.out.println("║     3. Comprar un vehículo (por nombre exacto).       ║");
        System.out.println("║     0. Volver al menú principal.                      ║");
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

    private static final int BUSCAR_VEHICULOS_NOMBRE = 1;
    private static final int BUSCAR_VEHICULOS_MARCA  = 2;
    private static final int COMPRAR_VEHICULO        = 3;
    private static final int VOLVER                  = 0;

    /**
     * Inicia la interacción con el concesionario
     * Llama al menú del concesionario
     */
    public void iniciarConcesionario() {
        int opcion;
        do {
            opcion = menuConcesionario(); // nuevo menú que ya hiciste

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

                    case COMPRAR_VEHICULO:
                        String nombreExacto = ingresarNombreExacto();
                        Vehiculo vehiculo = concesionario.comprarVehiculo(nombreExacto);
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

    private void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    private String ingresarNombre() {
        System.out.print("Ingrese el nombre del vehículo: ");
        return scanner.nextLine().trim();
    }

    private String ingresarMarca() {
        System.out.print("Ingrese la marca: ");
        return scanner.nextLine().trim();
    }

    private String ingresarNombreExacto() {
        System.out.print("Ingrese el nombre exacto del vehículo a comprar: ");
        return scanner.nextLine().trim();
    }



}

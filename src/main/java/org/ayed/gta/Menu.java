package org.ayed.gta;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    /**
     * cosntructo del menu
     */
    public Menu(){
        this.scanner = new Scanner(System.in);

    }
    /**
     * imprime el menu pricipal y solicita una opcion
     * @return opcion del menu
     */
    public int menuPrincipal() {
        limpiarConsola();
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║     MENÚ PRINCIPAL                                    ║");
        System.out.println("╠═══════════════════════════════════════════════════════╣");
        System.out.println("║     1. Agregar un vehículo.                           ║");
        System.out.println("║     2. Mostrar todos los vehículos..                  ║");
        System.out.println("║     3. Eliminar un vehículo, por nombre               ║");
        System.out.println("║     4. Mejorar permanentemente el garaje.             ║");
        System.out.println("║     5. Agregar créditos al garaje (para testear).     ║");
        System.out.println("║     6. Mostrar el valor total del garaje.             ║");
        System.out.println("║     7. Mostrar el costo total diario                  ║");
        System.out.println("║     8. Exportar garaje.                               ║");
        System.out.println("║     9. Importar garaje.                               ║");
        System.out.println("║     0. Terminar Programa                              ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un número.");
            scanner.next(); // descarta el input incorrecto
            System.out.print("Seleccione una opción: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpiar salto de línea
        return opcion;
    }
    /**
     * imrpime 50 lineas vacias para limpiar la cosola
     */
    public void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    /**
     * pausa el juego
     */

    public void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}

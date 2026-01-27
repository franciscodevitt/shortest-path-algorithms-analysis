package org.ayed.gta.Menus;

import java.util.Scanner;

import org.ayed.gta.Garaje;
import org.ayed.gta.Vehiculo;
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
        // Lógica para iniciar la misión según el vehículo seleccionado y la dificultad
        System.out.println("Iniciando misión con el vehículo: " + vehiculoSeleccionado.obtenerVehiculo());
        // Iniciar la misión...
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

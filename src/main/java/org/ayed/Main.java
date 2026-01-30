package org.ayed;

import org.ayed.gta.ui.MisionView;
//import org.ayed.gta.Menus.MenuInicio;
import org.ayed.gta.mapa.Mapa;
import org.ayed.gta.Auto;
import org.ayed.gta.Menus.MenuInicio;

import java.util.Scanner;

/**
 * Clase simple para probar la interfaz gráfica de misiones.
 */
public class Main {

     public static void main(String[] args) {
        MenuInicio menuInicio = new MenuInicio();
        menuInicio.iniciar();
    } 
    /* 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== PRUEBA DE INTERFAZ GRÁFICA ===");
        System.out.println("Selecciona un mapa para probar:");
        System.out.println("1. Mapa 1 (mapa1.txt)");
        System.out.println("2. Mapa 2 (mapa2.txt)");
        System.out.println("3. Mapa 3 (mapa3.txt)");
        System.out.println("4. Mapa 4 (mapa4.txt)");
        System.out.print("Opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        String rutaMapa;
        switch (opcion) {
            case 1:
                rutaMapa = "mapa1.txt";
                break;
            case 2:
                rutaMapa = "mapa2.txt";
                break;
            case 3:
                rutaMapa = "mapa3.txt";
                break;
            case 4:
                rutaMapa = "mapa4.txt";
                break;
            default:
                rutaMapa = "mapa1.txt";
        }
        
        try {
            // Cargar el mapa
            Mapa mapa = new Mapa(rutaMapa);
            
            // Crear un vehículo de prueba
            Auto vehiculo = new Auto("Vehículo Prueba", 5000, 50, 150);
            vehiculo.cargarAlMaximo();
            
            System.out.println("\n¡Iniciando interfaz gráfica!");
            System.out.println("Mapa: " + rutaMapa);
            System.out.println("Vehículo: " + vehiculo.getNombre());
            System.out.println("Gasolina: " + vehiculo.getGasolinaActual() + " litros");
            System.out.println("\nControles:");
            System.out.println("- Flechas o WASD para mover");
            System.out.println("- ESC para salir");
            System.out.println("\n");
            
            // Lanzar la interfaz gráfica
            int tiempoLimite=500;
            MisionView.iniciarMision(mapa, vehiculo, tiempoLimite);
            
            System.out.println("\n¡Interfaz cerrada!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
    
}

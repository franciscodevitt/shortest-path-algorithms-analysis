package org.ayed;

import javafx.application.Application;
import org.ayed.gta.ui.AppJavaFX;

import org.ayed.gta.Menus.MenuInicio;


/**
 * Clase principal del juego.
 * Inicia JavaFX UNA VEZ y luego ejecuta el menú.
 */
public class Main {

    public static void main(String[] args) {
        //Iniciar JavaFX en un hilo separado (solo se hace una vez)
        Thread hiloJavaFX = new Thread(() -> {
            Application.launch(AppJavaFX.class);
        });
        hiloJavaFX.setDaemon(true); // Se cierra cuando termine el programa
        hiloJavaFX.start();
        
        // PASO 2: Esperar a que JavaFX esté listo
        while (!AppJavaFX.estaIniciado()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        
        // PASO 3: Iniciar el menú del juego
        MenuInicio menuInicio = new MenuInicio();
        menuInicio.iniciar();
    } 
    
}

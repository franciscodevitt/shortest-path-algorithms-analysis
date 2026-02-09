package org.ayed.gta.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Clase simple para inicializar JavaFX UNA SOLA VEZ.
 * No muestra ninguna ventana, solo inicia la plataforma.
 */
public class AppJavaFX extends Application {

    private static boolean iniciado = false;

    @Override
    public void start(Stage primaryStage) {
        // No mostrar ninguna ventana
        // Solo marcar que JavaFX ya está listo
        Platform.setImplicitExit(false); // No cerrar JavaFX cuando se cierren ventanas
        iniciado = true;
    }

    /**
     * Verifica si JavaFX ya fue iniciado.
     */
    public static boolean estaIniciado() {
        return iniciado;
    }
}

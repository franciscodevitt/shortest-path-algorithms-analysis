package org.ayed.gta.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class GestorSonido {
    
    public static MediaPlayer reproducirMusica(String nombreArchivo) {
        Media media = obtenerSonido(nombreArchivo);
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
        return player;
    }
    
    public static void reproducirEfecto(String nombreArchivo) {
        MediaPlayer player = new MediaPlayer(obtenerSonido(nombreArchivo));
        player.play();
    }
    
    private static Media obtenerSonido(String nombre) {
        
        String ruta = GestorSonido.class.getResource("/sounds/" + nombre + ".wav").toExternalForm();
        Media media = new Media(ruta);
    
        return media;
    }
}

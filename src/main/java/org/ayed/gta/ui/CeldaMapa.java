package org.ayed.gta.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.net.URL;

/**
 * Representa una celda individual del mapa.
 * Cada celda puede mostrar terreno, vehículo, destino, etc.
 */
public class CeldaMapa extends StackPane {
    private int fila;
    private int columna;
    private String terreno;
    
    private Rectangle fondoTerreno;
    private ImageView imagenCelda;
    private Text iconoTexto;
    
    private static final int CELDA_TAMAÑO = 38;
    
    /**
     * Constructor de la celda del mapa.
     */
    public CeldaMapa(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        
        // Fondo - rectángulo para el terreno
        fondoTerreno = new Rectangle(CELDA_TAMAÑO, CELDA_TAMAÑO);
        fondoTerreno.setFill(Color.BLACK);
        fondoTerreno.setStroke(Color.web("#444444"));
        fondoTerreno.setStrokeWidth(0.5);
        
        // ImageView para mostrar imágenes (vehículos, objetos, etc.)
        imagenCelda = new ImageView();
        imagenCelda.setFitHeight(CELDA_TAMAÑO - 4);
        imagenCelda.setFitWidth(CELDA_TAMAÑO - 4);
        imagenCelda.setPreserveRatio(true);
        
        // Texto para iconos o caracteres (como emojis)
        iconoTexto = new Text();
        iconoTexto.setFont(new Font(16));
        
        // StackPane permite superponer elementos
        this.getChildren().addAll(fondoTerreno, imagenCelda, iconoTexto);
        
        // Forzar tamaño exacto para evitar variaciones
        this.setPrefSize(CELDA_TAMAÑO, CELDA_TAMAÑO);
        this.setMinSize(CELDA_TAMAÑO, CELDA_TAMAÑO);
        this.setMaxSize(CELDA_TAMAÑO, CELDA_TAMAÑO);
    }
    
    /**
     * Establece el color del terreno según el tipo.
     */
    public void setTerreno(String tipo) {
        this.terreno = tipo;
        Color color = obtenerColorTerreno(tipo);
        fondoTerreno.setFill(color);
        Image textura = obtenerTexturaTerreno(tipo);
        mostrarImagen(textura);
    }
    
    /**
     * Obtiene el color correspondiente al tipo de terreno.
     */
    private Color obtenerColorTerreno(String tipo) {
        switch (tipo) {
            case "+":  // Calle
                return Color.web("#8B6914");
            case "#":  // Edificio
                return Color.web("#333333");
            case "-":  // Parque
                return Color.web("#228B22");
            case "~":  // Agua
                return Color.web("#1E90FF");
            default:
                return Color.BLACK;
        }
    }

       /**
     * Obtiene el color correspondiente al tipo de terreno.
     */
    private Image obtenerTexturaTerreno(String tipo) {
        String rutaImagen;
        switch (tipo) {
            case "+":  // Calle
                rutaImagen = "/images/texturas/asfalto.png";
                break;
            case "#":  // Edificio
                rutaImagen = "/images/texturas/edificio2.png";
                break;
            case "-":  // Parque
                rutaImagen = "/images/texturas/cesped.png";
                break;
            case "~":  // Agua
                rutaImagen = "/images/texturas/agua2.png";
                break;
            default:
                rutaImagen = null;
        }

        if (rutaImagen != null) {
            URL url = getClass().getResource(rutaImagen);
            return new Image(url.toExternalForm());
        }
        return null;
    }
    
    /**
     * Muestra una imagen genérica en la celda.
     */
    public void mostrarImagen(Image imagen) {
        if (imagen != null) {
            imagenCelda.setImage(imagen);
        }
    }

    public void ocultarImagen() {
        imagenCelda.setImage(null);
    }
    
    /**
     * Muestra un ícono de texto (emoji o carácter).
     */
    public void mostrarIcono(String icono, Color color) {
        iconoTexto.setText(icono);
        if (color != null) {
            iconoTexto.setFill(color);
        } else {
            iconoTexto.setFill(Color.WHITE); // Color por defecto
        }
    }

    public void ocultarIcono() {
        iconoTexto.setText("");
    }
    
    /**
     * Resalta la celda (para mostrar destino, ruta, etc.).
     */
    public void resaltar(Color color) {
        fondoTerreno.setStroke(color);
        fondoTerreno.setStrokeWidth(2);
    }
    
    /**
     * Quita el resaltado de la celda.
     */
    public void quitarResaltado() {
        fondoTerreno.setStroke(Color.web("#444444"));
        fondoTerreno.setStrokeWidth(0.5);
    }
    
    /**
     * Obtiene la posición de la celda en el mapa.
     */
    public int getFila() {
        return fila;
    }
    
    public int getColumna() {
        return columna;
    }
    
    /**
     * Obtiene el tipo de terreno.
     */
    public String getTerreno() {
        return terreno;
    }
}

package org.ayed.gta.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Representa una celda individual del mapa.
 * Cada celda puede mostrar terreno, vehículo, destino, etc.
 */
public class CeldaMapa extends StackPane {
    private int fila;
    private int columna;
    private String terreno;
    
    private Rectangle fondoTerreno;
    private ImageView vehiculoView;
    private Text iconoTexto;
    
    private static final int CELL_SIZE = 20;
    
    /**
     * Constructor de la celda del mapa.
     */
    public CeldaMapa(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        
        // Fondo - rectángulo para el terreno
        fondoTerreno = new Rectangle(CELL_SIZE, CELL_SIZE);
        fondoTerreno.setFill(Color.BLACK);
        fondoTerreno.setStroke(Color.web("#444444"));
        fondoTerreno.setStrokeWidth(0.5);
        
        // ImageView para mostrar imágenes (vehículos, objetos, etc.)
        vehiculoView = new ImageView();
        vehiculoView.setFitHeight(CELL_SIZE - 4);
        vehiculoView.setFitWidth(CELL_SIZE - 4);
        vehiculoView.setPreserveRatio(true);
        
        // Texto para iconos o caracteres (como emojis)
        iconoTexto = new Text();
        iconoTexto.setFont(new Font(16));
        
        // StackPane permite superponer elementos
        this.getChildren().addAll(fondoTerreno, vehiculoView, iconoTexto);
        
        // Forzar tamaño exacto para evitar variaciones
        this.setPrefSize(CELL_SIZE, CELL_SIZE);
        this.setMinSize(CELL_SIZE, CELL_SIZE);
        this.setMaxSize(CELL_SIZE, CELL_SIZE);
    }
    
    /**
     * Establece el color del terreno según el tipo.
     */
    public void setTerreno(String tipo) {
        this.terreno = tipo;
        Color color = obtenerColorTerreno(tipo);
        fondoTerreno.setFill(color);
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
     * Muestra una imagen del vehículo en la celda.
     */
    public void mostrarVehiculo(Image imagen) {
        if (imagen != null) {
            vehiculoView.setImage(imagen);
            iconoTexto.setText("");  // Limpiar si hay texto
        }
    }
    
    /**
     * Muestra un ícono de texto (emoji o carácter).
     */
    public void mostrarIcono(String icono) {
        iconoTexto.setText(icono);
        iconoTexto.setFill(Color.WHITE);
        vehiculoView.setImage(null);  // Limpiar si hay imagen
    }
    
    /**
     * Oculta el vehículo/ícono de la celda.
     */
    public void ocultarVehiculo() {
        vehiculoView.setImage(null);
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

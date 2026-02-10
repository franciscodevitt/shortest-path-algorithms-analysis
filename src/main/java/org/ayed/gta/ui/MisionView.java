package org.ayed.gta.ui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.ayed.gta.mapa.Mapa;
import org.ayed.gta.mapa.Nodo;
import org.ayed.gta.Vehiculo;
import org.ayed.gta.mapa.Coordenada;
import javafx.scene.media.MediaPlayer;


/**
 * Interfaz gráfica para la misión.
 * SIMPLE: No extiende Application, solo crea una ventana nueva cada vez.
 */
public class MisionView {

    // Componentes de la interfaz
    private MisionController controller;
    private GridPane gridMapa;
    private CeldaMapa[][] celdas;
    private Label lblCombustible;
    private Label lblTiempo;
    private Label lblPosicion;
    private Label lblDestino;
    private VBox mensajeOverlay;
    private Label mensajeLabel;
    private boolean misionCompleta = false;
    
    // Datos de la misión
    private Mapa mapa;
    private Vehiculo vehiculo;
    private double tiempoLimite;
    private Image imagenVehiculo;
    
    // Ventana de la misión
    private Stage stage;
    // Música de fondo
    private MediaPlayer musicaFondo;

    /**
     * Constructor: recibe los datos de la misión.
     */
    public MisionView(Mapa mapa, Vehiculo vehiculo, double tiempoLimite) {
        this.mapa = mapa;
        this.vehiculo = vehiculo;
        this.tiempoLimite = tiempoLimite;
        
        // Obtener imagen del vehículo
        this.imagenVehiculo = GestorImagenesVehiculos.obtenerImagenVehiculo(vehiculo);
    }

    /**
     * Muestra la ventana de la misión.
     * Este método debe llamarse desde Platform.runLater()
     */
    public void mostrarVentana() {
        // Crear una ventana nueva
        stage = new Stage();
        
        // Crear el controlador
        controller = new MisionController(mapa, vehiculo, tiempoLimite);

        // Crear la interfaz
        BorderPane root = crearInterfazPrincipal();
        Scene scene = new Scene(root, 1200, 900);
        
        // Configurar eventos de teclado
        scene.setOnKeyPressed(this::manejarTeclaPresionada);
        
        // Configurar la ventana
        stage.setTitle("MISIÓN - Grafosaurios");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        
        // Centrar y dar foco
        stage.centerOnScreen();
        stage.requestFocus();
        scene.getRoot().requestFocus();

        // Iniciar música de fondo
        this.musicaFondo = GestorSonido.reproducirMusica("GTA-Theme");
        
        // Cuando se cierre la ventana, marcar como cerrada
        stage.setOnCloseRequest(e -> {
            stage.close();
        });
        
        // Renderizar mapa inicial
        renderizarMapa();
        actualizarInformacion();
    }
    
    /**
     * Crea la interfaz principal con GridPane.
     */
    private BorderPane crearInterfazPrincipal() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");
        
        // Panel izquierdo - GridPane con mapa
        gridMapa = new GridPane();
        gridMapa.setStyle("-fx-background-color: #000000; -fx-padding: 10;");
        gridMapa.setHgap(0);
        gridMapa.setVgap(0);
        
        // Crear celdas del mapa
        int altura = mapa.obtenerAltura();
        int ancho = mapa.obtenerAncho();
        
        celdas = new CeldaMapa[altura][ancho];
        
        //procesa las celdas
        for (int fila = 0; fila < altura; fila++) {
            for (int col = 0; col < ancho; col++) {
                CeldaMapa celda = new CeldaMapa(fila, col);
                
                //procesar celda y le añane las texturas e iconos necesarios
                procesarCelda(celda);
                
                //agregar celda al arreglo y al gridpane
                celdas[fila][col] = celda;
                gridMapa.add(celda, col, fila);
            }
        }
        
        // Panel derecho - Información
        VBox panelInfo = crearPanelInformacion();
        
        root.setCenter(gridMapa);
        root.setRight(panelInfo);
        
        // Overlay para mensajes de fin
        mensajeOverlay = crearOverlayMensaje();
        root.setBottom(mensajeOverlay);
        
        return root;
    }
    
    /**
     * Crea el panel de información a la derecha.
     */
    private VBox crearPanelInformacion() {
        VBox panel = new VBox(15);
        panel.setPrefWidth(500);
        panel.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-border-color: #444444;" +
            "-fx-border-width: 0 0 0 1;" +
            "-fx-background-color: #2a2a2a;" + 
            "-fx-padding: 80 20;"
        );
        
        // Título
        Label titulo = new Label("INFORMACIÓN");
        titulo.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #00ff00;");
        
        // Combustible
        lblCombustible = new Label("Combustible: -- L");
        lblCombustible.setStyle("-fx-font-size: 22; -fx-text-fill: #ffffff;");
        lblCombustible.setWrapText(true);

        // Tiempo restante
        lblTiempo = new Label("Tiempo Restante: -- Seg");
        lblTiempo.setStyle("-fx-font-size: 22; -fx-text-fill: #ffffff;");
        lblTiempo.setWrapText(true);
        
        // Posición
        lblPosicion = new Label("Posición: (-, -)");
        lblPosicion.setStyle("-fx-font-size: 22; -fx-text-fill: #ffffff;");
        lblPosicion.setWrapText(true);
        
        // Destino
        lblDestino = new Label("Destino: (-, -)");
        lblDestino.setStyle("-fx-font-size: 22; -fx-text-fill: #ffff00;");
        lblDestino.setWrapText(true);
        
        // Instrucciones
        Label instrucciones = new Label(
            "\n▼ CONTROLES ▼\n\n"+
            "↑ ↓ ← → o W A S D\n"+
            "Mover\n\n"+
            "ESC: Salir\n\n"+
            "REFERENCIAS:\n"+
            "⭐ = Recompensa extra (aleatoria)\n"+
            "⛔ = Congestion\n"
        );
        instrucciones.setStyle("-fx-font-size: 22; -fx-text-fill: #cccccc; -fx-padding: 10; -fx-border-color: #444444; -fx-border-width: 1;");
        instrucciones.setWrapText(true);
        
        
        
        panel.getChildren().addAll(
            titulo,
            lblCombustible,
            lblTiempo,
            lblPosicion,
            lblDestino,
            instrucciones
        );
        
        VBox.setVgrow(panel, javafx.scene.layout.Priority.ALWAYS);
        return panel;
    }
    
    /**
     * Crea el overlay para mensajes de fin de misión.
     */
    private VBox crearOverlayMensaje() {
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
        overlay.setPrefHeight(100);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(10));
        overlay.setVisible(false);
        
        mensajeLabel = new Label("");
        mensajeLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: 'Impact', 'Arial Black', sans-serif;");
        mensajeLabel.setWrapText(true);
        
        overlay.getChildren().add(mensajeLabel);
        
        return overlay;
    }


    public void renderizarMapa() {
        
        //obtengo la coordenada actual y la anterior del jugador
        Coordenada posicionAnteriorJugador = mapa.obtenerPosicionAnteriorJugador();
        Coordenada posicionActualJugador = mapa.obtenerPosicionJugador();
        Coordenada destino = mapa.obtenerDestino();

        if(posicionAnteriorJugador != null){

            //Quitar resaltado de la ruta optima anterior
            var rutaOptimaAnterior = mapa.obtenerRutaOptima(posicionAnteriorJugador, destino);
            while (!rutaOptimaAnterior.vacio()) {
                Nodo nodo = rutaOptimaAnterior.eliminar();
                Coordenada coord = nodo.obtenerCoordenada();
                int fila = coord.obtenerY();
                int col = coord.obtenerX();
                celdas[fila][col].quitarResaltado();
            }

            //limpio y vuelvo a procesar la celda anterior
            int filAnterior = posicionAnteriorJugador.obtenerY();
            int colAnterior = posicionAnteriorJugador.obtenerX();
            celdas[filAnterior][colAnterior].ocultarImagen();
            celdas[filAnterior][colAnterior].ocultarIcono();
            celdas[filAnterior][colAnterior].quitarResaltado();
            procesarCelda(celdas[filAnterior][colAnterior]);
        }
        if(posicionActualJugador != null){

            //resaltar la ruta optima
            var rutaOptimaActual = mapa.obtenerRutaOptima(posicionActualJugador, destino);
            while (!rutaOptimaActual.vacio()) {
                Nodo nodo = rutaOptimaActual.eliminar();
                Coordenada coord = nodo.obtenerCoordenada();
                int fila = coord.obtenerY();
                int col = coord.obtenerX();
                celdas[fila][col].resaltar(Color.CYAN);
                
            }

            //analizo y proceso la celda actual
            int filActual = posicionActualJugador.obtenerY();
            int colActual = posicionActualJugador.obtenerX();
            procesarCelda(celdas[filActual][colActual]);
        }

        
        

    
    }
    

    /**
     * Actualiza la información mostrada.
     */
    private void actualizarInformacion() {
        int combustible = vehiculo.getGasolinaActual();
        var posicion = mapa.obtenerPosicionJugador();
        var destino = mapa.obtenerDestino();
        double tiempoRestante = controller.getTiempoRestante();

        lblCombustible.setText(String.format("Combustible: %d L", combustible));

        lblTiempo.setText(String.format("Tiempo Restante: %.1f Seg", tiempoRestante));
        
        if (posicion != null) {
            lblPosicion.setText(String.format("Posición: (%d, %d)", 
                posicion.obtenerY(), posicion.obtenerX()));
        }
        
        if (destino != null) {
            lblDestino.setText(String.format("Destino: (%d, %d)", 
                destino.obtenerY(), destino.obtenerX()));
        }
        
        // Verificar estado de la misión
        if (!misionCompleta) {
            if (controller.isMisionCompletada()) {
                misionCompleta = true;
                mostrarMensajeFin("MISSION PASSED!\n RESPECT+", Color.LIME);
                musicaFondo.stop();
                GestorSonido.reproducirEfecto("mission-passed");
            } else if (combustible <= 0 || tiempoRestante <= 0) {
                misionCompleta = true;
                mostrarMensajeFin("MISSION FAILED!", Color.RED);
                musicaFondo.stop();
                GestorSonido.reproducirEfecto("ah-shit-here-we-go-again");
            } else if (tiempoRestante <= 0){
                misionCompleta = true;
                mostrarMensajeFin("¡TIEMPO AGOTADO!", Color.RED);
                musicaFondo.stop();
                GestorSonido.reproducirEfecto("ah-shit-here-we-go-again");
            }
        }
    }
    
    /**
     * Maneja las teclas presionadas.
     */
    private void manejarTeclaPresionada(KeyEvent event) {
        
        // ESC cierra la ventana
        if (event.getCode() == KeyCode.ESCAPE) {
            musicaFondo.stop();
            stage.close();
        }
        
        if (misionCompleta) {
            return;  // No permitir movimiento si la misión terminó
        }
        
        controller.manejarTecla(event.getCode());
        renderizarMapa();
        actualizarInformacion();
    }
    
    /**
     * Muestra un mensaje de fin de misión.
     */
    private void mostrarMensajeFin(String mensaje, Color color) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setTextFill(color);
        mensajeOverlay.setVisible(true);
    }
    
   
    private void procesarCelda(CeldaMapa celda){
        Coordenada posicionJugador = mapa.obtenerPosicionJugador();
        Coordenada destino = mapa.obtenerDestino();
        Coordenada recompensaExtra = mapa.obtenerCoordenadaRecompensaExtra();
        
        int fila = celda.getFila();
        int col = celda.getColumna();

        //carga las texturas de la celda según el terreno
        String terreno = mapa.obtenerTipoTerreno(fila, col);
        celda.setTerreno(terreno); // Establecer color según terreno

        //mostrar recompensa extra
        if (!mapa.seConsiguioRecompensaExtra() && recompensaExtra.obtenerX() == col && recompensaExtra.obtenerY() == fila){
            celda.mostrarIcono("⭐", Color.GOLD);
            celda.resaltar(Color.GOLD);
        }

        // Mostrar tráfico si existe
        if (mapa.tieneTrafico(col,fila)){ 
            celda.mostrarIcono("⛔", Color.RED); // Mostrar ícono de tráfico
        }

        // Mostrar destino (cuadrado verde)
        if (destino.obtenerX() == col && destino.obtenerY() == fila){
            celda.setDestino();
        }

        // Mostrar jugador
        if (posicionJugador.obtenerX() == col && posicionJugador.obtenerY() == fila){
            if (imagenVehiculo != null) {
                celda.ocultarIcono();
                celda.mostrarImagen(imagenVehiculo);
            } else {
                celda.ocultarIcono();
                celda .mostrarIcono("●", Color.BLUE);
            }
            celda.resaltar(Color.RED);
        }
        
    }
}

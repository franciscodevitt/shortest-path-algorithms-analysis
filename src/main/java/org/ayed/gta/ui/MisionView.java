package org.ayed.gta.ui;

import javafx.application.Application;
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


/**
 * Interfaz gráfica para la misión usando GridPane.
 * Cada celda del mapa es un nodo JavaFX individual.
 */
public class MisionView extends Application {
    private static Mapa mapaEstatico;
    private static Vehiculo vehiculoEstatico;
    private static Image imagenVehiculoEstatica;
    
    private static double tiempoLimiteEstatico;

    private MisionController controller;
    private GridPane gridMapa;
    private CeldaMapa[][] celdas;
    private Label lblCombustible;
    private Label lblTiempo;
    private Label lblPosicion;
    private Label lblDestino;
    private VBox mensajeOverlay;
    private Label mensajeLabel;
    private Stage primaryStage;
    private boolean misionTerminada = false;
    private Image imagenVehiculo;

    
    /**
     * Inicializa la misión con mapa y vehículo.
     * La imagen se obtiene automáticamente según el tipo de vehículo.
     */
    public static void iniciarMision(Mapa mapa, Vehiculo vehiculo, double tiempoLimite) {

        mapaEstatico = mapa;
        vehiculoEstatico = vehiculo;
        tiempoLimiteEstatico=tiempoLimite;
        imagenVehiculoEstatica = null;  // Se determinará por el tipo
        launch();
    }
    
    /**
     * Inicializa la misión con mapa, vehículo e imagen personalizada.
     */
    public static void iniciarMision(Mapa mapa, Vehiculo vehiculo, int tiempoLimite,Image imagen) {

        mapaEstatico = mapa;
        vehiculoEstatico = vehiculo;
        tiempoLimiteEstatico=tiempoLimite;
        imagenVehiculoEstatica = imagen;
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        controller = new MisionController(mapaEstatico, vehiculoEstatico, tiempoLimiteEstatico);

        // Obtener imagen del vehículo
        if (imagenVehiculoEstatica != null) {
            this.imagenVehiculo = imagenVehiculoEstatica;
            System.out.println("✓ Usando imagen personalizada proporcionada");
        } else {
            // Obtener imagen por defecto según tipo de vehículo
            String tipoVehiculo = vehiculoEstatico.getTipo().toString();
            System.out.println("▼ Buscando imagen por tipo: " + tipoVehiculo);
            this.imagenVehiculo = GestorImagenesVehiculos.obtenerImagenPorTipo(tipoVehiculo);
            
            if (this.imagenVehiculo != null) {
                System.out.println("✓ Imagen obtenida por tipo");
            } else {
                System.out.println("✗ No se pudo obtener imagen por tipo, usando ícono");
            }
        }

        // Crear interfaz
        BorderPane root = crearInterfazPrincipal();
        Scene scene = new Scene(root, 1200, 900);
        
        
        // Manejar eventos de teclado
        scene.setOnKeyPressed(this::manejarTeclaPresionada);
        
        stage.setTitle("MISIÓN - Grafosaurios");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        
        // Centrar ventana
        stage.centerOnScreen();
        
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
        int altura = mapaEstatico.obtenerAltura();
        int ancho = mapaEstatico.obtenerAncho();
        
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
        
        // Forzar que GridPane no agrande las celdas
        gridMapa.setStyle("-fx-background-color: #000000; -fx-padding: 10;");
        
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
        panel.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 15;");
        panel.setPrefWidth(250);
        panel.setStyle("-fx-text-fill: #ffffff; -fx-border-color: #444444; -fx-border-width: 0 0 0 1;");
        
        // Título
        Label titulo = new Label("INFORMACIÓN");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #00ff00;");
        
        // Combustible
        lblCombustible = new Label("Combustible: -- L");
        lblCombustible.setStyle("-fx-font-size: 12; -fx-text-fill: #ffffff;");
        lblCombustible.setWrapText(true);

        // Tiempo restante
        lblTiempo = new Label("Tiempo Restante: -- Seg");
        lblTiempo.setStyle("-fx-font-size: 12; -fx-text-fill: #ffffff;");
        lblTiempo.setWrapText(true);
        
        // Posición
        lblPosicion = new Label("Posición: (-, -)");
        lblPosicion.setStyle("-fx-font-size: 12; -fx-text-fill: #ffffff;");
        lblPosicion.setWrapText(true);
        
        // Destino
        lblDestino = new Label("Destino: (-, -)");
        lblDestino.setStyle("-fx-font-size: 12; -fx-text-fill: #ffff00;");
        lblDestino.setWrapText(true);
        
        // Instrucciones
        Label instrucciones = new Label("\n▼ CONTROLES ▼\n\n↑ ↓ ← → o W A S D\nMover\n\nR: Reintentar\nESC: Salir");
        instrucciones.setStyle("-fx-font-size: 11; -fx-text-fill: #cccccc; -fx-padding: 10; -fx-border-color: #444444; -fx-border-width: 1;");
        instrucciones.setWrapText(true);
        
        // Estado
        Label estado = new Label("\n⬤ ROJO = Jugador\n■ VERDE = Destino\n+ = Calle\n# = Edificio\n- = Parque\n~ = Agua");
        estado.setStyle("-fx-font-size: 10; -fx-text-fill: #aaaaaa; -fx-padding: 10; -fx-border-color: #444444; -fx-border-width: 1;");
        estado.setWrapText(true);
        
        panel.getChildren().addAll(
            titulo,
            lblCombustible,
            lblTiempo,
            lblPosicion,
            lblDestino,
            instrucciones,
            estado
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
        overlay.setPrefHeight(80);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(10));
        overlay.setVisible(false);
        
        mensajeLabel = new Label("");
        mensajeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        mensajeLabel.setWrapText(true);
        
        overlay.getChildren().add(mensajeLabel);
        
        return overlay;
    }
    
    /**
     * Renderiza el mapa en las celdas.
     */
    /* private void renderizarMapa() {
        int altura = mapaEstatico.obtenerAltura();
        int ancho = mapaEstatico.obtenerAncho();
        
        // Limpia todas las celdas
        for (int fila = 0; fila < altura; fila++) {
            for (int col = 0; col < ancho; col++) {
                celdas[fila][col].ocultarVehiculo();
                celdas[fila][col].quitarResaltado();
                if (mapaEstatico.tieneTrafico(col,fila)){ 
                    celdas[fila][col].mostrarIcono("+", Color.RED); // Mostrar ícono de tráfico
                }
                // Establecer color y textura según terreno
                String terreno = mapaEstatico.obtenerTipoTerreno(fila, col);
                celdas[fila][col].setTerreno(terreno); 
            }
        }
        
        // Mostrar destino (cuadrado verde)
        var destino = mapaEstatico.obtenerDestino();
        if (destino != null) {
            int filaDestino = destino.obtenerY();
            int colDestino = destino.obtenerX();
            celdas[filaDestino][colDestino].mostrarIcono("■", Color.LIME);
            celdas[filaDestino][colDestino].resaltar(Color.LIME);
        }
        
        // Mostrar jugador con imagen si está disponible, sino con ícono
        var jugador = mapaEstatico.obtenerPosicionJugador();
        if (jugador != null) {
            int filaJugador = jugador.obtenerY();
            int colJugador = jugador.obtenerX();
            
            if (imagenVehiculo != null) {
                celdas[filaJugador][colJugador].mostrarVehiculo(imagenVehiculo);
            } else {
                celdas[filaJugador][colJugador].mostrarIcono("●", Color.BLUE);
            }
            celdas[filaJugador][colJugador].resaltar(Color.RED);
        }

        // Mostrar recompensa extra si no ha sido recogida
        if (!mapaEstatico.seConsiguioRecompensaExtra()) {
            var recompensa = mapaEstatico.obtenerCoordenadaRecompensaExtra();
            if (recompensa != null) {
                int filaRecompensa = recompensa.obtenerY();
                int colRecompensa = recompensa.obtenerX();
                celdas[filaRecompensa][colRecompensa].mostrarIcono("⭐", Color.GOLD);
                celdas[filaRecompensa][colRecompensa].resaltar(Color.GOLD);
            }
        }

    } */

    public void renderizarMapa() {
        
        //obtengo la coordenada actual y la anterior del jugador
        Coordenada posicionAnteriorJugador = mapaEstatico.obtenerPosicionAnteriorJugador();
        Coordenada posicionActualJugador = mapaEstatico.obtenerPosicionJugador();
        Coordenada destino = mapaEstatico.obtenerDestino();

        if(posicionAnteriorJugador != null){

            //Quitar resaltado de la ruta optima anterior
            var rutaOptimaAnterior = mapaEstatico.obtenerRutaOptima(posicionAnteriorJugador, destino);
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
            var rutaOptimaActual = mapaEstatico.obtenerRutaOptima(posicionActualJugador, destino);
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
        int combustible = vehiculoEstatico.getGasolinaActual();
        var posicion = mapaEstatico.obtenerPosicionJugador();
        var destino = mapaEstatico.obtenerDestino();
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
        if (!misionTerminada) {
            if (controller.isMisionCompletada()) {
                misionTerminada = true;
                mostrarMensajeFin("¡MISIÓN COMPLETADA!", Color.LIME);
            } else if (combustible <= 0) {
                misionTerminada = true;
                mostrarMensajeFin("¡SIN COMBUSTIBLE!", Color.RED);
            } else if (tiempoRestante <= 0){
                misionTerminada = true;
                mostrarMensajeFin("¡TIEMPO AGOTADO!", Color.RED);
            }
        }
    }
    
    /**
     * Maneja las teclas presionadas.
     */
    private void manejarTeclaPresionada(KeyEvent event) {
        if (misionTerminada && event.getCode() == KeyCode.R) {
            reintentar();
            return;
        }
        
        if (event.getCode() == KeyCode.ESCAPE) {
            primaryStage.close();
            System.exit(0);
        }
        
        if (misionTerminada) {
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
    
    /**
     * Reinicia la misión.
     */
    private void reintentar() {
        misionTerminada = false;
        mensajeOverlay.setVisible(false);
        controller.reiniciarMision();
        renderizarMapa();
        actualizarInformacion();
    }

    private void procesarCelda(CeldaMapa celda){
        Coordenada posicionJugador = mapaEstatico.obtenerPosicionJugador();
        Coordenada destino = mapaEstatico.obtenerDestino();
        Coordenada recompensaExtra = mapaEstatico.obtenerCoordenadaRecompensaExtra();
        
        int fila = celda.getFila();
        int col = celda.getColumna();

        //carga las texturas de la celda según el terreno
        String terreno = mapaEstatico.obtenerTipoTerreno(fila, col);
        celda.setTerreno(terreno); // Establecer color según terreno

        //mostrar recompensa extra
        if (!mapaEstatico.seConsiguioRecompensaExtra() && recompensaExtra.obtenerX() == col && recompensaExtra.obtenerY() == fila){
            celda.mostrarIcono("⭐", Color.GOLD);
            celda.resaltar(Color.GOLD);
        }

        // Mostrar tráfico si existe
        if (mapaEstatico.tieneTrafico(col,fila)){ 
            celda.mostrarIcono("⛔", Color.RED); // Mostrar ícono de tráfico
        }

        // Mostrar destino (cuadrado verde)
        if (destino.obtenerX() == col && destino.obtenerY() == fila){
            celda.mostrarIcono("■", Color.LIME);
            celda.resaltar(Color.LIME);
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

package org.ayed.gta.ui;

import org.ayed.gta.mapa.Mapa;
import org.ayed.gta.mapa.Coordenada;
import org.ayed.gta.Vehiculo;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controlador de la misión.
 * Maneja la lógica de movimiento del vehículo en el mapa.
 */
public class MisionController {
    
    private Mapa mapa;
    private Vehiculo vehiculoActual;
    private int velocidadMovimiento = 1; // células por movimiento
    private boolean enMision = true;

    private float tiempoRestante;
    private float tiempoPorCelda;
    private static final int TASA_DE_DEMORA_TRAFICO = 5; // Factor de demora por tráfico
    
    /**
     * Inicializa el controlador con un mapa y un vehículo.
     */
    public MisionController(Mapa mapa, Vehiculo vehiculoActual, int tiempoLimite) {
        this.mapa = mapa;
        this.vehiculoActual = vehiculoActual;
        this.tiempoRestante = tiempoLimite;
        this.tiempoPorCelda = vehiculoActual.getVelocidadMaxima(); //tiempo en hacer 100m [segundos]
    }
    
    /**
     * Procesa la entrada del teclado para mover el vehículo.
     */
    public void manejarTecla(KeyCode codigo) {
        if (!enMision) return;
        
        switch (codigo) {
            case UP:
            case W:
                moverVehiculo(0, -velocidadMovimiento);
                break;
            case DOWN:
            case S:
                moverVehiculo(0, velocidadMovimiento);
                break;
            case LEFT:
            case A:
                moverVehiculo(-velocidadMovimiento, 0);
                break;
            case RIGHT:
            case D:
                moverVehiculo(velocidadMovimiento, 0);
                break;
            case ESCAPE:
                enMision = false;
                break;
            default:
                break;
        }
    }
    
    /**
     * Mueve el vehículo en la dirección especificada.
     * Valida que el movimiento sea a una posición válida en el mapa.
     */
    private void moverVehiculo(int deltaX, int deltaY) {
        Coordenada posicionActual = mapa.obtenerPosicionJugador();
        int nuevaX = posicionActual.obtenerX() + deltaX;
        int nuevaY = posicionActual.obtenerY() + deltaY;
        
        // Validar que la nueva posición sea válida
        if (mapa.esCaminable(nuevaX, nuevaY)) {
            mapa.moverJugador(nuevaX, nuevaY);
            // Consumir gasolina al moverse
            consumirGasolina();
            reducirTiempoLimite(nuevaX, nuevaY);
        }
    }
    
    /**
     * Consume gasolina del vehículo al moverse.
     */
    private void consumirGasolina() {
        int gasolinaActual = vehiculoActual.getGasolinaActual();
        if (gasolinaActual > 0) {
            // Consumir 1 litro por movimiento (ajustable)
            vehiculoActual.consumirGasolina(1);
        } else {
            enMision = false; // Fin de la misión sin gasolina
        }
    }

    private void reducirTiempoLimite(int x, int y) {
        // Reducir tiempo limite
        if (mapa.tieneTrafico(x, y)){
            tiempoRestante-= tiempoPorCelda*TASA_DE_DEMORA_TRAFICO;
        }else{
            tiempoRestante -= tiempoPorCelda;
        }
        
        if (tiempoRestante <= 0) {
            tiempoRestante = 0;
            enMision = false; // Fin de la misión por tiempo agotado
        }
    }
    
    /**
     * Verifica si la misión ha terminado.
     */
    public boolean isMisionCompletada() {
        return mapa.obtenerPosicionJugador().equals(mapa.obtenerDestino());
    }
    
    /**
     * Obtiene el mapa actual.
     */
    public Mapa getMapa() {
        return mapa;
    }
    
    /**
     * Obtiene el vehículo actual.
     */
    public Vehiculo getVehiculoActual() {
        return vehiculoActual;
    }
    
    /**
     * Obtiene el tiempo límite restante en segundos.
     */
    public float getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Verifica si la misión sigue en curso.
     */
    public boolean isEnMision() {
        return enMision;
    }
    
    /**
     * Termina la misión.
     */
    public void terminarMision() {
        enMision = false;
    }
    
    /**
     * Reinicia la misión (resetea vehículo y mapa a estado inicial).
     */
    public void reiniciarMision() {
        if (vehiculoActual != null) {
            vehiculoActual.cargarAlMaximo();
        }
        tiempoRestante = 5000;
        mapa.reiniciarMapa();
        enMision = true;
    }
}

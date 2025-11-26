package org.ayed.tda.aestrella;

import org.ayed.gta.mapa.Celda;

/**
 *  Implementacion de la Heuristica Distancia Manhattan.
 *  Estima la distancia al destino sumando el valor absoluto de las diferencias de las 
 *  coordenadas de ambos nodos.
 */

public class Manhattan implements Heuristica<Celda> {

    @Override
    public int calcularPuntaje(Celda origen, Celda destino) {
        int distancia = Math.abs(origen.obtenerCoordenada().obtenerX() - destino.obtenerCoordenada().obtenerX()) + 
                        Math.abs(origen.obtenerCoordenada().obtenerY() - destino.obtenerCoordenada().obtenerY());
        
        return distancia; 
    }
}
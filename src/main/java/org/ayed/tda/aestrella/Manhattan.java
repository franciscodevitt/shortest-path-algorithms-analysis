package org.ayed.tda.aestrella;

import org.ayed.gta.mapa.Nodo;

/**
 *  Implementacion de la interfaz Heuristica como distancia Manhattan.
 *  Estima la distancia entre dos Nodos usando sus Coordenadas.
 */

public class Manhattan implements Heuristica<Nodo> {

    /**
     *  Calcula la distancia entre el Nodo origen y el Nodo destino como la suma del valor 
     *  absoluto de las diferencias en las Coordenadas de ambos nodos.
     * 
     *  @param origen  Nodo cuya distancia al destino se quiere conocer.
     *  @param destino Nodo al que se tiene que llegar.
     *  @return        Distancia estimada entre ambos nodos.
     */
    @Override
    public int calcularPuntaje(Nodo origen, Nodo destino) {
        int distancia = Math.abs(origen.obtenerCoordenada().obtenerX() - destino.obtenerCoordenada().obtenerX()) + 
                        Math.abs(origen.obtenerCoordenada().obtenerY() - destino.obtenerCoordenada().obtenerY());
        
        return distancia; 
    }
}
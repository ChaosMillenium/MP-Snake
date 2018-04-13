/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author millenium
 */
public class ModeloJuego extends Observable {

    private int columnas = 60, filas = 60;
    private HashMap<Integer, Jugador> jugadores;
    private final int VELOCIDAD = 65;
    private final int TAMAÑOBASE = 3;

    public int añadirJugador() {
        int key = 1;
        for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
            key++;
        }
        Jugador nuevo = new Jugador();
        this.jugadores.put(key, nuevo);
        return key;
    }

    public int getFilas() {
        return this.filas;
    }

    public int getColumnas() {
        return this.columnas;
    }

    public int getTamañoBase() {
        return this.TAMAÑOBASE;
    }

    public Coordenadas[] asignarCoordInicio() {
        Coordenadas[] resultado = new Coordenadas[this.TAMAÑOBASE];
        Random r = new Random();
        for (int i = 0; i < resultado.length; i++) {
            Coordenadas coord = new Coordenadas(r.nextInt(columnas), r.nextInt(filas));
            if (!coincideCasilla(coord)) {
                resultado[i] = coord;
            } else {
                i--;
            }
        }
        return resultado;
    }

    private boolean coincideCasilla(Coordenadas coord) {
        for (Jugador jugador : this.jugadores.values()) {
            LinkedList<Coordenadas> serpiente = jugador.getSerpiente();
            //TODO: 
        }
        return false;    
    }
}

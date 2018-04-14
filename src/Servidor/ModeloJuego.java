/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
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
    private Map<Integer, Jugador> jugadores;
    private final int VELOCIDAD = 60;
    private final int TAMAÑOBASE = 3;

    public ModeloJuego(){
        this.jugadores = new HashMap<>();
    }
    
    public int añadirJugador() {
        int key = 1;
        while (this.jugadores.keySet().contains(key)) {
            key++;
        }
        Jugador nuevo = new Jugador(this.TAMAÑOBASE);
        asignarCoordInicio(nuevo);
        this.jugadores.put(key, nuevo);
        setChanged();
        notifyObservers();
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

    private void asignarCoordInicio(Jugador jugador) {
        Coordenadas[] coordArray = new Coordenadas[this.TAMAÑOBASE];
        Random r = new Random();
        int nuevaX = r.nextInt(columnas);
        int nuevaY = r.nextInt(filas);
        Coordenadas nuevaCoordInicio = new Coordenadas(nuevaX, nuevaY);
        coordArray[0] = nuevaCoordInicio;
        jugador.nuevaCabeza(nuevaCoordInicio);
        for (int i = 1; i < this.TAMAÑOBASE; i++) {
            Coordenadas nuevaCoord = new Coordenadas(nuevaX + (r.nextInt(2) - 1), nuevaY + (r.nextInt(2) - 1));
            if (!coincideCasilla(nuevaCoord)) {
                jugador.nuevaCabeza(nuevaCoord);
                coordArray[i] = nuevaCoordInicio;
            } else {
                i--;
            }
        }
    }

    private boolean coincideCasilla(Coordenadas coord) { //Solo comprueba con el resto de serpientes
        for (Jugador jugador : this.jugadores.values()) {
            LinkedList<Coordenadas> serpiente = jugador.getSerpiente();
            for (Coordenadas comparar : serpiente) {
                if (coord.equals(comparar)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cambiarDireccion(Direccion direccion, int id) {
        this.jugadores.get(id).setDireccion(direccion);
    }

}

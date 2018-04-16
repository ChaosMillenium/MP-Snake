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
    private ThreadActualizarTablero hiloTablero;

    public ModeloJuego() {
        this.jugadores = new HashMap<>();
        this.hiloTablero = new ThreadActualizarTablero(this);
    }

    public void añadirJugador() {
        this.hiloTablero.pausa();
        int key = siguienteKey();
        Jugador nuevo = new Jugador(this.TAMAÑOBASE);
        asignarCoordInicio(nuevo);
        this.jugadores.put(key, nuevo);
        if (this.jugadores.size() == 1) {
            this.hiloTablero.start();
        }
        setChanged();
        notifyObservers("NJ;" + key);
        this.hiloTablero.pausa();
    }

    public int siguienteKey() {
        int key = 1;
        while (this.jugadores.keySet().contains(key)) {
            key++;
        }
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

    public int getVELOCIDAD() {
        return VELOCIDAD;
    }

    private void asignarCoordInicio(Jugador jugador) {
        Random r = new Random();
        int nuevoX, nuevoY;
        boolean fin = false;
        while (!fin) {
            //Se asigna una coordenada aleatoria con un margen con los bordes
            nuevoX = r.nextInt(this.columnas - this.TAMAÑOBASE * 2) + this.TAMAÑOBASE;
            nuevoY = r.nextInt(this.filas - this.TAMAÑOBASE * 2) + this.TAMAÑOBASE;
            for (int i = 0; i < this.TAMAÑOBASE; i++) {
                Coordenadas coord = new Coordenadas(nuevoX + i, nuevoY);
                if (coincideCasilla(coord)) { //Se intenta de nuevo
                    jugador.eliminarSerpiente();
                    break;
                } else {
                    jugador.nuevaCabeza(coord);
                }
                if (i == this.TAMAÑOBASE - 1) {
                    fin = true;
                }
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

    public void eliminarJugador(int id) {
        this.jugadores.remove(id);
    }

    public boolean hayJugadores() {
        return !this.jugadores.isEmpty();
    }

    public Coordenadas[] getCoordenadas(int id) {
        Jugador jugador = this.jugadores.get(id);
        Coordenadas[] coordenadas = new Coordenadas[jugador.getSerpiente().size()];
        jugador.getSerpiente().toArray(coordenadas);
        return coordenadas;
    }
}

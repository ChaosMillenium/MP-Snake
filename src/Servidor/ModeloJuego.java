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
    private Thread hiloTablero;

    public ModeloJuego() {
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
        if (this.jugadores.size()==1){
            this.hiloTablero = new Thread(new ThreadActualizarTablero(this));
            this.hiloTablero.start();
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

    private void asignarCoordInicio(Jugador jugador) {
        Random r = new Random();
        int nuevoX,nuevoY;
        boolean fin = false;
        while (!fin) {
            nuevoX = r.nextInt(this.columnas);
            nuevoY = r.nextInt(this.filas - this.TAMAÑOBASE) + this.TAMAÑOBASE;
            for (int i = 0; i < this.TAMAÑOBASE; i++){
                Coordenadas coord = new Coordenadas(nuevoX + i, nuevoY);
                if (coincideCasilla(coord)) break;
                else jugador.nuevaCabeza(coord);
                if (i == this.TAMAÑOBASE-1) fin = true;
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

    public boolean hayJugadores(){
        return !this.jugadores.isEmpty();
    }
}

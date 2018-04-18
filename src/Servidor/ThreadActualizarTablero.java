/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author millenium
 */
public class ThreadActualizarTablero extends Thread {

    private ModeloJuego modelo;
    private Map<Integer, Jugador> jugadores;
    private boolean pausa = false;
    private final int VELOCIDAD;
    private final int PROBABILIDAD = 5;

    public ThreadActualizarTablero(ModeloJuego modelo) {
        this.modelo = modelo;
        this.VELOCIDAD = this.modelo.getVELOCIDAD();
        this.jugadores = this.modelo.getJugadores();
    }

    @Override
    public void run() {
        Random aleatTesoro = new Random();
        while (hayJugadores()) {
            try {
                while (this.pausa) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println("Error en hilo de tablero.");
                        this.start();
                    }
                }
                for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) { //Movimiento de jugadores
                    entrada.getValue().nuevaCabeza();
                    entrada.getValue().eliminarCola();
                    this.modelo.notificarMovimiento(entrada.getKey());
                }
                //TODO: Revisar probabilidades
                if (aleatTesoro.nextInt(100) < PROBABILIDAD) { //10% cada VELOCIDAD ms de generar un tesoro
                    this.modelo.generarTesoro();
                }
            } catch (ConcurrentModificationException e) {
                //Reintenta si no se puede acceder al mapa de jugadores
            }
            try {
                Thread.sleep(this.VELOCIDAD);
            } catch (InterruptedException e) {
                System.err.println("Error en hilo de tablero.");
                this.start();
            }
        }
    }

    public void pausa() {
        this.pausa = !this.pausa;
    }

    private boolean hayJugadores() {
        return this.modelo.hayJugadores();
    }
}

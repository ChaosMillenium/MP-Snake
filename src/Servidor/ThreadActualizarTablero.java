/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author millenium
 */
public class ThreadActualizarTablero extends Thread {

    private ModeloJuego modelo;
    private boolean pausa = false;
    private final int VELOCIDAD;
    private final int PROBABILIDAD = 1;

    public ThreadActualizarTablero(ModeloJuego modelo) {
        this.modelo = modelo;
        this.VELOCIDAD = this.modelo.getVELOCIDAD();
    }

    @Override
    public void run() {
        Random aleatTesoro = new Random();
        while (hayJugadores()) {
            while (this.pausa) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("Error en hilo de tablero.");
                    this.start();
                }
            }
            synchronized (this.modelo.getJugadores()) {
                for (Map.Entry<Integer, Jugador> entrada : this.modelo.getJugadores().entrySet()) { //Movimiento de jugadores
                    entrada.getValue().nuevaCabeza();
                    entrada.getValue().eliminarCola();
                    this.modelo.notificarMovimiento(entrada.getKey());
                    entrada.getValue().setEspera(false); //Ya se puede cambiar de dirección
                }
            }
            synchronized (this.modelo.getTesoros()) {
                //TODO: Revisar probabilidades
                if (this.modelo.getTesoros().isEmpty() || (aleatTesoro.nextInt(100) < PROBABILIDAD)) { //PROBABILIDAD % cada VELOCIDAD ms de generar un tesoro
                    this.modelo.generarTesoro();
                }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

            Set<Map.Entry<Integer, Jugador>> entradas = Collections.synchronizedSet(this.modelo.getJugadores().entrySet());
            synchronized (entradas) {
                for (Map.Entry<Integer, Jugador> entrada : entradas) { //Movimiento de jugadores
                    Jugador jugador = entrada.getValue();
                    jugador.nuevaCabeza();
                    jugador.eliminarCola();
                    this.modelo.notificarMovimiento(entrada.getKey());
                    jugador.setEspera(false); //Ya se puede cambiar de dirección                    
                }
            }
            synchronized (this.modelo.getTesoros()) {
                //TODO: Revisar probabilidades
                if (this.modelo.getTesoros().isEmpty() || (aleatTesoro.nextInt(100) < PROBABILIDAD)) { //PROBABILIDAD % cada VELOCIDAD ms de generar un tesoro
                    this.modelo.generarTesoro();
                }
            }
            this.modelo.calcularMovimientoAutomatico();
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

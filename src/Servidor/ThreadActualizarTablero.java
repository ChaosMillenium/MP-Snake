/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

/**
 *
 * @author millenium
 */
public class ThreadActualizarTablero extends Thread {

    private ModeloJuego modelo;
    private boolean pausa = false;
    private final int VELOCIDAD;
    
    public ThreadActualizarTablero(ModeloJuego modelo) {
        this.modelo = modelo;
        this.VELOCIDAD = this.modelo.getVELOCIDAD();
    }

    @Override
    public void run() {
        while (hayJugadores()) {
            while (this.pausa) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("Error en hilo de tablero.");
                    this.start();
                }
            }
            
            try{
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

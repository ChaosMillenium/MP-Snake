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
public class ThreadActualizarTablero implements Runnable{

    private ModeloJuego modelo;
    
    public ThreadActualizarTablero(ModeloJuego modelo){
        this.modelo=modelo;
    }
    
    @Override
    public void run() {
        while(hayJugadores()){
        }
    }
    
    private boolean hayJugadores(){
        return this.modelo.hayJugadores();
    }
}

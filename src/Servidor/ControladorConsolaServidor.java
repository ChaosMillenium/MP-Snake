/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

/**
 *
 * @author i.chicano.2016
 */
public class ControladorConsolaServidor {
    
    private ModeloJuego modelo;

    public ControladorConsolaServidor(ModeloJuego modelo) {
        this.modelo = modelo;
    }       
    
    public void eliminarJugador(int id) {
        this.modelo.finalizarJugador(id);
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Direccion;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author millenium
 */
public class ControladorServidor implements Observer {

    private ModeloJuego modelo;
    
    public ControladorServidor(ModeloJuego modelo){
        this.modelo=modelo;
    }
    
    public void iniciarServer() {
        ThreadServer servidor = new ThreadServer(this);
        servidor.startServer();
    }
    
    public int añadirJugador(){
        return this.modelo.añadirJugador();
    }
    
    public int getFilas(){
        return this.modelo.getFilas();
    }
    
    public int getColumnas(){
        return this.modelo.getColumnas();
    }
    
    public int getTamañoBase(){
        return this.modelo.getTamañoBase();
    }
    
    @Override
    public void update(Observable o, Object arg) {

    }    
    
    public void cambiarDireccion(String direccion, int id) {
        Direccion instanciaDir = Direccion.valueOf(direccion);
        this.modelo.cambiarDireccion(instanciaDir,id);
    }
    
}

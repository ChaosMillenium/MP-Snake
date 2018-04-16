/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author millenium
 */
public class ControladorServidor implements Observer {

    private ModeloJuego modelo;
    private ThreadServer servidor;

    public ControladorServidor(ModeloJuego modelo) {
        this.modelo = modelo;
    }

    public void iniciarServer() {
        this.servidor = new ThreadServer(this);
        this.servidor.startServer();
    }

    public void añadirJugador() {
        this.modelo.añadirJugador();
    }

    public int getFilas() {
        return this.modelo.getFilas();
    }

    public int getColumnas() {
        return this.modelo.getColumnas();
    }

    public int getTamañoBase() {
        return this.modelo.getTamañoBase();
    }

    @Override
    public void update(Observable o, Object arg) {
        String accion = (String) arg;
        String[] parseado = accion.split(";");
        if (parseado[0].equals("NJ")){
            int id = Integer.parseInt(parseado[1]);
            this.servidor.nuevoJugador(id,coordenadas(id));
        }
        
    }

    public void cambiarDireccion(int id,String direccion) {
        try {
            Direccion instanciaDir = Direccion.valueOf(direccion);
            this.modelo.cambiarDireccion(instanciaDir, id);
        } catch (IllegalArgumentException e) {
            System.err.println("Dirección inválida");
        }
    }

    public void eliminarJugador(int id) {
        this.modelo.eliminarJugador(id);
    }

    public int[] coordenadas(int id) {
        Coordenadas[] coordenadas = this.modelo.getCoordenadas(id);
        int[] coordInt = new int[coordenadas.length*2];
        int j = 0;
        for (int i = 0; i < coordInt.length; i+=2){
            coordInt[i] = coordenadas[j].getX();
            coordInt[i+1] = coordenadas[j].getY();
            j++;
        }
        return coordInt;
    }

    public int siguienteKey() {
        return this.modelo.siguienteKey();
    }

}

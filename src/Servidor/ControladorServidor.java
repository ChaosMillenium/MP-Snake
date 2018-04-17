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
        this.servidor = new ThreadServer(this);
    }

    public void iniciarServer() {
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
        switch (parseado[0]) {
            case "NJ": {
                int id = Integer.parseInt(parseado[1]);
                this.servidor.nuevoJugador(id, coordenadas(id));
                break;
            }
            case "MOV": {
                int id = Integer.parseInt(parseado[1]);
                int[] cabeza = {this.modelo.getCabeza(id).getX(),
                    this.modelo.getCabeza(id).getY()};
                int[] cola = {this.modelo.getAnteriorCola(id).getX(),
                    this.modelo.getAnteriorCola(id).getY()};
                this.servidor.moverJugador(id,cabeza,cola);
                break;
            }
            
            case "COL": {
                int id1 = Integer.parseInt(parseado[1]);
                int id2 = Integer.parseInt(parseado[2]);
                this.servidor.colision(id1, id2);
                break;
            }
            
            case "CBR": {
                int id = Integer.parseInt(parseado[1]);
                this.servidor.colision(id);
                break;
            }
            
            case "PTS": {
                int id = Integer.parseInt(parseado[1]);
                int puntos = Integer.parseInt(parseado[2]);
                this.servidor.darPuntos(id, puntos);
            }
            
            default:{
                System.err.println(accion);
                break;
            }
            
        }

    }

    public void cambiarDireccion(int id, String direccion) {
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
        int[] coordInt = new int[coordenadas.length * 2];
        int j = 0;
        for (int i = 0; i < coordInt.length; i += 2) {
            coordInt[i] = coordenadas[j].getX();
            coordInt[i + 1] = coordenadas[j].getY();
            j++;
        }
        return coordInt;
    }

    public int siguienteKey() {
        return this.modelo.siguienteKey();
    }

}

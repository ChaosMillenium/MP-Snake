/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author danie
 */
public class Serpiente {
    
    private LinkedList<Coordenadas> serp;
    private Direccion dir;
    private Coordenadas cabeza;
    private int id;
    private long puntos;

    public Serpiente(int id){
        //Metodo constructor de serpiente
        this.dir = Direccion.ARRIBA; //añadir direccion aleatoria en el futuro
        this.serp = new LinkedList<Coordenadas>();
        this.cabeza = null;
        this.puntos = 0;
        this.id = id;
    }

    public Direccion getDir() {
        return dir;
    }

    public void setDir(Direccion dir) {
        this.dir = dir;
    }

    public int getId() {
        return id;
    }

    public long getPuntos() {
        return puntos;
    }

    public void setPuntos(long puntos) {
        this.puntos = puntos;
    }
   
    
}

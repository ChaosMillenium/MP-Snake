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

    public int getId() {
        return id;
    }
    public long getPuntos(){
        return puntos;
    }
    public Serpiente(ArrayList<Coordenadas> coors){
        this.dir = Direccion.ARRIBA; //añadir direccion aleatoria en el futuro
        this.serp = new LinkedList<Coordenadas>();
        this.cabeza = coors.get(0);
        for(int i = 0; i < coors.size(); i++){
            this.serp.add(coors.get(i));
        }
    }

    
}

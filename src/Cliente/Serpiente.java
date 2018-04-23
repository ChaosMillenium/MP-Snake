/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.Coordenadas;
import java.util.LinkedList;

/**
 *
 * @author danie
 */
public class Serpiente {
    
    private LinkedList<Coordenadas> serp;
    private Coordenadas cabeza;
    private int id;
    private long puntos;

    public Serpiente(int id){
        //Metodo constructor de serpiente
        this.serp = new LinkedList<Coordenadas>();
        this.cabeza = null;
        this.puntos = 0;
        this.id = id;
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
    
    public Coordenadas getCabeza(){
        return this.serp.get(0);
    }
    
    public Coordenadas getCoordenadas(int n){
        return this.serp.get(n);
    }
    
    public void addCasilla(Coordenadas c){
        this.serp.add(c);
    }
    
    public void eliminarCola(){
        this.serp.remove(this.serp.size()-1);
    }
    
    public int getLongitud(){
        return this.serp.size();
    }
   
    
    
}

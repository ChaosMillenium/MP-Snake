/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.LinkedList;
import Utilidades.*;
/**
 *
 * @author millenium
 */
public class Jugador {
    private LinkedList<Coordenadas> serpiente;
    private int tamaño;
    private int puntos=0;
    private Direccion direccion=Direccion.ARRIBA;

    public Jugador(int tamaño) {
        this.tamaño = tamaño;
        this.serpiente = new LinkedList<>();
    }

    public LinkedList<Coordenadas> getSerpiente() {
        return serpiente;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
    public void nuevaCabeza(Coordenadas coord){
        this.serpiente.add(0,coord);
    }
}

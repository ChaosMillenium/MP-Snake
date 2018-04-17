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
    private int puntos = 0;
    private Direccion direccion = Direccion.ARRIBA;
    private Coordenadas anteriorCola;
    
    public Jugador(int tamaño) {
        this.tamaño = tamaño;
        this.serpiente = new LinkedList<>();
    }

    public LinkedList<Coordenadas> getSerpiente() {
        return serpiente;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Coordenadas getCabeza(){
        return this.serpiente.getFirst();
    }
    
    public Coordenadas getAnteriorCola() {
        return anteriorCola;
    }

    public void nuevaCabeza(Coordenadas coord) {
        this.serpiente.addFirst(coord);
    }

    public void nuevaCabeza() {
        Coordenadas cabeza = this.serpiente.getFirst();
        Coordenadas nuevaCabeza;
        switch (this.direccion) {
            case ARRIBA: {
                int nuevoY = this.serpiente.getFirst().getY() + 1;
                nuevaCabeza = new Coordenadas(cabeza.getX(), nuevoY);
                this.serpiente.addFirst(nuevaCabeza);
                break;
            }
            case ABAJO:{
                int nuevoY = this.serpiente.getFirst().getY() - 1;
                nuevaCabeza = new Coordenadas(cabeza.getX(), nuevoY);
                this.serpiente.addFirst(nuevaCabeza);
                break;
            }
            case IZQ:{
                int nuevoX = this.serpiente.getFirst().getY() - 1;
                nuevaCabeza = new Coordenadas(nuevoX, cabeza.getY());
                this.serpiente.addFirst(nuevaCabeza);
                break;
            }
            case DER:{
                int nuevoX = this.serpiente.getFirst().getY() + 1;
                nuevaCabeza = new Coordenadas(nuevoX, cabeza.getY());
                this.serpiente.addFirst(nuevaCabeza);
                break;
            }
        }
    }

    public void eliminarCola() {
        this.anteriorCola = this.serpiente.removeLast();
    }

    public void eliminarSerpiente() {
        this.serpiente.clear();
    }
    
    public void añadirPuntos(int puntos){
        this.puntos+=puntos;
        this.tamaño++;
        this.nuevaCabeza();
    }

    public int getPuntos() {
        return puntos;
    }
    
    
}

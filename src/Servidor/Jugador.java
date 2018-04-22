package Servidor;

import java.util.LinkedList;
import Utilidades.*;

public class Jugador {

    private LinkedList<Coordenadas> serpiente;
    private int tamaño;
    private int puntos = 0;
    private Direccion direccion = Direccion.ARRIBA;
    private Coordenadas anteriorCola;
    private boolean espera = false; //Valora que no se pueda cambiar de dirección hasta el próximo turno de tablero para evitar giros de 360º si se pulsa muy rápido

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

    public Coordenadas getCabeza() {
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
        Coordenadas nuevaCabeza = null;
        switch (this.direccion) {
            case ARRIBA: {
                int nuevoY = this.serpiente.getFirst().getY() - 1;
                nuevaCabeza = new Coordenadas(cabeza.getX(), nuevoY);
                break;
            }
            case ABAJO: {
                int nuevoY = this.serpiente.getFirst().getY() + 1;
                nuevaCabeza = new Coordenadas(cabeza.getX(), nuevoY);
                break;
            }
            case IZQ: {
                int nuevoX = this.serpiente.getFirst().getX() - 1;
                nuevaCabeza = new Coordenadas(nuevoX, cabeza.getY());
                break;
            }
            case DER: {
                int nuevoX = this.serpiente.getFirst().getX() + 1;
                nuevaCabeza = new Coordenadas(nuevoX, cabeza.getY());
                break;
            }
        }
        this.serpiente.addFirst(nuevaCabeza);
    }

    public void eliminarCola() {
        this.anteriorCola = this.serpiente.removeLast();
    }

    public void eliminarSerpiente() {
        this.serpiente.clear();
    }

    public void añadirPuntos(int puntos) {
        this.puntos += puntos;
        this.tamaño++;
        this.nuevaCabeza();
    }

    public int getPuntos() {
        return puntos;
    }

    public boolean isEspera() {
        return espera;
    }

    public void setEspera(boolean espera) {
        this.espera = espera;
    }

}

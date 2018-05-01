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
    private boolean manual = false;

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
        Coordenadas nuevaCabeza = moverEnDireccion();
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
        this.serpiente.addLast(this.anteriorCola);
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

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isManual() {
        return manual;
    }

    public Coordenadas moverEnDireccion() {
        switch (this.direccion) {
            case ARRIBA: {
                int nuevoY = this.serpiente.getFirst().getY() - 1;
                return new Coordenadas(this.serpiente.getFirst().getX(), nuevoY);
            }
            case ABAJO: {
                int nuevoY = this.serpiente.getFirst().getY() + 1;
                return new Coordenadas(this.serpiente.getFirst().getX(), nuevoY);
            }
            case IZQ: {
                int nuevoX = this.serpiente.getFirst().getX() - 1;
                return new Coordenadas(nuevoX, this.serpiente.getFirst().getY());
            }
            case DER: {
                int nuevoX = this.serpiente.getFirst().getX() + 1;
                return new Coordenadas(nuevoX, this.serpiente.getFirst().getY());
            }
            default: {
                return null;
            }
        }
    }
}

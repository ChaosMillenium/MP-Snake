/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author millenium
 */
public class ModeloJuego extends Observable {

    private int columnas, filas;
    private Map<Integer, Jugador> jugadores;
    private final int VELOCIDAD = 60;
    private final int TAMAÑOBASE = 3;
    private final int PUNTOSTESORO = 100;
    private ThreadActualizarTablero hiloTablero;
    private List<Coordenadas> tesoros;

    public ModeloJuego() {
        this.jugadores = Collections.synchronizedMap(new HashMap<>());
        this.tesoros = Collections.synchronizedList(new ArrayList<>());
    }

    public void añadirJugador() {
        boolean empezarJuego = this.jugadores.isEmpty();
        if (!empezarJuego) {
            this.hiloTablero.pausa();
        }
        int key = siguienteKey();
        Jugador nuevo = new Jugador(this.TAMAÑOBASE);
        asignarCoordInicio(nuevo, key);
        synchronized (this.jugadores) {
            this.jugadores.put(key, nuevo);
        }
        if (empezarJuego) {
            this.hiloTablero = new ThreadActualizarTablero(this);
            this.hiloTablero.start();
            this.generarTesoro();
        }
        setChanged();
        notifyObservers("NJ;" + key);
        if (!empezarJuego) {
            this.hiloTablero.pausa();
        }
    }

    public int siguienteKey() {
        //posible problema de las keys
        int key = 1;
        synchronized (this.jugadores) {
            while (this.jugadores.keySet().contains(key)) {
                key++;
            }
        }
        return key;
    }

    public int getFilas() {
        return this.filas;
    }

    public int getColumnas() {
        return this.columnas;
    }

    public int getTamañoBase() {
        return this.TAMAÑOBASE;
    }

    public int getVELOCIDAD() {
        return VELOCIDAD;
    }

    public Map<Integer, Jugador> getJugadores() {
        return jugadores;
    }

    public Coordenadas getCabeza(int id) {
        synchronized (this.jugadores) {
            return this.jugadores.get(id).getCabeza();
        }
    }

    public Coordenadas getAnteriorCola(int id) {
        synchronized (this.jugadores) {
            return this.jugadores.get(id).getAnteriorCola();
        }
    }

    public int getPUNTOSTESORO() {
        return PUNTOSTESORO;
    }

    public List<Coordenadas> getTesoros() {
        return tesoros;
    }

    private void asignarCoordInicio(Jugador jugador, int id) {
        Random r = new Random();
        int nuevoX, nuevoY;
        boolean fin = false;
        while (!fin) {
            //Se asigna una coordenada aleatoria con un margen con los bordes
            nuevoX = r.nextInt(this.columnas - this.TAMAÑOBASE * 2) + this.TAMAÑOBASE;
            nuevoY = r.nextInt(this.filas - this.TAMAÑOBASE * 2) + this.TAMAÑOBASE;
            for (int i = 0; i < this.TAMAÑOBASE; i++) {
                Coordenadas coord = new Coordenadas(nuevoX + i, nuevoY);
                if (colisionJugador(coord, id) != 0) { //Se intenta de nuevo
                    jugador.eliminarSerpiente();
                    break;
                } else {
                    jugador.nuevaCabeza(coord);
                }
                if (i == this.TAMAÑOBASE - 1) {
                    fin = true;
                }
            }
        }

    }

    private int colisionJugador(Coordenadas coord, int id) { //Solo comprueba con el resto de serpientes (devuelve id del choque, o 0 si no choca)
        synchronized (this.jugadores) {
            for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
                LinkedList<Coordenadas> serpiente = entrada.getValue().getSerpiente();
                for (Coordenadas comparar : serpiente) {
                    if ((id == entrada.getKey()) && (!comparar.equals(serpiente.getFirst()))) {
                        if (coord.equals(comparar)) {
                            return entrada.getKey();
                        }
                    }
                }
            }
        }
        return 0;
    }

    private boolean colisionBorde(Coordenadas coord) {
        int x = coord.getX();
        int y = coord.getY();
        return ((x < 0) || (y < 0) || (x >= this.columnas) || (y >= this.filas));
    }

    public void cambiarDireccion(Direccion direccion, int id) {
        synchronized (this.jugadores) {
            if (((direccion == Direccion.ARRIBA) && (direccion != Direccion.ABAJO)) //No se puede mover en la dirección contraria
                    || ((direccion == Direccion.ABAJO) && (direccion != Direccion.ARRIBA))
                    || ((direccion == Direccion.IZQ) && (direccion != Direccion.DER))
                    || ((direccion == Direccion.DER) && (direccion != Direccion.IZQ))) {
                this.jugadores.get(id).setDireccion(direccion);
            }
        }
    }

    public void eliminarJugador(int id) {
        synchronized (this.jugadores) {
            this.jugadores.remove(id);
        }
    }

    public boolean hayJugadores() {
        return !this.jugadores.isEmpty();
    }

    public Coordenadas[] getCoordenadas(int id) {
        Jugador jugador = this.jugadores.get(id);
        Coordenadas[] coordenadas = new Coordenadas[jugador.getSerpiente().size()];
        jugador.getSerpiente().toArray(coordenadas);
        return coordenadas;
    }

    public void notificarMovimiento(int id) { //Revisa las colisiones, si no colisiona envia movimiento y comprueba tesoro
        int idColision;
        setChanged();
        if ((idColision = this.colisionJugador(this.jugadores.get(id).getCabeza(), id)) != 0) {
            notifyObservers("COL;" + id + ";" + idColision);
            this.eliminarJugador(id);
            if (idColision != id) { //Si la colisión es entre 2 jugadores elimina al segundo
                this.eliminarJugador(idColision);
            }
        } else if (this.colisionBorde(this.jugadores.get(id).getCabeza())) {
            notifyObservers("CBR;" + id);
            this.eliminarJugador(id);
        } else {
            notifyObservers("MOV;" + id);
            if (this.colisionTesoro(this.jugadores.get(id).getCabeza())) {
                this.jugadores.get(id).añadirPuntos(this.PUNTOSTESORO);
                int nuevosPuntos = this.jugadores.get(id).getPuntos();
                setChanged();
                notifyObservers("PTS;" + id + ";" + nuevosPuntos);
            }
        }
    }

    public void generarTesoro() {
        Random r = new Random();
        Coordenadas candidato = new Coordenadas(r.nextInt(this.columnas), r.nextInt(this.filas));
        while (colisionJugador(candidato, 0) != 0) {
            candidato = new Coordenadas(r.nextInt(this.columnas), r.nextInt(this.filas));
        }
        this.tesoros.add(candidato);
        setChanged();
        notifyObservers("TSR;" + candidato.getX() + ";" + candidato.getY());
    }

    private boolean colisionTesoro(Coordenadas coord) {
        for (Coordenadas tesoro : this.tesoros) {
            if (coord.equals(tesoro)) {
                return true;
            }
        }
        return false;
    }

    public void setFilasColumnas(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
    }
}

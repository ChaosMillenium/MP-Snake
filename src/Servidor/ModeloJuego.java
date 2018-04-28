/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import static java.lang.Double.max;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author millenium
 */
public class ModeloJuego extends Observable {

    private int columnas, filas;
    private Map<Integer, Jugador> jugadores;
    private final int VELOCIDAD = 75;
    private final int TAMAÑOBASE = 3;
    private final int PUNTOSTESORO = 100;
    private ThreadActualizarTablero hiloTablero;
    private List<Coordenadas> tesoros;

    public ModeloJuego() {
        this.jugadores = new ConcurrentHashMap<>();
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

    private void asignarCoordInicio(Jugador jugador, int id) { //TODO: Rehacer
        Random r = new Random();
        int margen = this.TAMAÑOBASE;
        int nuevoX = r.nextInt(this.columnas - margen) + margen;
        int nuevoY = r.nextInt(this.filas - margen) + margen;
        Coordenadas nuevoCoord = new Coordenadas(nuevoX, nuevoY);
        if ((colisionJugador(nuevoCoord, id) == 0) && (colisionTesoro(nuevoCoord) == null)) {
            jugador.nuevaCabeza(nuevoCoord);
            Coordenadas arriba = new Coordenadas(nuevoX, nuevoY - 1);
            Coordenadas abajo = new Coordenadas(nuevoX, nuevoY + 1);
            Coordenadas der = new Coordenadas(nuevoX + 1, nuevoY);
            Coordenadas izq = new Coordenadas(nuevoX - 1, nuevoY);
            for (int i = 1; i < this.TAMAÑOBASE; i++) {
                if ((colisionJugador(arriba, id) == 0) && (colisionTesoro(abajo) == null)) {
                    jugador.nuevaCabeza(arriba);
                    arriba.aumentarX(-1);
                    abajo.aumentarX(-1);
                    izq.aumentarX(-1);
                    der.aumentarX(-1);
                } else if ((colisionJugador(abajo, id) == 0) && (colisionTesoro(abajo) == null)) {
                    jugador.nuevaCabeza(abajo);
                    arriba.aumentarX(1);
                    abajo.aumentarX(1);
                    izq.aumentarX(1);
                    der.aumentarX(1);
                } else if ((colisionJugador(der, id) == 0) && (colisionTesoro(der) == null)) {
                    jugador.nuevaCabeza(der);
                    arriba.aumentarY(1);
                    abajo.aumentarY(1);
                    izq.aumentarY(1);
                    der.aumentarY(1);
                } else if ((colisionJugador(izq, id) == 0) && (colisionTesoro(izq) == null)) {
                    jugador.nuevaCabeza(izq);
                    arriba.aumentarY(-1);
                    abajo.aumentarY(-1);
                    izq.aumentarY(-1);
                    der.aumentarY(-1);
                } else {
                    jugador.eliminarSerpiente();
                    asignarCoordInicio(jugador, id); //Empezar de nuevo
                }
            }
        } else {
            asignarCoordInicio(jugador, id); //Empezar de nuevo
        }
    }

    private int colisionJugador(Coordenadas coord, int id) { //Solo comprueba con el resto de serpientes (devuelve id del choque, o 0 si no choca)
        synchronized (this.jugadores) {
            for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
                LinkedList<Coordenadas> serpiente = entrada.getValue().getSerpiente();
                int primero;
                if (id == entrada.getKey()) { //Si se revisa la colisión con uno mismo, salta la cabeza (para evitar errores)
                    primero = 1;
                } else {
                    primero = 0;
                }
                for (int i = primero; i < serpiente.size(); i++) {
                    Coordenadas comparar = serpiente.get(i);
                    if (coord.equals(comparar)) {
                        return entrada.getKey();
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
            Jugador jugador = this.jugadores.get(id);
            if (!jugador.isEspera()) { //Comprueba que se pueda cambiar dirección
                if (jugador.getDireccion().equals(Direccion.IZQ) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.DER) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.ARRIBA) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.ABAJO) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                }
            }
        }
    }

    public void eliminarJugador(int id) {
        synchronized (this.jugadores) {
            this.jugadores.remove(id);
        }
    }

    public void finalizarJugador(int id) {
        this.eliminarJugador(id);
        notifyObservers("FIN;" + id);
    }

    public boolean hayJugadores() {
        return !this.jugadores.isEmpty();
    }

    public Coordenadas[] getCoordenadasAnt(int id) {
        Jugador jugador = this.jugadores.get(id);
        Coordenadas[] coordenadasAnt = new Coordenadas[jugador.getSerpiente().size()];
        LinkedList<Coordenadas> serpiente = jugador.getSerpiente();
        for (int i = 1; i < serpiente.size(); i++) {
            coordenadasAnt[i - 1] = serpiente.get(i);
        }
        coordenadasAnt[serpiente.size() - 1] = jugador.getAnteriorCola();
        return coordenadasAnt;
    }

    public Coordenadas[] getCoordenadas(int id) {
        Jugador jugador = this.jugadores.get(id);
        Coordenadas[] coordenadas = new Coordenadas[jugador.getSerpiente().size()];
        jugador.getSerpiente().toArray(coordenadas);
        return coordenadas;
    }

    public void notificarMovimiento(int id) { //Revisa las colisiones, si no colisiona envia movimiento y comprueba tesoro
        synchronized (this.jugadores) {
            int idColision;
            setChanged();

            if (!this.jugadores.isEmpty()) {
                if ((idColision = this.colisionJugador(this.jugadores.get(id).getCabeza(), id)) != 0) {
                    notifyObservers("COL;" + id + ";" + idColision);
                    this.eliminarJugador(id);
                    if (idColision != id) { //Si la colisión es entre 2 jugadores elimina al segundo
                        this.eliminarJugador(idColision);
                    }
                }
            }
            try {
                if (this.colisionBorde(this.jugadores.get(id).getCabeza())) {
                    notifyObservers("CBR;" + id);
                    this.eliminarJugador(id);
                } else {
                    notifyObservers("MOV;" + id);
                    if (!this.jugadores.isEmpty()) {
                        Coordenadas tesoro;
                        if ((tesoro = this.colisionTesoro(this.jugadores.get(id).getCabeza())) != null) {
                            this.tesoros.remove(tesoro);
                            this.jugadores.get(id).añadirPuntos(this.PUNTOSTESORO);
                            int nuevosPuntos = this.jugadores.get(id).getPuntos();
                            setChanged();
                            notifyObservers("PTS;" + id + ";" + nuevosPuntos);
                        }
                    }
                }
            } catch (NullPointerException ex) {
                //Si salta es que el jugador ya no existe
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

    private Coordenadas colisionTesoro(Coordenadas coord) {
        for (Coordenadas tesoro : this.tesoros) {
            if (coord.equals(tesoro)) {
                return tesoro;
            }
        }
        return null;
    }

    public void setFilasColumnas(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
    }

    public void setManual(int id, boolean manual) {
        this.jugadores.get(id).setManual(manual);
    }

    public void calcularMovimientoAutomatico() { //Calcula el siguiente movimiento de todos los jugadores que esten en modo automatico
        Set<Map.Entry<Integer, Jugador>> entradas = Collections.synchronizedSet(this.jugadores.entrySet());
        synchronized (entradas) {
            for (Map.Entry<Integer, Jugador> entrada : entradas) {
                Jugador jugador = entrada.getValue();
                if (!jugador.isManual()) {
                    Direccion direccion = jugador.getDireccion();
                    Coordenadas comprobar = jugador.moverEnDireccion();
                    if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar, busca huir
                        if (direccion.equals(Direccion.ABAJO) || direccion.equals(Direccion.ARRIBA)) {
                            jugador.setDireccion(Direccion.DER);  //Se prueba con el lado derecho y se recalcula
                            if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar
                                jugador.setDireccion(Direccion.IZQ); //Si aun asi fuera a colisionar esta muerto, nada se puede hacer
                            }
                        } else {
                            jugador.setDireccion(Direccion.ARRIBA);
                            if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar
                                jugador.setDireccion(Direccion.ABAJO);
                            }
                        }
                    } else { //Si no va a colisionar, busca un tesoro y va a por el
                        Coordenadas tesoro = tesoroMasCercano(jugador.getCabeza());
                        Direccion nuevaDireccion = calcularDireccion(tesoro, jugador.getCabeza());
                        cambiarDireccion(nuevaDireccion, entrada.getKey());
                    }
                }
            }
        }
    }

    private Direccion calcularDireccion(Coordenadas destino, Coordenadas origen) { //Calcula la direccion mejor a la que acercarse a la coordenada dada desde el origen
        int distanciaX = abs(origen.getX() - destino.getX());
        int distanciaY = abs(origen.getY() - destino.getY());

        if ((distanciaX < distanciaY) || (distanciaY == 0)) {
            if (origen.getX() < destino.getX()) {
                return Direccion.DER;
            } else {
                return Direccion.IZQ;
            }
        } else {
            if (origen.getY() < destino.getY()) {
                return Direccion.ABAJO;
            } else {
                return Direccion.ARRIBA;
            }
        }
    }

    private Coordenadas tesoroMasCercano(Coordenadas origen) {
        Coordenadas resultado = this.tesoros.get(0); //Por defecto
        double menorDistancia = Integer.MAX_VALUE;
        for (Coordenadas tesoro : this.tesoros) {
            int tesoroX = tesoro.getX();
            int tesoroY = tesoro.getY();
            int origenX = origen.getX();
            int origenY = origen.getY();

            double distancia = sqrt(pow(tesoroX - origenX, 2) + pow(tesoroY - origenY, 2));
            if (menorDistancia > distancia) {
                menorDistancia = distancia;
                resultado = tesoro;
            }
        }
        return resultado;
    }
}

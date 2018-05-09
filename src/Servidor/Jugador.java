package Servidor;

import java.util.LinkedList;
import Utilidades.*;

/**
 * Clase que guarda información sobre los jugadores, tales como su serpiente, y
 * permite realizar operaciones con esta información.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class Jugador {

    private LinkedList<Coordenadas> serpiente; //Serpiente asociada al jugador.
    private int tamaño; //Tamaño de la serpiente.
    private int puntos = 0; //Puntuación del jugador.
    private Direccion direccion = Direccion.ARRIBA; //Dirección en la que se mueve la serpiente del jugador.
    private Coordenadas anteriorCola; //Anterior casilla borrada, usada para algunos cálculos como el crecimiento de la serpiente.
    private boolean espera = false; //Valora que no se pueda cambiar de dirección hasta el próximo turno de tablero para evitar giros de 360º si se pulsa muy rápido.
    private boolean manual = false; //Valora si el jugador está en modo manual o automático (True para manual).

    /**
     * Crea un nuevo jugador.
     *
     * @param tamaño Tamaño de la serpiente del jugador.
     */
    public Jugador(int tamaño) {
        this.tamaño = tamaño;
        this.serpiente = new LinkedList<>();
    }

    /**
     * Devuelve una referencia a la lista que guarda las coordenadas de la
     * serpiente.
     *
     * @return Referencia a la lista que guarda las coordenadas de la serpiente.
     */
    public LinkedList<Coordenadas> getSerpiente() {
        return serpiente;
    }

    /**
     * Devuelve la dirección en la que se está moviendo la serpiente.
     *
     * @return Dirección en la que se está moviendo la serpiente.
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Cambia la dirección de la serpiente (Comprobando que se pueda realizar el
     * cambio de dirección).
     *
     * @param direccion Dirección a la que cambiar la dirección actual.
     */
    public void setDireccion(Direccion direccion) {
        if (!this.espera) { //Comprueba que se pueda cambiar dirección
            //Comprobamos que no se pueda hacer un giro de 360º o que no se cambie la dirección.
            if (this.direccion.equals(Direccion.IZQ) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                this.direccion = direccion;
                this.espera = true; //No se puede volver a cambiar dirección hasta siguiente ciclo
            } else if (this.direccion.equals(Direccion.DER) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                this.direccion = direccion;
                this.espera = true; //No se puede volver a cambiar dirección hasta siguiente ciclo
            } else if (this.direccion.equals(Direccion.ARRIBA) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                this.direccion = direccion;
                this.espera = true; //No se puede volver a cambiar dirección hasta siguiente ciclo
            } else if (this.direccion.equals(Direccion.ABAJO) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                this.direccion = direccion;
                this.espera = true; //No se puede volver a cambiar dirección hasta siguiente ciclo
            }
        }
    }

    /**
     * Devuelve las coordenadas de la cabeza de la serpiente.
     *
     * @return Coordenadas de la cabeza de la serpiente.
     */
    public Coordenadas getCabeza() {
        return this.serpiente.getFirst();
    }

    /**
     * Devuelve las coordenadas de la anterior cola de la serpiente.
     *
     * @return Coordenadas de la anterior cola de la serpiente.
     */
    public Coordenadas getAnteriorCola() {
        return anteriorCola;
    }

    /**
     * Añade las coordenadas indicadas al principio de la serpiente.
     *
     * @param coord Coordenadas que se convertirán en la nueva cabeza.
     */
    public void nuevaCabeza(Coordenadas coord) {
        this.serpiente.addFirst(coord);
    }

    /**
     * Añade unas nuevas coordenadas delante de la cabeza anterior de la
     * serpiente para simular movimiento.
     *
     * Se añadirá en la dirección en la que se esté moviendo la serpiente
     * actualmente.
     */
    public void nuevaCabeza() {
        Coordenadas nuevaCabeza = simularDireccion();
        this.serpiente.addFirst(nuevaCabeza);
    }

    /**
     * Elimina la casilla que esté al final de la serpiente.
     */
    public void eliminarCola() {
        this.anteriorCola = this.serpiente.removeLast();
    }

    /**
     * Elimina todas las casillas de la serpiente.
     */
    public void eliminarSerpiente() {
        this.serpiente.clear();
    }

    /**
     * Añade puntos al jugador y aumenta el tamaño de la serpiente.
     *
     * @param puntos Puntos a añadir.
     */
    public void añadirPuntos(int puntos) {
        this.puntos += puntos;
        this.tamaño++;
        this.serpiente.addLast(this.anteriorCola);
    }

    /**
     * Devuelve los puntos actuales del jugador.
     *
     * @return Puntos actuales del jugador.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Cambia el atributo "espera" al indicado.
     *
     * @param espera Valor al que se quiere poner el atributo "espera".
     */
    public void setEspera(boolean espera) {
        this.espera = espera;
    }

    /**
     * Cambia el atributo "manual" al indicado.
     *
     * @param manual Valor al que se quiere poner el atributo "manual".
     */
    public void setManual(boolean manual) {
        this.manual = manual;
    }

    /**
     * Comprueba si el jugador está en modo manual o automático.
     *
     * @return True si el jugador está en modo manual.
     */
    public boolean isManual() {
        return manual;
    }

    /**
     * Devuelve las coordenadas de la cabeza que resultarían si la serpiente se
     * moviera en la dirección actual.
     *
     * @return Coordenadas de una hipotética nueva cabeza.
     */
    public Coordenadas simularDireccion() {
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
                return null; //Caso imposible
            }
        }
    }
}

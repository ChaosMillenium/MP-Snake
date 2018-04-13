/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author millenium
 */
public class ModeloJuego extends Observable{
    private int columnas=60, filas=60;
    private HashMap<Integer,Jugador> jugadores;
    private final int VELOCIDAD=65;
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

/**
 *
 * @author i.chicano.2016
 */
public class ServidorMain {

    public static void main(String[] args) {
        ModeloJuego modelo = new ModeloJuego();
        ControladorServidor controlador = new ControladorServidor(modelo);

        modelo.addObserver(controlador);
        int[] filasColumnas = PeticionFilasColumnas.pedirFilasColumnas();
        modelo.setFilasColumnas(filasColumnas[0], filasColumnas[1]);
        controlador.iniciarServer();
    }
}

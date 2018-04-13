/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import Utilidades.*;

/**
 *
 * @author millenium
 */
public class ThreadServer implements Runnable {

    private ControladorServidor controlador;
    private Socket socket;
    private int socketID;
    private static Map<Integer, Socket> conexionesActivas = new HashMap<>();

    public ThreadServer(ControladorServidor controlador) {
        this.controlador = controlador;
    }

    private ThreadServer(ControladorServidor controlador, Socket socket, int id) {
        this.controlador = controlador;
        this.socket = socket;
        this.socketID = id;
    }

    public void startServer() {
        try (ServerSocket ssck = new ServerSocket(8000)) {
            System.out.println("Escuchando...");
            while (true) {
                Socket sck = ssck.accept();
                System.out.println("Conexión entrante");
                int key = this.controlador.añadirJugador();
                ThreadServer.conexionesActivas.put(key, sck);
                new Thread(new ThreadServer(this.controlador, sck, key)).start();
            }
        } catch (IOException e) {
            System.err.println("Error de E/S");
        }
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.idc(this.socketID));
            out.println(ConstructorMensajes.tab(this.controlador.getFilas(),this.controlador.getColumnas()));
            out.println(ConstructorMensajes.tsp(this.controlador.getTamañoBase()));
            int[] coordenadasIniciales = this.controlador.coordenadasIniciales();
            out.println(ConstructorMensajes.coi(coordenadasIniciales));
            while (true) {
                //Direccion, Fin
                
            }
        } catch (IOException e) {
            System.out.println("Error de E/S");
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.out.println("Error de E/S");
            }
        }
    }
}

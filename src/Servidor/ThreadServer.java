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

    public ThreadServer(ControladorServidor controlador) { //Constructor inicial
        this.controlador = controlador;
    }

    private ThreadServer(ControladorServidor controlador, Socket socket, int id) { //Constructor interno para generar un nuevo hilo por jugador, con su socket e identificador
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
                int key = this.controlador.siguienteKey();
                ThreadServer.conexionesActivas.put(key, sck);
                new Thread(new ThreadServer(this.controlador, sck, key)).start();
                this.controlador.añadirJugador();
            }
        } catch (IOException e) {
            System.err.println("Error de E/S");
        }
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out.println(ConstructorMensajes.tab(this.controlador.getFilas(), this.controlador.getColumnas())); //Manda tablero
            while (true) {
                String input = in.readLine();
                if (input != null) {
                    System.out.println(input); //Propósito de pruebas
                    String[] parseado = input.split(";");
                    if (ConstructorMensajes.isDir(parseado[0])) {
                        this.controlador.cambiarDireccion(Integer.parseInt(parseado[1]), parseado[2]);
                    }
                    if (ConstructorMensajes.isFin(parseado[0])) {
                        if (Integer.parseInt(parseado[1]) == (this.socketID)) {
                            ThreadServer.conexionesActivas.remove(this.socketID);
                            this.socket.close();
                            this.controlador.eliminarJugador(this.socketID);
                            return;
                        } else {
                            out.println(ConstructorMensajes.err("Error al transmitir ID propia"));
                        }
                    }

                } else {
                    throw new NullPointerException();
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S");
        } catch (NullPointerException e) {
            System.err.println("Error: El cliente se ha desconectado del servidor.");
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void nuevoJugador(int id, int[] coordenadas) {
        try {
            String mensaje = ConstructorMensajes.coi(coordenadas, id);
            for (Socket jugador : ThreadServer.conexionesActivas.values()) {
                PrintWriter out = new PrintWriter(jugador.getOutputStream(), true);
                out.println(mensaje);
            }
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }
}

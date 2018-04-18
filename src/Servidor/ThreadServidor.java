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
public class ThreadServidor implements Runnable {

    private ControladorServidor controlador;
    private Socket socket;
    private int socketID;
    private static Map<Integer, Socket> conexionesActivas = new HashMap<>();

    public ThreadServidor(ControladorServidor controlador) { //Constructor inicial
        this.controlador = controlador;
    }

    private ThreadServidor(ControladorServidor controlador, Socket socket, int id) { //Constructor interno para generar un nuevo hilo por jugador, con su socket e identificador
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
                ThreadServidor.conexionesActivas.put(key, sck);
                new Thread(new ThreadServidor(this.controlador, sck, key)).start();
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
            out.println(ConstructorMensajes.idc(this.socketID)); //Manda ID
            out.println(ConstructorMensajes.tab(this.controlador.getFilas(), this.controlador.getColumnas())); //Manda tablero
            while (true) {
                String input = in.readLine();
                if (input != null) {
                    System.out.println(input); //Propósito de pruebas
                    String[] parseado = input.split(";");
                    if (ConstructorMensajes.isDir(parseado[0])) {
                        this.controlador.cambiarDireccion(this.socketID, parseado[1]);
                    } else if (ConstructorMensajes.isFin(parseado[0])) {
                        if (!(Integer.parseInt(parseado[1]) == this.socketID)) {
                            System.err.println("Error: El identificador no coincide (FIN), procediendo a desconectarle");
                        }
                        ThreadServidor.conexionesActivas.remove(this.socketID);
                        this.controlador.eliminarJugador(this.socketID);
                        this.eliminarJugador(this.socketID);
                        this.socket.close();
                        return;

                    }

                } else {
                    throw new NullPointerException();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (IOException)");
        } catch (NullPointerException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (NullPointerException: Mensaje no reconocido/Desconectado sin aviso)");
        } finally {
            try {
                ThreadServidor.conexionesActivas.remove(this.socketID);
                this.controlador.eliminarJugador(this.socketID);
                this.eliminarJugador(this.socketID);
                this.socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void enviarMensaje(String mensaje) {
        try {
            for (Socket jugador : ThreadServidor.conexionesActivas.values()) {
                PrintWriter out = new PrintWriter(jugador.getOutputStream(), true);
                out.println(mensaje);
            }
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public void nuevoJugador(int id, int[] coordenadas) {
        enviarMensaje(ConstructorMensajes.coi(coordenadas, id));
    }

    public void eliminarJugador(int id) {
        enviarMensaje(ConstructorMensajes.elj(id));
    }

    public void moverJugador(int id, int[] cabeza, int[] cola) {
        enviarMensaje(ConstructorMensajes.mov(id, cabeza[0], cabeza[1], cola[0], cola[1]));
    }

    public void colision(int id1) {
        try {
            String col = ConstructorMensajes.err("Colisión con borde");
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            out1.println(col);
            eliminarJugador(id1);
            ThreadServidor.conexionesActivas.get(id1).close();
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public void colision(int id1, int id2) {
        try {
            String col1 = ConstructorMensajes.err("Colisión con " + id2);
            String col2 = ConstructorMensajes.err("Colisión con " + id1);
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(ThreadServidor.conexionesActivas.get(id2).getOutputStream(), true);
            out1.println(col1);
            out2.println(col2);
            eliminarJugador(id1);
            eliminarJugador(id2);
            ThreadServidor.conexionesActivas.get(id1).close();
            ThreadServidor.conexionesActivas.get(id2).close();
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public void darPuntos(int id, int puntos) {
        enviarMensaje(ConstructorMensajes.pts(id, puntos));
    }

    public void nuevoTesoro(int x, int y) {
        enviarMensaje(ConstructorMensajes.tsr(x, y));
    }

}

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
import java.util.ConcurrentModificationException;

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
                int key = this.controlador.siguienteKey(); //La key del mapa es la id del jugador
                ThreadServidor.conexionesActivas.put(key, sck);
                new Thread(new ThreadServidor(this.controlador, sck, key)).start();
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
            this.controlador.añadirJugador();
            for (int id : ThreadServidor.conexionesActivas.keySet()) {
                if (id != this.socketID) {
                    int[] coordenadas = this.controlador.getCoordenadas(id);
                    this.nuevoJugador(id, coordenadas);
                }
            }
            boolean acceder = true;
            while (acceder) {
                try {
                    for (Coordenadas coord : this.controlador.getTesoros()) {
                        this.nuevoTesoro(coord.getX(), coord.getY());
                    }
                    acceder = false;
                } catch (ConcurrentModificationException e) {
                    //Reintenta hasta que se pueda leer
                }
            }
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
                        this.eliminarJugador(this.socketID);
                        this.controlador.eliminarJugador(this.socketID);
                        return;
                    }

                } else {
                    throw new NullPointerException();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (IOException: Posible colisión)");
        } catch (NullPointerException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (NullPointerException: Mensaje no reconocido/Desconectado sin aviso)");
        } finally {
            this.eliminarJugador(this.socketID);
            this.controlador.eliminarJugador(this.socketID);
        }
    }

    private synchronized void enviarMensaje(String mensaje) {
        try {
            for (Socket jugador : ThreadServidor.conexionesActivas.values()) {
                PrintWriter out = new PrintWriter(jugador.getOutputStream(), true);
                out.println(mensaje);
            }
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public synchronized void nuevoJugador(int id, int[] coordenadas) {
        enviarMensaje(ConstructorMensajes.coi(coordenadas, id));
    }

    public synchronized void eliminarJugador(int id) {
        try {
            ThreadServidor.conexionesActivas.get(id).close();
        } catch (IOException ex) {
            System.err.println("Error de E/S");
        }
        enviarMensaje(ConstructorMensajes.elj(id));
    }

    public void moverJugador(int id, int[] cabeza, int[] cola) {
        enviarMensaje(ConstructorMensajes.mov(id, cabeza[0], cabeza[1], cola[0], cola[1]));
    }

    public synchronized void colision(int id1) {
        try {
            String col = ConstructorMensajes.err("Colisión con borde");
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            out1.println(col);
            eliminarJugador(id1);
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public synchronized void colision(int id1, int id2) {
        try {
            String col1 = ConstructorMensajes.err("Colisión con " + id2);
            String col2 = ConstructorMensajes.err("Colisión con " + id1);
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(ThreadServidor.conexionesActivas.get(id2).getOutputStream(), true);
            out1.println(col1);
            out2.println(col2);
            eliminarJugador(id1);
            eliminarJugador(id2);
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

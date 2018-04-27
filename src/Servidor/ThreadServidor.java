/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import Utilidades.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author millenium
 */
public class ThreadServidor implements Runnable {

    private ControladorServidor controlador;
    private Socket socket;
    private int socketID;
    private static Map<Integer, Socket> conexionesActivas = new ConcurrentHashMap<>();

    public ThreadServidor(ControladorServidor controlador) { //Constructor inicial
        this.controlador = controlador;
    }

    private ThreadServidor(ControladorServidor controlador, Socket socket, int id) { //Constructor interno para generar un nuevo hilo por jugador, con su socket e identificador
        this.controlador = controlador;
        this.socket = socket;
        this.socketID = id;
    }

    public void startServer() {
        try (ServerSocket serverSck = new ServerSocket(8000)) { //Inicia el servidor
            System.out.println("Escuchando...");
            while (true) {
                Socket sck = serverSck.accept(); //Acepta cliente
                System.out.println("Conexión entrante");
                int key = this.controlador.siguienteKey();
                new Thread(new ThreadServidor(this.controlador, sck, key)).start(); //Inicia un hilo por cliente
            }
        } catch (IOException e) {
            System.err.println("Error de E/S");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out.println(ConstructorMensajes.idc(this.socketID)); //Manda ID
                out.println(ConstructorMensajes.tab(this.controlador.getFilas(), this.controlador.getColumnas())); //Manda tablero
                this.controlador.añadirJugador(); //Añade jugador al modelo de juego
                ThreadServidor.conexionesActivas.put(this.socketID, this.socket); //Añade el nuevo cliente a la lista de conexiones activas
                //Envia todas las coordenadas de los jugadores ya existentes al nuevo jugador (incluido el suyo)
                for (int id : ThreadServidor.conexionesActivas.keySet()) {
                    int[] coordenadas = this.controlador.getCoordenadas(id);
                    out.println(ConstructorMensajes.coi(coordenadas, id));
                }
                //Se envían las coordenadas de todos los tesoros
                for (Coordenadas coord : this.controlador.getTesoros()) {
                    out.println(ConstructorMensajes.tsr(coord.getX(), coord.getY()));
                }
                while (true) {
                    String input = in.readLine(); //Lee un mensaje enviado desde el cliente
                    if (input != null) {
                        System.out.println(input); //Propósito de pruebas
                        String[] parseado = input.split(";"); //Parsea los mensajes
                        if (ConstructorMensajes.isDir(parseado[0])) { //Si la cabecera es un mensaje de dirección
                            this.controlador.cambiarDireccion(this.socketID, parseado[1]); //Cambia la dirección del jugador en el modelo
                        } else if (ConstructorMensajes.isFin(parseado[0])) { //Si se envía desde cliente un fin de conexión
                            if (!(Integer.parseInt(parseado[1]) == this.socketID)) {
                                System.err.println("Error: El identificador no coincide (FIN), procediendo a desconectarle");
                            }
                            this.eliminarJugador(this.socketID); //Avisa al resto de la eliminación de un jugador y cierra la conexión
                            this.controlador.eliminarJugador(this.socketID); //Elimina al jugador del modelo
                            return;
                        }

                    } else {
                        throw new NullPointerException(); //Lanza una excepción si el mensaje no sigue la codificación adecuada
                    }
                }
            } catch (IOException e) {
                System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (IOException: Finalización incorrecta por parte del cliente.)");
                break;
            } catch (NullPointerException e) {
                System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (NullPointerException: Mensaje no reconocido/Desconectado sin aviso)");
            } finally {
                try {
                    //this.eliminarJugador(this.socketID);
                    //this.controlador.eliminarJugador(this.socketID);
                    break;
                } catch (NullPointerException ex) {
                    break;
                    //Salta cuando ya se ha eliminado, por lo que no hay que repetirlo
                }
            }
        }
    }

    private synchronized void enviarMensaje(String mensaje) { //Envía un mensaje a todos los jugadores
        try {
            for (Socket jugador : ThreadServidor.conexionesActivas.values()) {
                PrintWriter out = new PrintWriter(jugador.getOutputStream(), true);
                out.println(mensaje);
            }
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public synchronized void nuevoJugador(int id, int[] coordenadas) { //Envía a todos los jugadores que se ha añadido un nuevo jugador
        enviarMensaje(ConstructorMensajes.coi(coordenadas, id));
    }

    public synchronized void eliminarJugador(int id) { //Envía a todos los jugadores que se ha eliminado un jugador
        int[] coordenadas = this.controlador.getCoordenadasAnt(id);
        enviarMensaje(ConstructorMensajes.elj(id, coordenadas));
        try {
            PrintWriter out = new PrintWriter(
                    ThreadServidor.conexionesActivas.get(id).getOutputStream(), true);
            out.println(ConstructorMensajes.fin(id));
            Thread.sleep(500);
        } catch (IOException ex) {
            System.err.println("Error de E/S");
        } catch (InterruptedException ex) {
            System.err.println("Error de interrupcion");
        }
        System.out.println("El cliente " + id + " ha salido.");
        
    }

    public void moverJugador(int id, int[] cabeza, int[] cola) { //Envía a todos los jugadores que se ha movido un nuevo jugador
        String mensaje = ConstructorMensajes.mov(id, cabeza[0], cabeza[1], cola[0], cola[1]);
        enviarMensaje(mensaje);
        System.out.println(mensaje);
    }

    public synchronized void colision(int id1) { //Envía a todos los jugadores que se ha ocurrido una colisión con un borde
        try {
            String col = ConstructorMensajes.err("Colisión con borde");
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            out1.println(col);
            eliminarJugador(id1);
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public synchronized void colision(int id1, int id2) { //Envía a todos los jugadores que ha ocurrido una colisión entre dos jugadores, o uno consigo mismo
        try {
            if (id1 == id2) {
                String col = ConstructorMensajes.err("Colisión de " + id1 + " consigo mismo");
                PrintWriter out = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
                out.println(col);
                eliminarJugador(id1);
            } else {
                String col1 = ConstructorMensajes.err("Colisión con " + id2);
                String col2 = ConstructorMensajes.err("Colisión con " + id1);
                PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
                PrintWriter out2 = new PrintWriter(ThreadServidor.conexionesActivas.get(id2).getOutputStream(), true);
                out1.println(col1);
                out2.println(col2);
                eliminarJugador(id1);
                eliminarJugador(id2);
            }
        } catch (IOException e) {
            //TODO: Controlar excepcion
        }
    }

    public void darPuntos(int id, int puntos) { //Envía a todos los jugadores que se ha añadido puntos a un jugador
        enviarMensaje(ConstructorMensajes.pts(id, puntos));
    }

    public void nuevoTesoro(int x, int y) { //Envía a todos los jugadores que se ha añadido un nuevo tesoro
        enviarMensaje(ConstructorMensajes.tsr(x, y));
    }

}

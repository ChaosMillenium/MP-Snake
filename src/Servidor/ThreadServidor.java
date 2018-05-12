package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import Utilidades.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor del juego que envía y recibe mensajes que manda desde y hacia el
 * controlador del servidor (Es parte del controlador).
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ThreadServidor implements Runnable {

    private ControladorServidor controlador;
    private Socket socket;
    private int socketID;
    private static Map<Integer, Socket> conexionesActivas = new ConcurrentHashMap<>();
    private int jugadores;

    /**
     * Constructor inicial para crear el servidor.
     *
     * @param controlador Controlador del servidor.
     */
    public ThreadServidor(ControladorServidor controlador) {
        this.controlador = controlador;
    }

    /**
     * Constructor interno para generar un nuevo hilo por jugador, con su socket
     * e identificador.
     *
     * @param controlador Controlador del servidor.
     * @param socket Socket de conexión con el cliente.
     * @param id ID del jugador.
     */
    private ThreadServidor(ControladorServidor controlador, Socket socket, int id) {
        this.controlador = controlador;
        this.socket = socket;
        this.socketID = id;
    }

    /**
     * Inicia el servidor, que crea un hilo por nuevo cliente.
     */
    public void startServer() {
        try (ServerSocket serverSck = new ServerSocket(8000)) { //Inicia el servidor
            System.out.println("Escuchando...");
            while (true) {
                Socket sck = serverSck.accept(); //Acepta cliente
                System.out.println("Conexión entrante");
                int key = this.controlador.siguienteKey(); //Busca la siguiente clave libre.
                this.jugadores++; //Añade un jugador
                new Thread(new ThreadServidor(this.controlador, sck, key), "Cliente " + key).start(); //Inicia un hilo por cliente
            }
        } catch (IOException e) {
            System.err.println("Error de E/S");
        }
    }

    /**
     * Hilo del cliente, que enviará los mensajes iniciales para que inicie el
     * juego y escuchará las peticiones del cliente.
     */
    @Override
    public void run() {
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
                    //System.out.println(input); //Propósito de pruebas
                    String[] parseado = input.split(";"); //Parsea los mensajes
                    if (ConstructorMensajes.isDir(parseado[0])) { //Si la cabecera es un mensaje de dirección
                        this.controlador.cambiarDireccion(this.socketID, parseado[1]); //Cambia la dirección del jugador en el modelo
                    } else if (ConstructorMensajes.isFin(parseado[0])) { //Si se envía desde cliente un fin de conexión
                        if (!(Integer.parseInt(parseado[1]) == this.socketID)) {
                            System.err.println("Error: El identificador no coincide (FIN), procediendo a desconectarle");
                        }
                        break;
                    } else if (ConstructorMensajes.isMan(parseado[0])) { //Pone el modo manual o automático
                        this.controlador.setManual(this.socketID, parseado[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (IOException: Finalización incorrecta por parte del cliente.)");
        } catch (NullPointerException e) {
            System.err.println("Error: El cliente " + this.socketID + " se ha desconectado del servidor. (NullPointerException: Mensaje no reconocido/Desconectado sin aviso)");
        } finally {
            try {
                //Se cierra la conexión y se elimina del juego y del mapa de conexiones.
                this.controlador.finalizarJugador(this.socketID);
                Socket sck = ThreadServidor.conexionesActivas.get(this.socketID);
                ThreadServidor.conexionesActivas.remove(this.socketID);
                this.jugadores--;
                sck.close();
                if (this.jugadores <= 0) {
                    ThreadServidor.conexionesActivas.clear();
                }
            } catch (NullPointerException | IOException ex) {
                //Salta cuando ya se ha eliminado, por lo que no hay que repetirlo
            }
        }
    }

    /**
     * Envía un mensaje a todos los jugadores
     *
     * @param mensaje
     */
    private synchronized void enviarMensaje(String mensaje) {

        for (Socket jugador : ThreadServidor.conexionesActivas.values()) {
            try {
                PrintWriter out = new PrintWriter(jugador.getOutputStream(), true);
                out.println(mensaje);
            } catch (IOException e) {
                System.err.println("Error de E/S al enviar mensaje a socket");
            }

        }
    }

    /**
     * Envía a todos los jugadores que se ha añadido un nuevo jugador
     *
     * @param id ID del jugador.
     * @param coordenadas Array de enteros con las coordenadas (Pares X, Impares
     * Y).
     */
    public synchronized void nuevoJugador(int id, int[] coordenadas) {
        enviarMensaje(ConstructorMensajes.coi(coordenadas, id));
    }

    /**
     * Envía a todos los jugadores que se ha eliminado un jugador.
     *
     * @param id ID del jugador.
     */
    public synchronized void eliminarJugador(int id) {
        //Envia ELJ a todos los jugadores.
        enviarMensaje(ConstructorMensajes.elj(id));
        try {
            PrintWriter out = new PrintWriter(
                    ThreadServidor.conexionesActivas.get(id).getOutputStream(), true);
            //Manda un FIN al cliente.
            out.println(ConstructorMensajes.fin(id));
            Thread.sleep(100); //Evita errores de concurrencia.
            Socket sck = ThreadServidor.conexionesActivas.get(id); //Busca el socket y cierra la conexión.
            ThreadServidor.conexionesActivas.remove(id);
            sck.close();
        } catch (IOException ex) {
            System.err.println("Error de E/S al enviar mensaje a socket");
        } catch (InterruptedException ex) {
            System.err.println("Error de interrupcion de hilo");
        }
        System.out.println("El cliente " + id + " ha salido.");

    }

    /**
     * Envía a todos los jugadores que se ha movido un nuevo jugador.
     *
     * @param id ID del jugador.
     * @param cabeza Array con las coordenadas de la cabeza nueva (Pares X,
     * Impares Y).
     * @param cola Array con las coordenadas de la cola antigua (Pares X,
     * Impares Y).
     */
    public void moverJugador(int id, int[] cabeza, int[] cola) {
        String mensaje = ConstructorMensajes.mov(id, cabeza[0], cabeza[1], cola[0], cola[1]);
        enviarMensaje(mensaje);
    }

    /**
     * Envía a todos los jugadores que se ha ocurrido una colisión con un borde.
     *
     * @param id1 ID del jugador.
     */
    public synchronized void colision(int id1) {
        try {
            String col = ConstructorMensajes.err("Colisión con borde");
            PrintWriter out1 = new PrintWriter(ThreadServidor.conexionesActivas.get(id1).getOutputStream(), true);
            out1.println(col);
            eliminarJugador(id1);
        } catch (IOException e) {
        }
    }

    /**
     * Envía a todos los jugadores que ha ocurrido una colisión entre dos
     * jugadores, o uno consigo mismo.
     *
     * @param id1 ID del jugador 1 que ha colisionado.
     * @param id2 ID del jugador 2 que ha colisionado (puede ser el mismo).
     */
    public synchronized void colision(int id1, int id2) {
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
        }
    }

    /**
     * Envía a todos los jugadores que se ha añadido puntos a un jugador
     *
     * @param id ID del jugador.
     * @param puntos Puntos a otorgar.
     */
    public void darPuntos(int id, int puntos) {
        enviarMensaje(ConstructorMensajes.pts(id, puntos));
    }

    /**
     * Envía a todos los jugadores que se ha añadido un nuevo tesoro.
     *
     * @param x Coordenada X del tesoro.
     * @param y Coordenada Y del tesoro.
     */
    public void nuevoTesoro(int x, int y) {
        enviarMensaje(ConstructorMensajes.tsr(x, y));
    }
}

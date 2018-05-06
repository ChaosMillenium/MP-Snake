package Cliente;

import Utilidades.ConstructorMensajes;
import Utilidades.Direccion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Escucha las peticiones recibidas por el Servidor
 * 
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ThreadEscucha extends Thread {
    private ControladorCliente controlador; //Controlador al cual envia los datos
    private Socket socket; //Socket al que nos conectamos
    private boolean manual; //Modo de juego, manual o automatico
    
    /**
     * Metodo de creacion del hilo
     * 
     * @param socket Socket que se utilizara para establecer la conexion
     * @param controlador Controlador que recibira los datos
     * @param esManual Modo de juego del Cliente, automatico o manual
     */
    public ThreadEscucha(Socket socket, ControladorCliente controlador, boolean esManual) {
        this.socket = socket;
        this.controlador = controlador;
        this.manual = esManual;
    }
    
    /**
     * Establecera conexion con el servidor y se dispondra a escuchar los mensajes
     * provenientes de este
     */
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.man(this.manual));
            while (true) {
                //Bucle que escucha constantemente mensajes del servidor
                String mensaje = input.readLine();
                System.out.println(mensaje); //pruebas
                this.controlador.selectorMensaje(mensaje);
            }
        } catch (IOException ex) {
            //Cierra conexion si hay algun problema
            this.cerrarConexion();
        }
    }
    
    /**
     * Envia al Servidor la direccion que el cliente ha pulsado
     * 
     * @param dir Direccion a la que desea diriguirse el jugador
     */
    public void enviarDireccion(Direccion dir) {
        try {
            //Envia los cambios de direccion al modelo para que efectue los calculos apropiados
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            String direccion = dir.toString();
            out.println(ConstructorMensajes.dir(direccion)); //Construye un mensaje tipo DIR y lo envia
        } catch (IOException ex) {
            System.err.println("Error de E/S");
        }
    }
    
    /**
     * Envia al Servidor el deseo del cliente de finalizar la conexion
     * 
     * @param id ID del Cliente a eliminar
     */
    public void enviarFin(int id) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.fin(id)); //Construye un mensaje tipo FIN y lo envia
        } catch (IOException ex) {
            System.err.println("Error de E/S");
        }
    }
    
    /**
     * Cierra la conexion con el servidor y seguidamente cierra el programa
     */
    public void cerrarConexion() {
        try {
            JOptionPane.showMessageDialog(null, "El servidor ha cerrado la conexión.");
            this.socket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Error de E/S");
        }
    }
}

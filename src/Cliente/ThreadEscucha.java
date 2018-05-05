package Cliente;

import Utilidades.ConstructorMensajes;
import Utilidades.Direccion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ThreadEscucha extends Thread {
    private ControladorCliente controlador;
    private Socket socket;
    private boolean manual;

    public ThreadEscucha(Socket socket, ControladorCliente controlador, boolean esManual) {
        this.socket = socket;
        this.controlador = controlador;
        this.manual = esManual;
    }

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

    public void enviarDireccion(Direccion dir) {
        try {
            //Envia los cambios de direccion al modelo para que efectue los calculos apropiados
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            String direccion = dir.toString();
            out.println(ConstructorMensajes.dir(direccion));
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }

    public void enviarFin(int id) {
        try {
            //Envia un Fin al servidor 
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.fin(id));
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }

    public void cerrarConexion() {
        try {
            //Cierra la conexion con el servidor y seguidamente cierra el programa
            JOptionPane.showMessageDialog(null, "El servidor ha cerrado la conexión.");
            this.socket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }
}

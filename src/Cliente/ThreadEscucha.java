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
                String mensaje = input.readLine();
                System.out.println(mensaje); //pruebas
                this.controlador.selectorMensaje(mensaje);
            }
        } catch (IOException ex) {
            this.cerrarConexion();
        }
    }

    public void enviarDireccion(Direccion dir) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            String direccion = dir.toString();
            //System.out.println(direccion);
            out.println(ConstructorMensajes.dir(direccion));
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }

    public void enviarFin(int id) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.fin(id));
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }

    public void cerrarConexion() {
        try {
            JOptionPane.showMessageDialog(null, "El servidor ha cerrado la conexión.");
            this.socket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción
        }
    }
}

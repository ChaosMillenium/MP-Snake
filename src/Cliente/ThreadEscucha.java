/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.ConstructorMensajes;
import Utilidades.Direccion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author i.chicano.2016
 */
public class ThreadEscucha extends Thread {

    private ControladorCliente controlador;
    private Socket socket;

    public ThreadEscucha(Socket socket, ControladorCliente controlador) {
        this.socket = socket;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                String mensaje = input.readLine();
                this.controlador.selectorMensajes(mensaje);

            }
        } catch (IOException ex) {
            System.err.println("Error de E/S"); //TODO: Controlar excepción

        }
    }

    public void enviarDireccion(Direccion dir) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConstructorMensajes.dir(dir.toString()));
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
}

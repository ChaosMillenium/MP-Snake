/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author millenium
 */
public class ThreadServer implements Runnable{
    private ControladorServidor controlador;

    public ThreadServer(ControladorServidor controlador) {
        this.controlador = controlador;
    }
    
    public void startServer(){
        try(ServerSocket ssck = new ServerSocket(8000)){
            System.out.println("Escuchando...");
            while(true){
                Socket sck = ssck.accept();
                System.out.println("Conexión entrante");
                new Thread(new ThreadServer(this.controlador)).start();
            }
        }
        catch (IOException e){
            System.err.println("Error de E/S");
        }
    }

    @Override
    public void run() {
        //TODO: 
    }
}

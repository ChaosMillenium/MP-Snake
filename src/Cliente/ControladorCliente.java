/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.Direccion;
import com.sun.glass.events.KeyEvent;
import java.io.IOException;
import java.net.Socket;

import java.net.UnknownHostException;

import java.util.Observable;

import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */

public class ControladorCliente extends Observable{
    
    private Serpiente ser;
    private ThreadEscucha listener;

    public void establecerConexion() {
        boolean reintentar = true;
        while (reintentar) {
            try {
                String dirIP = JOptionPane.showInputDialog("Introduce la IP del servidor");
                String puerto = JOptionPane.showInputDialog("Introduce el puerto");
                Socket socket = new Socket(dirIP, Integer.parseInt(puerto));
                this.listener = new ThreadEscucha(socket,this);
                this.listener.start();
                reintentar = false;
            } catch (UnknownHostException e) {
                int yesNo = JOptionPane.showConfirmDialog(null, "Dirección no encontrada. ¿Reintentar?", "Conexión no encontrada", JOptionPane.YES_NO_OPTION);
                if (yesNo == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            } catch (IOException ex) {
                int yesNo = JOptionPane.showConfirmDialog(null, "Error de E/S. ¿Reintentar?", "Error de conexión", JOptionPane.YES_NO_OPTION);
                if (yesNo == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }
    }

    public void setDirAct(int key) {
        //CONTROL DE DIRECCION
        switch (key) {
            case KeyEvent.VK_UP: {
                if (ser.getDir() != Direccion.ABAJO) {
                    ser.setDir(Direccion.ARRIBA);
                    this.listener.enviarDireccion(Direccion.ARRIBA);
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (ser.getDir() != Direccion.ARRIBA) {
                    ser.setDir(Direccion.ABAJO);
                    this.listener.enviarDireccion(Direccion.ABAJO);
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                if (ser.getDir() != Direccion.DER) {
                    ser.setDir(Direccion.IZQ);
                    this.listener.enviarDireccion(Direccion.IZQ);
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if (ser.getDir() != Direccion.IZQ) {
                    ser.setDir(Direccion.DER);
                    this.listener.enviarDireccion(Direccion.DER);
                }
            }
        }
    }
    
    public void finalizar(){
    }
}

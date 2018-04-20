/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.*;
import com.sun.glass.events.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class ControladorCliente extends Observable {

    private ArrayList<Serpiente> serpientes;
    private ArrayList<Coordenadas> tesoros;
    private Serpiente ser;
    private ThreadEscucha listener;

    public void establecerConexion() {
        boolean reintentar = true;
        while (reintentar) {
            try {
                String dirIP = JOptionPane.showInputDialog("Introduce la IP del servidor");
                String puerto = JOptionPane.showInputDialog("Introduce el puerto del servidor");
                Socket socket = new Socket(dirIP, Integer.parseInt(puerto));
                this.listener = new ThreadEscucha(socket, this);
                this.listener.start();
                reintentar = false;
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
                break;
            }
        }
    }

    public void selectorMensaje(String msg){
        String[] msgSplit = msg.split(";");
        setChanged();
        switch(msgSplit[0]){
            case "TAB":{
                VistaCliente v = new VistaCliente(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]));
                break;
            }
            case "TSR":{
                Coordenadas tes = new Coordenadas(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]));
                this.tesoros.add(tes);
                notifyObservers(tes);
                break;
            }
            case "ELJ":{
                for(Serpiente serpi : this.serpientes){
                    if(serpi.getId() == Integer.parseInt(msgSplit[1])){
                        this.serpientes.remove(serpi);
                        notifyObservers(serpi);
                        break;
                    }
                }
                break;
            }
            case "PTS":{
                for(Serpiente serpi : this.serpientes){
                    if(serpi.getId() == Integer.parseInt(msgSplit[1])){
                        serpi.setPuntos(Integer.parseInt(msgSplit[2]));
                        notifyObservers(serpi);
                        break;
                    }
                }
                break;
            }
            case "COI":{
                Serpiente s = new Serpiente(Integer.parseInt(msgSplit[1]));
                for(int i = 2;i < msgSplit.length; i+=2){
                    int x = Integer.parseInt(msgSplit[i]);
                    int y = Integer.parseInt(msgSplit[i+1]);
                    Coordenadas c = new Coordenadas(x,y);
                    if(i == 2){
                        s.setCabeza(c);
                    }else{
                        s.addCasilla(c);
                    }
                }
                notifyObservers(s);
                break;
            }
            case "MOV":{
                for(Serpiente serpi : this.serpientes){
                    if(serpi.getId() == Integer.parseInt(msgSplit[1])){
                        Coordenadas cabeza = new Coordenadas(Integer.parseInt(msgSplit[2]), Integer.parseInt(msgSplit[3]));
                        Coordenadas cola = new Coordenadas(Integer.parseInt(msgSplit[4]), Integer.parseInt(msgSplit[5]));
                        serpi.setCabeza(cabeza);
                        serpi.eliminarCola();
                        notifyObservers(serpi);
                        break;
                    }
                }
                break;
            }
            case "FIN":{
                
                break;
            }
            case "ERR":{
                //lanzar mensaje error
                break;
            }
            default :{
                //lanzar mensaje error
                break;
            }
        }
    }
}

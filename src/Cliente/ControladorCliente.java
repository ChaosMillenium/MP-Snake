/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.*;
import com.sun.glass.events.KeyEvent;
import java.awt.HeadlessException;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class ControladorCliente extends Observable{

    private Serpiente serpienteCliente;
    private ThreadEscucha listener;
    
    public ControladorCliente(){
        
    }
    
    
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
            } catch (HeadlessException | IOException | NumberFormatException ex) {
                int yesNo = JOptionPane.showConfirmDialog(null, "Error " + ex + ". ¿Reintentar?", "Error de conexión", JOptionPane.YES_NO_OPTION);
                if (yesNo == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }
    }

    public void setDirAct(int key) {
        //CONTROL DE DIRECCION
        //direccines cambiadas, pulsa W y va arriba aunque si lees el codigo no deberia hacer eso
        switch (key) {
            case KeyEvent.VK_UP: {
                if (serpienteCliente.getDir() != Direccion.ABAJO) {
                    serpienteCliente.setDir(Direccion.ARRIBA);
                    this.listener.enviarDireccion(Direccion.ARRIBA);
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (serpienteCliente.getDir() != Direccion.ARRIBA) {
                    serpienteCliente.setDir(Direccion.ABAJO);
                    this.listener.enviarDireccion(Direccion.ABAJO);
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                if (serpienteCliente.getDir() != Direccion.DER) {
                    serpienteCliente.setDir(Direccion.IZQ);
                    this.listener.enviarDireccion(Direccion.IZQ);
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if (serpienteCliente.getDir() != Direccion.IZQ) {
                    serpienteCliente.setDir(Direccion.DER);
                    this.listener.enviarDireccion(Direccion.DER);
                }
                break;
            }
        }
    }    

    public void selectorMensaje(String msg){
            if(!msg.isEmpty()){
            String[] msgSplit = msg.split(";");
            setChanged();
            switch(msgSplit[0]){
                case "IDC":{
                    notifyObservers(msg);
                    break;
                }
                case "TAB":{
                    VistaCliente v = new VistaCliente(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]), this);
                    this.addObserver(v);
                    break;
                }
                case "TSR":{
                    notifyObservers(msg);
                    break;
                }
                case "ELJ":{
                    notifyObservers(msg);
                    break;
                }
                case "PTS":{
                    notifyObservers(msg);
                    break;
                }
                case "COI":{
                    this.serpienteCliente = new Serpiente(Integer.parseInt(msgSplit[1]));
                    for(int i = 2;i < msgSplit.length; i+=2){
                        int x = Integer.parseInt(msgSplit[i]);
                        int y = Integer.parseInt(msgSplit[i+1]);
                        Coordenadas c = new Coordenadas(x,y);
                        this.serpienteCliente.addCasilla(c);
                    }
                    notifyObservers(msg);
                    break;
                }
                case "MOV":{
                    //System.out.println("controladorM");
                    notifyObservers(msg);
                    break;
                }
                case "FIN":{
                    this.listener.cerrarConexion();
                    notifyObservers(msg);
                    break;
                }
                case "ERR":{
                    this.listener.cerrarConexion();
                    notifyObservers(msg);
                    break;
                }
                default :{
                    //lanzar mensaje error
                    break;
                }
            }
        }
    }
    
}

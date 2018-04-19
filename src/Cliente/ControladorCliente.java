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
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class ControladorCliente extends Observable{
    
    private ArrayList<Serpiente> serpientes;
    private ArrayList<Coordenadas> tesoros;
        
    public ControladorCliente(){
        
    }
    
    public void establecerConexion(){
        try{
        String dirIP= JOptionPane.showInputDialog("Introduce la IP del servidor\n");
        int port= Integer.parseInt(JOptionPane.showInputDialog("Introduce el puerto del servidor\n"));
        Socket s = new Socket(dirIP, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        } catch(IOException ex){
            System.out.println(ex.getCause());
        }        
    }
    
    public void setDirAct(int key){
        //CONTROL DE DIRECCION
        switch(key){
            case KeyEvent.VK_UP: {
                if(this.serpiente.g != Direccion.ABAJO){
                    this.dirAct = Direccion.ARRIBA;
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if(this.dirAct != Direccion.ARRIBA){
                    this.dirAct = Direccion.ABAJO;
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                if(this.dirAct != Direccion.DER){
                    this.dirAct = Direccion.IZQ;
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if(this.dirAct != Direccion.IZQ){
                    this.dirAct = Direccion.DER;
                }
            }    
        }   
    }
    
    public void selectorMensaje(String msg){
        String[] msgSplit = msg.split(";");
        setChanged();
        
        /*if(isDir(type)){ 
        
        }else if(isFin(type)){
            
        
        }else if(isCoi(type)){
        }else if(isMov(type)){
        }else if(isErr(type)){
        }*/
        
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
                        break;
                    }
                }
                break;
            }
            case "PTS":{
                for(Serpiente serpi : this.serpientes){
                    if(serpi.getId() == Integer.parseInt(msgSplit[1])){
                        serpi.setPuntos(Integer.parseInt(msgSplit[2]));
                    }
                break;
            }
            case "COI":{
                break;
            }
            case "MOV":{
                break;
            }
            case "ERR":{
                break;
            }
        }
    }
}

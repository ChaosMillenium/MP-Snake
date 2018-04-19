/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utilidades.Direccion;
import com.sun.glass.events.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class ControladorCliente {
    
    private Direccion dirAct;
    private Serpiente ser;
    
    public ControladorCliente(){
        //this.ser = new Serpiente( getMessage ); Pide la server datos y fabrica serpiente
        this.dirAct = Direccion.ARRIBA; //cogerlo de la serpiente 
    }
    
    
    
    public void establecerConexion(){
        try{
        String dirIP= JOptionPane.showInputDialog("Introduce la IP del servidor\n" + "Servidor corriendo en puerto 9090");
        Socket s = new Socket(dirIP, 9090);
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
                if(this.dirAct != Direccion.ABAJO){
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
    
    
    
    
    
    
    
}

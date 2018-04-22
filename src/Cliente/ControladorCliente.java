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
public class ControladorCliente extends Observable {

    private Serpiente serpienteCliente;
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
                this.listener.enviarDireccion(Direccion.ARRIBA);
                break;
            }
            case KeyEvent.VK_DOWN: {
                this.listener.enviarDireccion(Direccion.ABAJO);
                break;
            }
            case KeyEvent.VK_LEFT: {
                this.listener.enviarDireccion(Direccion.IZQ);
                break;
            }
            case KeyEvent.VK_RIGHT: {
                this.listener.enviarDireccion(Direccion.DER);
                break;
            }
        }
    }

    public void selectorMensaje(String msg) {
        if (!msg.isEmpty()) {
            String[] msgSplit = msg.split(";");
            setChanged();
            switch (msgSplit[0]) {
                case "TAB": {
                    //Cuando llega el tamaño de tablero se crea la vista del tablero
                    VistaCliente v = new VistaCliente(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]), this);
                    this.addObserver(v);
                    break;
                }
                case "COI": {
                    this.serpienteCliente = new Serpiente(Integer.parseInt(msgSplit[1]));
                    for (int i = 2; i < msgSplit.length; i += 2) {
                        int x = Integer.parseInt(msgSplit[i]);
                        int y = Integer.parseInt(msgSplit[i + 1]);
                        Coordenadas c = new Coordenadas(x, y);
                        this.serpienteCliente.addCasilla(c);
                    }
                    notifyObservers(msg);
                    break;
                }
                case "MOV": {
                    //System.out.println("controladorM");
                    notifyObservers(msg);
                    break;
                }
                case "FIN": {
                    notifyObservers(msg);
                    this.listener.cerrarConexion();
                    System.exit(0);
                    break;
                }
                case "ERR": {
                    notifyObservers(msg);
                    this.listener.cerrarConexion();
                    System.exit(0);
                    break;
                }
                default: {
                    notifyObservers(msg);
                    break;
                }
            }
        }
    }

}

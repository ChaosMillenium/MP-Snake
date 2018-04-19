package Cliente;

import Utilidades.*;
import com.sun.glass.events.KeyEvent;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JOptionPane;

public class ControladorCliente extends Observable {

    private ArrayList<Serpiente> serpientes;
    private ArrayList<Coordenadas> tesoros;
    private Serpiente serpienteCliente;
    private ThreadEscucha listener;

    public ControladorCliente(){
        this.serpientes = new ArrayList<>();
        this.tesoros = new ArrayList<>();
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

    public void selectorMensaje(String msg) {
        String[] msgSplit = msg.split(";");
        setChanged();
        switch (msgSplit[0]) {
            case "IDC": {
                this.serpienteCliente = new Serpiente(Integer.parseInt(msgSplit[1]));
                break;
            }
            case "TAB": {
                VistaCliente v = new VistaCliente(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]));
                this.addObserver(v);
                break;
            }
            case "TSR": {
                Coordenadas tes = new Coordenadas(Integer.parseInt(msgSplit[1]), Integer.parseInt(msgSplit[2]));
                this.tesoros.add(tes);
                notifyObservers(tes);
                break;
            }
            case "ELJ": {
                for (Serpiente serpi : this.serpientes) {
                    if (serpi.getId() == Integer.parseInt(msgSplit[1])) {
                        this.serpientes.remove(serpi);
                        break;
                    }
                }
                break;
            }
            case "PTS": {
                for (Serpiente serpi : this.serpientes) {
                    if (serpi.getId() == Integer.parseInt(msgSplit[1])) {
                        serpi.setPuntos(Integer.parseInt(msgSplit[2]));
                    }
                    break;
                }
                /*
            case "COI":{
                break;
            }
            case "MOV":{
                break;
            }
            case "ERR":{
                break;
            }
                 */
            }
        }

    }

}

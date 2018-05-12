package Cliente;

import Utilidades.*;
import com.sun.glass.events.KeyEvent;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import javax.swing.JOptionPane;
/**
 * Controlador del Cliente (MVC), el cual recibira todos los datos del Servidor 
 * y se los pasara a las vistas.
 * 
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ControladorCliente extends Observable {
    private int id;                     //Id de este cliente
    private ThreadEscucha listener;     //Thread de este cliente
    
    /**
     * Conecta al Cliente y al Servidor y activa un hilo que escucha 
     * constantemente peticiones del Servidor.
     */
    public void establecerConexion(){
        boolean reintentar = true;
        while (reintentar) { //Se intenta conectar hasta que lo consigue
            try {
                String[] datos = PeticionIPPuerto.pedirIPPuerto(); //Recoge los datos necesarios para establecer la conexion
                Socket socket = new Socket(datos[0], Integer.parseInt(datos[1]));
                this.listener = new ThreadEscucha(socket, this, Boolean.parseBoolean(datos[2]));
                this.listener.start(); //Inicia el hilo de escucha
                reintentar = false; //Deja de intentar conectarse una vez que se ha conectado
            } catch (HeadlessException | IOException | NumberFormatException ex){
                //Si surge algun problema lanza excepcion y pregunta si se quiere intentar otra vez
                int yesNo = JOptionPane.showConfirmDialog(null, "Error " + ex + ". ¿Reintentar?", "Error de conexión", JOptionPane.YES_NO_OPTION);
                if (yesNo == JOptionPane.NO_OPTION){
                    System.exit(0); //Si dice que no cierra el programa
                }
            }
        }
    }
    
    /**
     * Envia al servidor un Fin, seguidamente cierra conexion
     */
    public void cerrarConexion() {
        this.listener.enviarFin(this.id);
        this.listener.cerrarConexion();
        System.exit(0);
    }
    
    /**
     * Envia al Servidor la direccion que el jugador ha pulsado
     * 
     * @param key Tecla pulsada por el Cliente
     */
    public void setDirAct(int key) {
        switch (key) {
            case KeyEvent.VK_UP: {
                //Pulsar flecha arriba
                this.listener.enviarDireccion(Direccion.ARRIBA);
                break;
            }
            case KeyEvent.VK_DOWN: {
                //Pulsa flecha abajo
                this.listener.enviarDireccion(Direccion.ABAJO);
                break;
            }
            case KeyEvent.VK_LEFT: {
                //Pulsa flecha izquierda
                this.listener.enviarDireccion(Direccion.IZQ);
                break;
            }
            case KeyEvent.VK_RIGHT: {
                //Pulsa flecha derecha
                this.listener.enviarDireccion(Direccion.DER);
                break;
            }
        }
    }

    /**
     * Determina que hacer con cada mensaje recibido por el hilo de escucha
     * 
     * @param msg Mensaje recibido del hilo
     */
    public void selectorMensaje(String msg) {
        if (msg != null) {
            String[] parseado = msg.split(";"); //divide el mensaje
            setChanged();
            switch (parseado[0]) {
                case "TAB": {
                    //Cuando llega el tamaño de tablero se crea la vista del tablero
                    VistaCliente v = new VistaCliente(Integer.parseInt(parseado[1]), Integer.parseInt(parseado[2]), this, this.id);
                    this.addObserver(v);
                    break;
                }
                case "IDC": {
                    //Se crea vista puntuaciones
                    this.id = Integer.parseInt(parseado[1]);
                    Puntuacion p = new Puntuacion(this);
                    this.addObserver(p);
                    break;
                }
                case "COI": {
                    //Envia a la vista el mismo mensaje recibido
                    notifyObservers(msg);
                    break;
                }
                case "MOV": {
                    //Envia a la vista el mismo mensaje recibido
                    notifyObservers(msg);
                    break;
                }
                case "FIN": {
                    //Cierra la conexion
                    this.listener.cerrarConexion();
                    break;
                }
                case "ERR": {
                    //Muestra el mensaje de error recibido y termina la conexion
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

package Cliente;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase encargada de detectar las pulsaciones del teclado del cliente
 * 
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class DetectorTeclas implements KeyListener {
    private ControladorCliente controlador;

    public DetectorTeclas(ControladorCliente controlador) {
        this.controlador = controlador;
    }
    
    /**
     * No se utiliza
     */
    @Override
    public void keyTyped(KeyEvent e) {
        //No pasa nada
    }
    
    /**
     * Envia al controlador la dirrecion pulsada por el jugador
     * 
     * @param e Tecla pulsada
     */
    @Override
    public void keyPressed(KeyEvent e) {
        this.controlador.setDirAct(e.getKeyCode());
    }
    
    /**
     * No se utiliza
     */
    @Override
    public void keyReleased(KeyEvent e) {
        //No pasa nada
    }
}

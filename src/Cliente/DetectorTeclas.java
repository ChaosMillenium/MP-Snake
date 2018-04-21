/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author millenium
 */
public class DetectorTeclas implements KeyListener {
    
    private ControladorCliente controlador;

    public DetectorTeclas(ControladorCliente controlador) {
        this.controlador = controlador;
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        //No pasa nada
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.controlador.setDirAct(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //No pasa nada
    }
}

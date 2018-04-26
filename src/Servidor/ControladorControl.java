package Servidor;

import java.util.Observable;
import java.util.Observer;

public class ControladorControl extends Observable {

    private ModeloJuego modelo;

    public ControladorControl(ModeloJuego modelo) {
        this.modelo = modelo;

    }

    public void ControladorControl(Object msg) {
        String mensaje = (String) msg;
        String[] parseado = mensaje.split(";");
        modelo.notifyObservers(parseado[0]);

    }

}

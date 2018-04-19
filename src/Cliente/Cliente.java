package Cliente;

public class Cliente {

    public static void main(String[] args) {
        ControladorCliente controlador = new ControladorCliente();
        controlador.establecerConexion();
        Puntuacion UIPuntuacion = new Puntuacion(controlador);
        controlador.addObserver(UIPuntuacion);
    }  
}

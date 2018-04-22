package Cliente;

public class Cliente {

    public static void main(String[] args) {
        ControladorCliente controlador = new ControladorCliente();
        Puntuacion UIPuntuacion = new Puntuacion(controlador);
        controlador.addObserver(UIPuntuacion);
        controlador.establecerConexion();

    }
}

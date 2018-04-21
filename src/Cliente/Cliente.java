package Cliente;

public class Cliente {

    public static void main(String[] args) {
        ControladorCliente controlador = new ControladorCliente();
        //VistaCliente v = new VistaCliente(60,60);
        controlador.establecerConexion();
        Puntuacion UIPuntuacion = new Puntuacion(controlador);
        controlador.addObserver(UIPuntuacion);
    }  
}

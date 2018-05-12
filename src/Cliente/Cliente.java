package Cliente;

/**
 * Clase principal del cliente, inicializa al cliente.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class Cliente {

    /**
     * Inicia el cliente.
     *
     * @param args
     */
    public static void main(String[] args) {
        ControladorCliente controlador = new ControladorCliente();
        controlador.establecerConexion();

    }
}

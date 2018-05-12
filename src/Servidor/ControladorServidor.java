package Servidor;

import Utilidades.Coordenadas;
import Utilidades.Direccion;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Controlador Servidor, el cual recibirá el mensaje y actuará en consecuencia a
 * este
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ControladorServidor implements Observer {

    private ModeloJuego modelo; //Modelo de juego
    private ThreadServidor servidor; //Hilo del servidor

    /**
     * Crea un controlador del servidor.
     *
     * @param modelo Modelo de juego.
     */
    public ControladorServidor(ModeloJuego modelo) {
        this.modelo = modelo;
        this.servidor = new ThreadServidor(this);
    }

    /**
     * Inicia el servidor, creando la vista de la consola..
     */
    public void iniciarServer() {
        VistaConsolaServidor control = new VistaConsolaServidor(this);
        modelo.addObserver(control);
        control.setVisible(true);
        this.servidor.startServer();
    }

    /**
     * Añade un jugador al modelo de juego.
     */
    public void añadirJugador() {
        this.modelo.añadirJugador();
    }

    /**
     * Devuelve las filas del tablero.
     *
     * @return Filas del tablero.
     */
    public int getFilas() {
        return this.modelo.getFilas();
    }

    /**
     * Devuelve las columnas del tablero.
     *
     * @return columnas del tablero.
     */
    public int getColumnas() {
        return this.modelo.getColumnas();
    }

    /**
     * Recibe una actualización del modelo en forma de mensaje, que se traducirá
     * de alguna manera en una acción por parte del servidor.
     *
     * @param o Observado (Modelo de juego).
     * @param arg Mensaje recibido del modelo.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!((String) arg).isEmpty()) {
            String accion = (String) arg;
            String[] parseado = accion.split(";");
            switch (parseado[0]) {
                //Nuevo jugador, hay que avisar al resto de jugadores. (NJ;1)
                case "NJ": {
                    int id = Integer.parseInt(parseado[1]);
                    this.servidor.nuevoJugador(id, this.getCoordenadas(id));
                    break;
                }
                //Movimiento de jugador. (MOV;1;10;12;15;12)
                case "MOV": {
                    int id = Integer.parseInt(parseado[1]);
                    int[] cabeza = {this.modelo.getCabeza(id).getX(),
                        this.modelo.getCabeza(id).getY()};
                    int[] cola = {this.modelo.getAnteriorCola(id).getX(),
                        this.modelo.getAnteriorCola(id).getY()};
                    this.servidor.moverJugador(id, cabeza, cola);
                    break;
                }
                //Colisión entre dos jugadores o consigo mismo. (COL;1;2)
                case "COL": {
                    int id1 = Integer.parseInt(parseado[1]);
                    int id2 = Integer.parseInt(parseado[2]);
                    this.servidor.colision(id1, id2);
                    break;
                }
                //Colisión con borde. (CBR;1)
                case "CBR": {
                    int id = Integer.parseInt(parseado[1]);
                    this.servidor.colision(id);
                    break;
                }
                //Obtención de puntos. (PTS;500)
                case "PTS": {
                    int id = Integer.parseInt(parseado[1]);
                    int puntos = Integer.parseInt(parseado[2]);
                    this.servidor.darPuntos(id, puntos);
                    break;
                }
                //Nuevo tesoro. (TSR;10;10)
                case "TSR": {
                    int x = Integer.parseInt(parseado[1]);
                    int y = Integer.parseInt(parseado[2]);
                    this.servidor.nuevoTesoro(x, y);
                    break;
                }
                //Fin de conexión.
                case "FIN": {
                    this.servidor.eliminarJugador(Integer.parseInt(parseado[1]));
                    break;
                }
                default: {
                    System.err.println(accion);
                    break;
                }

            }
        }

    }

    /**
     * Cambia la dirección de un jugador.
     *
     * @param id ID del jugador.
     * @param direccion Dirección nueva.
     */
    public void cambiarDireccion(int id, String direccion) {
        try {
            Direccion instanciaDir = Direccion.valueOf(direccion);
            this.modelo.cambiarDireccion(instanciaDir, id);
        } catch (IllegalArgumentException e) {
            System.err.println("Dirección inválida");
        }
    }

    /**
     * Se genera un array de coordenadas de un jugador.
     *
     * @param id ID del jugador.
     * @return Array de coordenadas (Pares X, Impares Y).
     */
    public int[] getCoordenadas(int id) {
        //Obtiene las coordenadas del modelo.
        Coordenadas[] coordenadas = this.modelo.getCoordenadas(id);
        //Se transforma el array de tipo Coordenadas a tipo int.
        int[] coordInt = new int[coordenadas.length * 2];
        int j = 0;
        for (int i = 0; i < coordInt.length; i += 2) {
            coordInt[i] = coordenadas[j].getX();
            coordInt[i + 1] = coordenadas[j].getY();
            j++;
        }
        return coordInt;
    }

    /**
     * Busca siguiente ID libre.
     *
     * @return Siguiente ID libre.
     */
    public int siguienteKey() {
        return this.modelo.siguienteKey();
    }

    /**
     * Devuelve las coordenadas de los tesoros.
     *
     * @return Set con coordenadas de los tesoros.
     */
    public Set<Coordenadas> getTesoros() {
        return this.modelo.getTesoros();
    }

    /**
     * Expulsa a un jugador del juego.
     *
     * @param id ID del juego.
     */
    public void finalizarJugador(int id) {
        this.modelo.finalizarJugador(id);
    }

    /**
     * Cambia el modo de un jugador (manual/automático);
     *
     * @param id ID del jugador.
     * @param manual "1" si manual, "0" si automático.
     */
    public void setManual(int id, String manual) {
        //Por defecto la partida empieza en automático (para evitar colisiones antes de generar el tablero),
        //y en caso de seleccionar manual, se llama a esta función
        //que hace que se active este modo.
        boolean manualBol;
        if (manual.equals("1")) {
            manualBol = true;
        } else {
            manualBol = false;
        }
        this.modelo.setManual(id, manualBol);
    }

}

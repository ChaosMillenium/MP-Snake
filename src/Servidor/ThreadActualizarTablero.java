package Servidor;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Hilo que realiza los movimientos en el tablero de juego (Es parte del
 * modelo).
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ThreadActualizarTablero extends Thread {

    private ModeloJuego modelo; //Modelo de juego.
    private boolean pausa = false; //Indica que el juego está en pausa (Actualmente solo usado para evitar errores de concurrencia).
    private final int VELOCIDAD; //Velocidad de juego (transferida desde el modelo).
    private final int PROBABILIDAD = 3; //Probabilidad de aparición de un tesoro (X%).

    /**
     * Crea el hilo de tablero de juego.
     *
     * @param modelo Modelo de juego.
     */
    public ThreadActualizarTablero(ModeloJuego modelo) {
        this.modelo = modelo;
        this.VELOCIDAD = this.modelo.getVELOCIDAD();
        this.setName("Tablero");
    }

    /**
     * Inicio del hilo del tablero.
     */
    @Override
    public void run() {
        Random aleatTesoro = new Random(); //Usado para calcular la probabilidad de tesoro.
        while (hayJugadores()) { //Si no hay jugadores se termina el hilo de juego.
            if (this.pausa) {
                try {
                    Thread.sleep(VELOCIDAD * 2); //Espera dos ciclos de tablero para despausar otra vez (Para mantener la fluidez de juego).
                    this.pausa(); //Quita la pausa y continúa el juego.
                } catch (InterruptedException e) {
                    System.err.println("Error en hilo de tablero.");
                }
            }
            Set<Map.Entry<Integer, Jugador>> entradas = Collections.synchronizedSet(this.modelo.getJugadores().entrySet());
            synchronized (entradas) {
                for (Map.Entry<Integer, Jugador> entrada : entradas) { //Movimiento de jugadores
                    Jugador jugador = entrada.getValue();
                    jugador.nuevaCabeza();
                    jugador.eliminarCola();
                    this.modelo.notificarMovimiento(entrada.getKey());
                    jugador.setEspera(false); //Ya se puede cambiar de dirección                    
                }
            }
            synchronized (this.modelo.getTesoros()) {
                if (this.modelo.getTesoros().isEmpty() || (aleatTesoro.nextInt(100) < PROBABILIDAD)) { //PROBABILIDAD % cada VELOCIDAD ms de generar un tesoro
                    this.modelo.generarTesoro();
                }
            }
            this.modelo.calcularMovimientoAutomatico(); //Calcula las direcciones de los jugadores que estén en modo automático para el siguiente ciclo.
            /*Existiría la posibilidad de incluir este cálculo en un hilo distinto, 
              pero al ser un juego a pequeña escala (pocos jugadores) este cálculo no será pesado de hacer en un ciclo de juego.
             */
            try {
                Thread.sleep(this.VELOCIDAD);
            } catch (InterruptedException e) {
                System.err.println("Error en hilo de tablero.");
                this.start();
            }
        }
    }

    /**
     * Pone en pausa el juego
     */
    public void pausa() {
        this.pausa = !this.pausa;
    }

    /**
     * Evalúa si hay jugadores activos.
     * @return True si hay jugadores en el mapa.
     */
    private boolean hayJugadores() {
        return this.modelo.hayJugadores();
    }
}

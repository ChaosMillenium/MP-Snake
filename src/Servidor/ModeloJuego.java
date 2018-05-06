package Servidor;

import Utilidades.Coordenadas;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import Utilidades.Direccion;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Modelo de juego (MVC), del que se obtendrán todos los datos de juego y los
 * guardará.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ModeloJuego extends Observable {

    private int columnas, filas; //filas y columnas del tablero de juego
    private Map<Integer, Jugador> jugadores; //Mapa de jugadores, la clave es el id de jugador
    private final int VELOCIDAD = 75; //Velocidad de juego (cuando menor el valor, más rápido)
    private final int TAMAÑOBASE = 3; //tamaño inicial de las serpientes
    private final int PUNTOSTESORO = 100; //puntos otorgados por capturar un tesoro
    private ThreadActualizarTablero hiloTablero;
    private Set<Coordenadas> tesoros; //lista de tesoros

    /**
     * Crea el modelo de juego e inicializa las colecciones de jugadores y
     * tesoros.
     */
    public ModeloJuego() {
        this.jugadores = new ConcurrentHashMap<>();
        this.tesoros = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Añade un jugador al modelo, y empieza la partida si es el primero en
     * entrar.
     */
    public void añadirJugador() {
        boolean empezarJuego = !hayJugadores(); //Se evalúa si el juego está en marcha, para pausarlo si es necesario (Evita errores de concurrencia).
        if (!empezarJuego) {
            this.hiloTablero.pausa(); //Pausa
        }
        int key = siguienteKey(); //busca la siguiente clave disponible
        Jugador nuevo = new Jugador(this.TAMAÑOBASE);
        asignarCoordInicio(nuevo, key); //Se buscan unas coordenadas libres en las que poner al nuevo jugador
        synchronized (this.jugadores) {
            this.jugadores.put(key, nuevo);
        }
        if (empezarJuego) {
            //Si el juego no está activo, se comienza un nuevo hilo con el juego.
            this.hiloTablero = new ThreadActualizarTablero(this);
            this.hiloTablero.start();
            this.generarTesoro(); //Tesoro inicial
        }
        setChanged();
        //Se notifica a todos los observadores (controlador, vista consola) que se ha añadido un nuevo jugador
        notifyObservers("NJ;" + key);
        if (!empezarJuego) {
            this.hiloTablero.pausa(); //Despausa
        }
    }

    /**
     * Se busca el posible ID libre (el menor que no esté escogido)
     *
     * @return El siguiente ID libre.
     */
    public int siguienteKey() {
        int key = 1;
        synchronized (this.jugadores) {
            while (this.jugadores.keySet().contains(key)) {
                key++;
            }
        }
        return key;
    }

    /**
     * Devuelve el número de filas en juego.
     *
     * @return Número de filas.
     */
    public int getFilas() {
        return this.filas;
    }

    /**
     * Devuelve el número de columnas en juego.
     *
     * @return Número de columnas.
     */
    public int getColumnas() {
        return this.columnas;
    }

    /**
     * Devuelve la velocidad de juego.
     *
     * @return Velocidad de juego.
     */
    public int getVELOCIDAD() {
        return VELOCIDAD;
    }

    /**
     * Devuelve una referencia al mapa que guarda a los jugadores.
     *
     * @return Mapa de los jugadores.
     */
    public Map<Integer, Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Devuelve la cabeza de una serpiente dada.
     *
     * @param id ID del jugador del que se quiere buscar la cabeza.
     * @return Coordenadas de la cabeza de la serpiente.
     */
    public Coordenadas getCabeza(int id) {
        synchronized (this.jugadores) {
            return this.jugadores.get(id).getCabeza();
        }
    }

    /**
     * Devuelve la cola que ha desaparecido en un movimiento de una serpiente
     * dada.
     *
     * @param id ID del jugador del que se quiere buscar la cola.
     * @return Coordenadas de la anterior cola de la serpiente.
     */
    public Coordenadas getAnteriorCola(int id) {
        synchronized (this.jugadores) {
            return this.jugadores.get(id).getAnteriorCola();
        }
    }

    /**
     * Devuelve una referencia a la lista que guarda los tesoros.
     *
     * @return Lista de los tesoros.
     */
    public Set<Coordenadas> getTesoros() {
        return tesoros;
    }

    /**
     * Asigna aleatoriamente las coordenadas a un jugador (si están libres).
     *
     * @param jugador Referencia al jugador.
     * @param id ID del jugador.
     */
    private void asignarCoordInicio(Jugador jugador, int id) {

        Random r = new Random();
        int margen = this.TAMAÑOBASE * 2; //Margen desde los bordes
        boolean repetir = true;
        while (repetir) {
            int nuevoX = r.nextInt(this.columnas - margen) + margen; //Coordenada aleatoria X
            int nuevoY = r.nextInt(this.filas - margen) + margen; //Coordenada aleatoria Y
            Coordenadas nuevoCoord = new Coordenadas(nuevoX, nuevoY);
            /*
                * A partir de aquí se buscan coordenadas próximas a la primera seleccionada aleatoriamente para añadirlas.
                * Si no existe posibilidad, se empieza de nuevo el método.
             */
            if ((colisionJugador(nuevoCoord, id) == 0) && (colisionTesoro(nuevoCoord) == null)) {
                jugador.nuevaCabeza(nuevoCoord);
                Coordenadas arriba = new Coordenadas(nuevoX, nuevoY - 1);
                Coordenadas abajo = new Coordenadas(nuevoX, nuevoY + 1);
                Coordenadas der = new Coordenadas(nuevoX + 1, nuevoY);
                Coordenadas izq = new Coordenadas(nuevoX - 1, nuevoY);
                for (int i = 1; i < this.TAMAÑOBASE; i++) {
                    if (i == this.TAMAÑOBASE - 1) {
                        repetir = false;
                    }
                    if ((colisionJugador(arriba, id) == 0) && (colisionTesoro(abajo) == null)) {
                        jugador.nuevaCabeza(arriba);
                        arriba.aumentarX(-1);
                        abajo.aumentarX(-1);
                        izq.aumentarX(-1);
                        der.aumentarX(-1);
                    } else if ((colisionJugador(abajo, id) == 0) && (colisionTesoro(abajo) == null)) {
                        jugador.nuevaCabeza(abajo);
                        arriba.aumentarX(1);
                        abajo.aumentarX(1);
                        izq.aumentarX(1);
                        der.aumentarX(1);
                    } else if ((colisionJugador(der, id) == 0) && (colisionTesoro(der) == null)) {
                        jugador.nuevaCabeza(der);
                        arriba.aumentarY(1);
                        abajo.aumentarY(1);
                        izq.aumentarY(1);
                        der.aumentarY(1);
                    } else if ((colisionJugador(izq, id) == 0) && (colisionTesoro(izq) == null)) {
                        jugador.nuevaCabeza(izq);
                        arriba.aumentarY(-1);
                        abajo.aumentarY(-1);
                        izq.aumentarY(-1);
                        der.aumentarY(-1);
                    } else {
                        jugador.eliminarSerpiente();
                        repetir = true;
                    }

                }
            }
        }
    }

    /**
     * Comprueba la colisión con otros jugadores (incluido él mismo).
     *
     * @param coord Coordenada con la que se comprueba la colisión.
     * @param id ID del jugador al que pertenece la coordenada.
     * @return ID del jugador con el que se ha realizado la colisión (0 si no
     * colisiona).
     */
    private int colisionJugador(Coordenadas coord, int id) {
        synchronized (this.jugadores) {
            for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
                LinkedList<Coordenadas> serpiente = entrada.getValue().getSerpiente();
                int primero;
                if (id == entrada.getKey()) { //Si se revisa la colisión con uno mismo, salta la cabeza (para evitar errores)
                    primero = 1;
                } else {
                    primero = 0;
                }
                for (int i = primero; i < serpiente.size(); i++) {
                    Coordenadas comparar = serpiente.get(i);
                    if (coord.equals(comparar)) {
                        return entrada.getKey();
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Comprueba la colisión con los bordes del tablero.
     *
     * @param coord Coordenada a comprobar la colisión.
     * @return True si ha colisionado con un borde
     */
    private boolean colisionBorde(Coordenadas coord) {
        int x = coord.getX();
        int y = coord.getY();
        return ((x < 0) || (y < 0) || (x >= this.columnas) || (y >= this.filas));
    }

    /**
     * Cambia la dirección de la serpiente indicada.
     *
     * @param direccion Dirección a la que se quiere cambiar.
     * @param id ID del jugador cuya dirección se quiere cambiar.
     */
    public void cambiarDireccion(Direccion direccion, int id) {
        synchronized (this.jugadores) {
            Jugador jugador = this.jugadores.get(id);
            if (!jugador.isEspera()) { //Comprueba que se pueda cambiar dirección
                if (jugador.getDireccion().equals(Direccion.IZQ) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.DER) && !(direccion.equals(Direccion.DER) || direccion.equals(Direccion.IZQ))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.ARRIBA) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                } else if (jugador.getDireccion().equals(Direccion.ABAJO) && !(direccion.equals(Direccion.ARRIBA) || direccion.equals(Direccion.ABAJO))) {
                    jugador.setDireccion(direccion);
                    jugador.setEspera(true); //No se puede volver a cambiar dirección hasta siguiente ciclo
                }
            }
        }
    }

    /**
     * Elimina un jugador del mapa de jugadores.
     *
     * @param id ID del jugador a eliminar.
     * @see ModeloJuego#finalizarJugador(int) .
     */
    private void eliminarJugador(int id) {
        synchronized (this.jugadores) {
            this.jugadores.remove(id);
        }
    }

    /**
     * Elimina un jugador del juego.
     *
     * @param id ID del jugador a eliminar.
     */
    public void finalizarJugador(int id) {
        this.hiloTablero.pausa(); //Pausa para evitar errores de concurrencia
        setChanged();
        notifyObservers("FIN;" + id); //Notifica la eliminación de un jugador
        this.eliminarJugador(id);
        this.hiloTablero.pausa();
    }

    /**
     * Devuelve true si hay jugadores en el mapa.
     *
     * @return True si hay jugadores en el mapa.
     */
    public boolean hayJugadores() {
        return !this.jugadores.isEmpty();
    }

    /**
     * Devuelve las coordenadas de una serpiente indicada.
     *
     * @param id ID de la serpiente.
     * @return Array de coordenadas de la serpiente.
     */
    public Coordenadas[] getCoordenadas(int id) {
        Jugador jugador = this.jugadores.get(id);
        Coordenadas[] coordenadas = new Coordenadas[jugador.getSerpiente().size()];
        jugador.getSerpiente().toArray(coordenadas);
        return coordenadas;
    }

    /**
     * Notifica el movimiento de un jugador a los observadores, incluidas
     * colisiones y obtenciones de tesoros.
     *
     * @param id ID del jugador del que se envian las notificaciones.
     */
    public void notificarMovimiento(int id) {
        synchronized (this.jugadores) {
            int idColision;
            setChanged();

            if (hayJugadores()) { //Evita error de concurrencia
                if ((idColision = this.colisionJugador(this.jugadores.get(id).getCabeza(), id)) != 0) { //Comprueba colisión con otros jugadores.
                    notifyObservers("COL;" + id + ";" + idColision);
                    this.eliminarJugador(id);
                    if (idColision != id) { //Si la colisión es entre 2 jugadores elimina al segundo
                        this.eliminarJugador(idColision);
                    }
                }
            }
            try {
                if (this.colisionBorde(this.jugadores.get(id).getCabeza())) { //Comprueba colisión con los bordes.
                    notifyObservers("CBR;" + id);
                    this.eliminarJugador(id);
                } else { //Si no colisiona, notifica movimiento.
                    notifyObservers("MOV;" + id);
                    if (!this.jugadores.isEmpty()) {
                        Coordenadas tesoro;
                        if ((tesoro = this.colisionTesoro(this.jugadores.get(id).getCabeza())) != null) { //Comprueba captura de tesoros
                            this.tesoros.remove(tesoro);
                            this.jugadores.get(id).añadirPuntos(this.PUNTOSTESORO);
                            int nuevosPuntos = this.jugadores.get(id).getPuntos();
                            setChanged();
                            notifyObservers("PTS;" + id + ";" + nuevosPuntos);
                        }
                    }
                }
            } catch (NullPointerException ex) {
                //Si salta es que el jugador ya no existe
                System.err.println("El jugador no existe");
            }
        }
    }

    /**
     * Genera un tesoro en el juego.
     */
    public void generarTesoro() {
        Random r = new Random();
        Coordenadas candidato = new Coordenadas(r.nextInt(this.columnas), r.nextInt(this.filas));
        while (colisionJugador(candidato, 0) != 0) { //Comprueba que no se genere un tesoro encima de un jugador
            candidato = new Coordenadas(r.nextInt(this.columnas), r.nextInt(this.filas));
        }
        boolean añadido = this.tesoros.add(candidato); //Comprueba que el tesoro no exista ya.
        if (añadido) {
            setChanged();
            notifyObservers("TSR;" + candidato.getX() + ";" + candidato.getY());
        }
    }

    /**
     * Comprueba si la coordenada coincide con un tesoro.
     *
     * @param coord Coordenadas a comprobar.
     * @return Coordenadas del tesoro con el que ha colisionado.
     */
    private Coordenadas colisionTesoro(Coordenadas coord) {
        for (Coordenadas tesoro : this.tesoros) {
            if (coord.equals(tesoro)) {
                return tesoro;
            }
        }
        return null;
    }

    /**
     * Guarda las filas y columnas del tablero de juego.
     *
     * @param filas
     * @param columnas
     */
    public void setFilasColumnas(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
    }

    /**
     * Guarda para un jugador si la serpiente está en modo manual o automático.
     *
     * @param id ID del jugador.
     * @param manual True si juega en manual, False si juega en automático.
     */
    public void setManual(int id, boolean manual) {
        this.jugadores.get(id).setManual(manual);
    }

    /**
     * Calcula el siguiente movimiento de todos los jugadores que estén en modo
     * automático.
     */
    public void calcularMovimientoAutomatico() {
        Set<Map.Entry<Integer, Jugador>> entradas = Collections.synchronizedSet(this.jugadores.entrySet());
        synchronized (entradas) {
            for (Map.Entry<Integer, Jugador> entrada : entradas) {
                Jugador jugador = entrada.getValue();
                if (!jugador.isManual()) {
                    Coordenadas tesoro = tesoroMasCercano(jugador.getCabeza()); //Busca el tesoro más cercano
                    Direccion nuevaDireccion = calcularDireccion(tesoro, jugador.getCabeza(), jugador.getDireccion()); //Calcula la mejor dirección por la que ir a por ese tesoro
                    cambiarDireccion(nuevaDireccion, entrada.getKey()); //Cambia la dirección de la serpiente
                    Direccion direccion = jugador.getDireccion();
                    Coordenadas comprobar = jugador.simularDireccion(); //Obtiene la que sería la nueva cabeza.
                    if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar, busca huir
                        if (direccion.equals(Direccion.ABAJO) || direccion.equals(Direccion.ARRIBA)) {
                            jugador.setDireccion(Direccion.DER);  //Se prueba con el lado derecho y se recalcula
                            comprobar = jugador.simularDireccion();
                            if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar
                                jugador.setDireccion(Direccion.IZQ); //Si aun asi fuera a colisionar esta muerto, nada se puede hacer
                            }
                        } else {
                            jugador.setDireccion(Direccion.ARRIBA);
                            comprobar = jugador.simularDireccion();
                            if (colisionBorde(comprobar) || (colisionJugador(comprobar, entrada.getKey())) != 0) { //Si va a colisionar
                                jugador.setDireccion(Direccion.ABAJO);
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * Calcula la mejor dirección a la que acercarse a la coordenada dada desde el origen.
     * @param destino
     * @param origen
     * @param direccionOriginal Dirección original.
     * @return Dirección óptima.
     */
    private Direccion calcularDireccion(Coordenadas destino, Coordenadas origen, Direccion direccionOriginal) { 
        int distanciaX = abs(origen.getX() - destino.getX());
        int distanciaY = abs(origen.getY() - destino.getY());

        if ((distanciaX < distanciaY) && ((distanciaX != 0)) || (distanciaY == 0)) {
            if (origen.getX() < destino.getX()) {
                if (direccionOriginal != Direccion.IZQ) {
                    return Direccion.DER;
                } else {
                    if (origen.getY() < destino.getY()) {
                        return Direccion.ABAJO;
                    } else {
                        return Direccion.ARRIBA;
                    }
                }
            } else {
                if (direccionOriginal != Direccion.DER) {
                    return Direccion.IZQ;
                } else {
                    if (origen.getY() < destino.getY()) {
                        return Direccion.ABAJO;
                    } else {
                        return Direccion.ARRIBA;
                    }
                }
            }
        } else {
            if (origen.getY() < destino.getY()) {
                if (direccionOriginal != Direccion.ARRIBA) {
                    return Direccion.ABAJO;
                } else {
                    if (origen.getX() < destino.getX()) {
                        return Direccion.DER;
                    } else {
                        return Direccion.IZQ;
                    }
                }
            } else {
                if (direccionOriginal != Direccion.ABAJO) {
                    return Direccion.ARRIBA;
                } else {
                    if (origen.getX() < destino.getX()) {
                        return Direccion.DER;
                    } else {
                        return Direccion.IZQ;
                    }
                }
            }
        }
    }
    
    /**
     * Busca el tesoro más cercano a las coordenadas dadas.
     * @param origen
     * @return Coordenadas del tesoro más cercano a las coordenadas.
     */
    private Coordenadas tesoroMasCercano(Coordenadas origen) {
        Iterator<Coordenadas> it = this.tesoros.iterator();
        Coordenadas resultado = it.next(); //Por defecto
        double menorDistancia = Integer.MAX_VALUE;
        for (Coordenadas tesoro : this.tesoros) {
            int tesoroX = tesoro.getX();
            int tesoroY = tesoro.getY();
            int origenX = origen.getX();
            int origenY = origen.getY();

            double distancia = sqrt(pow(tesoroX - origenX, 2) + pow(tesoroY - origenY, 2)); //Calcula la distancia en diagonal entre los dos puntos.
            if (menorDistancia > distancia) {
                menorDistancia = distancia;
                resultado = tesoro;
            }
        }
        return resultado;
    }
}

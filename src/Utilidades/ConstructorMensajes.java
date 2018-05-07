package Utilidades;

/**
 * Clase que genera los mensajes del protocolo Cliente - Servidor (y los
 * valida). Ver la especificación dada en la práctica y la ampliación sobre esta
 * de este protocolo para más detalles.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class ConstructorMensajes {

    /**
     * Genera un mensaje IDC.
     *
     * @param id
     * @return Mensaje IDC.
     */
    public static String idc(int id) {
        return "IDC;" + id;
    }

    /**
     * Genera un mensaje TAB.
     *
     * @param filas
     * @param columnas
     * @return Mensaje TAB.
     */
    public static String tab(int filas, int columnas) {
        return "TAB;" + filas + ";" + columnas;
    }

    /**
     * Genera un mensaje TSP.
     *
     * @param tamaño Tamaño de la serpiente.
     * @return Mensaje TSP.
     */
    public static String tsp(int tamaño) {
        return "TSP;" + tamaño;
    }

    /**
     * Genera un mensaje COI.
     *
     * @param coordenadas Array con las parejas de coordenadas que ocupa la
     * serpiente.
     * @param id
     * @return Mensaje COI.
     */
    public static String coi(int[] coordenadas, int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("COI;").append(id).append(";");
        for (int i = 0; i < coordenadas.length - 1; i++) {
            sb.append(coordenadas[i]).append(";");
        }
        sb.append(coordenadas[coordenadas.length - 1]);
        return sb.toString();
    }

    /**
     * Genera un mensaje DIR.
     *
     * @param direccion
     * @return Mensaje DIR.
     */
    public static String dir(String direccion) {
        return "DIR;" + direccion;
    }

    /**
     * Genera un mensaje FIN (De cliente a servidor).
     *
     * @param id
     * @return Mensaje FIN.
     */
    public static String fin(int id) {
        return "FIN;" + id;
    }

    /**
     * Genera un mensaje FIN (De servidor a cliente).
     *
     * @return Mensaje FIN.
     */
    public static String fin() {
        return "FIN";
    }

    /**
     * Genera un mensaje ERR.
     *
     * @param mensaje Mensaje de error a enviar.
     * @return Mensaje ERR.
     */
    public static String err(String mensaje) {
        return "ERR;" + mensaje;
    }

    /**
     * Genera un mensaje ELJ.
     *
     * @param id
     * @return Mensaje ELJ.
     */
    public static String elj(int id) {
        return "ELJ;" + id;
    }

    /**
     * Genera un mensaje MOV.
     *
     * @param id
     * @param cabezaX Cabeza a añadir (Coordenada X).
     * @param cabezaY Cabeza a añadir (Coordenada Y).
     * @param colaX Cola a eliminar (Coordenada X).
     * @param colaY Cola a eliminar (Coordenada Y).
     * @return Mensaje MOV.
     */
    public static String mov(int id, int cabezaX, int cabezaY, int colaX, int colaY) {
        return "MOV;" + id + ";" + cabezaX + ";" + cabezaY + ";" + colaX + ";" + colaY;
    }

    /**
     * Genera un mensaje PTS.
     *
     * @param id
     * @param puntos
     * @return Mensaje PTS.
     */
    public static String pts(int id, int puntos) {
        return "PTS;" + id + ";" + puntos;
    }

    /**
     * Genera un mensaje TSR.
     *
     * @param x Coordenada X del tesoro.
     * @param y Coordenada Y del tesoro.
     * @return Mensaje TSR.
     */
    public static String tsr(int x, int y) {
        return "TSR;" + x + ";" + y;
    }

    /**
     * Genera un mensaje MAN.
     *
     * @param manual True si el jugador juega en modo manual.
     * @return Mensaje MAN.
     */
    public static String man(boolean manual) {
        int manualInt;
        if (manual) {
            manualInt = 1;
        } else {
            manualInt = 0;
        }
        return "MAN;" + manualInt;
    }

    /**
     * Analiza si el parámetro es la cabecera de un mensaje DIR.
     *
     * @param mensaje
     * @return True si el parámetro es "DIR".
     */
    public static boolean isDir(String mensaje) {
        return mensaje.equals("DIR");
    }

    /**
     * Analiza si el parámetro es la cabecera de un mensaje FIN.
     *
     * @param mensaje
     * @return True si el parámetro es "FIN".
     */
    public static boolean isFin(String mensaje) {
        return mensaje.equals("FIN");
    }

    /**
     * Analiza si el parámetro es la cabecera de un mensaje MAN.
     *
     * @param mensaje
     * @return True si el parámetro es "MAN".
     */
    public static boolean isMan(String mensaje) {
        return mensaje.equals("MAN");
    }
}

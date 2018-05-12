package Utilidades;

import java.awt.Color;

/**
 * Clase que selecciona un color para cada jugador.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class SelectorColor {

    private static final String[] SELECCIONCOLORES = { //Selección de colores
        "#ff0000",//rojo
        "#00ff00",//verde
        "#8B7D7B",//marrón
        "#FF4500",//azulOscuro
        "#FF4500",//naranjaRojo
        "#8A2BE2",//violeta
        "#8B8B83",//marfil4
        "#7FFFD4",//aguamarino
        "#6B8E23",//oliva
        "#FF00FF",//magenta
        "#696969",//gris oscuro
        "#D2691E",//chocolate
       
    };

    /**
     * Devuelve un color en función de un entero dado.
     *
     * @param id Semilla.
     * @return Color pseudo-aleatorio.
     */
    public static Color generarColor(int id) {
        int selector = id % SelectorColor.SELECCIONCOLORES.length;
        return Color.decode(SelectorColor.SELECCIONCOLORES[selector]);
    }
}

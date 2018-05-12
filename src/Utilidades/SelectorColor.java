package Utilidades;

import java.awt.Color;

/**
 * Clase que selecciona un color para cada jugador.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class SelectorColor {

    private static final String[] SELECCIONCOLORES = { //Selección de colores
        "#ff0000",
        "#d000ff",
        "#00ff00",
        "#00ff8c",
        "#800000",
        "#008cff",
        "#0000ff",
        "#7700ff",
        "#ff00c7",
        "#ff0066",
        "#acbf00",
        "#00ffff",
        "#805300",
        "#00800c",
        "#008037",
        "#800040",
        "#068000"
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author millenium
 */
public class SelectorColor {

    private static final String[] seleccionColores = { //Selección de colores
        "#ff0000",
        "#ff7700",
        "#00ff00",
        "#00ff8c",
        "#00ffff",
        "#008cff",
        "#0000ff",
        "#7700ff",
        "#d000ff",
        "#ff00c7",
        "#ff0066",
        "#acbf00",
        "#800000",
        "#805300",
        "#00800c",
        "#008037",
        "#800040",
        "#068000"
    };

    public static Color generarColor(int id) {
        int selector = id % SelectorColor.seleccionColores.length;
        Color resultado = Color.decode(SelectorColor.seleccionColores[selector]);
        return resultado;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import javax.swing.*;

/**
 *
 * @author i.chicano.2016
 */
public class PeticionFilasColumnas {

    public static int[] pedirFilasColumnas() {
        JTextField campoFilas = new JTextField(3);
        JTextField campoColumnas = new JTextField(3);
        JLabel texto = new JLabel("Introduzca las filas y las columnas del tablero de juego.");

        JPanel panel = new JPanel();
        panel.add(texto);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(new JLabel("Filas: "));
        panel.add(campoFilas);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(new JLabel("Columnas: "));
        panel.add(campoColumnas);
        
        JOptionPane.showConfirmDialog(null, panel,
                "Introduzca filas y columnas", JOptionPane.OK_CANCEL_OPTION);
        int filas = Integer.parseInt(campoFilas.getText());
        int columnas = Integer.parseInt(campoColumnas.getText());
        int[] filasColumnas = {filas, columnas};
        return filasColumnas;
    }
}

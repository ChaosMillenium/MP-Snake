
package Servidor;

import javax.swing.*;


public class PeticionFilasColumnas {

    public static int[] pedirFilasColumnas() throws NumberFormatException, NullPointerException{
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

        int eleccion = JOptionPane.showConfirmDialog(null, panel,
                "Introduzca filas y columnas", JOptionPane.OK_CANCEL_OPTION);
        if (eleccion == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
        int filas = Integer.parseInt(campoFilas.getText());
        int columnas = Integer.parseInt(campoColumnas.getText());
        int[] filasColumnas = {filas, columnas};
        return filasColumnas;

    }
}

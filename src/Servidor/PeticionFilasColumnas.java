package Servidor;

import javax.swing.*;

/**
 * Ventana usada para pedir las filas y columnas del tablero de juego.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class PeticionFilasColumnas {

    /**
     * Crea una ventana para pedir las filas y columnas del tablero.
     *
     * @return Array con las filas y columnas (2 posiciones, posición 0 filas,
     * posición 1 columnas).
     * @throws NumberFormatException Lanzada cuando los valores introducidos no
     * son válidos.
     * @throws NullPointerException Lanzada cuando no se han introducido
     * valores.
     */
    public static int[] pedirFilasColumnas() throws NumberFormatException, NullPointerException {
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

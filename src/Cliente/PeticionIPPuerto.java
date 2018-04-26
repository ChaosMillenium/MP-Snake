/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import javax.swing.*;

/**
 *
 * @author i.chicano.2016
 */
public class PeticionIPPuerto {

    public static String[] pedirIPPuerto() throws NumberFormatException, NullPointerException {
        JTextField campoIP = new JTextField(3);
        JTextField campoPuerto = new JTextField(3);
        JLabel texto = new JLabel("Introduzca la IP y puerto del servidor.");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(texto);
        panel.add(new JLabel("IP: "));
        panel.add(campoIP);
        panel.add(new JLabel("Puerto: "));
        panel.add(campoPuerto);

        int eleccion = JOptionPane.showConfirmDialog(null, panel,
                "Introduzca IP y puerto", JOptionPane.OK_CANCEL_OPTION);
        if (eleccion == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
        String[] resultado = {
            campoIP.getText(),
            campoPuerto.getText()
        };
        return resultado;
    }
}

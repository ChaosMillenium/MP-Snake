/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.Container;
import java.awt.GridLayout;
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
        JRadioButton selectManual = new JRadioButton("",true);
        JRadioButton selectAuto = new JRadioButton();
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(selectManual);
        grupo.add(selectAuto);
        JPanel panel = new JPanel();
        JPanel ipPuerto = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        ipPuerto.setLayout(new GridLayout(0, 1));
        ipPuerto.add(texto);
        ipPuerto.add(new JLabel("IP: "));
        ipPuerto.add(campoIP);
        ipPuerto.add(new JLabel("Puerto: "));
        ipPuerto.add(campoPuerto);
        ipPuerto.add(new JLabel("Modo automático: "));
        panel.add(ipPuerto);
        JPanel selector = new JPanel();
        selector.setLayout(new GridLayout(0, 2));
        selector.add(new JLabel("Manual: "));
        selector.add(selectManual);
        selector.add(new JLabel("Automático: "));
        selector.add(selectAuto);
        panel.add(selector);

        int eleccion = JOptionPane.showConfirmDialog(null, panel,
                "Introduzca IP y puerto", JOptionPane.OK_CANCEL_OPTION);
        if (eleccion == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
        String opcion;
        if (selectManual.isSelected()){
            opcion = "true";
        }
        else{
            opcion = "false";
        }
        String[] resultado = {
            campoIP.getText(),
            campoPuerto.getText(),
            opcion
        };
        return resultado;
    }
}

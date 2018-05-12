package Cliente;

import java.awt.GridLayout;
import javax.swing.*;

/**
 * Clase que envía una peticiín al cliente para poder conectarse al servidor.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class PeticionIPPuerto {

    /**
     * Solicita al cliente los datos necesarios para iniciar la conexión con el
     * servidor.
     *
     * @return Devuelve un array de String que contiene la IP, el puerto y
     * automático o manual.
     *
     * @throws NumberFormatException Los valores introducidos no son válidos.
     * @throws NullPointerException No se ha introducido ningún valor.
     */
    public static String[] pedirIPPuerto() throws NumberFormatException, NullPointerException {
        JTextField campoIP = new JTextField(3); //Se introducira la IP aquí
        JTextField campoPuerto = new JTextField(3); //Se introducira el puerto aquí
        JLabel texto = new JLabel("Introduzca la IP y puerto del servidor.");
        JRadioButton selectManual = new JRadioButton("", true); //Selector de modo manual, inicialmente activado
        JRadioButton selectAuto = new JRadioButton(); //Selector de modo automático
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(selectManual);
        grupo.add(selectAuto);

        JPanel panel = new JPanel(); //JPanel contenedor de todos los elementos
        JPanel ipPuerto = new JPanel(); //JPanel contenedor de los JPanels y JLabels asociados
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ipPuerto.setLayout(new GridLayout(0, 1));
        ipPuerto.add(texto);
        ipPuerto.add(new JLabel("IP: "));
        ipPuerto.add(campoIP);
        ipPuerto.add(new JLabel("Puerto: "));
        ipPuerto.add(campoPuerto);
        ipPuerto.add(new JLabel("Modo automático: "));
        panel.add(ipPuerto);

        JPanel selector = new JPanel(); //JPanel contenedor de los JButtons y JLabels asociados
        selector.setLayout(new GridLayout(0, 2));
        selector.add(new JLabel("Manual: "));
        selector.add(selectManual);
        selector.add(new JLabel("Automático: "));
        selector.add(selectAuto);
        panel.add(selector);

        int eleccion = JOptionPane.showConfirmDialog(null, panel,
                "Introduzca IP y puerto", JOptionPane.OK_CANCEL_OPTION);
        if (eleccion == JOptionPane.CANCEL_OPTION) { //Si el jugador elige no iniciar la conexión el programa se cierra.
            System.exit(0);
        }
        String opcion;
        if (selectManual.isSelected()) { //Si el modo seleccionado es manual.
            opcion = "true";
        } else { //Si el modo seleccionado es automático.
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

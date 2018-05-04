
package Servidor;

import javax.swing.JOptionPane;

public class ServidorMain {

    public static void main(String[] args) {
        ModeloJuego modelo = new ModeloJuego();
        ControladorServidor controlador = new ControladorServidor(modelo);

        modelo.addObserver(controlador);

        while (true) {
            try {
                int[] filasColumnas = PeticionFilasColumnas.pedirFilasColumnas();
                if ((filasColumnas[0] > 9) && (filasColumnas[1]) > 9) { //El tablero no puede ser menor de 10x10
                    modelo.setFilasColumnas(filasColumnas[0], filasColumnas[1]);
                    break;
                }
                else throw new NumberFormatException();
                
            } catch (NumberFormatException | NullPointerException ex) {
                int resultado = JOptionPane.showConfirmDialog(null, "Error al introducir los datos. ¿Reintentar?", "Error al introducir los datos", JOptionPane.OK_CANCEL_OPTION);
                if (resultado != JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        }
        controlador.iniciarServer();
        
    }
}

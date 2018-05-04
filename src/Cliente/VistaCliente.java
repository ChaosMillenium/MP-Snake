package Cliente;

import Utilidades.SelectorColor;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class VistaCliente extends javax.swing.JFrame implements Observer {

    private JPanel[][] grid; //Matriz filas x columnas
    private ControladorCliente controlador;
    private Map<Integer, LinkedList<JPanel>> serpientes;

    public VistaCliente(int filas, int columnas, ControladorCliente c) {
        initComponents();
        this.setTitle("Snake");
        //this.serpientes = new ArrayList<>();
        this.controlador = c;
        this.serpientes = new HashMap<>();
        this.setLayout(new GridLayout(filas, columnas));
        this.grid = new JPanel[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JPanel pixel = new JPanel();
                pixel.setBackground(Color.white);
                pixel.setBorder(BorderFactory.createLineBorder(Color.black));
                this.grid[i][j] = pixel;
                this.add(pixel);
            }
        }

        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.toFront();
        this.requestFocus();
        this.addKeyListener(new DetectorTeclas(this.controlador));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void update(Observable o, Object arg) {
        String mensaje = (String) arg;
        String[] msg = mensaje.split(";");
        switch (msg[0]) {
            case "TSR": {
                this.grid[Integer.parseInt(msg[2])][Integer.parseInt(msg[1])].setBackground(Color.orange);
                break;
            }
            case "ELJ": {
                try {
                    int id = Integer.parseInt(msg[1]);
                    LinkedList<JPanel> serpiente = this.serpientes.get(id);
                    for (JPanel panel : serpiente) {
                        panel.setBackground(Color.white);
                    }
                    this.serpientes.remove(id);
                } catch (NullPointerException e) {
                }
                break;

            }

            case "COI": {
                LinkedList<JPanel> serpiente = new LinkedList<>();
                int id = Integer.parseInt(msg[1]);
                for (int i = 2; i < msg.length; i = i + 2) {
                    int fila = Integer.parseInt(msg[i + 1]);
                    int columna = Integer.parseInt(msg[i]);
                    this.grid[fila][columna].setBackground(SelectorColor.generarColor(id));
                    serpiente.add(this.grid[fila][columna]);
                }
                this.serpientes.put(id, serpiente);
            }
            case "MOV": {
                try {
                    int id = Integer.parseInt(msg[1]);
                    LinkedList<JPanel> serpiente = this.serpientes.get(id);
                    int filaNueva = Integer.parseInt(msg[3]);
                    int columnaNueva = Integer.parseInt(msg[2]);
                    int filaEliminar = Integer.parseInt(msg[5]);
                    int columnaEliminar = Integer.parseInt(msg[4]);
                    this.grid[filaNueva][columnaNueva].setBackground(SelectorColor.generarColor(id));
                    this.grid[filaEliminar][columnaEliminar].setBackground(Color.white);
                    serpiente.remove(this.grid[filaEliminar][columnaEliminar]);
                    serpiente.addFirst(this.grid[filaNueva][columnaNueva]);
                } catch (NullPointerException ex) {
                    //Si llega movimiento de un jugador que no existe no pasa nada.
                }
                break;
            }
            case "FIN": {
                JOptionPane.showMessageDialog(this, "El servidor ha cerrado la conexión");
                //El controlador cierra la conexión y el programa
                break;
            }
            case "ERR": {
                JOptionPane.showMessageDialog(this, msg[1]);
                //El controlador cierra la conexión y el programa
                break;
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

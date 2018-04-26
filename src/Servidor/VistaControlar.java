package Servidor;

import Utilidades.SelectorColor;
import Cliente.ThreadEscucha;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class VistaControlar extends javax.swing.JFrame implements Observer{

    private ModeloJuego modelo;

    public VistaControlar(ModeloJuego observado) {
        initComponents();
        this.setTitle("Control");
        this.modelo = observado;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setBackground(Color.GRAY);
        this.setPreferredSize(new Dimension(250, 250));
        this.setVisible(true);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void update(Observable o, Object arg) {
        if (!((String) arg).isEmpty()) {
            String serpi = (String) arg;
            String[] parseado = serpi.split(";");
            if (parseado[0].equals("NJ")) {
                int id = Integer.parseInt(parseado[1]);
                JPanel jugador = crearPanelNuevoJugador(id);
                this.add(jugador);
                this.setVisible(true);
            }
        }
    }
    
    private JPanel crearPanelNuevoJugador(int id){
        JPanel jugador = new JPanel();
        JPanel panelNombre = new JPanel();
        panelNombre.setBackground(SelectorColor.generarColor(Integer.parseInt(Integer.toString(id))));
        JLabel lblJugador = new JLabel("Jugador: ");
        JLabel nombre = new JLabel(String.valueOf(id));
        JPanel panelAccion = new JPanel();
        panelAccion.setBackground(Color.orange);
        JButton eliminar = new JButton("Eliminar");
        ActionListener accionElim = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.eliminarJugador(id);
            }
        };
        eliminar.addActionListener(accionElim);
        panelNombre.add(lblJugador);
        panelNombre.add(nombre);
        panelAccion.add(eliminar);
        jugador.add(panelNombre);
        jugador.add(panelAccion);
        return jugador;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

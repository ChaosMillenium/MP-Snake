package Servidor;

import Utilidades.SelectorColor;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.*;

public class VistaConsolaServidor extends javax.swing.JFrame implements Observer {

    private ControladorServidor controlador;
    private Map<Integer, JPanel> jugadores;
    private JButton eliminarTodos;

    public VistaConsolaServidor(ControladorServidor observado) {
        initComponents();
        this.setTitle("Control");
        this.controlador = observado;
        this.jugadores = new HashMap<>();
        this.eliminarTodos = new JButton("Expulsar a todos");
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.eliminarTodos.setEnabled(true);
        this.eliminarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                expulsarTodos();
            }
        });
        this.add(this.eliminarTodos);
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
                JPanel jug = crearPanelNuevoJugador(id);
                this.pack();
                this.jugadores.put(id, jug);
                this.add(jug);
                this.setVisible(true);
            } else if (parseado[0].equals("CBR") || parseado[0].equals("COL") || parseado[0].equals("FIN")) {
                JPanel jug = this.jugadores.get(Integer.parseInt(parseado[1]));
                jug.setVisible(false);
                this.remove(this.jugadores.get(Integer.parseInt(parseado[1])));
            }
        }
    }

    private JPanel crearPanelNuevoJugador(int id) {
        JPanel menu = new JPanel();
        JPanel panelNombre = new JPanel();
        JPanel panelEliminar = new JPanel();

        panelNombre.setBackground(SelectorColor.generarColor(Integer.parseInt(Integer.toString(id))));
        panelEliminar.setBackground(SelectorColor.generarColor(Integer.parseInt(Integer.toString(id))));

        JLabel lblNombre = new JLabel(String.valueOf(id));
        JLabel lblJugador = new JLabel("Jugador: ");

        JButton eliminar = new JButton("Eliminar");
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ocultarPanel(id);
            }
        });

        panelNombre.add(lblJugador);
        panelNombre.add(lblNombre);

        panelEliminar.add(eliminar);

        menu.add(panelNombre);
        menu.add(panelEliminar);
        return menu;
    }

    public void ocultarPanel(int id) {
        this.controlador.finalizarJugador(id);
    }

    public void expulsarTodos() {
        if (!this.jugadores.isEmpty()) {
            Set<Integer> keys = this.jugadores.keySet();
            if (!keys.isEmpty()) {
                for (int i : keys) {
                    ocultarPanel(i);
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

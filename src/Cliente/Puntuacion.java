package Cliente;

import Utilidades.SelectorColor;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Puntuacion extends javax.swing.JFrame implements Observer, ActionListener {

    private ControladorCliente jugadorObs;
    private JButton desconectar;
    private int id;
    private Map<Integer, JLabel> puntuaciones; //Mapa para guardar las puntuaciones, la clave es el id de jugador

    public Puntuacion(ControladorCliente observado) {
        initComponents();
        this.puntuaciones = new HashMap<>();
        this.setTitle("Puntuaciones");
        this.jugadorObs = observado;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel menu = new JPanel();
        this.add(menu);
        this.desconectar = new JButton("Fin de partida");
        this.desconectar.setEnabled(true);
        this.desconectar.addActionListener(this);
        menu.add(this.desconectar);
        this.setBackground(Color.GREEN);
        this.setPreferredSize(new Dimension(250, 250));
        this.setVisible(true);
        this.setFocusableWindowState(false);
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
            if (parseado[0].equals("IDC") || parseado[0].equals("COI")) {
                if (parseado[0].equals("IDC")) {
                    int id = Integer.parseInt(parseado[1]);
                    this.id = id;
                }
                if (noExiste(id)) {
                    JPanel jugador = crearPanelNuevoJugador(id);
                    this.add(jugador);
                    this.setVisible(true);
                }
            } else if (parseado[0].equals("PTS")) {
                int id = Integer.parseInt(parseado[1]);
                long puntos = Long.parseLong(parseado[2]);
                JLabel puntuacion = this.puntuaciones.get(id);
                if (puntuacion != null) {
                    puntuacion.setText(String.valueOf(puntos));
                }
            } else if (parseado[0].equals("TAB")) {

            }
        }
    }

    private JPanel crearPanelNuevoJugador(int id) {
        JPanel jugador = new JPanel();
        jugador.setBackground(SelectorColor.generarColor(id));
        JPanel panelNombre = new JPanel();
        panelNombre.setBackground(Color.CYAN);
        JLabel lblJugador = new JLabel("Jugador: ");
        JLabel nombre = new JLabel(String.valueOf(id));
        JPanel panelPuntuacion = new JPanel();
        panelPuntuacion.setBackground(Color.orange);
        JLabel lblPuntos = new JLabel("Puntos: ");
        JLabel puntuacion = new JLabel("0");
        this.puntuaciones.put(id, puntuacion);
        panelNombre.add(lblJugador);
        panelNombre.add(nombre);
        panelPuntuacion.add(lblPuntos);
        panelPuntuacion.add(puntuacion);
        jugador.add(panelNombre);
        jugador.add(panelPuntuacion);
        return jugador;
    }

    private boolean noExiste(int id) {
        for (Integer idComparar : this.puntuaciones.keySet()) {
            if (id == idComparar) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.jugadorObs.cerrarConexion();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

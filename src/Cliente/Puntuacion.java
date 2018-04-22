package Cliente;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Puntuacion extends javax.swing.JFrame implements Observer {

    private ControladorCliente jugadorObs;
    private Map<Integer, JLabel> puntuaciones; //Mapa para guardar las puntuaciones, la clave es el id de jugador

    public Puntuacion(ControladorCliente observado) {
        initComponents();
        this.puntuaciones = new HashMap<>();
        this.setTitle("Puntuaciones");
        this.jugadorObs = observado;
        this.setLayout(new GridLayout(0, 1));
        this.setBackground(Color.GREEN);
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
            if (parseado[0].equals("IDC") || parseado[0].equals("COI")) {
                int id = Integer.parseInt(parseado[1]);
                JPanel jugador = new JPanel();
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
                this.add(jugador);
                this.setVisible(true);
            } else if (parseado[0].equals("PTS")) {
                int id = Integer.parseInt(parseado[1]);
                long puntos = Long.parseLong(parseado[2]);
                JLabel puntuacion = this.puntuaciones.get(id);
                if (puntuacion != null) {
                    puntuacion.setText(String.valueOf(puntos));
                }
            }
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

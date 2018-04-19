package Cliente;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Puntuacion extends javax.swing.JFrame implements Observer {
    
    private ControladorCliente jugadorObs;
    
    public Puntuacion(ControladorCliente observado) {
        initComponents();
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
        Serpiente serpi = (Serpiente) arg;
        int id = serpi.getId();
        long puntos = serpi.getPuntos();
        JPanel jugador = new JPanel();
        JLabel nombre = new JLabel();
        nombre.setText(String.valueOf(id));
        JLabel puntuacion = new JLabel();
        puntuacion.setText(String.valueOf(puntos));
        jugador.add(nombre);
        jugador.add(puntuacion);
        this.add(jugador);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

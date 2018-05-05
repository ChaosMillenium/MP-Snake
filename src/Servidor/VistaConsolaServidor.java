package Servidor;

import Utilidades.SelectorColor;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
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
        JPanel panelBoton = new JPanel();
        this.eliminarTodos = new JButton("Expulsar a todos");
        this.setMinimumSize(new Dimension(250, 250));
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.eliminarTodos.setEnabled(true);        //boton que expulsa a todos los jugadores del servidor
        this.eliminarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                expulsarTodos();
            }
        });
        panelBoton.add(this.eliminarTodos);
        this.eliminarTodos.setAlignmentY(this.eliminarTodos.CENTER_ALIGNMENT);
        this.add(panelBoton);
        this.setBackground(Color.GRAY);
        this.setVisible(true);
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
        if (!((String) arg).isEmpty()) {
            String serpi = (String) arg;
            String[] parseado = serpi.split(";");
            if (parseado[0].equals("NJ")) {
                //Cuando hay un nuevo jugador crea un nuevo panel
                int id = Integer.parseInt(parseado[1]);
                JPanel jug = crearPanelNuevoJugador(id);
                this.jugadores.put(id, jug);
                this.add(jug);
                this.revalidate();
                this.repaint();
                this.pack();
            } else if (parseado[0].equals("CBR") || parseado[0].equals("FIN")) {
                //Cuando un jugador se choca con un borde se elimia
                eliminarPanel((Integer.parseInt(parseado[1])));
            } else if (parseado[0].equals("COL")) {
                //Cuando un jugador se choca con otro jugador se borran
                eliminarPanel((Integer.parseInt(parseado[1])));
                eliminarPanel((Integer.parseInt(parseado[2])));
            }
        }
    }

    private JPanel crearPanelNuevoJugador(int id) {
        //Crea un nuevo panel para 1 jugador
        JPanel menu = new JPanel();                 //panel contenedor de todo
        JPanel panelNombre = new JPanel();          //panel contenedor de los JLabel
        JPanel panelEliminar = new JPanel();        //panel contenedor de boton

        panelNombre.setBackground(SelectorColor.generarColor(Integer.parseInt(Integer.toString(id))));
        panelEliminar.setBackground(SelectorColor.generarColor(Integer.parseInt(Integer.toString(id))));

        JLabel lblNombre = new JLabel(String.valueOf(id));
        JLabel lblJugador = new JLabel("Jugador: ");

        JButton eliminar = new JButton("Eliminar"); //boton que elimina un jugador
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

    public void eliminarPanel(int i) {
        //Elimina un panel de la vista, elimina tambien al jugador del mapa
        try{
        this.getContentPane().remove(this.jugadores.get(i));
        this.revalidate();
        this.repaint();
        this.jugadores.remove(i);}
        catch (NullPointerException e){
            System.err.println("Error: panel no encontrado al eliminar");
        }
    }

    public void ocultarPanel(int id) {
        //Avisa al controlador para que finaliza la conexion con ese cliente
        //eliminarPanel(id);
        this.controlador.finalizarJugador(id);
    }

    public void expulsarTodos() {
        //Finaliza la conexion con todos los clientes
        if (!this.jugadores.isEmpty()) {
            Set<Integer> keys = Collections.synchronizedSet(this.jugadores.keySet());
            if (!keys.isEmpty()) {
                synchronized (keys) {
                    for (int i : keys) {
                        ocultarPanel(i);
                    }
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

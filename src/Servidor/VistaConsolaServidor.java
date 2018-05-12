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

/**
 * Vista de la consola del servidor, que servirá para expulsar tanto jugadores
 * individuales como todos los del servidor.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class VistaConsolaServidor extends javax.swing.JFrame implements Observer {

    private ControladorServidor controlador; //controlador del servidor.
    private Map<Integer, JPanel> jugadores; //Mapa de jugadores en la vista.
    private JButton eliminarTodos; //Botón de eliminar a todos los jugadores.

    /**
     * Crea la vista en base al controlador del servidor.
     *
     * @param observado
     */
    public VistaConsolaServidor(ControladorServidor observado) {
        initComponents();
        this.setTitle("Control");
        this.controlador = observado;
        this.jugadores = new HashMap<>();
        JPanel panelBoton = new JPanel();
        this.eliminarTodos = new JButton("Expulsar a todos");
        this.setMinimumSize(new Dimension(250, 250));
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.eliminarTodos.setEnabled(true); //boton que expulsa a todos los jugadores del servidor
        this.eliminarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                expulsarTodos();
            }
        });
        panelBoton.add(this.eliminarTodos);
        this.eliminarTodos.setAlignmentY(JButton.CENTER_ALIGNMENT);
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
    
    /**
     * Decide el jugador a añadir o eliminar dependiendo del mensaje que le llegue del controlador.
     * @param o Controlador que envía la actualización.
     * @param arg Mensaje que llega del controlador.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!((String) arg).isEmpty()) {
            String serpi = (String) arg;
            String[] parseado = serpi.split(";");
            switch (parseado[0]) {
                case "NJ":
                    //Cuando hay un nuevo jugador crea un nuevo panel
                    int id = Integer.parseInt(parseado[1]);
                    JPanel jug = crearPanelNuevoJugador(id);
                    this.jugadores.put(id, jug);
                    this.add(jug);
                    this.revalidate();
                    this.repaint();
                    this.pack();
                    break;
                case "CBR":
                case "FIN":
                    //Cuando un jugador se choca con un borde se elimina.
                    eliminarPanel((Integer.parseInt(parseado[1])));
                    break;
                case "COL":
                    //Cuando un jugador se choca con otro jugador se borran.
                    eliminarPanel((Integer.parseInt(parseado[1])));
                    eliminarPanel((Integer.parseInt(parseado[2])));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Crea un panel para un nuevo jugador.
     * @param id ID del jugador.
     * @return Panel creado.
     */
    private JPanel crearPanelNuevoJugador(int id) {
        //Crea un nuevo panel para e1 jugador.
        JPanel menu = new JPanel();                 //panel contenedor de todo
        JPanel panelNombre = new JPanel();          //panel contenedor de los JLabel
        JPanel panelEliminar = new JPanel();        //panel contenedor de boton

        //Se crea un panel con el mismo color que el jugador.
        //Destacar que este color se obtiene del paquete utilidades, así que entre distintas versiones de cliente y servidor
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
    /**
     * Elimina un panel de la vista, elimina tambien al jugador del mapa
     * @param i ID del jugador.
     */
    private void eliminarPanel(int i) {
        
        try {
            this.getContentPane().remove(this.jugadores.get(i));
            this.revalidate();
            this.repaint();
            this.jugadores.remove(i);
        } catch (NullPointerException e) {
            System.err.println("Error: panel no encontrado al eliminar");
        }
    }
    /**
     * Avisa al controlador para que finaliza la conexion con ese cliente.
     * @param id ID del jugador.
     */
    private void ocultarPanel(int id) {
        this.controlador.finalizarJugador(id);
    }

    /**
     * Finaliza la conexion con todos los clientes
     */
    private void expulsarTodos() {
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

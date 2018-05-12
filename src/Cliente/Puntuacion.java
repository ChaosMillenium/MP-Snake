package Cliente;

import Utilidades.SelectorColor;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * 
 * 
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class Puntuacion extends javax.swing.JFrame implements Observer, ActionListener {
    private ControladorCliente jugadorObs;      //Controlador asociado
    private JButton desconectar;                //Boton para desconectarse del servidor
    private int id;                             //Id de la vista
    private Map<Integer, JLabel> puntuaciones;  //Mapa para guardar las puntuaciones, la clave es el id de jugador
    private Map<Integer, JPanel> jugadores;     //Mapa para guardar los paneles de los jugadores

    public Puntuacion(ControladorCliente observado) {
        initComponents();
        this.puntuaciones = new HashMap<>();
        this.jugadores = new HashMap<>();
        this.setTitle("Puntuaciones");
        this.jugadorObs = observado;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel menu = new JPanel();
        this.desconectar = new JButton("Fin de partida");
        this.desconectar.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.desconectar.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.desconectar.setEnabled(true);
        this.desconectar.addActionListener(this);
        menu.add(this.desconectar);
        this.add(menu);
        this.setMinimumSize(new Dimension(250, 250));
        this.setVisible(true);
        this.setFocusableWindowState(false);
        this.pack();
        //Seleccionamos un box layout en el que el boton desconectar se encuentra en la parte superior de la vista
        //Conforme se vayan conectando mas jugadores se añadiran a la vista
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * Método que se actualiza cada vez que se llama la vista puntuación
 * @param o es el objeto que te envía
 * @param arg es el mensaje
 */
    @Override
    public void update(Observable o, Object arg) {
        if (!((String) arg).isEmpty()) {
            String serpi = (String) arg;
            String[] parseado = serpi.split(";");
            if (parseado[0].equals("IDC") || parseado[0].equals("COI")) {
                //Cuando llega una nueva serpiente
                int id = Integer.parseInt(parseado[1]);
                if (parseado[0].equals("IDC")) {
                    //Solo llega un IDC
                    this.id = id;
                }
                if (noExiste(id)) {
                    //Se añaden los datos del jugador a la vista(id + puntos)
                    JPanel jugador = crearPanelNuevoJugador(id);
                    this.add(jugador);
                    this.jugadores.put(id, jugador);
                    this.revalidate();
                    this.repaint();
                }
            } else if (parseado[0].equals("PTS")) {
                //Cambia los puntos indicados a la serpiente indicada
                int id = Integer.parseInt(parseado[1]);
                long puntos = Long.parseLong(parseado[2]);
                JLabel puntuacion = this.puntuaciones.get(id);
                if (puntuacion != null) {
                    puntuacion.setText(String.valueOf(puntos));
                }
            } else if (parseado[0].equals("ELJ")) {
                //Elimina el jugador indicado
                int id = Integer.parseInt(parseado[1]);
                JPanel jgdr = this.jugadores.get(id);
                try {
                    this.getContentPane().remove(jgdr);
                    this.revalidate();
                    this.repaint();
                    this.jugadores.remove(id);
                } catch (NullPointerException ex) {
                    System.err.println("No existe");
                }
            }
        }
    }
/**
 * Crea la vista de los paneles
 * @param id identificador de usuario
 * @return devuelve el panel de un jugador nuevo con su puntuación cada vez que se llama a la función
 */
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
        //
        jugador.add(panelNombre);
        jugador.add(panelPuntuacion);
        //añadimos estos paneles al final, uno único, que será el que devuelva esta función
        return jugador;
    }
/**
 * probamos que el id sea válido
 * @param id identificador de usuario
 * @return booleano en caso afirmativo true de que sea un id válido
 */
    private boolean noExiste(int id) {
        for (Integer idComparar : this.puntuaciones.keySet()) {
            if (id == idComparar) {
                return false;
            }
        }
        return true;
    }
/**
 * cierra la conexión del jugador al que le afecta el botón
 * @param e el evento del botón en el panel nuevo jugador
 */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.jugadorObs.cerrarConexion();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

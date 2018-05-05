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
    private JPanel[][] grid;                            //Matriz filas x columnas
    private int id;                                     //Id de este cliente
    private ControladorCliente controlador;             //Controlador de la vista
    private Map<Integer, LinkedList<JPanel>> serpientes;//Mapa de las serpientes con key su id

    public VistaCliente(int filas, int columnas, ControladorCliente c, int id) {
        initComponents();
        this.id = id;
        this.setTitle("Snake");
        this.controlador = c;
        this.serpientes = new HashMap<>();
        this.setLayout(new GridLayout(filas, columnas));
        this.grid = new JPanel[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                //Rellenados el grid de paneles blancos con el borde negro
                JPanel pixel = new JPanel();
                pixel.setBackground(Color.white);
                pixel.setBorder(BorderFactory.createLineBorder(Color.black));
                this.grid[i][j] = pixel;
                //Añadimos cada panel al jFrame
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
        //Añadimos el KeyListener al jFrame
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
        String msg = (String) arg;
        String[] parseado = msg.split(";");
        switch (parseado[0]) {
            case "TSR": {
                //Cada vez que se ñade un tesoro lo pintamos de naranja
                this.grid[Integer.parseInt(parseado[2])][Integer.parseInt(parseado[1])].setBackground(Color.orange);
                break;
            }
            case "ELJ": {
                //Cuando recibe un mensaje de eliminar serpiente viene acompañado de la Id a eliminar
                //Con el mapa elimamos la serpiente
                //Hemos decidido que la vista del cliente se encargue de esto en lugar de mandar cada coordenada desde el modelo por peligro de desincronizacion
                try {
                    int id = Integer.parseInt(parseado[1]);
                    LinkedList<JPanel> serpiente = this.serpientes.get(id);
                    for (JPanel panel : serpiente) {
                        panel.setBackground(Color.white);
                    }
                    this.serpientes.remove(id);
                } catch (NullPointerException e) {
                    //No debe pasar nada
                }
                break;
            }
            case "COI": {
                //Mensaje de nueva serpiente
                LinkedList<JPanel> serpiente = new LinkedList<>();
                int id = Integer.parseInt(parseado[1]);
                //Añade al mapa de una serpiente nueva
                for (int i = 2; i < parseado.length; i = i + 2) {
                    //Pinta la serpiente de un color en funcion de su id
                    int fila = Integer.parseInt(parseado[i + 1]);
                    int columna = Integer.parseInt(parseado[i]);
                    this.grid[fila][columna].setBackground(SelectorColor.generarColor(id));
                    if (id == this.id) {
                        //Si se trata de nuestra serpiente le pinta un borde amarillo para poder distinguirla
                        this.grid[fila][columna].setBorder(BorderFactory.createLineBorder(Color.yellow));    
                    }
                    serpiente.add(this.grid[fila][columna]);
                }
                this.serpientes.put(id, serpiente);
            }
            case "MOV": {
                try {
                    //Genera las nuevas y  viejas, filas y columnas
                    int id = Integer.parseInt(parseado[1]);
                    LinkedList<JPanel> serpiente = this.serpientes.get(id);
                    int filaNueva = Integer.parseInt(parseado[3]);
                    int columnaNueva = Integer.parseInt(parseado[2]);
                    int filaEliminar = Integer.parseInt(parseado[5]);
                    int columnaEliminar = Integer.parseInt(parseado[4]);
                    this.grid[filaNueva][columnaNueva].setBackground(SelectorColor.generarColor(id));
                    this.grid[filaEliminar][columnaEliminar].setBackground(Color.white);
                    //Pinta la nueva de su color y la vieja de blanco
                    if (id == this.id) {
                        //Si el id conincide con el de la vista le añade un borde amarillo y vuelve a poner borde negro al bloque viejo
                        this.grid[filaNueva][columnaNueva].setBorder(BorderFactory.createLineBorder(Color.yellow));
                        this.grid[filaEliminar][columnaEliminar].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    }
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
                JOptionPane.showMessageDialog(this, parseado[1]);
                //El controlador cierra la conexión y el programa
                break;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

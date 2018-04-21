package Cliente;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class VistaCliente extends javax.swing.JFrame implements Observer {

    private JPanel[][] grid; //Matriz filas x columnas
    private ControladorCliente controlador;

    public VistaCliente(int filas, int columnas, ControladorCliente c) {
        initComponents();
        this.setTitle("Snake");
        //this.serpientes = new ArrayList<>();
        this.controlador = c;
        this.setLayout(new GridLayout(filas, columnas));
        this.grid = new JPanel[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JPanel pixel = new JPanel();
                pixel.setBackground(Color.white);
                //pixel.setOpaque(true);
                pixel.setBorder(BorderFactory.createLineBorder(Color.black));
                this.grid[i][j] = pixel;
                this.add(pixel);
            }
        }
        
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.grid[10][10].setBackground(Color.blue);
        
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
        /*if(this.grid[11][11] == null){
            System.out.println("nulo");
        }else{
        this.grid[11][11].setBackground(Color.red);
        this.grid[12][12].setBackground(Color.black);
        }*/
        String mensaje = (String) arg;
        String[] msg = mensaje.split(";");
        //this.grid[11][11].setBackground(Color.red);
        switch(msg[0]){
            case "TAB":{
                //this.grid[11][11].setBackground(Color.red);
                this.grid = new JPanel[Integer.parseInt(msg[1])][Integer.parseInt(msg[2])];
                break;
            }
            case "TSR":{
                    this.grid[Integer.parseInt(msg[1])][Integer.parseInt(msg[2])] = JPanel();

                    this.grid[Integer.parseInt(msg[1])][Integer.parseInt(msg[2])].setBackground(Color.yellow);
                
               
                break;
            }
            case "ELJ":{
                for(int i = 2; i < msg.length; i+=2){
                    this.grid[Integer.parseInt(msg[1])][Integer.parseInt(msg[2])] = new JPanel();
                    this.grid[Integer.parseInt(msg[1])][Integer.parseInt(msg[2])].setBackground(Color.white);
                }
                //jugador eliminado terminar hilo?
                break;
            }
            case "PTS":{
                //no hacer nada, se ocupa la otra vista
                break;
            }
            case "COI":{
                for(int i = 2;i < msg.length; i=i+2){
                    this.grid[Integer.parseInt(msg[i])][Integer.parseInt(msg[i+1])] = new JPanel();
                    this.grid[Integer.parseInt(msg[i])][Integer.parseInt(msg[i+1])].setBackground(Color.red);
                }
            }
            case "MOV":{
                this.grid[Integer.parseInt(msg[2])][Integer.parseInt(msg[3])] = new JPanel();
                this.grid[Integer.parseInt(msg[2])][Integer.parseInt(msg[3])].setBackground(Color.red);
                this.grid[Integer.parseInt(msg[4])][Integer.parseInt(msg[5])] = new JPanel();
                this.grid[Integer.parseInt(msg[4])][Integer.parseInt(msg[5])].setBackground(Color.white);
                break;
            }
            case "FIN":{
                JOptionPane.showInputDialog("Fin de partida");
                break;
            }
            case "ERR":{
                JOptionPane.showInputDialog(msg[1]);
                break;
            }
            default :{
                JOptionPane.showInputDialog("Error");
                break;
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

package Cliente;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

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
                pixel.setBorder(BorderFactory.createLineBorder(Color.black));
                this.grid[i][j] = pixel;
                this.add(pixel);
            }
        }
        
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new DetectorTeclas(this.controlador));
        
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
        String mensaje = (String) arg;
        String[] msg = mensaje.split(";");
        switch(msg[0]){
            case "TSR":{
                this.grid[Integer.parseInt(msg[2])][Integer.parseInt(msg[1])].setBackground(Color.orange);
                break;
            }
            case "ELJ":{
                for(int i = 2; i < msg.length; i+=2){
                    this.grid[Integer.parseInt(msg[2])][Integer.parseInt(msg[1])].setBackground(Color.white);
                }
                break;
            }
            case "COI":{
                for(int i = 2;i < msg.length; i=i+2){
                    this.grid[Integer.parseInt(msg[i+1])][Integer.parseInt(msg[i])].setBackground(Color.red);
                }
            }
            case "MOV":{
                /*if(this.grid[Integer.parseInt(msg[3])][Integer.parseInt(msg[2])].getBackground().equals(Color.orange)){
                    switch (msg[6]) {
                        case "ARRIBA":
                            try{
                                this.grid[Integer.parseInt(msg[3])][Integer.parseInt(msg[2])+1].setBackground(Color.red);
                            }catch (ArrayIndexOutOfBoundsException ex){
                                //no se debe hacer nada, no es que falte codigo
                            }
                            break;
                        case "ABAJO":
                            try{
                                this.grid[Integer.parseInt(msg[3])][Integer.parseInt(msg[2])-1].setBackground(Color.red);
                            }catch (ArrayIndexOutOfBoundsException ex){
                                //no se debe hacer nada, no es que falte codigo
                            }
                            break;
                        case "IZQ":
                            try{
                                this.grid[Integer.parseInt(msg[3])-1][Integer.parseInt(msg[2])].setBackground(Color.red);
                            }catch (ArrayIndexOutOfBoundsException ex){
                                //no se debe hacer nada, no es que falte codigo
                            }
                            break;
                        case "DER":
                            try{
                                this.grid[Integer.parseInt(msg[3])+1][Integer.parseInt(msg[2])].setBackground(Color.red);
                            }catch (ArrayIndexOutOfBoundsException ex){
                                //no se debe hacer nada, no es que falte codigo
                            }
                            break;
                        case "NULL":
                            //Esto solo dberia poder pasar si al ser creado hay un tesoro justo enfrente nuestra
                            //Como la direccion a la que nos dirigimos por defecto es haci arriba se utiliza el mismo codigo
                            System.out.println("Error MOV tesoro comido"); //test si pasa alguna vez
                            try{
                                this.grid[Integer.parseInt(msg[3])][Integer.parseInt(msg[2])+1].setBackground(Color.red);
                            }catch (ArrayIndexOutOfBoundsException ex){
                                //no se debe hacer nada, no es que falte codigo
                            }
                            break;
                        default:
                            break;
                    }
                }*/
                this.grid[Integer.parseInt(msg[3])][Integer.parseInt(msg[2])].setBackground(Color.red);
                this.grid[Integer.parseInt(msg[5])][Integer.parseInt(msg[4])].setBackground(Color.white);
                break;
            }
            case "FIN":{
                JOptionPane.showMessageDialog(this, "El servidor ha cerrado la conexión");
                //El controlador cierra la conexión y el programa
                break;
            }
            case "ERR":{
                JOptionPane.showMessageDialog(this, msg[1]);
                //El controlador cierra la conexión y el programa
                break;
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

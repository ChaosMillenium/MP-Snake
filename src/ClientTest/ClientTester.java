/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import Utilidades.ConstructorMensajes;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author millenium
 */
public class ClientTester {
    public static void main(String[] args) throws IOException{
        try (Socket socket = new Socket("127.0.0.1",8000)) {
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String input;
            ArrayList<String> mensajes = new ArrayList<>();
            while (!(input = read.readLine()).equals("TSP;3")){
                System.out.println(input);
                mensajes.add(input);
            }
            String ID=mensajes.get(0);
            String[] IDparseado = ID.split(";");
            output.writeBytes(ConstructorMensajes.dir("IZQ", Integer.parseInt(IDparseado[1])));
            socket.close();
        }
    }
}
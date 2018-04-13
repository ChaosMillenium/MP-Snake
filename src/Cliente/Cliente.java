/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author danie
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            Socket socket = new Socket(ipServer, puertoServer);
            DataOutputStream streamToServer = new DataOutputStream(socket.getOutputStream());
            InputStreamReader streamFromServer = new InputStreamReader(socket.getInputStream());
            BufferedReader serverInput = new BufferedReader(streamFromServer);
            
            
            
            
        }
        
        
        
        
        
        
    }  
}

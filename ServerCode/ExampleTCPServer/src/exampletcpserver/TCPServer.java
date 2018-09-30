package exampletcpserver;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TCPServer extends Thread {
    
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static Connection dbConnection;
    
    public static void main(String[] args) {
        try{
            serverSocket = new ServerSocket(9998);
            System.out.print("Server is listening... \n");
            connectToDatabase();
            
            while(true){
                
                clientSocket = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String receivedMessage = br.readLine();
                System.out.print("Message from Android device: " + receivedMessage + "\n");
                
                DatabaseThread db = new DatabaseThread(dbConnection, receivedMessage);
                db.start();
                
                clientSocket.close();
            }
        } catch(Exception ex){}
    }
    
    private static void connectToDatabase() throws ClassNotFoundException, SQLException{
       
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        dbConnection = DriverManager.getConnection(
                         "jdbc:derby://localhost:1527/EMAPP",
                         "morel",
                         "1234");
        System.out.println("Connected to Database.");
    }
}

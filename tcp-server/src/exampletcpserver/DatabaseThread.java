package exampletcpserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseThread extends Thread{
        
    private static Connection dbcon;
    private String msg;
    
    public DatabaseThread(Connection dbcon, String msg){
        this.msg = msg;
        this.dbcon = dbcon;
    }
 
    @Override
    public void run(){
        
        try{
            String cmd = "INSERT INTO UNSAFE " + "VALUES ('" + msg + "')";
            String cmdu = "UPDATE UNSAFE";
            
            Statement statement = dbcon.createStatement();
            statement.executeUpdate(cmd);
            statement.executeUpdate(cmdu);
            statement.close();
            
            System.out.println("Executed a SQL command.");
        } catch(SQLException sql){}    
    }
    
   
}

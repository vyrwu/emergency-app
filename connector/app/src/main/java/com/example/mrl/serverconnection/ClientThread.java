package com.example.mrl.serverconnection;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
    private String msg;

    public ClientThread(String msg){
        this.msg = msg;
    }
    @Override
    public void run(){
        try
        {
            Socket socket = new Socket();

            int portNumber = 9998;
            InetAddress addr = InetAddress.getByName("192.168.1.217"); //192.168.1.10

            InetSocketAddress isa = new InetSocketAddress(addr,portNumber);
            socket.connect(isa);

            PrintStream pr = new PrintStream(socket.getOutputStream());

            String tmp = msg;
            pr.println(tmp);
            socket.close();
        }
        catch(Exception e){

        }
    }
}

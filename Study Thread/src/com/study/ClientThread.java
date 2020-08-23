package com.study;

import java.net.Socket; 
import java.io.IOException; 
import java.io.BufferedReader; 
import java.io.InputStreamReader; 

public class ClientThread implements Runnable
{ 
    Socket socket = null; 
    public ClientThread(Socket socket) { 
        this.socket = socket; 
    } 
    public void run() { 
        if(socket!=null) { 
            BufferedReader br = null; 
            try{ 
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String str = br.readLine(); 
                while(str!=null) { 
                    System.out.println(str); 
                    str = br.readLine(); 
                } 
                System.out.println(">>> Client Thread end"); 
            }catch(IOException ioe){ 
    //            ioe.printStackTrace(); 
            }finally{ 
                if(br!=null) { 
                    try{br.close();}catch(IOException ioe){} 
                } 
                if(socket!=null){ 
                    try{socket.close();}catch(IOException ioe){} 
                } 
            } 
        } 
        else { 
            System.out.println("Client Socket is null"); 
        } 
    } 
} 


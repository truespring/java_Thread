package com.study;
import java.net.*; 
import java.io.*; 

public class SimpleClient {
    public static String enterName(BufferedReader br, PrintWriter out) {
        String str = null; 
        try{ 
            System.out.print("대화명 입력: "); 
            str = br.readLine(); 
            out.println(str); 
        }catch(IOException ioe) { 
            ioe.printStackTrace(); 
        } 
        return str; 
    }
    public static void main(String[] args) {
        Socket socket = null; 
        BufferedReader keyboard = null; 
        PrintWriter out = null; 

        try{ 
            // 1. 서버와 연결 작업을 처리 - socket 생성 
            socket = new Socket("127.0.0.1", 5000); 

            // 2. 서버가 보낸 글을 읽는 ClientThread 객체 생성, 실행 
            ClientThread ct = new ClientThread(socket); 
            Thread t = new Thread(ct); 
            t.start(); 

            // 3. 키보드로 부터 글을 읽고 서버에 전송. 
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            enterName(keyboard, out); 
            String str = keyboard.readLine(); 
            while(str!=null){ 
                out.println(str); // 서버 전송 
                str = keyboard.readLine(); // 키보드에서 읽기 
            } 
        }catch(IOException ioe){ 
            ioe.printStackTrace(); 
        }finally{ 
            // 4. 스트림과 소켓의 연결을 끊는다. 
            if(keyboard!=null) { 
                try{keyboard.close();}catch(IOException ioe){} 
            } 
            if(out!=null) {
                out.close(); 
            } 
            if(socket!=null) { 
                try{socket.close();}catch(IOException ioe){} 
            } 
        } 
    } 
} 
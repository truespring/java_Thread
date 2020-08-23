package com.study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
	private String clientID = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter out = null;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void joinClient() {
		try {
			clientID = br.readLine();
			if (clientID == null || clientID.equals(" ")) {
				clientID = "손님_" + Math.random();
			}
		} catch (IOException ioe) {
		} finally {
			SimpleServer.addClient(this);

			String msg = SimpleServer.msgKey + clientID + "님이 입장하셨습니다.";
			SimpleServer.broadCasting(msg);
			System.out.println(msg);
		}
	}

	public void exitClient() {
		SimpleServer.removeClient(this);

		String msg = SimpleServer.msgKey + clientID + "님이 퇴장하셨습니다.";
		SimpleServer.broadCasting(msg);
		System.out.println(msg);
	}

	// 클라이언트와 연결된 출력스트림을 통해 출력하는 메소드
	public void sendMessage(String message) {
		out.println(message);
	}

	public void run(){         
        try{ 
            //1. 소켓을 통해 스트림 생성(Input, Output) 
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            joinClient(); 

            //2. 클라이언트가 보낸 글을 읽어 들인다. 
            String str = br.readLine(); 
            String strResult = null; 
            while(str!=null) { 
                //3. 클라이언트가 보낸 글이 있는 동안 SimpleServer의 broadCasting()메소드를 통해 연결된 모든 클라이언트들에게 글을 전송한다. 
                strResult = SimpleServer.broadCasting(clientID, str); 
                System.out.println(strResult); 
                str = br.readLine(); 
            } 
        }catch (IOException e) { 
//                e.printStackTrace(); 
        }finally{ 
            //4. io, socket 연결을 종료한다. 
            if(br!=null) { 
                try{br.close();}catch(IOException ioe){} 
            } 
            if(out!=null) {
                out.close(); 
            } 
            if(socket!=null) { 
                try{socket.close();}catch(IOException ioe){} 
            } 

            //5. SimpleServer의 removeClient() 를 통해 자기 자신을 대화대상에서 제거 한다. 
            exitClient(); 

        }//end of finally 
    }
}

package com.study;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleServer {
	public static final String msgKey = "알림) ";
//연결된 클라이언트들의 정보를 저장하는 ArrayList - 대화대상목록(List) 
	private static ArrayList<ServerThread> list = new ArrayList<ServerThread>();

//private static HashMap<String, ServerThread> map = new HashMap<String, ServerThread>(); 
//클라이언트를 대화대상목록에서 제거하는 메소드 
//ServerThread에서 클라이언트와 연결이 종료될 때 호출한다. 
	public static void removeClient(ServerThread st) {
		list.remove(st);
	}

//연결한 클라이언트를 대화대상목록에 추가하는 메소드 
//클라이언트와 연결될때 호출된다. 
	public static void addClient(ServerThread st) {
		list.add(st);
	}

//대화대상목록의 모든 클라이언트들에게 인수로 받은 글을 전송하는 메소드 
//클라이언트가 보낸 메세지(글)이 있을때 ServerThread로 부터 호출된다. 
	public static void broadCasting(String message) {
		ServerThread st = null;
		for (int i = 0; i < list.size(); i++) {
			st = list.get(i);
			st.sendMessage(message);
		}
	}

	public static String broadCasting(String clientID, String message) {
		String sendMSG = clientID + ": " + message;
		ServerThread st = null;
		for (int i = 0; i < list.size(); i++) {
			st = list.get(i);
			st.sendMessage(sendMSG);
		}

		return sendMSG;
	}

	public static void main(String args[]) {
		ServerSocket ss = null;
		Socket socket = null;
		int i = 0;
		try {
			System.out.println("<Lee Wan-Geun's Simple Chatting Server>");
			// 1. server 소켓생성
			ss = new ServerSocket(5000);

			while (true) {
				// 2. 클라이언트의 연결을 무한 반복하며 기다린다.
				System.out.println(msgKey + "클라이언트 연결을 대기 중입니다.");
				socket = ss.accept();

				// 3. 클라이언트가 연결되면 ServerThread를 생성한 뒤 Thread로 실행시킨다.
				ServerThread st = new ServerThread(socket);
				Thread t = new Thread(st);
				t.start();

				// 4. 연결된 클라이언트의 정보(ServerThread)를 대화대상목록(ArrayList)에 추가 시킨다.'
				// ServerThread에서 추가
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ioe) {
				}
			}
		}
	}// end of method
}

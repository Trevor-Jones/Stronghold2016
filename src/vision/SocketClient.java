package vision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import config.VisionConfig;

public class SocketClient {
	public Socket visionSocket;
	PrintWriter out;
	BufferedReader in;
	boolean lostConnection = false;
	int updateNumber = 0;
	
	String xml = "";
	
	public boolean getSocketStatus() {
		try {
			return lostConnection ? lostConnection : visionSocket.isConnected();			
		} catch (Exception e) {
			return false;
		}
	}
	
	public void initSockets() {
		try {
			visionSocket = new Socket(VisionConfig.hostName, VisionConfig.port);
			out = new PrintWriter(visionSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));	
			out.println("connected");
			
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		try{
			out.println(updateNumber);
			updateNumber++;
//			if(in.readLine() != null)
				xml = in.readLine();
//			else 
//				lostConnection = true;
		} catch (IOException e) {
			lostConnection = true;
			System.out.println("ioexception");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		}
	}
	
	public String getXML(){
		return xml;
	}
}

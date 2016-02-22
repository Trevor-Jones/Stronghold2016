package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import config.VisionConfig;

public class SocketClient {
	Socket visionSocket;
	PrintWriter out;
	BufferedReader in;
	
	String xml;
	
	public boolean getSocketStatus() {
		return visionSocket.isConnected();
	}
	
	public void initSockets() {
		try {
			visionSocket = new Socket(VisionConfig.hostName, VisionConfig.port);
			out = new PrintWriter(visionSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));
			
			
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		try{
			xml = in.readLine();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getXML(){
		return xml;
	}
}

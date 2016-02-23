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
	boolean lostConnection = false;
	
	String xml;
	
	public boolean getSocketStatus() {
		return lostConnection ? lostConnection : visionSocket.isConnected();
	}
	
	public void initSockets() {
		try {
			visionSocket = new Socket(VisionConfig.hostName, VisionConfig.port);
			out = new PrintWriter(visionSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));
			
			
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
			if(in.readLine() != null)
				xml = in.readLine();	
			else 
				lostConnection = true;
		} catch (IOException e) {
			lostConnection = true;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getXML(){
		return xml;
	}
}

package vision;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import config.VisionConfig;
import util.SocketClient;

public class Vision {
	private XMLParser xmlParser = new XMLParser();
	private VisionStruct vs;
	public SocketClient socket = new SocketClient();
	
	public Vision() {
		socket.initSockets();
	}
	
	public double getDistance(int goalNumber) {
		return vs.goals[goalNumber].distance;
	}
	
	public double getRotation(int goalNumber) {
		return vs.goals[goalNumber].rotation;
	}
	
	public double getTranslation(int goalNumber) {
		return vs.goals[goalNumber].translation;
	}
	
	public double getArea(int goalNumber) {
		return vs.goals[goalNumber].area;
	}
	
	public int getHighestArea() {
		int goalNumber = 0;
		for(int i = 1; i < vs.goals.length; i++) {
			if(vs.goals[i].area > vs.goals[i-1].area){
				goalNumber = i;
			}
		}
		return goalNumber;
	}
	
	public void update() {
		vs = xmlParser.parseString(socket.getXML());
	}
}

package core;

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
	Goal[] goals;
	public SocketClient socket = new SocketClient();
	
	public double getDistance(int goalNumber) {
		return goals[goalNumber].distance;
	}
	
	public double getRotation(int goalNumber) {
		return goals[goalNumber].rotation;
	}
	
	public double getTranslation(int goalNumber) {
		return goals[goalNumber].translation;
	}
	
	public double getArea(int goalNumber) {
		return goals[goalNumber].area;
	}
	
	public int getHighestArea() {
		int goalNumber = 0;
		for(int i = 1; i < goals.length; i++) {
			if(goals[i].area > goals[i-1].area){
				goalNumber = i;
			}
		}
		return goalNumber;
	}
	
	public void parseString(String xml) {
		try {
			Document doc = stringToDoc(xml);
			doc.getDocumentElement().normalize();
					
			NodeList nList = doc.getElementsByTagName("goal");
			goals = new Goal[nList.getLength()];
			
			for (int i = 0; i < nList.getLength(); i++) {	
				Node nNode = nList.item(i);
	
				Element eElement = (Element) nNode;
				
				goals[i].translation = Integer.parseInt(eElement.getAttribute("translation"));
				goals[i].rotation = Integer.parseInt(eElement.getAttribute("rotation"));
				goals[i].distance = Integer.parseInt(eElement.getAttribute("distance"));
				goals[i].area = Integer.parseInt(eElement.getAttribute("area"));
			
		    }
		} catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	public void update() {
		parseString(socket.getXML());
	}
	
	public static Document stringToDoc(String xml) throws Exception {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}

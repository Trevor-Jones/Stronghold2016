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

import config.ShooterConfig;
import config.VisionConfig;
import core.RobotCore;
import util.PID;
import util.SocketClient;
import util.XMLParser;

public class VisionCore {
	private XMLParser xmlParser = new XMLParser();
	public VisionStruct vs;
	public SocketClient socket = new SocketClient();
	
	PID turnPID = new PID(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);

		public VisionCore(RobotCore core) {
		socket.initSockets();
		
		
		}
	
	
	
	public void updatePID(int wantGoal){
		turnPID.update(this.vs.getRotation(wantGoal), 0);
	}
	
	public double[]  getTurnPID(){
		double[] d = new double[2];
		
		d[0] = turnPID.getOutput();
		d[1] = -1 * turnPID.getOutput();
	
		return d;
	}
	
	public void update() {
		vs = xmlParser.parseString(socket.getXML());
	}
}

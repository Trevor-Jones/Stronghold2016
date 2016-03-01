package vision;

import config.ShooterConfig;
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
	
	public double  getTurnPID(){
		return turnPID.getOutput();
	}
	
	public void update() {
		vs = xmlParser.parseString(socket.getXML());
	}
}

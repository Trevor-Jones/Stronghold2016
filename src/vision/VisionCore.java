package vision;

import config.ShooterConfig;
import core.RobotCore;
import util.PID;
import util.SocketClient;
import util.XMLParser;

public class VisionCore {
	private XMLParser xmlParser = new XMLParser();
	public VisionStruct vs = new VisionStruct();
	public SocketClient socket = new SocketClient();
	
	PID turnPID = new PID(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);

	public VisionCore(RobotCore core) {	
	}
	
	public void updateTurnPID(int wantGoal){
		turnPID.update(vs.getRotation(wantGoal), 0);
	}
	
	public double getTurnPID(){
		return turnPID.getOutput();
	}
	
	public void startTurnPID(){
		turnPID.start();
	}
	
	public void resetTurnPID(){
		turnPID.stop();
		turnPID.reset();
	}
	
	public void update() {
		socket.update();
		vs = xmlParser.parseString(socket.getXML());
	}
}

package vision;

import config.ShooterConfig;
import config.VisionConfig;
import core.RobotCore;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import util.PID;

public class VisionCore {
	private XMLParser xmlParser = new XMLParser();
//	public NetworkTable table;
	public VisionStruct vs = new VisionStruct();
//	private String emptyXML = "<?xml version=\"1.0\"?><vision frameNumber = \"0\"></vision>";
	private String emptyXML = "dang";
	public SocketClient socket = new SocketClient();
	
	PID turnPID = new PID(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);

	public VisionCore(RobotCore core) {	
		socket.initServer();						
	}
	
	public void updateTurnPID(int wantGoal){
		turnPID.update(vs.getRotation(wantGoal), 0);
	}
	
	public void changePIDConstants(double kP, double kI, double kD) {
		turnPID.updateConstants(kP, kI, kD);
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
		try{
			socket.update();
			vs = xmlParser.parseString(socket.getXML());
		} catch(Exception e) {
			
		}
	}
	
//	public void update() {
//    	try {
//    		vs = xmlParser.parseString(server.getString("value", emptyXML));
//    		System.out.println(table.getString("value", emptyXML));
//    	}
//    	catch (TableKeyNotDefinedException e) {
//    		
//    	}
//	}
	
}

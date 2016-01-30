package core;
import edu.wpi.first.wpilibj.Timer;
import config.*;

public class Interpreter
{
	private int autoStep = 0;
	private double[][] commands;
	private Drive drive;
	private Timer timer = new Timer();
	private RobotCore robotCore;
	private boolean isFirst = true;
	private double prevAng = 0;
	private double angChange = 0;
	
	public Interpreter(Drive drive, RobotCore robotCore){
		this.drive = drive;
		this.robotCore = robotCore;
	}
	
	public void interpInit() {
		commands = Parser.parse();
	}
	
	public void next(){
		isFirst = true;
		System.out.println("Advancing to next step");
		autoStep++;
	}
	
	private void turn(double velocity, double turnAng) {
//		turnAng*=-1;
		double currAng = robotCore.navX.getAngle();
		double error = turnAng+angChange;
		double kP = 0.02;
		double angVelocity = kP*error;
		
		if(angVelocity > 1) 
			angVelocity = 1;
		else if(angVelocity < -1)
			angVelocity = -1;
		
		angVelocity *= velocity;
		drive.set(angVelocity, -angVelocity);
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if((turnAng > 0 && angChange > turnAng) || turnAng > 0 && (angChange+360) < turnAng) {
		
		}
		
		else if (turnAng < 0 && angChange < turnAng || turnAng < 0 && (angChange-360) > turnAng) {
			
		}
		
		if (Math.abs(prevAng - currAng) > InterpConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
			System.out.println("Added in else");
		}
		
		System.out.print("angChange: " + angChange + "\tcurrAng: " + currAng + "\tprevAng: " + prevAng + "\tisFirst: " + isFirst + "\terror: " + error + "\tvelocity: " + angVelocity);
		prevAng = currAng;
	}
	
	private void waitTimer(double wantValue){
		if(isFirst){
			timer.start();
			isFirst = false;
		}
		
		if(timer.get() >= wantValue) {
			timer.stop();
			timer.reset();
			System.out.println("ITS DONE");
			next();
		}
		System.out.println("navX: " + robotCore.navX.getAngle());
	}
	
	private void waitGyro(double ang){

		double currAng = robotCore.navX.getAngle();
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if (Math.abs(prevAng - currAng) > InterpConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
			System.out.println("Added in else");
		}
		
		if((ang > 0 && angChange > ang) || ang > 0 && (angChange+360) < ang) {
			next();
		}
		
		else if (ang < 0 && angChange < ang || ang < 0 && (angChange-360) > ang) {
			next();
		}
		System.out.println("angChange: " + angChange + "\tcurrAng: " + currAng + "\tprevAng: " + prevAng + "\tisFirst: " + isFirst  + "\tnavX: " + robotCore.navX.getAngle());
		prevAng = currAng;
	}
	
	public void dispatch(){
		if((commands[autoStep][0]) == -1) {	//Dead line
			drive.move(0, 0);
			System.out.println("Dead line at " + autoStep);
		}	
		else if ((commands[autoStep][0]) == Steps.getStep(Type.DRIVE)){	//Drive
			drive.move(commands[autoStep][1],commands[autoStep][2]);
			next();
			return;
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_TIMER)) {	//Wait
			waitTimer(commands[autoStep][1]);
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_GYRO)) {	//Wait
			waitGyro(commands[autoStep][1]);
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.TURN)) {	//Wait
			turn(commands[autoStep][1],commands[autoStep][2]);
		}
		System.out.println("\tnavX: " + robotCore.navX.getAngle());
	}
}

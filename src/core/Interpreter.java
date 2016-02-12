package core;
import edu.wpi.first.wpilibj.Timer;
import config.*;
import util.ChooserType;
import util.Dashboard;

public class Interpreter
{
	private int autoStep = 0;
	private double[][] commands;
	private Drive drive;
	private Intake intake;
	private Timer timer = new Timer();
	private RobotCore robotCore;
	private boolean isFirst = true;
	private boolean isFirstTimer = true;
	private double prevAng = 0;
	private double angChange = 0;
	private String fileName;
	private Dashboard dashboard;
	
	public Interpreter(Drive drive, RobotCore robotCore, Intake intake, Dashboard dashboard){
		this.drive = drive;
		this.robotCore = robotCore;
		this.intake = intake;
		this.dashboard = dashboard;
	}
	
	public void interpInit() {
		fileName = dashboard.getFileName();
		commands = Parser.parse(fileName);
	}
	
	public void next(){
		isFirst = true;
		System.out.println("Advancing to next step");
		autoStep++;
		isFirstTimer = true;
	}
	
	private void waitEncoder(double leftWant, double rightWant) {
		if(isFirst) {
			robotCore.encLeft.reset();
			robotCore.encRight.reset();
			isFirst = false;
		}
		
		System.out.println("right: " + robotCore.encRight.getDistance() + "\tleft: " + robotCore.encLeft.getDistance());
		
		if(robotCore.encLeft.getDistance() > leftWant && robotCore.encRight.getDistance() > rightWant)  {
			next();
		}
	}
	
	private void turn(double velocity, double turnAng) {
//		turnAng*=-1;
		double currAng = robotCore.navX.getAngle();
		double error = turnAng+angChange;
		double kP = 0.015;
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
		
		if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold) {
			if(isFirstTimer) {
				timer.start();
				isFirstTimer = false;
			}
			
			if(timer.get() > InterpConfig.turnNextTime) {
				if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold){
					next();
				}
				timer.reset();
				timer.stop();
			}
		}
		
		System.out.print("timer: " + timer.get() + "\tangChange: " + angChange + "\tcurrAng: " + currAng + "\twantAng: " + turnAng + "\tprevAng: " + prevAng + "\tisFirst: " + isFirst + "\terror: " + error + "\tvelocity: " + angVelocity);
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
			drive.moveNoRamp(commands[autoStep][1],Math.toRadians(commands[autoStep][2]));
			System.out.println("Setting left to: " + drive.leftCimGroup.c1.get() + "Setting right to: " + drive.rightCimGroup.c1.get());
			next();
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_TIMER)) {	//Wait for timer
			waitTimer(commands[autoStep][1]);
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_GYRO)) {	//Wait for gyro
			waitGyro(commands[autoStep][1]);
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.TURN)) {	//Turn
			turn(commands[autoStep][1],commands[autoStep][2]);
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.INTAKE)) {	//Intake
			intake.pickupBall();
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_ENCODER)) {	//Wait for encoder
			waitEncoder(commands[autoStep][1],commands[autoStep][2]);
		}
		System.out.println("\tnavX: " + robotCore.navX.getAngle());
//		intake.update();
	}
}

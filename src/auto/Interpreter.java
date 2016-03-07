package auto;
import edu.wpi.first.wpilibj.Timer;
import config.*;
import core.Drive;
import core.Intake;
import core.RobotCore;
import core.Shooter;
import util.ChooserType;
import util.Dashboard;
import util.Util;
import vision.VisionCore;

/**
 * Interprets and runs scripts parsed for auto
 * @author Trevor
 *
 */
public class Interpreter {
	private int autoStep = 0;
	private double[][] commandsOne;
	private double[][] commandsTwo;
	private Drive drive;
	private Intake intake;
	private Timer timer = new Timer();
	private RobotCore robotCore;
	private boolean isFirst = true;
	private boolean isFirstTimer = true;
	private double prevAng = 0;
	private double angChange = 0;
	private String fileNameOne;
	private String fileNameTwo;
	private Dashboard dashboard;
	private Shooter shooter;
	private VisionCore vision;
	private boolean firstFile = true;
	private boolean secondFile = false;
	
	/**
	 * 
	 * @param drive
	 * @param robotCore
	 * @param intake
	 * @param dashboard
	 * @param shooter
	 */
	public Interpreter(Drive drive, RobotCore robotCore, Intake intake, Dashboard dashboard, Shooter shooter, VisionCore vision){
		this.drive = drive;
		this.robotCore = robotCore;
		this.intake = intake;
		this.dashboard = dashboard;
		this.shooter = shooter;
		this.vision = vision;
	}
	
	/**
	 * Run at auto init to get and parse script file 
	 */
	public void interpInit() {
		fileNameOne = dashboard.getFileNameOne();
		commandsOne = Parser.parse(fileNameOne);

		fileNameTwo = dashboard.getFileNameTwo();
		commandsTwo = Parser.parse(fileNameTwo);
	}
	
	/**
	 * Advances to next step in script
	 */
	public void next(){
		isFirst = true;
		System.out.println("Advancing to next step");
		autoStep++;
		isFirstTimer = true;
	}
	
	/**
	 * Waits for encoder to reach a certain value before advancing
	 * @param leftWant
	 * @param rightWant
	 */
	private void waitEncoder(double leftWant, double rightWant) {
		if(isFirst) {
			robotCore.driveEncLeft.reset();
			robotCore.driveEncRight.reset();
			isFirst = false;
		}
		
		System.out.println("right: " + robotCore.driveEncRight.getDistance() + "\tleft: " + robotCore.driveEncLeft.getDistance());
		
		if(robotCore.driveEncLeft.getDistance() > leftWant && robotCore.driveEncRight.getDistance() > rightWant)  {
			next();
		}
	}
	
	/**
	 * Turns the robot to a specific angle before advancing to the next step
	 * @param velocity
	 * @param turnAng
	 */
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
	
	/**
	 * Waits for a specified amount of time
	 * @param wantValue time to wait in seconds
	 */
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
	
	private void spinToGoal() {	
		drive.set(InterpConfig.turnSpeed, -InterpConfig.turnSpeed);
		if(Util.withinBounds(vision.vs.getDistance(0), -InterpConfig.visionAngThreshold, InterpConfig.visionAngThreshold)) {
			//Goal found
			drive.set(0, 0);
			next();
		}
	}
	/**
	 * Waits until the robot is at a specified angle
	 * @param ang
	 */
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
	
	/**
	 * Run periodically to read through and execute the script
	 */
	public void update(){
		if(firstFile) {
			dispatcher(commandsOne);
		}
	
		else if(secondFile) {
			dispatcher(commandsTwo);
		}
		
		System.out.println("\tnavX: " + robotCore.navX.getAngle());
        dashboard.update();
		intake.update();
		shooter.update();
		vision.update();
	}
	
	public void dispatcher(double[][] commands) {
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
			if(isFirst) {
				intake.pickupBall();
				isFirst = false;
			}
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_ENCODER)) {	//Wait for encoder
			waitEncoder(commands[autoStep][1],commands[autoStep][2]);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.SHOOT)) {	//Shoot using vision
			shooter.shoot();
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.SPIN_GOAL)) {	//Spin until goal is found
			spinToGoal();
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.END)) {
			firstFile = false;
			secondFile = true;
		}
	}
}

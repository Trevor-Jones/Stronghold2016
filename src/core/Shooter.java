package core;

import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.PID;
import util.Util;
import vision.VisionCore;

/**
 * Controls the shooter mechanism
 * @author Trevor
 *
 */
public class Shooter{
	public Encoder leftMotorEnc;
	public Encoder rightMotorEnc;
	public Solenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, false);
	private PID leftPID;
	private PID rightPID;
	private PID turnPID;
	private boolean isShooting;
	private double shootSpeed;
	double currentPos;
	double waitDistance;
	boolean isFirst;
	VisionCore vs;
	Drive drive;
	Timer timer = new Timer();

	/**
	 * 
	 * @param core
	 * @param drive
	 * @param vision
	 */
	public Shooter(RobotCore core, Drive drive, VisionCore vs){
		leftMotorEnc = core.motorOneEnc;
		rightMotorEnc = core.motorTwoEnc;
		solOne = core.solOne;
	
		this.drive = drive;
		this.vs = vs;

		leftMotorEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);
		rightMotorEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);

		leftMotorEnc.reset();
		rightMotorEnc.reset();

		solOne.set(false);
		leftMotor.set(0);
		rightMotor.set(0);
		isShooting = false;
		
		leftPID = new PID(ShooterConfig.kPLeft, ShooterConfig.kILeft, ShooterConfig.kDLeft);
		rightPID = new PID(ShooterConfig.kPRight, ShooterConfig.kIRight, ShooterConfig.kDRight);
		turnPID = new PID(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);
		isFirst = true;
	}

	/**
	 * Run periodically to control shooting process
	 */
	public void update(){
		leftPID.update(leftMotorEnc.getRate(), shootSpeed);
		rightPID.update(rightMotorEnc.getRate(), shootSpeed);
		leftMotor.set(leftPID.getOutput());
		rightMotor.set(rightPID.getOutput());
		
		if(isShooting) {
			turnPID.update(vs.getShootingGoal()[1], 0); 
			drive.set(turnPID.getOutput(), -turnPID.getOutput());
		}
		
		if(Util.withinThreshold(vs.getShootingGoal()[1], 0, ShooterConfig.angTolerance)){
			timer.start();
			
			if (isMotorsFastEnough(shootSpeed) && isShooting && timer.get() > ShooterConfig.turnTime){
				launchBall();
				
				if(isFirst) {
					currentPos = leftMotorEnc.getDistance();
					isFirst =  false;
				}

				if (Math.abs(leftMotorEnc.getDistance() - currentPos) < waitDistance){
					solOne.set(false);
					shootSpeed = 0;
					isShooting = false;
					isFirst = true;
				}
			}
		}
		
		System.out.println("Shooter Left Distance: " + leftMotorEnc.getDistance() + "\tShooter Right Distance: " + rightMotorEnc.getDistance());
	}

	/**
	 * Starts the shooting process at a given speed
	 * @param shootSpeed
	 */
	public void shoot(double shootSpeed){
		isShooting = true;
		this.shootSpeed = shootSpeed;
		waitDistance = ShooterConfig.waitTime * leftMotorEnc.getRate();
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		isShooting = true;
		shootSpeed = 1; //vision.getDistance()*ShooterConfig.distanceSpeedConstant;
	}
	
	/**
	 * Stops the shooting process
	 */
	public void cancelShot() {
		isShooting = false;
		shootSpeed = 0;
	}
	
	/**
	 * Sets the speed of the shooting motors
	 * @param speed
	 */
	public void setSpeed(double speed) {
		shootSpeed = speed;
	}
	
	/**
	 * Starts the motors at a speed determined from vision
	 */
	public void setSpeed() {
		shootSpeed = 1;//vision.getDistance()*ShooterConfig.distanceSpeedConstant;
	}

	/**
	 * Actuates a solenoid to launch the ball
	 */
	public void launchBall() {
		solOne.set(true);
	}
	
	/**
	 * Checks if the motors are at a specific speed
	 * @param motorSpeed
	 * @return
	 */
	public boolean isMotorsFastEnough(double motorSpeed){
		return (leftMotorEnc.getRate() > motorSpeed && rightMotorEnc.getRate() > motorSpeed);
	}

}

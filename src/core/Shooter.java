package core;

import java.util.HashMap;
import java.util.Map;

import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.PID;
import util.Util;

/**
 * Controls the shooter mechanism
 * @author Trevor
 *
 */
public class Shooter{
	public Encoder leftMotorEnc;
	public Encoder rightMotorEnc;
	public DoubleSolenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, true);
	private PID leftPID;
	private PID rightPID;
	private PID turnPID;
	private boolean isShooting;
	private double shootSpeed;
	double currentPos;
	double waitDistance = 0.5;
	double speed;
	boolean isFirst;
	boolean stopping;
	boolean isFirstTimer;
	boolean usingVision;
	int wantGoal;
	Vision vision;
	Drive drive;
	Timer timer = new Timer();
	Map<Integer, Double> map = new HashMap<Integer, Double>();

	/**
	 * 
	 * @param core
	 * @param drive
	 * @param vision
	 */
	public Shooter(RobotCore core, Drive drive, Vision vision){
		leftMotorEnc = core.motorOneEnc;
		rightMotorEnc = core.motorTwoEnc;
		solOne = core.solOne;
		speed = 0;
		usingVision = false;
		this.vision = vision;
	
		initShooterTable();
		
		this.drive = drive;
		
		leftMotorEnc.setDistancePerPulse(ShooterConfig.distancePerPulseLeft);
		rightMotorEnc.setDistancePerPulse(ShooterConfig.distancePerPulseRight);

		leftMotorEnc.reset();
		rightMotorEnc.reset();

		solOne.set(DoubleSolenoid.Value.kForward);
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
		wantGoal = vision.getHighestArea();
		
		System.out.println("left shooter enc: " + leftMotorEnc.getRate() + "\tright shooter enc: " + rightMotorEnc.getRate());
		if(!stopping) {
			leftPID.update(leftMotorEnc.getRate(), shootSpeed);
			rightPID.update(rightMotorEnc.getRate(), shootSpeed);
			speed+=leftPID.getOutput();
			leftMotor.ramp(speed,0.05);
			rightMotor.ramp(speed,0.05);
		}
		else {
			leftMotor.ramp(0,0.05);
			rightMotor.ramp(0,0.05);
		}
		
		if(isShooting && usingVision) {
			turnPID.update(vision.getRotation(wantGoal), 0); 
			drive.set(turnPID.getOutput(), -turnPID.getOutput());
		}
		
		if(isShooting && Util.withinThreshold(vision.getRotation(wantGoal), 0, ShooterConfig.angTolerance)){
			if(isFirstTimer){
				timer.start();
				isFirstTimer = false;
			}
			
			System.out.print("timer: " + timer.get() + "\t");
			if (isMotorsFastEnough(shootSpeed) && isShooting && timer.get() > ShooterConfig.turnTime){
				solOne.set(DoubleSolenoid.Value.kForward);
				
				if(isFirst) {
					currentPos = rightMotorEnc.getDistance();
					isFirst =  false;
				}

				if (Math.abs(rightMotorEnc.getDistance() - currentPos) > waitDistance){
					solOne.set(DoubleSolenoid.Value.kReverse);
					System.out.println("Done Fam");
					shootSpeed = 0;
					isShooting = false;
					isFirst = true;
					stopping = true;
					isFirstTimer = true;
				}
			}
		}
//		System.out.println("Shooter Left Distance: " + leftMotorEnc.getDistance() + "\tShooter Right Distance: " + rightMotorEnc.getDistance());
	}

	/**
	 * Starts the shooting process at a given speed
	 * @param shootSpeed
	 */
	public void shoot(double shootSpeed){
		isShooting = true;
		this.shootSpeed = shootSpeed;
		waitDistance = ShooterConfig.waitTime * leftMotorEnc.getRate();
		isFirstTimer = true;
		stopping = false;
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		isShooting = true;
		isFirstTimer = true;
		stopping = false;
		if(usingVision) {
			shootSpeed = vision.getDistance(wantGoal)*ShooterConfig.distanceSpeedConstant;
		}
		else {
			shootSpeed = ShooterConfig.constantSpeed;
		}
	}
	
	/**
	 * Stops the shooting process
	 */
	public void cancelShot() {
		isShooting = false;
		shootSpeed = 0;
		stopping = true;
	}
	
	/**
	 * Enables or disable vision use withing shooter
	 * @param visionUse
	 */
	public void setVisionUse(boolean visionUse) {
		usingVision = visionUse;
	}
	
	/**
	 * Sets the speed of the shooting motors
	 * @param speed
	 */
	public void setSpeed(double speed) {
		shootSpeed = speed;
		stopping = false;
	}
	
	public void setRawSpeed(double speed) {
		leftMotor.set(speed);
		rightMotor.set(speed);
		shootSpeed = speed;
	}
	
	/**
	 * Starts the motors at a speed determined from vision
	 */
	public void setSpeed() {
		stopping = false;
		shootSpeed = vision.getDistance(wantGoal)*ShooterConfig.distanceSpeedConstant;
	}

	/**
	 * Actuates a solenoid to launch the ball
	 */
	public void launchBall() {
		if(solOne.get() == DoubleSolenoid.Value.kReverse)
			solOne.set(DoubleSolenoid.Value.kForward);
		else if(solOne.get() == DoubleSolenoid.Value.kForward)
			solOne.set(DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * Checks if the motors are at a specific speed
	 * @param motorSpeed
	 * @return
	 */
	public boolean isMotorsFastEnough(double motorSpeed){
		return (Util.withinThreshold(leftMotorEnc.getRate(), motorSpeed, ShooterConfig.motorSpeedTolerance) && Util.withinThreshold(rightMotorEnc.getRate(), motorSpeed, ShooterConfig.motorSpeedTolerance));
	}
	
	/**
	 * returns velocity based off of distance from vision
	 * @param distance distance from 1-100
	 */
	public double getVelocityDistance(double distance){
		return map.get(distance);
	}
	
	/**
	 * initializes the hashmap for distance to velocity
	 */
	public void initShooterTable() {
		for(int i = 0; i < ShooterConfig.distanceToVelocity.length; i++) {
			for(int q = 0; q <= ShooterConfig.maxDistanceVision/ShooterConfig.distanceToVelocity.length; q++){				
				map.put((i*(ShooterConfig.maxDistanceVision/ShooterConfig.distanceToVelocity.length))+q, ShooterConfig.distanceToVelocity[i]);
			}
		}
	}

}

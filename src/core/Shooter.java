package core;

import java.util.HashMap;
import java.util.Map;

import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;
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
	public DoubleSolenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, true);
	private PID leftPID;
	private PID rightPID;
	private boolean isShooting;
	private double shootSpeed = 0;
	double currentPos;
	double speedLeft;
	double speedRight;
	boolean isFirst;
	boolean stopping;
	boolean isFirstTimer;
	boolean isFirstMotors;
	boolean usingVision;
	Timer timerOne = new Timer();
	int wantGoal;
	VisionCore vision;
	Drive drive;
	Dashboard dash;
	Timer timer = new Timer();

	/**
	 * 
	 * @param core
	 * @param drive
	 * @param vision
	 */
	public Shooter(RobotCore core, Drive drive, VisionCore vision, Dashboard dash){
		leftMotorEnc = core.shooterOneEnc;
		rightMotorEnc = core.shooterTwoEnc;
		solOne = core.shooterSol;
		speedLeft = 0;
		speedRight = 0;
		usingVision = true;
		this.dash = dash;
		this.vision = vision;
		
		this.drive = drive;
		
		
		solOne.set(DoubleSolenoid.Value.kForward);
		leftMotor.set(0);
		rightMotor.set(0);
		isShooting = false;
		
		leftPID = new PID(ShooterConfig.kPLeft, ShooterConfig.kILeft, ShooterConfig.kDLeft);
		rightPID = new PID(ShooterConfig.kPRight, ShooterConfig.kIRight, ShooterConfig.kDRight);
		isFirst = true;
	}

	/**
	 * Run periodically to control shooting process
	 */
	public void update(){
		dash.putDouble("leftMotorEnc", leftMotorEnc.getRate());
		dash.putDouble("rightMotorEnc", rightMotorEnc.getRate());
		dash.putDouble("leftOutput", leftPID.getOutput());
		dash.putDouble("rightOutput", rightPID.getOutput());
		dash.putDouble("wantSpeed", shootSpeed);
		wantGoal = vision.vs.getHighestArea();
//		System.out.println("left shooter enc: " + leftMotorEnc.getRate() + "\tright shooter enc: " + rightMotorEnc.getRate());
		
		if(!stopping) {
			if(isFirstMotors) {
				timerOne.start();
				speedLeft = shootSpeed;
				speedRight = shootSpeed;
				isFirstMotors = false;
			}
			if(timerOne.get() > 0.5) {
				leftPID.update(Util.limit(leftMotorEnc.getRate(), 0, 1), shootSpeed);
				rightPID.update(Util.limit(rightMotorEnc.getRate(), 0, 1), shootSpeed);
				speedLeft+=leftPID.getOutput();
				speedRight+=rightPID.getOutput();
			}
			leftMotor.set(speedLeft);
			rightMotor.set(speedRight);
		}
		else {
			leftMotor.ramp(0,0.05);
			rightMotor.ramp(0,0.05);
			timerOne.stop();
			timerOne.reset();
			speedLeft = 0;
			speedRight = 0;
			shootSpeed = 0;
			isShooting = false;
			isFirst = true;
			isFirstTimer = true;
			stopping = true;
			vision.resetTurnPID();
		}
		dash.putDouble("drive output", vision.getTurnPID());
		dash.putDouble("rotation", vision.vs.getRotation(wantGoal));
		
		if(isShooting && usingVision) {
			vision.updateTurnPID(wantGoal);
			double driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeed, ShooterConfig.rotateMaxSpeed);
			drive.setNoRamp(driveSpeed, -driveSpeed);
		}
		
		if(isShooting && Util.withinThreshold(vision.vs.getRotation(wantGoal), 0, ShooterConfig.angTolerance)){
			if(isFirstTimer){
				timer.start();
				isFirstTimer = false;
			}
			
//			System.out.print("timer: " + timer.get() + "\t");
			
			if (isMotorsFastEnough(shootSpeed) && timer.get() > ShooterConfig.turnTime){
				solOne.set(DoubleSolenoid.Value.kForward);
				
				if(isFirst) {
					timer.reset();
					timer.start();
					isFirst =  false;
				}

				if (timer.get() > ShooterConfig.waitTimeStop){
					solOne.set(DoubleSolenoid.Value.kReverse);
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
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isShooting = true;
		isFirstTimer = true;
		isFirst = true;
		stopping = false;
		setSpeed(shootSpeed);
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isShooting = true;
		isFirstTimer = true;
		isFirst = true;
		stopping = false;
		if(usingVision) {
//			shootSpeed = dash.getSpeed();
			setSpeed();
			vision.startTurnPID();
			isFirstMotors = true;
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
		vision.resetTurnPID();
	}
	
	/**
	 * Enables or disable vision use withing shooter
	 * @param visionUse
	 */
	public void setVisionUse(boolean visionUse) {
		usingVision = visionUse;
	}
	
	public void stopShooter() {
		shootSpeed = 0;
		stopping = true;
	}
	
	public void setRawSpeed(double speed) {
		leftMotor.set(speed);
		rightMotor.set(speed);
		shootSpeed = speed;
	}
	
	/**
	 * Sets the speed of the shooting motors
	 * @param speed
	 */
	public void setSpeed(double speed) {
		shootSpeed = speed;
		stopping = false;
		speedLeft = shootSpeed * ShooterConfig.startSpeedScalar;
		speedRight = shootSpeed * ShooterConfig.startSpeedScalar;
	}
	
	/**
	 * Starts the motors at a speed determined from vision
	 */
	public void setSpeed() {
		stopping = false;
//		shootSpeed = dash.getSpeed();
		shootSpeed = getVelocityDistance(vision.vs.getDistance(wantGoal));
		speedLeft = shootSpeed * ShooterConfig.startSpeedScalar;
		speedRight = shootSpeed * ShooterConfig.startSpeedScalar;
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
//		System.out.println("leftRate : "  + leftMotorEnc.getRate() + "\trightRate : " + rightMotorEnc.getDistance() + "\twantSpeed : " + motorSpeed);
//		return (Util.withinThreshold(leftMotorEnc.getRate(), motorSpeed, ShooterConfig.motorSpeedTolerance) && Util.withinThreshold(rightMotorEnc.getRate(), motorSpeed, ShooterConfig.motorSpeedTolerance));
		return leftMotorEnc.getRate() > motorSpeed; /*&& rightMotorEnc.getRate() > motorSpeed;*/
	}
	
	/**
	 * returns velocity based off of distance from vision
	 * @param distance distance from 1-100
	 */
	public double getVelocityDistance(double distance){
//		if(distance < 80) {
//			return (distance * ShooterConfig.distanceSpeedConstant) + 1.42;
//		}
//		else {
			return ShooterConfig.constantSpeed;
//		}
	}

}

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
	boolean withinThreshold;
	Timer timerOne = new Timer();
	int wantGoal;
	VisionCore vision;
	Drive drive;
	Dashboard dash;
	Timer timer = new Timer();
	Timer leftTimer = new Timer();
	Timer rightTimer = new Timer();
	double encoderLeftDistance;
	double encoderRightDistance;
	double leftRate;
	double rightRate;
	boolean isFirstLeft = true;
	boolean isFirstRight = true;
	boolean closeMode = false;

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
	
	public void updateShooterDash() {
		dash.putDouble("leftMotorEnc", leftRate);
		dash.putDouble("rightMotorEnc", rightRate);
		dash.putDouble("leftMotorDistance", leftMotorEnc.getDistance());
		dash.putDouble("rightMotorDistance", rightMotorEnc.getDistance());
		dash.putDouble("leftOutput", leftPID.getOutput());
		dash.putDouble("rightOutput", rightPID.getOutput());
		dash.putDouble("wantSpeed", shootSpeed);
		dash.putDouble("drive output", vision.getTurnPID());
		dash.putDouble("rotation", vision.vs.getRotation(wantGoal));
	}
	
	public void updateShooterWheels() {
		if(!stopping) {
			if(isFirstMotors) {
				timerOne.start();
				isFirstMotors = false;
			}
			if(timerOne.get() > 0.5) {
				leftPID.update(Util.limit(leftRate, 0, 1), shootSpeed);
				rightPID.update(Util.limit(rightRate, 0, 1), shootSpeed);
				speedLeft+=leftPID.getOutput();
				speedRight+=rightPID.getOutput();
				speedLeft = Util.limit(speedLeft, dash.getSpeed()-.1, dash.getSpeed()+.1);
				speedRight = Util.limit(speedRight, dash.getSpeed()-.1, dash.getSpeed()+.1);
			}
			leftMotor.set(speedLeft);
			rightMotor.set(speedRight);
		}
		else {
			leftMotor.ramp(0,0.05);
			rightMotor.ramp(0,0.05);
			
		}
	}
	
	public void updateTurn() {
		if(isShooting && usingVision) {
			if(Math.abs(vision.vs.getRotation(wantGoal)) < ShooterConfig.changePIDAng) {
				vision.changePIDConstants(ShooterConfig.kPDriveClose, ShooterConfig.kIDriveClose, ShooterConfig.kDDriveClose);
				closeMode = true;
			}
			
			else {
				vision.changePIDConstants(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);
				closeMode = false;
			}
			
			vision.updateTurnPID(wantGoal);
			double turnPIDOutput = vision.getTurnPID();
			double driveSpeed = 0;
			
			if(closeMode) {	
				if(turnPIDOutput < 0) {
					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeedClose, -ShooterConfig.rotateMinSpeedClose);
				}
				
				else {
					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeedClose, -ShooterConfig.rotateMaxSpeedClose);
				}
			}
			
			else {
				if(turnPIDOutput < 0) {
					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeed, -ShooterConfig.rotateMinSpeed);
				}
				
				else {
					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeed, -ShooterConfig.rotateMaxSpeed);
				}
			}
			drive.setNoRamp(driveSpeed, -driveSpeed);
		}
	}
	
	public void checkAngle() {
		if(usingVision) {
			if(Util.withinThreshold(vision.vs.getRotation(wantGoal), 0, ShooterConfig.angTolerance)) {
				withinThreshold = true;
			}
			
			else {
				withinThreshold = false;
			}
		}
		
		else {
			withinThreshold = true;
		}
	}

	/**
	 * Run periodically to control shooting process
	 */
	public void update(){
		wantGoal = vision.vs.getHighestArea();
		updateLeftRate();
		updateRightRate();
		updateShooterDash();
		updateShooterWheels();
		updateTurn();
		checkAngle();
		
		if(isShooting && withinThreshold){
			if(isFirstTimer){
				timer.start();
				isFirstTimer = false;
			}
			
			if (isMotorsFastEnough(shootSpeed) && timer.get() > ShooterConfig.turnTime){
				solOne.set(DoubleSolenoid.Value.kReverse);
				
				if(isFirst) {
					timer.reset();
					timer.start();
					isFirst =  false;
				}

				if (timer.get() > ShooterConfig.waitTimeStop){
					solOne.set(DoubleSolenoid.Value.kForward);
					stopping = true;
					timerOne.stop();
					timerOne.reset();
					speedLeft = 0;
					speedRight = 0;
					shootSpeed = 0;
					isShooting = false;
					isFirst = true;
					isFirstTimer = true;
					vision.resetTurnPID();
				}
			}
		}
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		isShooting = true;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isFirstTimer = true;
		isFirst = true;
		if(usingVision) {
			setSpeed();
			vision.startTurnPID();
		}
		else {
//			shootSpeed = ShooterConfig.constantSpeed;
//			shootSpeed = getVelocityDistance(vision.vs.getDistance(wantGoal));
			shootSpeed = dash.getSpeed();
			stopping = false;
			speedLeft = shootSpeed;
			speedRight = shootSpeed;
		}
		isFirstMotors = true;
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
	 * Enables or disable vision use within shooter
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
		stopping = false;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isFirstTimer = true;
		isFirst = true;
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
		speedLeft = shootSpeed;
		speedRight = shootSpeed;
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
//		return (Util.withinThreshold(leftRate, motorSpeed, ShooterConfig.motorSpeedTolerance) && Util.withinThreshold(rightRate, motorSpeed, ShooterConfig.motorSpeedTolerance));
//		return leftMotorEnc.getRate() > motorSpeed && rightMotorEnc.getRate() > motorSpeed;
		return true;
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
	
	public boolean getState() {
		return isShooting;
	}
	
	public void updateLeftRate() {
		if(isFirstLeft) {
			encoderLeftDistance = leftMotorEnc.getDistance();
			leftTimer.start();
			isFirstLeft = false;
		}
		if(leftTimer.get() > 0.1) {
			isFirstLeft = true;
			leftRate = (leftMotorEnc.getDistance()-encoderLeftDistance)/leftTimer.get();
			leftTimer.stop();
			leftTimer.reset();
		}
	}
	
	public void updateRightRate() {
		if(isFirstRight) {
			encoderRightDistance = rightMotorEnc.getDistance();
			rightTimer.start();
			isFirstRight = false;
		}
		if(rightTimer.get() > 0.1) {
			isFirstRight = true;
			rightRate = (rightMotorEnc.getDistance()-encoderRightDistance)/rightTimer.get();
			rightTimer.stop();
			rightTimer.reset();
		}
	}

}

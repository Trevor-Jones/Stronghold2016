package core;

import java.util.HashMap;
import java.util.Map;

import components.CIM;
import config.IntakeArmConfig;
import config.ShooterConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;
import util.FileSaver;
import util.PID;
import util.Util;
import vision.VisionCore;

/**
 * Controls the shooter mechanism
 * @author Trevor
 *
 */
public class Shooter{
	private VisionCore vision;
	private Drive drive;
	private Dashboard dash;
	private Intake intake;
	private RobotCore robotCore;
	
	public Encoder leftMotorEnc;
	public Encoder rightMotorEnc;
	public DoubleSolenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, true);
	
	public ShooterWheels shooterWheels;
	
	private boolean isFirst;
	private boolean stopping;
	private boolean isFirstTimer;
	private boolean isFirstMotors;
	private boolean usingVision;
	private boolean withinThreshold;
	private boolean adjustWithLeft = false;
	private boolean isShooting;
	private boolean filledRightRate = true;
	private boolean isFirstLeft = true;
	private boolean isFirstRight = true;
	private boolean closeMode = false;
	private boolean resettingShot = true;
	private boolean doneShooting = false;
	
	private Timer timerOne = new Timer();
	private Timer timer = new Timer();
	private Timer leftTimer = new Timer();
	private Timer rightTimer = new Timer();
	private Timer resetTimer = new Timer();
	
	private int wantGoal;
	private double shootSpeed = 0;
	private double wantAng;
	private double driveSpeed = 0;
	
	FileSaver fileSaverLeft = new FileSaver("shooterDataLeft.txt");
	FileSaver fileSaverRight = new FileSaver("shooterDataRight.txt");

	/**
	 * 
	 * @param core
	 * @param drive
	 * @param vision
	 */
	public Shooter(RobotCore core, Drive drive, VisionCore vision, Dashboard dash, Intake intake){
		solOne = core.shooterSol;
		usingVision = false;
		this.dash = dash;
		this.vision = vision;
		this.robotCore = core;
		this.intake = intake;
		
		shooterWheels = new ShooterWheels(dash, core);
		new Thread(shooterWheels).start();
		
		this.drive = drive;		
		
		solOne.set(DoubleSolenoid.Value.kForward);
		leftMotor.set(0);
		rightMotor.set(0);
		isShooting = false;
		
		isFirst = true;
	}
	
	public void updateShooterDash() {
		dash.putDouble("leftShooterEnc", shooterWheels.getLeftRate());
		dash.putDouble("rightShooterEnc", shooterWheels.getRightRate());
		dash.putDouble("leftShooterDistance", leftMotorEnc.getDistance());
		dash.putDouble("rightShooterDistance", rightMotorEnc.getDistance());
		dash.putDouble("shooterWantSpeed", shootSpeed);
		dash.putDouble("shooter drive output", vision.getTurnPID());
		dash.putDouble("rotation", vision.vs.getRotation(wantGoal));
		dash.putDouble("wantAng", wantAng);
		dash.putDouble("leftEncCount", leftMotorEnc.get());
		dash.putDouble("rightEncCount", rightMotorEnc.get());
		
		fileSaverLeft.write(Double.toString(leftMotorEnc.getRate()));
		fileSaverRight.write(Double.toString(rightMotorEnc.getRate()));
	}
	
	public void updateTurn() {
		if(isShooting && usingVision) {
			if(Math.abs(Math.abs(robotCore.navX.getAngle()) -  Math.abs(wantAng)) < ShooterConfig.changePIDAng) {
				vision.changePIDConstants(ShooterConfig.kPDriveClose, ShooterConfig.kIDriveClose, ShooterConfig.kDDriveClose);
				closeMode = true;
			}
			
			else {
				vision.changePIDConstants(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);
				closeMode = false;
			}
			
			vision.updateTurnPID(robotCore.navX.getAngle(), wantAng);
			double turnPIDOutput = vision.getTurnPID();
			driveSpeed = 0;
			
			dash.putBoolean("navX within", Util.withinThreshold(robotCore.navX.getAngle(), wantAng, 1));
			dash.putBoolean("drive within", Util.withinThreshold(driveSpeed, 0, 0.2));
			dash.putBoolean("timer within", resetTimer.get() > 0.5);
			dash.putBoolean("resettingShot", resettingShot);
			dash.putBoolean("withinThreshold", withinThreshold);
			
			
			
			if(!withinThreshold && !resettingShot && resetTimer.get() > 0.5 && Util.withinThreshold(robotCore.navX.getAngle(), wantAng, 1) && Util.withinThreshold(driveSpeed, 0, 0.2)) {
//				wantAng = robotCore.navX.getAngle() + ((180/Math.PI)*Math.atan(vision.vs.getRotation(wantGoal)/(vision.vs.getDistance(wantGoal)*1.52)));
				resettingShot = true;
				resetTimer.reset();
				resetTimer.start();
			}
			
			if(resetTimer.get() > 0.5 && resettingShot) {
				resettingShot = false;
				shoot();
			}
			
			if(closeMode) {	
				if(turnPIDOutput < 0) {
					driveSpeed = -ShooterConfig.rotateMinSpeedClose + turnPIDOutput;
//					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeedClose, -ShooterConfig.rotateMinSpeedClose);
				}
				
				else {
					driveSpeed = ShooterConfig.rotateMinSpeedClose + turnPIDOutput;
//					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeedClose, ShooterConfig.rotateMaxSpeedClose);
				}
			}
			
			else {
				if(turnPIDOutput < 0) {
					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeed, -ShooterConfig.rotateMinSpeed);
				}
				
				else {
					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeed, ShooterConfig.rotateMaxSpeed);
				}
			}
			
			if(Math.abs(Math.abs(vision.vs.getRotation(wantGoal))) < ((ShooterConfig.rotationTolerance/2))) {
				driveSpeed = 0;				
			}
			
			if(Math.abs(Math.abs(robotCore.navX.getAngle())) < 1){
				driveSpeed = 0;
			}
			
			dash.putDouble("driveSpeed", driveSpeed);
			if(closeMode && !adjustWithLeft) {
				drive.setNoRamp(0, driveSpeed);								
			}
			
			else if(closeMode && adjustWithLeft) {
				drive.setNoRamp(-driveSpeed, 0);								
			}
			else {
				drive.setNoRamp(-driveSpeed, driveSpeed);		
			}
		}
	}
	
	public void checkAngle() {
		if(usingVision) {
			if(Util.withinThreshold(vision.vs.getRotation(wantGoal), 0, ShooterConfig.rotationTolerance) && Math.abs(driveSpeed) < 0.02 ) {
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
	
	public void updateShooterWheels() {
		leftMotor.set(shooterWheels.getWantSpeedLeft());
		rightMotor.set(shooterWheels.getWantSpeedRight());
	}

	/**)
	 * Run periodically to control shooting process
	 */
	public void update(){
		dash.putBoolean("isMotorsFast", isMotorsFastEnough(shootSpeed));
		wantGoal = vision.vs.getHighestArea();
		updateShooterDash();
		updateShooterWheels();
		updateTurn();
		checkAngle();
		
		if(isShooting && withinThreshold && isMotorsFastEnough(shootSpeed)){
			if(isFirstTimer){
				timer.start();
				isFirstTimer = false;
			}
			
			if (timer.get() > ShooterConfig.velocitySettleTime){
				solOne.set(DoubleSolenoid.Value.kReverse);

				fileSaverLeft.write("SHOOT");
				fileSaverRight.write("SHOOT");
				
				if(isFirst) {
					shooterWheels.stop();
					timer.reset();
					timer.start();
					isFirst =  false;
					doneShooting = true;
				}
			}
		}
		
		else {
			isFirstTimer = true;
			timer.stop();
			timer.reset();
		}
			
		if (timer.get() > ShooterConfig.waitTimeStop && doneShooting){
			solOne.set(DoubleSolenoid.Value.kForward);
			stopping = true;
			timerOne.stop();
			timerOne.reset();
			shootSpeed = 0;
			isShooting = false;
			doneShooting = false;
			isFirst = true;
			isFirstTimer = true;
			vision.resetTurnPID();
		}
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		resettingShot = false;
		doneShooting = false;
		resetTimer.stop();
		resetTimer.reset();
		resetTimer.start();
		timerOne.stop();
		timerOne.reset();
		intake.arm.setPos(0);
		drive.toLowGear();
		isShooting = true;
		isFirstMotors = true;
		isFirstTimer = true;
		isFirst = true;
		wantAng = robotCore.navX.getAngle() + ((180/Math.PI)*Math.atan(vision.vs.getRotation(wantGoal)/(vision.vs.getDistance(wantGoal)*1.52)));
		if(wantAng-robotCore.navX.getAngle() >= 0) {
			adjustWithLeft = true;
		}
		else if(wantAng-robotCore.navX.getAngle() < 0) {
			adjustWithLeft = false;
		}
		
		if(usingVision) {
			shooterWheels.setSpeed();
			vision.startTurnPID();
		}
		else {
			setSpeed();
		}
	}
	
	/**
	 * Stops the shooting process
	 */
	public void cancelShot() {
		isShooting = false;
		isFirst = true;
		isFirstMotors = true;
		isFirstTimer = true;
		
		shooterWheels.stop();
		vision.resetTurnPID();
	}
	
	/**
	 * Enables or disable vision use within shooter
	 * @param visionUse
	 */
	public void setVisionUse(boolean visionUse) {
		usingVision = visionUse;
	}
	
	public void setRawSpeed(double speed) {
		leftMotor.set(speed);
		rightMotor.set(speed);
		shootSpeed = speed;
		stopping = false;
		isFirstTimer = true;
		isFirst = true;
	}
	
	/**
	 * Starts the motors at a speed determined from vision
	 */
	public void setSpeed() {
		stopping = false;
		shootSpeed = dash.getSpeed();
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
		return Util.withinThreshold(Math.abs(shooterWheels.getLeftRate()), motorSpeed, ShooterConfig.motorSpeedTolerance) 
			&& Util.withinThreshold(Math.abs(shooterWheels.getRightRate()), motorSpeed, ShooterConfig.motorSpeedTolerance);
	}
	
	public boolean getState() {
		return isShooting;
	}
	
	public void changeShooterSpeed(double changeVal) {
		dash.putDouble("shooterSpeed", (dash.getSpeed()+changeVal));
	}
}

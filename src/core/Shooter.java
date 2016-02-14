package core;



import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.PID;
import util.Util;

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
	Vision vision;
	Drive drive;
	Timer timer = new Timer();

	public Shooter(RobotCore core, Drive drive, Vision vision){
		leftMotorEnc = core.motorOneEnc;
		rightMotorEnc = core.motorTwoEnc;
		solOne = core.solOne;
	
		this.drive = drive;
		this.vision = vision;

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

	public void update(){
		leftPID.update(leftMotorEnc.getRate(), shootSpeed);
		rightPID.update(rightMotorEnc.getRate(), shootSpeed);
		leftMotor.set(leftPID.getOutput());
		rightMotor.set(rightPID.getOutput());
		
		if(isShooting) {
			turnPID.update(vision.getAng(), 0); // TODO add vision angle here
			drive.set(turnPID.getOutput(), -turnPID.getOutput());
		}
		
		if(Util.withinThreshold(vision.getAng(), (double)0, ShooterConfig.angTolerance)){
			timer.start();
			
			if (isMotorsFastEnough(shootSpeed) && isShooting && timer.get() > ShooterConfig.turnTime){
				solOne.set(true);
				
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
		
	}

	public void shoot(double shootSpeed){
		isShooting = true;
		this.shootSpeed = shootSpeed;
		waitDistance = ShooterConfig.waitTime * leftMotorEnc.getRate();
	}
	
	public void shoot() {
		isShooting = true;
		shootSpeed = vision.getDistance()*ShooterConfig.distanceSpeedConstant;
	}

	public boolean isMotorsFastEnough(double motorSpeed){
		return (leftMotorEnc.getRate() > motorSpeed && rightMotorEnc.getRate() > motorSpeed);
	}

}

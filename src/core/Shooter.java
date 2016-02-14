package core;

import java.util.Timer;

import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import util.PID;

public class Shooter{

	public Encoder leftMotorEnc;
	public Encoder rightMotorEnc;
	public Solenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, false);
	private PID leftPID;
	private PID rightPID;
	private boolean isShooting;
	private double shootSpeed;
	double currentPos;
	double waitDistance;
	boolean isFirst;

	public Shooter(RobotCore core){
		leftMotorEnc = core.motorOneEnc;
		rightMotorEnc = core.motorTwoEnc;
		solOne = core.solOne;

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
		isFirst = true;
	}

	public void update(){
		leftPID.update(leftMotorEnc.getRate(), shootSpeed);
		rightPID.update(rightMotorEnc.getRate(), shootSpeed);
		leftMotor.set(leftPID.getOutput());
		rightMotor.set(rightPID.getOutput());
		
		if (isMotorsFastEnough(shootSpeed) && isShooting){
			solOne.set(true);
			
			if(isFirst) {
				currentPos = leftMotorEnc.getDistance();
				isFirst =  false;
			}

			if (Math.abs(leftMotorEnc.getDistance() - currentPos) < waitDistance){
				solOne.set(false);
				shootSpeed = 0;
				isShooting = false;
			}
		}
	}

	public void shoot(double shootSpeed){
		isShooting = true;
		this.shootSpeed = shootSpeed;
		waitDistance = ShooterConfig.waitTime * leftMotorEnc.getRate();
	}

	public boolean isMotorsFastEnough(double motorSpeed){
		return (leftMotorEnc.getRate() > motorSpeed && rightMotorEnc.getRate() > motorSpeed);
	}

}

package core;

import sensors.SharpIR;
import util.*;
import components.CIM;
import config.IntakeRollerConfig;

public class IntakeRoller {
	private CIM intakeCim = new CIM(IntakeRollerConfig.intakeMotorChn, IntakeRollerConfig.intakeCimFlip);
	private double speed;
	private IntakeArm arm;
	public SharpIR sharp;

	public IntakeRoller(IntakeArm arm, SharpIR sharp) {
		this.arm = arm;
		speed = IntakeRollerConfig.intakeSpeed;
		this.sharp = sharp;
	}
	
	public void runIntakeRoller() { 
		speed = IntakeRollerConfig.intakeSpeed;
	}
	
	public void setRawSpeed(double speed) {
		intakeCim.set(speed);
	}
	
	public void setSpeed (double speed) {
		this.speed = speed;
	}
	
	public void update() {
		intakeCim.set(speed);
		
		if(sharp.isBallInIntake() && arm.getPos() < IntakeRollerConfig.posThresholdDrop) {
			speed = 0;
		}
		
		else if(sharp.isBallInIntake() && arm.getPos() > IntakeRollerConfig.posThresholdDrop) {
			speed = 0;
		}
	}
	
	public double getSpeed() {
		return speed;
	}
	
}

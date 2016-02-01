package core;

import util.*;
import components.Cim;
import config.IntakeRollerConfig;
import edu.wpi.first.wpilibj.AnalogInput;;

public class IntakeRoller {
	private Cim intakeCim = new Cim(IntakeRollerConfig.intakeMotorChn, IntakeRollerConfig.intakeCimFlip);
	private AnalogInput sharpIR = new AnalogInput(IntakeRollerConfig.sharpIRPort);
	private double speed;
	private IntakeArm arm;
	
	public IntakeRoller(IntakeArm arm) {
		this.arm = arm;
		speed = IntakeRollerConfig.intakeSpeed;
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
		
		if(sharpIR.getVoltage() > IntakeRollerConfig.sharpIRVoltThreshold && arm.getPos() < IntakeRollerConfig.posThresholdDrop) {
			speed = 0;
		}
		
		else if(sharpIR.getVoltage() < IntakeRollerConfig.sharpIRVoltThreshold && arm.getPos() > IntakeRollerConfig.posThresholdDrop) {
			speed = 0;
		}
	}
	
	public double getSpeed() {
		return speed;
	}
	
}

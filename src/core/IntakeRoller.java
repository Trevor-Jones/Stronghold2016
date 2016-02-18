package core;

import sensors.SharpIR;
import util.*;
import components.CIM;
import config.IntakeRollerConfig;

/**
 * Controlls the roller portion of the intake
 * @author Trevor
 *
 */
public class IntakeRoller {
	private CIM intakeCim = new CIM(IntakeRollerConfig.intakeMotorChn, IntakeRollerConfig.intakeCimFlip);
	private double speed;
	private IntakeArm arm;
	public SharpIR sharp;

	/**
	 * 
	 * @param arm
	 * @param sharp
	 */
	public IntakeRoller(IntakeArm arm, SharpIR sharp) {
		this.arm = arm;
		speed = IntakeRollerConfig.intakeSpeed;
		this.sharp = sharp;
	}
	
	/**
	 * Starts the roller intake process with a constant speed
	 */
	public void runIntakeRoller() { 
		speed = IntakeRollerConfig.intakeSpeed;
	}
	
	/**
	 * Sets the roller motor to a raw speed
	 * @param speed 
	 */
	public void setRawSpeed(double speed) {
		intakeCim.set(speed);
	}
	
	/**
	 * Starts the roller intake process with the given speed
	 * @param speed
	 */
	public void setSpeed (double speed) {
		this.speed = speed;
	}
	
	/**
	 * Run periodically to control roller intake process
	 */
	public void update() {
		intakeCim.set(speed);
		
		if(!sharp.isBallInIntake() && arm.getPos() > IntakeRollerConfig.posThresholdDrop) {
			speed = 0;
		}
		
		else if(sharp.isBallInIntake() && arm.getPos() < IntakeRollerConfig.posThresholdPickup) {
			speed = 0;
		}
	}
	
	/**
	 * Returns the speed that the roller is set to
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}
}
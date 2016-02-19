package core;

import util.*;
import edu.wpi.first.wpilibj.Encoder;
import components.CIM;
import config.IntakeArmConfig;

/**
 * Controls the pickup arm
 * @author Trevor
 *
 */
public class IntakeArm {
	private PID pid = new PID(IntakeArmConfig.kP, IntakeArmConfig.kI, IntakeArmConfig.kD);
	private Encoder armEnc; 
	private CIM armMotor = new CIM(IntakeArmConfig.armMotorChn, IntakeArmConfig.armMotorFlipped);
	private double wantPos = 0;
	private double currPos = 0;
	private int posIndex = 0;
	
	private double position [] = 
	{
		IntakeArmConfig.pickupPosition,
		IntakeArmConfig.homePosition
	};
	
	/**
	 * 
	 * @param core
	 */
	public IntakeArm( RobotCore core) {
		
		armEnc = core.armEnc;
		armEnc.setDistancePerPulse(IntakeArmConfig.armEncDistPulse);
		armEnc.reset();
		currPos = armEnc.getDistance();
	}
	
	/**
	 * Sets position to an index in the position array
	 * @param posIndex
	 */
	public void setPos(int posIndex) {
		wantPos = position[posIndex];
		this.posIndex = posIndex;
	}
	
	/**
	 * Sets the arm to a raw position
	 * @param pos
	 */
	public void setRawPos(double pos) {
		wantPos = pos;
	}
	
	/**
	 * Runs periodically to update PID and move arm to wantPos
	 */
	public void update() {
		currPos = armEnc.getDistance();
		pid.update(currPos, wantPos);
		setArmSpeed(Util.limit(pid.getOutput(), IntakeArmConfig.minArmSpeed, IntakeArmConfig.maxArmSpeed));
		System.out.println("Arm Encoder Distance: " + armEnc.getDistance());
	}
	
	/**
	 * Sets the speed of the arm motor
	 * @param speed
	 */
	public void setArmSpeed(double speed) {
		armMotor.set(speed);
	}
	
	/**
	 * 
	 * @return Current position of arm 
	 */
	public double getPos() {
		return currPos;
	}
	
	/**
	 * 
	 * @return Wanted position of arm
	 */
	public double getWantPos() {
		return wantPos;
	}
	
	/**
	 * Toggles the arm between up and down position
	 */
	public void togglePos() {
		if(posIndex == 0) {
			posIndex = 1;
		}
		else if(posIndex == 1) {
			posIndex = 0;
		}
		
		wantPos = position[posIndex];
	}
	
	
}

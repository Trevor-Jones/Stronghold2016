package core;

import util.*;
import edu.wpi.first.wpilibj.Encoder;
import components.Cim;
import config.IntakeArmConfig;

public class IntakeArm {
	private PID pid = new PID(IntakeArmConfig.kP, IntakeArmConfig.kI, IntakeArmConfig.kD);
	private Encoder armEnc = new Encoder(IntakeArmConfig.armEncChnA, IntakeArmConfig.armEncChnB);
	private Cim armMotor = new Cim(IntakeArmConfig.armMotorChn, IntakeArmConfig.armMotorFlipped);
	private double wantPos = 0;
	private double currPos = 0;
	
	private double position [] = 
	{
		IntakeArmConfig.pickupPosition,
		IntakeArmConfig.homePosition
	};
	
	public IntakeArm() {
		armEnc.setDistancePerPulse(IntakeArmConfig.armEncDistPulse);
		armEnc.reset();
		currPos = armEnc.getDistance();
	}
	
	public void setPos(int pos) {
		wantPos = position[pos];
	}
	
	public void setRawPos(double pos) {
		wantPos = pos;
	}
	
	public void update() {
		currPos = armEnc.getDistance();
		pid.update(currPos, wantPos);
		setArmSpeed(Util.limit(pid.getOutput(), IntakeArmConfig.minArmSpeed, IntakeArmConfig.maxArmSpeed));
	}
	
	public void setArmSpeed(double speed) {
		armMotor.set(speed);
	}
	
	public double getPos() {
		return currPos;
	}
	
	public double getWantPos() {
		return wantPos;
	}
}

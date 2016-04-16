package core;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import util.Dashboard;
import util.PID;

public class ShooterWheels implements Runnable {
	
	private boolean stopping;
	private PID leftPID;
	private PID rightPID;
	private Dashboard dash;
	private Encoder leftMotorEnc;
	private Encoder rightMotorEnc;
	private double wantSpeedLeft;
	private double wantSpeedRight;
	private double shootSpeed = 0;
	private double leftRate;
	private double rightRate;

	
	public ShooterWheels(Dashboard dash, RobotCore core) {
		this.dash = dash;
		leftMotorEnc = core.shooterLeftEnc;
		rightMotorEnc = core.shooterRightEnc;
		
		leftPID = new PID(ShooterConfig.kPLeft, ShooterConfig.kILeft, ShooterConfig.kDLeft);
		rightPID = new PID(ShooterConfig.kPRight, ShooterConfig.kIRight, ShooterConfig.kDRight);
	}
	
	@Override
	public void run() {
		while(true) {
			leftRate = leftMotorEnc.getRate();
			rightRate = rightMotorEnc.getRate();
			
			leftPID.updateConstants(dash.getPLeft(), ShooterConfig.kILeft, ShooterConfig.kDLeft);
			rightPID.updateConstants(dash.getPRight(), ShooterConfig.kIRight, ShooterConfig.kDRight);
			
			if(!stopping) {
				leftPID.update(Math.abs(leftRate), shootSpeed);
				rightPID.update(Math.abs(rightRate), shootSpeed);
				wantSpeedLeft+=leftPID.getOutput();
				wantSpeedRight+=rightPID.getOutput();
			}
			
			try {
				Thread.sleep(5);				
			} catch(Exception e) {
				
			}
		}
	}
	
	public void setSpeed() {
		shootSpeed = dash.getSpeed();
		stopping = false;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
	}
	
	public void setSpeed(double shootSpeed) {
		this.shootSpeed = shootSpeed;
		stopping = false;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
	}
	
	public void stop() {
		stopping = true;
		shootSpeed = 0;
	}
	
	public double getWantSpeedLeft() {
		return wantSpeedLeft;
	}
	
	public double getWantSpeedRight() {
		return wantSpeedRight;
	}
	
	public double getLeftRate() {
		return leftRate;
	}
	
	public double getRightRate() {
		return rightRate;
	}

}

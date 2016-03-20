package core;

import components.TwoCimGroup;
import config.DriveConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import util.PID;

import java.lang.Math;

/**
 * Core components that make the robot move
 * 
 * @author Trevor
 *
 */
public class Drive {
	
	private RobotCore robotCore;
	private Encoder encLeft;
	private Encoder encRight;
	public TwoCimGroup leftCimGroup = new TwoCimGroup(DriveConfig.leftC1Chn, DriveConfig.leftC2Chn, DriveConfig.leftC1IsFliped, DriveConfig.leftC2IsFlipped);
	public TwoCimGroup rightCimGroup = new TwoCimGroup(DriveConfig.rightC1Chn, DriveConfig.rightC2Chn, DriveConfig.rightC1IsFlipped, DriveConfig.rightC2IsFlipped);
	public DoubleSolenoid shiftingSol = new DoubleSolenoid(DriveConfig.shiftSolPortA, DriveConfig.shiftSolPortB);
//	private PID drivePID = new PID(DriveConfig.kP, DriveConfig.kI, DriveConfig.kD);
	
	public Drive (RobotCore core) {
		robotCore = core;
		encLeft = core.driveEncLeft;
		encRight = core.driveEncRight;
	}
	
	/**
	 * Moves the robot in a specified direction at a specified velocity
	 * 
	 * @param r velocity between -1 and 1
	 * @param theta angle of joystick in radians
	 * 
	  */
	public void move(double r, double theta) {
		double xPos = r*Math.cos(theta);
		double yPos = r*Math.sin(theta);
		
		double x = xPos * Math.abs(xPos);
		double y = yPos * Math.abs(yPos);
		
		double left = y + x;
		double right = y - x;
        System.out.println("left : " + left + "\tright : " +  right);
        leftCimGroup.set(left);
        rightCimGroup.set(right);
        
//        System.out.println("Drive Encoder Left: " + encLeft.getDistance() + "\tDrive Encoder Right: " + encRight.getDistance());
	}
	
	public void moveNoRamp(double r, double theta) {
		
		double xPos = r*Math.cos(theta);
		double yPos = r*Math.sin(theta);
		
		double x = xPos * Math.abs(xPos);
        double y = yPos * Math.abs(yPos);
		
        double left = y + x;
        double right = y - x;
        
        leftCimGroup.setNoRamp(left);
        rightCimGroup.setNoRamp(right);
	}
	
	public void set(double left, double right) {
		leftCimGroup.set(left);
		rightCimGroup.set(right);
	}
	
	public void setNoRamp(double left, double right) {
		leftCimGroup.setNoRamp(left);
		rightCimGroup.setNoRamp(right);
	}
	
	public void toFastGear() {
		shiftingSol.set(DoubleSolenoid.Value.kReverse);
		System.out.println("fast");
	}
	
	public void toSlowGear() {
		shiftingSol.set(DoubleSolenoid.Value.kForward);
		System.out.println("slow");
	}
}

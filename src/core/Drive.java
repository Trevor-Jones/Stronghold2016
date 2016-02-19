package core;

import components.TwoCimGroup;
import config.DriveConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

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
	public Solenoid shiftingSol = new Solenoid(DriveConfig.shiftSolPort);
	
	public Drive (RobotCore core) {
		robotCore = core;
		encLeft = core.encLeft;
		encRight = core.encRight;
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
        
        leftCimGroup.set(left);
        rightCimGroup.set(right);
        
        System.out.println("Drive Encoder Left: " + encLeft.getDistance() + "\tDrive Encoder Right: " + encRight.getDistance());
	}
	
	public void moveNoRamp(double r, double theta) {
		
		double xPos = r*Math.cos(theta);
		double yPos = r*Math.sin(theta);
		
		double x = xPos * Math.abs(xPos);
        double y = yPos * Math.abs(yPos);
		
        double left = y + x;
        double right = y - x;
        
        System.out.println("left in move: " + left + "\tright in drive: " + right);
        
        leftCimGroup.setNoRamp(left);
        rightCimGroup.setNoRamp(right);
	}
	
	public void set(double left, double right) {
		leftCimGroup.set(left);
		rightCimGroup.set(right);
	}
	
	public void toFastGear() {
		shiftingSol.set(true);
	}
	
	public void toSlowGear() {
		shiftingSol.set(false);
	}
}

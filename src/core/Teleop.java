package core;

import config.IntakeArmConfig;
import config.JoyConfig;

/**
 * Joins user input with core components
 * @author Trevor
 *
 */
public class Teleop {
	private RobotCore robotCore;
	private Drive drive;
	private Intake intake;
	
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Teleop (RobotCore robotCore, Drive drive, Intake intake)
	{
		this.robotCore = robotCore;
		this.drive = drive;
		this.intake = intake;
	}
		
	/**
	 * Periodic functionality including drive
	 */
	public void run() {
		robotCore.joy.update();
		joyDrive();
		joyIntake();
	}
	
	/**
	 * Runs drive code 
	 */
	private void joyDrive() {
		double[] rTheta = robotCore.joy.getRTheta();
		drive.move(rTheta[0], rTheta[1]);		
	}
	
	/**
	 * Runs intake code
	 */
	private void joyIntake() {
		intake.update();
		
		if(robotCore.joy.getButton(JoyConfig.intakeButton)) {
			intake.pickupBall();
			
			if(robotCore.joy.getButton(JoyConfig.cancelButton)) {
				intake.setStep(100);
			}
		}
		
		if(robotCore.joy.getButton(JoyConfig.armUpButton)) {
			intake.arm.setPos(IntakeArmConfig.homePosArray);
		}
		
		if(robotCore.joy.getButton(JoyConfig.armDownButton)) {
			intake.arm.setPos(IntakeArmConfig.pickupPosArray);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerInButton)) {
			intake.roller.setRawSpeed(-1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerOutButton)) {
			intake.roller.setRawSpeed(1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerStopButton)) {
			intake.roller.setRawSpeed(0);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerIntakeButton)) {
			intake.roller.runIntakeRoller();
		}
		
		if(robotCore.joy.getButton(JoyConfig.fastGearButton)) {
			drive.toFastGear();
		}
		
		if(robotCore.joy.getButton(JoyConfig.slowGearButton)) {
			drive.toSlowGear();
		}
	}
}

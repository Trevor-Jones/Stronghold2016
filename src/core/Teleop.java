package core;

import config.*;

/**
 * Joins user input with core components
 * @author Trevor
 *
 */
public class Teleop {
	private RobotCore robotCore;
	private Drive drive;
	private Intake intake;
	private Shooter shooter;
	private Climber climber;
	
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Teleop (RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Climber climber)
	{
		this.robotCore = robotCore;
		this.drive = drive;
		this.intake = intake;
		this.shooter = shooter;
		this.climber = climber;
	}
		
	/**
	 * Periodic functionality including drive
	 */
	public void run() {
		robotCore.joy.update();
		joyDrive();
		joyIntake();
		joyShooter();
		joyClimber();
	}
	
	/**
	 * Runs drive code 
	 */
	private void joyDrive() {
		double[] rTheta = robotCore.joy.getRTheta();
		drive.move(rTheta[0], rTheta[1]);	

		if(robotCore.joy.getDpadUp()) {
			drive.toFastGear();
		}
		
		if(robotCore.joy.getDpadDown()) {
			drive.toSlowGear();
		}
	}
	
	/**
	 * Runs intake code
	 */
	private void joyIntake() {
		intake.update();
		
		if(robotCore.joy.getButton(JoyConfig.intakeButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.pickupBall();
		}
		
		if(robotCore.joy.getButton(JoyConfig.cancelIntakeButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.setStep(100);
		}
		
		if(robotCore.joy.getButton(JoyConfig.armUpButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.arm.setPos(IntakeArmConfig.homePosArray);
		}
		
		if(robotCore.joy.getButton(JoyConfig.armDownButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.arm.setPos(IntakeArmConfig.pickupPosArray);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerInButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setRawSpeed(-1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerOutButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setRawSpeed(1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerStopButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setRawSpeed(0);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerIntakeButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.runIntakeRoller();
		}
		
		if(robotCore.joy.getButton(JoyConfig.toggleArmPosButton)) {
			intake.arm.togglePos();
		}
	}
	
	private void joyShooter() {
		shooter.update();
		
		if(robotCore.joy.getButton(JoyConfig.shootButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.shoot(); //TODO add vision capability
		}
		
		if(robotCore.joy.getButton(JoyConfig.cancelShotButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.cancelShot();
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterConstantSpeedButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.setSpeed(ShooterConfig.constantSpeed);
		}
		
		if(robotCore.joy.getButton(JoyConfig.setShooterSpeedButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.setSpeed();
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterStopButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.setSpeed(0);
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterLaunchButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.launchBall();
		}
	}
	
	private void joyClimber() {
		climber.update();
		
		if(robotCore.joy.getButton(JoyConfig.climbButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton))  {
			climber.climb();
		}
	}
}

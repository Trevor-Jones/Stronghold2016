package core;

import util.Dashboard;
import vision.VisionCore;

public class Test {
	private RobotCore robotCore;
	private Drive drive;
	private Intake intake;
	private Shooter shooter;
	private Climber climber;
	private Dashboard dashboard;
	private VisionCore vision;
	
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Test (RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Climber climber, Dashboard dashboard, VisionCore vision)
	{
		this.robotCore = robotCore;
		this.drive = drive;
		this.intake = intake;
		this.shooter = shooter;
		this.dashboard = dashboard;
		this.vision = vision;
		
//		this.climber = climber;
	}
}

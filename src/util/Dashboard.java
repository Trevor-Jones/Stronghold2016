package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import config.DashboardConfig;

/**
 * Controlls the smartDashboard
 * @author Trevor
 *
 */
public class Dashboard {

	private SendableChooser autoChooser = new SendableChooser();
	private SendableChooser autoGoalChooser = new SendableChooser();
	
	/**
	 * Creates sections of dashboard
	 */
	public Dashboard() {
		autoChooser.addDefault("Do Nothing", new ChooserType(DashboardConfig.idDoNothing));
		autoChooser.addObject("To Auto Zone", new ChooserType(DashboardConfig.idDriveForward));
		SmartDashboard.putData("AutoMode", autoChooser);
		
		autoGoalChooser.addDefault("Don't Shoot", new ChooserType(DashboardConfig.idNoShoot));
		autoGoalChooser.addObject("Left Goal", new ChooserType(DashboardConfig.idLeftGoal));
		autoGoalChooser.addObject("Middle Goal", new ChooserType(DashboardConfig.idMiddleGoal));
		autoGoalChooser.addObject("Right Goal", new ChooserType(DashboardConfig.idRightGoal));
	}
	
	/**
	 * Gives the id of the selected autoType
	 * @return id of auto type
	 */
	public int getAutoType() {
		return ((ChooserType) autoChooser.getSelected()).getId();
	}
	
	/**
	 * Gives the corresponding file name of the autoType selected
	 * @return file name
	 */
	public String getFileName() {
		int id = ((ChooserType) autoChooser.getSelected()).getId();
		
		switch (id) {
			case 0:
				return DashboardConfig.doNothingFileName;
	
			case 1:
				return DashboardConfig.driveForwardFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	/**
	 * Gives which goal to shoot at
	 * @return goal number from 0 to 4
	 */
	public int getGoalNumber() {
		return ((ChooserType) autoGoalChooser.getSelected()).getId();
	}
}
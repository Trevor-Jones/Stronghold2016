package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import config.DashboardConfig;

public class Dashboard {

	private SendableChooser autoChooser = new SendableChooser();
	
	public Dashboard() {
		autoChooser.addDefault("Do Nothing", new ChooserType(DashboardConfig.idDoNothing));
		autoChooser.addObject("To Auto Zone", new ChooserType(DashboardConfig.idDriveForward));
		SmartDashboard.putData("AutoMode", autoChooser);
	}
	
	public int getAutoType() {
		return ((ChooserType) autoChooser.getSelected()).getId();
	}
	
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
}
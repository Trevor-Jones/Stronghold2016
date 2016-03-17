package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import vision.VisionCore;
import config.DashboardConfig;
import config.ShooterConfig;
import config.VisionConfig;

/**
 * Controlls the smartDashboard
 * @author Trevor
 *
 */
public class Dashboard {

	VisionCore vision;
	private SendableChooser autoChooserOne = new SendableChooser();
	private SendableChooser autoChooserTwo = new SendableChooser();
	
	/**
	 * Creates sections of dashboard
	 */
	public Dashboard(VisionCore vision) {
		this.vision = vision;
		
		autoChooserOne.addDefault("Do Nothing", new ChooserType(DashboardConfig.idDoNothing));
		autoChooserOne.addObject("To Auto Zone", new ChooserType(DashboardConfig.idDriveForward));
		autoChooserOne.addObject("Rock Wall", new ChooserType(DashboardConfig.idRockWall));
		autoChooserOne.addObject("Rough Terrain", new ChooserType(DashboardConfig.idRoughTerrain));
		autoChooserOne.addObject("Cheval de Frise", new ChooserType(DashboardConfig.idChevalDeFrise));
		autoChooserOne.addObject("Moat", new ChooserType(DashboardConfig.idMoat));
		SmartDashboard.putData("Auto Mode", autoChooserOne);

		autoChooserTwo.addDefault("Defense One", new ChooserType(DashboardConfig.idDefenseOne));
		autoChooserTwo.addObject("Defense Two", new ChooserType(DashboardConfig.idDefenseTwo));
		autoChooserTwo.addObject("Defense Three", new ChooserType(DashboardConfig.idDefenseThree));
		autoChooserTwo.addObject("Defense Four", new ChooserType(DashboardConfig.idDefenseFour));
		autoChooserTwo.addObject("Defense Five", new ChooserType(DashboardConfig.idDefenseFive));
		SmartDashboard.putData("Auto Mode Two", autoChooserTwo);
		
		SmartDashboard.putDouble("shooterSpeed", ShooterConfig.constantSpeed);
	}
	
	/**
	 * Gives the id of the selected autoType
	 * @return id of auto type
	 */
	public int getAutoType() {
		return ((ChooserType) autoChooserOne.getSelected()).getId();
	}
	
	public void update() {
		SmartDashboard.putBoolean("Connected to Jetson", vision.socket.getSocketStatus());
		for(int i = 0; i < VisionConfig.numberOfGoals; i++){
			SmartDashboard.putString("Goal " + i, vision.vs.goals[i].toString());
		}
		SmartDashboard.putString("xml:", vision.socket.getXML());
	}
	
	/**
	 * Gives the corresponding file name of the autoType selected
	 * @return file name
	 */
	public String getFileNameOne() {
		int id = ((ChooserType) autoChooserOne.getSelected()).getId();
		
		switch (id) {
			case 1:
				return DashboardConfig.driveForwardFileName;
				
			case 2:
				return DashboardConfig.rockWallFileName;
				
			case 3:
				return DashboardConfig.roughTerrainFileName;
				
			case 4:
				return DashboardConfig.chevalDeFriseFileName;
				
			case 5:
				return DashboardConfig.moatFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	public String getFileNameTwo() {
		int id = ((ChooserType) autoChooserTwo.getSelected()).getId();
		
		switch (id) {
			case 0:
				return DashboardConfig.defenseOneFileName;
	
			case 1:
				return DashboardConfig.defenseTwoFileName;

			case 2:
				return DashboardConfig.defenseThreeFileName;

			case 3:
				return DashboardConfig.defenseFourFileName;

			case 4:
				return DashboardConfig.defenseFiveFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	public double getSpeed() {
		return SmartDashboard.getDouble("shooterSpeed");
	}
	
	public void putDouble(String key, double num) {
		SmartDashboard.putDouble(key, num);
	}
}
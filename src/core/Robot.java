
package core;

import sensors.SharpIR;
import util.Dashboard;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotCore robotCore = new RobotCore();
	
	Drive drive = new Drive(robotCore); 
	IntakeArm arm = new IntakeArm(robotCore);
	 
	IntakeRoller roller = new IntakeRoller(arm, robotCore.sharp);
	Intake intake = new Intake(arm, roller);
	Shooter shooter = new Shooter(robotCore);
	Teleop teleop = new Teleop(robotCore, drive, intake, shooter);
	Dashboard dashboard = new Dashboard();
	Interpreter interp = new Interpreter(drive, robotCore, intake, dashboard, shooter);
	Auto auto = new Auto(robotCore, drive, interp, intake);
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

    }

    public void autonomousInit() {
    	interp.interpInit();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	auto.run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        teleop.run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

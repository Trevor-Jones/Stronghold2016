
package core;

import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;

/**
 * Contains all code required for auto
 * @author Trevor
 *
 */
public class Auto {
	public Interpreter interpreter;
	
	/**
	 * 
	 * @param robotCore
	 * @param drive
	 * @param intake
	 * @param shooter
	 * @param dashboard
	 */
	public Auto(RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Dashboard dashboard){
		interpreter = new Interpreter(drive, robotCore, intake, dashboard, shooter);
	}
	
	/**
	 * Periodically runs through autonomous actions
	 */
	public void run(){
		interpreter.dispatch();
	}
}



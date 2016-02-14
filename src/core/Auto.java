
package core;

import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;

public class Auto {
	public Interpreter interpreter;
	
	public Auto(RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Dashboard dashboard){
		interpreter = new Interpreter(drive, robotCore, intake, dashboard, shooter);
	}
	
	/**
	 * Runs through autonomous actions
	 */
	public void run(){
		interpreter.dispatch();
	}
}



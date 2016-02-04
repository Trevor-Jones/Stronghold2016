
package core;

import edu.wpi.first.wpilibj.Timer;

public class Auto {
	RobotCore robotcore;
	Drive drive;
	Intake intake;
	Interpreter interpreter;
	Timer tm = new Timer();
	
	private boolean isRunning = false;	//Whether the timer is running or not
	private int counter = 0;
	
	public Auto(RobotCore robotCore, Drive drive, Interpreter interpreter, Intake intake){
		this.robotcore = robotCore;
		this.drive = drive;
		this.intake = intake;
		this.interpreter = interpreter;
	}
	
	/**
	 * Runs through autonomous actions
	 */
	public void run(){
		interpreter.dispatch();
	}
}



package core;

import util.PID;
import components.CIM;
import config.ClimberConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 * Components that control the climber mechanism
 * @author Trevor
 *
 */
public class Climber {
	CIM climberMotor = new CIM(ClimberConfig.chnClimberMotor, false);
	Solenoid climberSol = new Solenoid(ClimberConfig.chnSol);

	Encoder climberEnc;

	boolean isClimb = false;
	int step = 0;
	double wantSpeed = 0;

	/**
	 * 
	 * @param core
	 */
	public Climber(RobotCore core) {
		this.climberEnc = core.climberEnc;
		this.climberEnc.setDistancePerPulse(ClimberConfig.maxRPM);
	}

	/**
	 * Run periodically to run through actions for climbing
	 */
	public void update() {
		switch (step) {
			case 0:
				if (isClimb) {
					climberSol.set(true);
					climberMotor.ramp(ClimberConfig.upSpeed);
					climberEnc.reset();
					step++;
				}
				break;
	
			case 1:
				if (isClimb && (climberEnc.getDistance() > ClimberConfig.distance)) {
					climberMotor.ramp(ClimberConfig.downSpeed);
					climberSol.set(false);
					climberEnc.reset();
					step++;
				}
				break;
				
			case 2:
				if (isClimb && (climberEnc.getDistance() > ClimberConfig.distance)) {
					climberMotor.set(0);
					step = 0;
					isClimb = false;
				}
				break;
	
			default:
				break;
		}
	}

	/**
	 * Starts the climbing sequence
	 */
	public void climb() {
		isClimb = true;
		climberEnc.reset();
	}
}

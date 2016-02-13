package core;

import util.PID;
import components.CIM;
import config.ClimberConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber
{
	CIM climberMotor = new CIM(ClimberConfig.chnAClimber, false);
	Solenoid climberSol = new Solenoid(ClimberConfig.chnSol);

	Encoder climberEnc;

	PID climberPID = new PID(ClimberConfig.kP, 0, 0);

	boolean isClimb = false;
	int step = 0;
	long time;
	double wantSpeed = 0;

	public Climber(RobotCore core)
	{
		this.climberEnc = core.climberEnc;
		this.climberEnc.setDistancePerPulse(ClimberConfig.maxRPM);

	}

	public void update()
	{

		climberPID.update(climberEnc.getRate(), wantSpeed);
		climberMotor.set(climberPID.getOutput());

		switch (step)
		{
		case 0:
			if (isClimb)
			{
				climberSol.set(true);
				wantSpeed = ClimberConfig.down;
				time = System.currentTimeMillis() + ClimberConfig.waitTime;
				step++;

			}
			break;

		case 1:
			if (isClimb && (System.currentTimeMillis() > time))
			{
				wantSpeed = ClimberConfig.up;
				climberSol.set(false);
				time = System.currentTimeMillis() + ClimberConfig.waitTime;
				step++;
			}
			break;
		case 2:
			if (isClimb && (System.currentTimeMillis() > time))
			{
				wantSpeed = 0;
				step = 0;
				time = 0;
				isClimb = false;
			}
			break;

		default:
			break;
		}

	}

	public void climb()
	{
		isClimb = true;
	}

}

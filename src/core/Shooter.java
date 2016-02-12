package core;

import java.util.Timer;

import components.CIM;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class Shooter
{

	public Encoder motorOneEnc;
	public Encoder motorTwoEnc;
	public Solenoid solOne;
	public CIM motorOne = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM motorTwo = new CIM(ShooterConfig.ChnMotorTwo, false);
	private boolean isShooting;
	private double shootSpeed;
	double currentPos;
	double waitDistance;

	public Shooter(RobotCore core)
	{
		motorOneEnc = core.motorOneEnc;
		motorTwoEnc = core.motorTwoEnc;
		solOne = core.solOne;

		motorOneEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);
		motorTwoEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);

		motorOneEnc.reset();
		motorTwoEnc.reset();

		solOne.set(false);
		motorOne.set(0);
		motorTwo.set(0);
		isShooting = false;
	}

	public void update()
	{

		if (isMotorsFastEnough(shootSpeed) && isShooting)
		{
			solOne.set(true);

			currentPos = motorOneEnc.getDistance();

			if ((motorOneEnc.getDistance() - currentPos) < waitDistance
					&& isMotorsFastEnough(shootSpeed))
			{
				solOne.set(false);
				motorOne.set(0);
				motorTwo.set(0);
				isShooting = false;

			}

		}


	}

	public void shoot(double shootSpeed)
	{
		isShooting = true;
		this.shootSpeed = shootSpeed;
		waitDistance = ShooterConfig.waitTime * motorOneEnc.getRate();
		motorOne.set(shootSpeed);
		motorTwo.set(shootSpeed);
	}

	public boolean isMotorsFastEnough(double motorSpeed)
	{
		return (motorOneEnc.getRate() > motorSpeed && motorTwoEnc.getRate() > motorSpeed);
	}

}

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

	public Shooter(RobotCore core)
	{
		motorOneEnc = core.motorOneEnc;
		motorTwoEnc = core.motorTwoEnc;
		solOne = core.solOne;

		motorOneEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);
		motorTwoEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);

		motorOneEnc.reset();
		motorTwoEnc.reset();

	}

	public void shoot()
	{

		motorOne.set(1.0);
		motorTwo.set(1.0);

		if (isMotorsFastEnough())
			solOne.set(true);

		double currentPos = motorOneEnc.getDistance();
		double waitDistance = ShooterConfig.waitTime * motorOneEnc.getRate();
		if ((motorOneEnc.getDistance() - currentPos) < waitDistance
				&& isMotorsFastEnough())
		{
			solOne.set(false);
			motorOne.set(0);
			motorTwo.set(0);
		}

	}

	public boolean isMotorsFastEnough()
	{
		boolean answer = false;
		if (motorOneEnc.getRate() > ShooterConfig.motorWantSpeed
				&& motorTwoEnc.getRate() > ShooterConfig.motorWantSpeed)
			answer = true;
		return answer;
	}

}

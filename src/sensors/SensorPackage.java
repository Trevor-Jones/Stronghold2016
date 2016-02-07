package sensors;

import config.IntakeArmConfig;
import edu.wpi.first.wpilibj.Encoder;

public class SensorPackage
{
	public Encoder armEnc; 
	 public SensorPackage(int armEncChnA, int armEncChnB)
	{
		armEnc = new Encoder(armEncChnA, armEncChnB);
	}
}

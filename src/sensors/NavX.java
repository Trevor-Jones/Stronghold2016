package sensors;

import config.NavXConfig;
import edu.wpi.first.wpilibj.SerialPort;
import lib.navX.IMUAdvanced;

public class NavX extends IMUAdvanced
{
	private double offsetAngle = 0;
	private static SerialPort serial = new SerialPort(NavXConfig.baudRate, SerialPort.Port.kMXP); 
		
	public NavX()
	{
		super(serial, NavXConfig.updateRateHz);
	}

	public double getAngle()
	{
		return super.getYaw() +  offsetAngle;
	}
	
	public void setOffsetAngle(double ang)
	{
		offsetAngle = ang;
	}
}
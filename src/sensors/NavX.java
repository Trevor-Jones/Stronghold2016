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

	/**
	 * 	@return angle in radians
	 */
	public double getAngle()
	{
		float angle = super.getYaw() * ((float) Math.PI/180);
		
		return angle;
	}
	
	public void setOffsetAngle(double ang)
	{
		offsetAngle = ang;
	}
}
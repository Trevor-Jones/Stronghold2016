package config;

public class IntakeArmConfig {
	
	// PID constants
	public static final double kP = 0;
	public static final double  kI = 0;
	public static final double kD = 0;
	
	// Encoder channels for arm motors
	public static final int armEncChnA = 12;
	public static final int armEncChnB = 13;
	
	// Encoder config
	public static final double armEncDistPulse = 1;
	
	// Motor config
	public static final boolean armMotorFlipped = false;
	public static final int armMotorChn = 11;
	public static final double minArmSpeed = -1;
	public static final double maxArmSpeed = 1;
	
	// Distance levels
	public static final double pickupPosition = 0;
	public static final double homePosition = 10;
	
	// Distance array values
	public static final int pickupPosArray = 0;
	public static final int homePosArray = 1;
	public static final double getVoltageThreshold = 0;
	
}

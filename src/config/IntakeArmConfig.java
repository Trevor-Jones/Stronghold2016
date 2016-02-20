package config;

public class IntakeArmConfig {
	
	// PID constants
	public static final double kPUp = 0.4;
	public static final double  kIUp = 0;
	public static final double kDUp = 0;
	
	public static final double kPDown = 0.08;
	public static final double  kIDown = 0;
	public static final double kDDown = 0;
	
	// Encoder channels for arm motors
	public static final int armEncChnA = 8;
	public static final int armEncChnB = 9;
	
	// Encoder config
	public static final double armEncDistPulse = 0.03369;
	
	// Motor config
	public static final boolean armMotorFlipped = true;
	public static final int armMotorChn = 7;
	public static final double minArmSpeed = -0.7;
	public static final double maxArmSpeed = 0.7;
	
	// Distance levels
	public static final double pickupPosition = 0;
	public static final double homePosition = -9.7;
	
	// Distance array values
	public static final int pickupPosArray = 0;
	public static final int homePosArray = 1;
	public static final double getVoltageThreshold = 0;
	
}

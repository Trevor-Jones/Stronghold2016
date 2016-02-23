package config;

public class ShooterConfig
{

	public static final int ChnAMotorOneEnc = 2;
	public static final int ChnBMotorOneEnc = 3;
	public static final int ChnBMotorTwoEnc = 7;
	public static final int ChnAMotorTwoEnc = 6;
	public static final int ChnSolA = 0;
	public static final int ChnSolB = 1;
	public static final double distancePerPulseRight = -0.0000547;
	public static final double distancePerPulseLeft = 0.0000547;
	public static final int ChnMotorOne = 9;
	public static final int ChnMotorTwo = 8;
	public static final double waitTime = 0.5;
	
	public static final double kPLeft = 1;
	public static final double kILeft = 0;
	public static final double kDLeft = 0;
	
	public static final double kPRight = 1;
	public static final double kIRight = 0;
	public static final double kDRight = 0;

	public static final double kPDrive = 1;
	public static final double kIDrive= 0;
	public static final double kDDrive = 0;
	
	public static final double angTolerance = 20;
	public static final double turnTime = 0.5;
	public static final double distanceSpeedConstant = 1;
	
	public static final double constantSpeed = 0.3;
	public static final double motorSpeedTolerance = 0.1;
	
	public static final double[] distanceToVelocity = {};
	public static final int maxDistanceVision = 100;
	
}

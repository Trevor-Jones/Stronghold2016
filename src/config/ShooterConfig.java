package config;

public class ShooterConfig
{

	public static final int ChnAMotorOneEnc = 2;
	public static final int ChnBMotorOneEnc = 3;
	public static final int ChnBMotorTwoEnc = 7;
	public static final int ChnAMotorTwoEnc = 6;
	public static final int ChnSolA = 0;
	public static final int ChnSolB = 1;
	public static final double distancePerPulseRight = -0.00004;
	public static final double distancePerPulseLeft = 0.000045;
	public static final int ChnMotorOne = 9;
	public static final int ChnMotorTwo = 8;
	public static final double waitTime = 0.5;
	public static final double waitTimeStop = 1;
	
	public static final double kPLeft = .02;
	public static final double kILeft = 0;
	public static final double kDLeft = 0;
	
	public static final double kPRight = .02;
	public static final double kIRight = 0;
	public static final double kDRight = 0;

	public static final double kPDrive = 0.018;
	public static final double kIDrive= 0;
	public static final double kDDrive = 0;

	public static final double kPDriveClose = 0.025;
	public static final double kIDriveClose = 0;
	public static final double kDDriveClose = 0;
	
	public static final double angTolerance = 8;
	public static final double turnTime = 2;
	public static final double distanceSpeedConstant = .009;
	
	public static final double constantSpeed = 0.52;
	public static final double motorSpeedTolerance = .1;
	
	public static final int maxDistanceVision = 100;
	
	public static final double rotateMaxSpeed = 0.55;
	public static final double rotateMinSpeed = 0.2;
	public static final double rotateMaxSpeedClose = 0.35;
	public static final double rotateMinSpeedClose = 0.15;
	public static final double startSpeedScalar = 0.3;
	
	public static final double changePIDAng = 20;
	
}

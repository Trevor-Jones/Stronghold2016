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
	public static final double distancePerPulseLeft = 0.000050;
	public static final int ChnMotorOne = 9;
	public static final int ChnMotorTwo = 8;
	public static final double waitTime = 0.5;
	public static final double waitTimeStop = 0.5;
	
	public static final double kPLeft = .05;
	public static final double kILeft = 0;
	public static final double kDLeft = 0;
	
	public static final double kPRight = .05;
	public static final double kIRight = 0;
	public static final double kDRight = 0;

	public static final double kPDrive = 0.012;
	public static final double kIDrive= 0;
	public static final double kDDrive = 0;
	
	public static final double angTolerance = 5;
	public static final double turnTime = 0.6;
	public static final double distanceSpeedConstant = .009;
	
	public static final double constantSpeed = 0.68;
	public static final double motorSpeedTolerance = 0;
	
	public static final double[] distanceToVelocity = {.2,.3,.4,.5,.6,.7,.8,.9,1};
	public static final int maxDistanceVision = 100;
	
	public static final double rotateMaxSpeed = 0.4;
	public static final double startSpeedScalar = 0.7;
	
}

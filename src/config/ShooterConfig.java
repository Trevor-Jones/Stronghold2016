package config;


public class ShooterConfig
{

	public static final int ChnAMotorOneEnc = 7;//TODO Fix
	public static final int ChnBMotorOneEnc = 8; //TODO fix
	public static final int ChnBMotorTwoEnc = 9;
	public static final int ChnAMotorTwoEnc = 10;
	public static final int ChnSol = 0;
	public static final double motorWantSpeed = 0.5;
	public static final double distancePerPulse = 0.15;
	public static final int ChnMotorOne = 12;
	public static final int ChnMotorTwo = 13;
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
	
	public static final double constantSpeed = 0.5;
	
}

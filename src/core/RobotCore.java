package core;

import config.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import sensors.*;

/**
 * Contains various sensors such as joysticks and accelerometers
 * 
 * @author Trevor
 *
 */
public class RobotCore {
	public MyJoystick joy = new MyJoystick(JoyConfig.drivePort);
	public Enc driveEncRight = new Enc(DriveConfig.chnAEncRight,DriveConfig.chnBEncRight,DriveConfig.encRightDisPerPulse);
	public Enc driveEncLeft = new Enc(DriveConfig.chnAEncLeft,DriveConfig.chnBEncLeft,DriveConfig.encLeftDisPerPulse);
	public NavX navX = new NavX();
	public Encoder armEnc = new Encoder(IntakeArmConfig.armEncChnA, IntakeArmConfig.armEncChnB);
	public SharpIR sharp = new SharpIR();
	public Encoder shooterOneEnc = new Encoder(ShooterConfig.ChnAMotorOneEnc, ShooterConfig.ChnBMotorOneEnc);
	public Encoder shooterTwoEnc = new Encoder(ShooterConfig.ChnAMotorTwoEnc, ShooterConfig.ChnBMotorTwoEnc);
	public DoubleSolenoid shooterSol = new DoubleSolenoid(ShooterConfig.ChnSolA, ShooterConfig.ChnSolB);
	public PowerDistributionPanel pdp = new PowerDistributionPanel();
//	public Encoder climberEnc = new Encoder(ClimberConfig.ChnAEnc,ClimberConfig.ChnBEnc);
	
	public RobotCore(){
		
		shooterOneEnc.setDistancePerPulse(ShooterConfig.distancePerPulseLeft);
		shooterTwoEnc.setDistancePerPulse(ShooterConfig.distancePerPulseRight);

		shooterOneEnc.reset();
		shooterTwoEnc.reset();
		
		driveEncRight.setDistancePerPulse(DriveConfig.encRightDisPerPulse);	
		driveEncLeft.setDistancePerPulse(DriveConfig.encLeftDisPerPulse);
		
		driveEncRight.reset();
		driveEncLeft.reset();

	}

}
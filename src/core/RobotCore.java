package core;

import config.*;
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
	public Enc encRight = new Enc(DriveConfig.chnAEncRight,DriveConfig.chnBEncRight,DriveConfig.encRightDisPerPulse);
	public Enc encLeft = new Enc(DriveConfig.chnAEncLeft,DriveConfig.chnBEncLeft,DriveConfig.encLeftDisPerPulse);
	public NavX navX = new NavX();
	public Encoder armEnc = new Encoder(IntakeArmConfig.armEncChnA, IntakeArmConfig.armEncChnB);
	public SharpIR sharp = new SharpIR();
	public Encoder motorOneEnc = new Encoder(ShooterConfig.ChnAMotorOneEnc, ShooterConfig.ChnBMotorOneEnc);
	public Encoder motorTwoEnc = new Encoder(ShooterConfig.ChnAMotorTwoEnc, ShooterConfig.ChnBMotorTwoEnc);
	public Solenoid solOne = new Solenoid(ShooterConfig.ChnSol);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
		public Encoder climberEnc = new Encoder(ClimberConfig.ChnAEnc,ClimberConfig.ChnBEnc);
	

}
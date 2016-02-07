package core;

import java.util.Timer;

import config.ShooterConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

public class Shooter { 
	
	public Encoder motorOneEnc;
	public Encoder motorTwoEnc;
	public Solenoid solOne;
	public Shooter(RobotCore core){
		motorOneEnc = core.motorOneEnc;
		motorTwoEnc = core.motorTwoEnc;
		solOne = core.solOne;
			
		motorOneEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);
		motorOneEnc.setDistancePerPulse(ShooterConfig.distancePerPulse);
		
		motorOneEnc.reset();
		motorTwoEnc.reset();
	
	}
	
	public void shoot(){
		
		while(!isMotorsFastEnough()){}
		solOne.set(true);
		
		double currentPos = motorOneEnc.getDistance();
		
		double	waitDistance = ShooterConfig.waitTime * motorOneEnc.getRate();
		
		while((motorOneEnc.getDistance() - currentPos) < waitDistance ){}
	
	
	
	}
	public boolean isMotorsFastEnough(){
		boolean answer = false;
		if(motorOneEnc.getRate() > ShooterConfig.motorWantSpeed && motorTwoEnc.getRate() > ShooterConfig.motorWantSpeed) answer = true;
		return answer;
	}	
	
}


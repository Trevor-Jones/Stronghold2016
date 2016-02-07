package sensors;

import config.IntakeRollerConfig;
import edu.wpi.first.wpilibj.AnalogInput;
import config.SharpConfig;

public class SharpIR
{

	private AnalogInput sharpIR = new AnalogInput(IntakeRollerConfig.sharpIRPort);

	public double getDistance(){		
	return (sharpIR.getValue() * SharpConfig.scalingFactor);
	}
	
	public boolean isBallInIntake(){
		
		boolean isBallInIntake;
		
		if(this.getDistance() < IntakeRollerConfig.sharpIRDistance) isBallInIntake = true;
		else {isBallInIntake = false;}
		
		return isBallInIntake;
	}
	
}

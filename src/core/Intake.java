package core;

import config.IntakeArmConfig;
import config.IntakeConfig;
import util.Util;

/**
 * Link between roller and arm
 * @author Trevor
 *
 */
public class Intake {
	IntakeArm arm;
	IntakeRoller roller;
	private int step;
	private boolean isFirst = true;
	
	/**
	 * 
	 * @param arm
	 * @param roller
	 */
	public Intake (IntakeArm arm, IntakeRoller roller) {
		this.arm = arm;
		this.roller = roller;
		step = 100;
	}
	
	/**
	 * Resets and starts pickup process
	 */
	public void pickupBall() {
		step = 0;
	}
	
	/**
	 * Run periodically to update arm, roller, and pickup process
	 */
	public void update() {
		arm.update();
		roller.update();
		
//		System.out.println("Step: " + step + "\tRoller speed: " + roller.getSpeed());
		
		switch(step) {
			case 0:
				arm.setPos(IntakeArmConfig.pickupPosArray);
				if(Util.withinThreshold(arm.getPos(), arm.getWantPos(), IntakeConfig.armPosThreshold)) 
					step++;
				break;
			
			case 1:
				if(isFirst) {
					roller.runIntakeRoller();
					isFirst = false;
				}
				
				if(roller.getSpeed() == 0){
					step++;
					isFirst = true;
				}
				break;
				
			case 2:
				arm.setPos(IntakeArmConfig.homePosArray);
				if(Util.withinThreshold(arm.getPos(), arm.getWantPos(), IntakeConfig.armPosThreshold)) 
					step++;
				break;
			
			case 3:
				if(isFirst) {
					roller.runIntakeRoller();
					isFirst = false;
				}
				if(roller.getSpeed() == 0){
					step++;
					isFirst = true;
				}
				break;
			
			case 4:
				roller.setSpeed(0);
				break;
		}
	}
	
	/**
	 * Sets the pickup process to a specific step
	 * @param step
	 */
	public void setStep(int step) {
		this.step = step;
	}
}
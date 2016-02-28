package vision;

import config.VisionConfig;
import vision.Goal;


public class VisionStruct {
	 public Goal goals[] = new Goal[VisionConfig.numberOfGoals]; 
	public int frameNumber = 0;	

	
	public Goal getGoal(int goalNumber){
		return goals[goalNumber];
	}
	
	public double getDistance(int goalNumber) {
		return this.goals[goalNumber].distance;
	}
	
	public double getRotation(int goalNumber) {
		return this.goals[goalNumber].rotation;
	}
	
	public double getTranslation(int goalNumber) {
		return this.goals[goalNumber].translation;
	}
	
	public double getArea(int goalNumber) {
		return this.goals[goalNumber].area;
	}
	
	public int getHighestArea() {
		int goalNumber = 0;
		for(int i = 1; i < this.goals.length; i++) {
			if(this.goals[i].area > this.goals[i-1].area){
				goalNumber = i;
			}
		}
		return goalNumber;
	}
	

}
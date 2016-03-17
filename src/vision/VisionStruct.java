package vision;

import config.VisionConfig;
import vision.Goal;


public class VisionStruct {
	public Goal goals[] = new Goal[VisionConfig.numberOfGoals]; 
	public int frameNumber = 0;	

	public VisionStruct(){
		for(int i = 0; i < goals.length; i++) {
			goals[i] = new Goal();
		}
	}
	
	public Goal getGoal(int goalNumber){
		return goals[goalNumber];
	}
	
	public double getDistance(int goalNumber) {
		return goals[goalNumber].distance;
	}
	
	public double getRotation(int goalNumber) {
		return -goals[goalNumber].rotation + VisionConfig.angleOffset;
	}
	
	public double getTranslation(int goalNumber) {
		return goals[goalNumber].translation;
	}
	
	public double getArea(int goalNumber) {
		return goals[goalNumber].area;
	}
	
	public int getHighestArea() {
		int goalNumber = 0;
		for(int i = 1; i < goals.length; i++) {
			if(goals[i].area > goals[i-1].area){
				goalNumber = i;
			}
		}
		return goalNumber;
//		return 0;
	}
	

}
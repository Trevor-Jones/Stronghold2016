package vision;

import config.VisionConfig;
import vision.Goal;


public class VisionStruct {
	Goal goals[] = new Goal[VisionConfig.numberOfGoals]; 
	int frameNumber = 0;	
}
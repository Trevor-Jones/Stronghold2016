package vision;

import config.VisionConfig;

public class VisionCore {

	SocketClient skt = new SocketClient();
	ImageParser img = new ImageParser();	

	public VisionStruct getVisionData(){return img.parseString(skt.updateVision());}
	
	public int[] getShootingGoal(){
		
		int[] transrot;
	if( == 1) transrot = this.getVisionData().goalOne; 
		
	}
	
}

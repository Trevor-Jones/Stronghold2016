package vision;

import util.ChooserType;
import util.Dashboard;
import config.VisionConfig;

public class VisionCore {

	SocketClient skt = new SocketClient();
	ImageParser img = new ImageParser();	
	Dashboard dash;
	
	public VisionCore(Dashboard dash){
		this.dash = dash;
		
		
	}
	public VisionStruct getVisionData(){return img.parseString(skt.updateVision());}
	
	public int[] getShootingGoal(){
		
		int[] transrot = null;

 		return transrot; 
	}
	
	

}

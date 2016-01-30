package core;

public class Steps {

	public static int getStep(Type type) {
		
		int i;
		
		switch (type) {
		case DRIVE:
			i = 0;
			break;
		case WAIT_TIMER:
			i=1;
			break;
		case WAIT_GYRO:
			i=2;
			break;
		case TURN:
			i=3;
			break;
		default:
			i = -1; 
			break;
		}
		return i;
	}
	
}

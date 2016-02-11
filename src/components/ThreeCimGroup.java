package components;

/**
 * Wraps set of three motors that make up one side of the robot
 * @author Trevor
 *
 */
public class ThreeCimGroup {

	public CIM c1;
	public CIM c2;
	public CIM c3;
	boolean m1IsReverse;
	boolean m2IsReverse;
	boolean m3IsReverse;
	
	/**
	 * Creates grouping given three motors
	 * @param m1Chn
	 * @param m2Chn
	 * @param m3Chn
	 */
	public ThreeCimGroup(int m1Chn, int m2Chn, int m3Chn, boolean m1IsFlipped, boolean m2IsFlipped ,boolean m3IsFlipped){
		c1 = new CIM(m1Chn, m1IsFlipped);
		c2 = new CIM(m2Chn, m2IsFlipped);
		c3 = new CIM(m3Chn, m3IsFlipped);
	}
	
	/**
	 * Sets all motors to given speed with ramping
	 * @param velocity between -1 to 1
	 */
	public void set(double velocity) {
		c1.ramp(velocity);
		c2.ramp(velocity);
		c3.ramp(velocity);
	}
	
	/**
	 * Sets all motors to given speed without ramping
	 * @param velocity
	 */
	public void setNoRamp(double velocity) {
		c1.set(velocity);
		c2.set(velocity);
		c3.set(velocity);
	}
	
}

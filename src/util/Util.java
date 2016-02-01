package util;

public class Util {

	public static double limit(double num, double min, double max) {
		if(num < min)
			return min;
		
		if(num > max)
			return max;
		
		return num;
	}
	
	public static boolean withinBounds(double num, double min, double max) {
		return (num < max && num > min);
	}
	
	public static boolean withinThreshold(double currNum, double wantNum, double threshold) {
		return (currNum < (wantNum+threshold) && currNum > (wantNum-threshold));
	}
}

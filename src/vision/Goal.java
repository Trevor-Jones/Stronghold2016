package vision;

import org.w3c.dom.Element;

public class Goal {
	
	public double rotation;
	public double distance;
	public double translation;
	public double area;
	
	public Goal(){}
	public Goal(Element n){
			this.translation = Integer.parseInt(n.getAttribute("translation"));
			this.rotation = Integer.parseInt(n.getAttribute("rotation"));
			this.distance = Integer.parseInt(n.getAttribute("distance"));
			this.area = Integer.parseInt(n.getAttribute("area"));	
	}


	@Override
	public String toString() {
	return "Translation:" + this.translation + "\tRotation" + this.rotation;
	}	
	
}


package expAI;

import java.awt.Point;

import map.Map;
import resources.*;

public class ExpDrone extends Drone {
	
	public ExpDrone(Map map, Point position) {
		super(map, position);
		this.TYPE = "EXPDRONE";
	}

}

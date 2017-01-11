package resources;

import java.awt.Point;
import java.util.UUID;
import map.Map;

public class ExplorationDrone extends Resource {
	
	public ExplorationDrone(Map map, Point center, String shape, int size){
		super(map, center, shape, size);
		type = "EXPDRONE";
		harvestable = false;
		pathable = false;
	}
}

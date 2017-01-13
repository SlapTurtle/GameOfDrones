package resources;

import java.awt.Point;
import java.util.UUID;
import map.Map;

public class ExplorationDrone extends Resource {
	
	public static final String type = "EXPDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = true;
	
	public ExplorationDrone(Point center, String shape, int size){
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}
}
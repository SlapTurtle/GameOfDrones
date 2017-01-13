package resources;

import java.awt.Point;

import map.Map;

public class HarvestingDrone extends Resource {
	
	public static final String type = "HARDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = false;
	
	public HarvestingDrone(Point center, String shape, int size){
			super(center, shape, size);
			super.pathable = pathable;
			super.harvestable = harvestable;
			super.type = type;
	}	
}
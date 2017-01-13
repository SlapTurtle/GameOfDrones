package resources;

import java.awt.Point;

import map.Map;

public class HarvestingDrone extends Resource {
	
	public static final String TYPE = "HARDRONE";
	public static boolean harvestable = false;
	public static boolean pathable = true;
	
	public HarvestingDrone(Point center, String shape, int size){
			super(center, shape, size);
			type = "HARDRONE";
			harvestable = false;
			pathable = false;
	}	
}
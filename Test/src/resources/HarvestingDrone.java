package resources;

import java.awt.Point;

import map.Map;

public class HarvestingDrone extends Resource {
	
	public HarvestingDrone(Map map, Point center, String shape, int size){
			super(map, center, shape, size);
			type = "HARDRONE";
			harvestable = false;
			pathable = false;
	}	
}
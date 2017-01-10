package resources;

import java.awt.Point;

import map.Map;

public class Empty extends Resource {
	
	public Empty(Map map, Point center, String shape, int size){
		super(map, center, shape, size);
		type = "EMPTY";
		harvestable = false;
		pathable = true;
	}
}
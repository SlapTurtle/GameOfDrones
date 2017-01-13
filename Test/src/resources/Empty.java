package resources;

import java.awt.Point;

import map.Map;

public class Empty extends Resource {
	
	public static final String TYPE = "EMPTY";
	public static boolean harvestable = false;
	public static boolean pathable = true;
	
	public Empty(Map map, Point center, String shape, int size){
		super(map, center, shape, size);
		type = "EMPTY";
		harvestable = false;
		pathable = true;
	}
}
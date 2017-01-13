package resources;

import java.awt.Point;

import map.Map;

public class Water extends Resource {

	public static final String TYPE = "WATER";
	public static boolean harvestable = false;
	public static boolean pathable = false;
	
	public Water(Point center, String shape, int size) {
		super(center, shape, size);
		type = "WATER";
		harvestable = false;
		pathable = false;
	}

}
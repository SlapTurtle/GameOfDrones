package resources;

import java.awt.Point;

import map.Map;

public class Base extends Resource {

	public static final String TYPE = "BASE";
	public static boolean harvestable = false;
	public static boolean pathable = false;
	
	public Base(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "BASE";
		harvestable = false;
		pathable = false;
	}

}
package resources;

import java.awt.Point;

import map.Map;

public class Rock extends Resource {

	public static final String TYPE = "ROCK";
	public static boolean harvestable = false;
	public static boolean pathable = false;
	
	public Rock(Point center, String shape, int size) {
		super(center, shape, size);
		type = "ROCK";
		harvestable = false;
		pathable = false;
	}

}
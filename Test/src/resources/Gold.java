package resources;

import java.awt.Point;

import map.Map;

public class Gold extends Resource {

	public static final String TYPE = "GOLD";
	public static boolean harvestable = false;
	public static boolean pathable = false;

	public Gold(Point center, String shape, int size) {
		super(center, shape, size);
		type = "GOLD";
		harvestable = true;
		pathable = true;
	}

}
package resources;

import java.awt.Point;

public class Water extends Resource {

	public static final String type = "WATER";
	public static boolean harvestable = false;
	public static boolean pathable = false;
	
	public Water(Point center, String shape, int size) {
		super(center, shape, size, pathable, harvestable, type);
	}
}
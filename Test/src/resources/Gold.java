package resources;

import java.awt.Point;

public class Gold extends Resource {

	public static final String type = "GOLD";
	public static final boolean harvestable = true;
	public static final boolean pathable = false;

	public Gold(Point center, String shape, int size) {
		super(center, shape, size, pathable, harvestable, type);
	}

}
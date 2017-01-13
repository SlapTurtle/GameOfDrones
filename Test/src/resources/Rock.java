package resources;

import java.awt.Point;

public class Rock extends Resource {

	public static final String type = "ROCK";
	public static final boolean harvestable = false;
	public static final boolean pathable = false;
	
	public Rock(Point center, String shape, int size) {
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}
}
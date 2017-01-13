package resources;

import java.awt.Point;

public class Base extends Resource {

	public static final String type = "BASE";
	public static final boolean harvestable = false;
	public static final boolean pathable = false;
	
	public Base(Point center, String shape, int size) {
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}
}
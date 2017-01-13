package resources;

import java.awt.Point;

public class Empty extends Resource {
	
	public static final String type = "EMPTY";
	public static final boolean harvestable = false;
	public static final boolean pathable = true;
	
	public Empty(Point center, String shape, int size){
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}
}
package resources;

import java.awt.Point;

public class Exp_drone extends Resource {
	
	public static final String type = "EXPDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = true;
	
	public Exp_drone(Point center, String shape, int size){
		super(center, shape, size);
		super.pathable = pathable;
		super.harvestable = harvestable;
		super.type = type;
	}
}
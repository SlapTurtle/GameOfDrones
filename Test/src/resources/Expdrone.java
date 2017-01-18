package resources;

import java.awt.Point;

public class Expdrone extends Resource {
	
	public static final String type = "EXPDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = true;
	
	public String id;
	
	public Expdrone(Point center, String shape, int size, String id){
		super(center, shape, size, pathable, harvestable, type);
	}
}
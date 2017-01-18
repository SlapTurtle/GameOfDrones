package resources;

import java.awt.Point;

public class Hardrone extends Resource {
	
	public static final String type = "HARDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = false;
	
	public String id;
	
	public Hardrone(Point center, String shape, int size, String id){
		super(center, shape, size, pathable, harvestable, type);
	}	
}
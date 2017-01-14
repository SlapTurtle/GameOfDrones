package resources;

import java.awt.Point;

public class Har_drone extends Resource {
	
	public static final String type = "HARDRONE";
	public static final boolean harvestable = false;
	public static final boolean pathable = false;
	
	public String id;
	
	public Har_drone(Point center, String shape, int size, String id){
		super(center, shape, size, pathable, harvestable, type);
	}	
}
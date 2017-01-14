package resources;

import java.awt.Point;

public class Tree extends Resource {

	public static final String type = "TREE";
	public static boolean harvestable = true;
	public static boolean pathable = false;
	
	public Tree(Point center, String shape, int size) {
		super(center, shape, size, pathable, harvestable, type);
	}
}
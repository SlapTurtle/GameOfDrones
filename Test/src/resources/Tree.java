package resources;

import java.awt.Point;

import map.Map;

public class Tree extends Resource {

	public static final String TYPE = "TREE";
	public static boolean harvestable = true;
	public static boolean pathable = false;
	
	public Tree(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "TREE";
		harvestable = true;
		pathable = false;
	}

}
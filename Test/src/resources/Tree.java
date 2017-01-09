package resources;

import java.awt.Point;

import map.Map;

public class Tree extends Resource {

	public Tree(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "TREE";
		harvestable = true;
		pathable = false;
	}

}
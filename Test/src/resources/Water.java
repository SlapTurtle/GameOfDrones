package resources;

import java.awt.Point;

import map.Map;

public class Water extends Resource {

	public Water(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "WATER";
		harvestable = false;
		pathable = false;
	}

}
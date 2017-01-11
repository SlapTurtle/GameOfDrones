package resources;

import java.awt.Point;

import map.Map;

public class Rock extends Resource {

	public Rock(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "ROCK";
		harvestable = false;
		pathable = false;
	}

}
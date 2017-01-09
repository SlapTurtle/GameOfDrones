package resources;

import java.awt.Point;

import map.Map;

public class Gold extends Resource {

	public Gold(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "GOLD";
		harvestable = true;
		pathable = true;
	}

}
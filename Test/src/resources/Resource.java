package resources;

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.awt.Point;
import java.io.Serializable;

/** Base class representing all resources and their properties. */
public abstract class Resource implements Serializable {
	private static final long serialVersionUID = 1L;

	UUID cluster;
	
	public String type;
	public boolean harvestable;
	public boolean pathable;
	
	public Point center;
	public int shape;
	public int size;
	
	protected Resource(Point center, String shape, int size, boolean pathable, boolean harvestable, String type) {
		cluster = UUID.randomUUID();
		this.center = center;
		this.shape = evaluateShape(shape);
		this.size = size;
		this.pathable = pathable;
		this.harvestable = harvestable;
		this.type = type;
	}
	
	public static Class<?> toClass(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		Class<?> c = null;
		try {
			c = Class.forName("resources." + s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	} 

	public static boolean isPathable(String name) {
		try {
			return toClass(name).getField("pathable").getBoolean(null);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int evaluateShape(String shape) {
		switch (shape) {
		case "circular":return 0;
		case "square"  :return 1;
		case "scatter" :return 2;
		case "polygon" :return 3;
		default		   :return 0;
		}
	}

	/** Gets all points associated with this Resource. */
	public LinkedList<Point> getPoints(Random random) {
		LinkedList<Point> list = new LinkedList<Point>();
		list.add(center);

		// Circular
		if (shape == 0 || shape == 2) {
			for (int y = -size+center.y; y <= size+center.y; y++) {
				for (int x = -size+center.x; x <= size+center.x; x++) {
					Point p = new Point(x,y);
					if (p.distance(center) <= size) {
						list.add(p);
					}
				}
			}
		}

		// Polygon
		if (shape == 3) {
			Point[] reshaper = new Point[2];
			for (int k = 0; k < 2; k++) {
				reshaper[k] = new Point(random.nextInt(2*size+1)-size-1+center.x, random.nextInt(2*size+1)-size-1+center.y);
			}
			for (int y = (-2*size)+center.y; y <= (2*size)+center.y; y++) {
				for (int x = (-2*size)+center.x; x <= (2*size)+center.x; x++) {
					Point p = new Point(x,y);
					if (p.distance(center) <= size || p.distance(reshaper[0]) <= size || p.distance(reshaper[1]) <= size) {
						list.add(p);
					}
				}
			}
		}

		// Scatter
		if (shape == 2) {
			for (int i = 0; i < random.nextInt(size) + size/2; i++) {
				if (list.size() > 2) {
					list.remove(random.nextInt(list.size()));
				} else {
					break;
				}
			}
		}
		
		return list;
	}	
}
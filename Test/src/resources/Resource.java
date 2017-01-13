package resources;

import org.cmg.resp.knowledge.Tuple;

import map.Map;

import java.util.UUID;
import java.util.LinkedList;
import java.util.List;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


/** Base class representing all resources and their properties. */
public class Resource {
	Map map;
	UUID cluster;
	public Point center;
	public static String type;
	private static final String TYPE = "default";
	public static boolean harvestable = false;
	public static boolean pathable = true;
	int shape;
	int size;
	
	protected Resource(Map map, Point center, String shape, int size) {
		cluster = UUID.randomUUID();
		this.map = map;
		this.center = center;
		this.shape = evaluateShape(shape);
		this.size = size;
	}

	public static String type() {
		return type;
	}
	
	public static Class toClass(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		Class c = null;
		try {
			c = Class.forName("resources." + s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	} 

	public static boolean isPathable(String name) {
		Object o = toClass(name);
		boolean b = false;
		try {
			Field f = toClass(name).getField("pathable");
			if (f.getType() == boolean.class) {
				b = f.getBoolean(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public int evaluateShape(String shape) {
		switch (shape) {
		case "circular":return 0;
		case "square"  :return 1;
		case "scatter" :return 2;
		case "polygon" :return 3;
		default		   :return 0;
		}
	}

	public List<Tuple> addTuples() {
		
		List<Tuple> list = new LinkedList<Tuple>();

		// TODO CALL ALGORITHM
		
		return list;
	}
	
	/** Gets all points associated with this Resource. */
	public List<Point> getPoints() {
		List<Point> list = new LinkedList<Point>();
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
				reshaper[k] = new Point(map.random.nextInt(2*size+1)-size-1+center.x, map.random.nextInt(2*size+1)-size-1+center.y);
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
			for (int i = 0; i < map.random.nextInt(size) + size/2; i++) {
				if (list.size() > 2) {
					list.remove(map.random.nextInt(list.size()));
				} else {
					break;
				}
			}
		}
		
		return list;
	}
	
}
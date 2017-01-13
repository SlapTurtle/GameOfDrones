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
	UUID cluster;
	
	private static final String TYPE = "default";
	public static String type;
	
	public static boolean harvestable = false;
	public static boolean pathable = true;
	
	public Point center;
	public int shape;
	public int size;
	
	protected Resource(Point center, String shape, int size) {
		cluster = UUID.randomUUID();
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

//	public List<Tuple> addTuples() {
//		
//		List<Tuple> list = new LinkedList<Tuple>();
//
//		// TODO CALL ALGORITHM
//		
//		return list;
//	}
	
}
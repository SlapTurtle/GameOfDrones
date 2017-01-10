package resources;

import org.cmg.resp.knowledge.Tuple;

import map.Map;

import java.util.UUID;
import java.util.LinkedList;
import java.util.List;
import java.awt.Point;


/** Base class representing all resources and their properties. */
public class Resource {
	Map map;
	UUID cluster;
	public Point center;
	public static String type;
	private static final String TYPE = "default";
	public static boolean harvestable = false;
	protected static boolean pathable = true;
	int shape;
	int size;
	
	protected Resource(Map map, Point center, String shape, int size) {
		cluster = UUID.randomUUID();
		this.map = map;
		this.center = center;
		this.shape = evaluateShape(shape);
		this.size = size;
		//System.out.println("New cluster, ID=" + cluster.toString());
	}
	
	public static String type() {
		return type;
	}
	
	public int evaluateShape(String shape) {
		switch (shape) {
		case "circular":return 0;
		case "square"  :return 1;
		case "scatter" :return 2;
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
		
		int centerX = (int)center.getX();
		int centerY = (int)center.getY();
		int hsize = size;
		
		if (shape == 1) {
			Point p = new Point(centerX,centerY);
			if (p.distance(center) <= (double) hsize){
				list.add(p);
			}
		}
		
		if (shape == 0 || shape == 2) {
			//System.out.println("Creating circular cluster " + centerX + ", " + centerY + ", radius " + size);
			for (int y = -hsize+centerX; y <= hsize+centerY; y++) {
				for (int x = -hsize+centerX; x <= hsize+centerX; x++) {
					Point p = new Point(x,y);
					if (p.distance(center) <= (double)hsize) {
						list.add(p);
					}
				}
			}
				
		}
		
		if (shape == 2) {
			for (int i = 0; i < map.random.nextInt(size) + 2; i++) {
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
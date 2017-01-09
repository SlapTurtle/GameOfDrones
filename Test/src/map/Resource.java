package map;

import org.cmg.resp.knowledge.Tuple;
import java.util.UUID;
import java.util.LinkedList;
import java.util.List;
import java.awt.Point;

public class Resource {

	Map map;
	UUID cluster;
	public Point center;
	String type;
	private static final String TYPE = "default";
	static boolean harvestable = false;
	static boolean pathable = true;
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

class Gold extends Resource {

	public Gold(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "GOLD";
		harvestable = true;
		pathable = true;
	}

}

class Tree extends Resource {

	public Tree(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "TREE";
		harvestable = true;
		pathable = false;
	}

}

class Water extends Resource {

	public Water(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "WATER";
		harvestable = false;
		pathable = false;
	}

}

class Base extends Resource {

	public Base(Map map, Point center, String shape, int size) {
		super(map, center, shape, size);
		type = "BASE";
		harvestable = false;
		pathable = false;
	}

}

class ExplorationDrone extends Resource {
	
	public ExplorationDrone(Map map, Point center, String shape, int size){
		super(map, center, shape, size);
		type = "EXPDRONE";
		harvestable = false;
		pathable = false;
	}
}	
class HarvestingDrone extends Resource {
	
	public HarvestingDrone(Map map, Point center, String shape, int size){
			super(map, center, shape, size);
			type = "HARDRONE";
			harvestable = false;
			pathable = false;
		}	
	}


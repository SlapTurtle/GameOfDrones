package map;

import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;

/** Class representing areas in the Map Tublesplace. */
public class World {
	
	public static final int DEFAULT = 40;
	private int x, y;
	Point center;
	
	public World() {
		this.x = DEFAULT;
		this.y = DEFAULT;
		Init(new Point(0,0));
	}
	
	public World(Point center) {
		this.x = DEFAULT;
		this.y = DEFAULT;
		Init(center);
	}
	
	public World(int n) {
		this.x = n;
		this.y = n;
		Init(new Point(0,0));
	}
	
	public World(Point center, int n) {
		this.x = set(n);
		this.y = set(n);
		Init(center);
	}

	public World(Point center, int x, int y) {
		
		this.x = set(x);
		this.y = set(y);
		Init(center);
	}
	
	public void Init(Point center) {
		this.center = center;
		//System.out.println("Center is " + center.x + ", " + center.y);
		
//		if (!center.equals(new Point(0,0))) {
//			System.out.println("Center is " + center.x + ", " + center.y);
//			System.out.println("New world not part of initial map");
//			adjustBounds();
//		}
	}
	
	/** Adjusts the bounds of the Map to contain the World. */
//	public void adjustBounds() {
//		if (center.getX() < map.center.getX()) {
//			map.bounds[0] -= x;
//			map.world.x += x;
//		} else if (center.getX() > map.center.getX()) {
//			map.bounds[1] += x;
//			map.world.x += x;
//		} else if (center.getY() < map.center.getY()) {
//			map.bounds[2] -= y;
//			map.world.y += y;
//		} else {
//			map.bounds[3] += x;
//			map.world.y += y;
//		}
//	}
	
	public int set(int z) {
		return (z > 0 && z < Integer.MAX_VALUE) ? z : DEFAULT;
	}
	
	public int X() {
		return x;
	}
	
	public int Y() {
		return y;
	}
	
	public boolean pointInWorld(int[] bounds, Point p) {
		return (p.getX() >= bounds[0] && p.getX() <= bounds[1] && p.getY() >= bounds[2] && p.getY() <= bounds[3] );
	}
	
	public boolean pointInWorldDistance(Point p, int dist) {
		int X = X()/2 - dist;
		int Y = Y()/2 - dist;
		return (p.x >= center.x - X && p.x <= center.x + X && p.y >= center.y - Y && p.y <= center.y + Y);
	}

	public boolean pointNearCenter(Point p) {
		return (p.distance(new Point(0,0)) <= 3); 
	}

	public static LinkedList<Point> getNeighbors(Point p) {
		LinkedList<Point> list = new LinkedList<Point>();
		for (int y = p.y-1; y <= p.y+1; y++)
			for (int x = p.x-1; x <= p.x+1; x++) 
				if (!(x == p.x && y == p.y))
					list.add(new Point(x,y));
		return list;
	}
	
	public static LinkedList<Point> getNeighbors(Point p, int dist) {
		LinkedList<Point> list = new LinkedList<Point>();
		for (int y = p.y-dist; y <= p.y+dist; y+=dist)
			for (int x = p.x-dist; x <= p.x+dist; x+=dist) 
				if (!(x == p.x && y == p.y))
					list.add(new Point(x,y));
		return list;
	}
	
}

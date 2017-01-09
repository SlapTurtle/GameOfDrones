package map;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;
import java.awt.Point;
import java.util.UUID;

public class World {
	
	UUID ID = UUID.randomUUID();
	public static final int DEFAULT = 20;
	Map map;
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
		System.out.println("Center is " + center.x + ", " + center.y);
		
//		if (!center.equals(new Point(0,0))) {
//			System.out.println("Center is " + center.x + ", " + center.y);
//			System.out.println("New world not part of initial map");
//			adjustBounds();
//		}
	}
	
	public void adjustBounds() {
		if (center.getX() < map.center.getX()) {
			map.bounds[0] -= x;
			map.world.x += x;
		} else if (center.getX() > map.center.getX()) {
			map.bounds[1] += x;
			map.world.x += x;
		} else if (center.getY() < map.center.getY()) {
			map.bounds[2] -= y;
			map.world.y += y;
		} else {
			map.bounds[3] += x;
			map.world.y += y;
		}
	}
	
	public int set(int z) {
		return (z > 0 && z < Integer.MAX_VALUE) ? z : DEFAULT;
	}
	
	public int X() {
		return x;
	}
	
	public int Y() {
		return y;
	}
	
	public boolean pointInWorld(Point p) {
		return (p.getX() >= map.bounds[0] && p.getX() <= map.bounds[1] && p.getY() >= map.bounds[2] && p.getY() <= map.bounds[3] );
	}
	
	public boolean pointNearCenter(Point p) {
		return map.center != null ? p.distance(map.center) <= 3 : false; 
	}

	
	
}

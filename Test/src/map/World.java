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

public class World {
	
	public static final int DEFAULT = 30;
	Map map;
	private int x, y;
	Point center;
	
	public World() {
		this.x = DEFAULT;
		this.y = DEFAULT;
		center = new Point(x/2, y/2);
	}
	
	public World(int n) {
		this.x = set(n);
		this.y = set(n);
		center = new Point(x/2, y/2);
	}

	public World(int x, int y) {
		
		this.x = set(x);
		this.y = set(y);
		center = new Point(x/2, y/2);
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
		return (p.getX() >= 0 && p.getX() < x && p.getY() >= 0 && p.getY() < y );
	}
	
	public boolean pointNearCenter(Point p) {
		return map.center != null ? p.distance(map.center.center) <= 3 : false; 
	}

	
	
}

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
import java.util.Random;
import java.util.UUID;

public class Generator extends Agent {
	
	Map map;
	String mapID;
	World world;
	UUID worldID;
	String seed;
	
	public Generator(Map map, UUID mapID, World world, String seed) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.mapID = mapID.toString();
		this.world = world;
		this.world.ID = worldID;
		this.seed = seed;
	}
	
	protected void doRun() {
		try {
			populateMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void populateMap() throws Exception {
		Random r = map.random;
		
		if (world.center.equals(map.center)) {
			map.base = new Base(map, world.center, "circular", 1);
			putResource(map.base, map.base.center);
		}
		
		//for (int i = 0; i < Math.min(world.X(), world.Y()) / 4; i++) {
		
			Point center = getRandomPoint();
			Resource gold0 = new Gold(map, center, "square", 2);
//			
//			center = getRandomPoint();
//			Resource gold = new Gold(map, center, "scatter", 1);
			
//			for (int j = 0; j < 4; j++) {
//				center = getRandomPoint();
//				Resource tree = new Tree(map, center, "square", r.nextInt(1) + 1);
//				addResource(tree);
//			}			
			
			addResource(gold0);
//			addResource(gold);
			
		//}
		
		Point centerW = getRandomPoint();
		Resource water = new Water(map, centerW, "square", r.nextInt(3) + 2);
		addResource(water);
		
	}
	
	public Point getRandomPoint() {
		Random r = map.random;
		int x, y;
		x = (r.nextInt(world.X()) - world.X()/2) + (world.center.x - map.center.x);
		y = (r.nextInt(world.Y()) - world.Y()/2) + (world.center.y - map.center.y);
		Point p = new Point(x,y);
		System.out.println("Random point: " + x + "," + y);
		
		//Point p = new Point(r.nextInt(world.X()), r.nextInt(world.Y()));
		//Point p = translatePoint(r.nextInt(world.X()), r.nextInt(world.Y()));
		//System.out.println(p.getX() + ", " + p.getY());
		return p;
	}
	
	public Point translatePoint(int x, int y) {
		Point p = new Point(-(world.X()/2)+x + world.center.x,-(world.Y()/2)+y + world.center.y);
//		System.out.println("translating p=" + x + "," + y + "to " + p.x + "," + p.y);
//		p.setLocation((p.x + (world.center.getX() - map.center.getX())), (p.y + (world.center.getY() - map.center.getY())));
//		System.out.println("now: " + p.x + "," + p.y);
		return p;
	}
	
	public void putResource(Resource resource, Point p) throws Exception {
		Tuple t = new Tuple(resource.type, (int)p.getX(), (int)p.getY());
//		System.out.println(t.getElementAt(String.class, 0) + " at " + t.getElementAt(Integer.class, 1) + ", " + t.getElementAt(Integer.class, 2));
		
		put(t, Self.SELF);
	}
	
	public void addResource(Resource resource) throws Exception {
		
		for (Point p : resource.getPoints()) {
			//System.out.println("checking " + p.getX() + ", " + p.getY());
			if (world.pointInWorld(p) && !world.pointNearCenter(p)) {
				//System.out.println("adding " + p.getX() + ", " + p.getY());
				putResource(resource, p);
			}
			
		}
		
	}
	
}

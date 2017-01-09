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
	String seed;
	
	public Generator(Map map, UUID mapID, World world, String seed) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.mapID = mapID.toString();
		this.world = world;
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
		
		map.center = new Base(map, world.center, "circular", 1);
		putResource(map.center, map.center.center);
		
		for (int i = 0; i < Math.min(world.X(), world.Y() / 4); i++) {
			
			Point center = new Point(r.nextInt(map.world.X()), r.nextInt(map.world.Y()));
			Resource gold0 = new Gold(map, center, "scatter", r.nextInt(2) + 1);
			
			center = new Point(r.nextInt(map.world.X()), r.nextInt(map.world.Y()));
			Resource gold = new Gold(map, center, "scatter", r.nextInt(1) + 1);
			
			for (int j = 0; j < 4; j++) {
				center = new Point(r.nextInt(map.world.X()), r.nextInt(map.world.Y()));
				Resource tree = new Tree(map, center, "scatter", r.nextInt(1) + 1);
				addResource(tree);
			}			
			
			addResource(gold0);
			addResource(gold);
			
		}
		
		Point centerW = new Point(r.nextInt(map.world.X()), r.nextInt(map.world.Y()));
		Resource water = new Water(map, centerW, "circular", r.nextInt(3) + 3);
		addResource(water);
		
	}
	
	public void putResource(Resource resource, Point p) throws Exception {
		Tuple t = new Tuple(resource.type, (int)p.getX(), (int)p.getY());
//		System.out.println(t.getElementAt(String.class, 0) + " at " + t.getElementAt(Integer.class, 1) + ", " + t.getElementAt(Integer.class, 2));
		put(t, Self.SELF);
	}
	
	public void addResource(Resource resource) throws Exception {
		
		for (Point p : resource.getPoints()) {	
			if (map.world.pointInWorld(p) && !map.world.pointNearCenter(p)) {
				putResource(resource, p);
			}
			
		}
		
	}
	
}

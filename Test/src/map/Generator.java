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

import expAI.ExpDrone;
import resources.Base;
import resources.Gold;
import resources.Resource;
import resources.Water;

import java.awt.Point;
import java.util.Random;
import java.util.UUID;
import resources.*;

/** Agent used for the generation of content in a given world. */
public class Generator extends Agent {
	
	Map map;
	String mapID;
	World world;
	UUID worldID;
	Random random;
	String seed;
	boolean b = false;
	
	public Generator(Map map, UUID mapID, World world, String seed) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.mapID = mapID.toString();
		this.world = world;
		this.world.ID = worldID;
		this.seed = seed;
		random = new Random(seed.hashCode());
	}
	
	protected void doRun() {
		try {
			populateMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Primary method used to initiate the content generation algorithm of the Generator Agent of a given World. */
	public void populateMap() throws Exception {
		if (world.center.equals(map.center)) {
			map.base = new Base(map, world.center, "circular", 1);
			putResource(map.base, map.base.center);
			Point p = new Point(map.base.center.x, map.base.center.y+1);
			ExplorationDrone drone = new ExplorationDrone(map, p, "circular", 1);
			ExpDrone expdrone = new ExpDrone(map, p);
			map.map.addAgent(expdrone);
			putResource(drone, p);
		}
		
		for (int i = 0; i < Math.min(world.X(), world.Y()) / 4; i++) {
		
			Point center = getRandomPoint();
			Resource gold0 = new Gold(map, center, "scatter", 2);
			
			center = getRandomPoint();
			Resource gold = new Gold(map, center, "scatter", 1);
			
			for (int j = 0; j < 4; j++) {
				center = getRandomPoint();
				Resource tree = new Tree(map, center, "square", random.nextInt(1) + 1);
				addResource(tree);
			}			
			
			addResource(gold0);
			addResource(gold);
			
		}
		
//		Point centerW = getRandomPoint();
//		Resource water = new Water(map, centerW, "square", r.nextInt(3) + 2);
//		addResource(water);
		
	}

	/** Returns a random point inside the World associated with the Generator Agent.
	 * The point is represented using its generalized map coordinates. */
	public Point getRandomPoint() {
		return translatePoint(random.nextInt(world.X()), random.nextInt(world.Y()));
	}

	/** Tanslates a point from its internal world coordinates to its generalized map coordinates.
	 * @param */
	public Point translatePoint(int x, int y) {
		Point p = new Point(
				(x - world.X()/2) + (world.center.x - map.center.x),
				(y - world.Y()/2) + (world.center.y - map.center.y));
		return p;
	}
	
	public void putResource(Resource resource, Point p) throws Exception {
		Tuple t = new Tuple(resource.type, (int)p.getX(), (int)p.getY());	
		put(t, Self.SELF);
	}
	
	/** Adds a resource cluster's tubles to the Map Tublespace.
	 * @param */
	public void addResource(Resource resource) throws Exception {
		for (Point p : resource.getPoints()) {
			if (world.pointInWorld(p) && !world.pointNearCenter(p)) {
				putResource(resource, p);
			}
		}
	}
	
}

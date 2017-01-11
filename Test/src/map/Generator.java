package map;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;
import expAI.ExpDrone;
import resources.Base;
import resources.Gold;
import resources.Resource;
import util.Position;

import java.awt.Point;
import java.lang.reflect.Constructor;
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
		Dice dice = new Dice(random);
		
		// Initial field initalization
		if (world.center.equals(map.center)) {
			map.base = new Base(map, world.center, "circular", 1);
			putResource(map.base, map.base.center);
			
			for (int i = -1; i < 1; i+=2) {
				Point p = new Point(map.base.center.x, map.base.center.y+i);
				ExplorationDrone drone = new ExplorationDrone(map, p, "circular", 1);
				ExpDrone expdrone = new ExpDrone(map, new Position(p.x,p.y));
				map.map.addAgent(expdrone);
				putResource(drone, p);
				
//				Point p2 = new Point(map.base.center.x+i, map.base.center.y);
//				ExplorationDrone drone2 = new ExplorationDrone(map, p2, "circular", 1);
//				ExpDrone expdrone2 = new ExpDrone(map, p2);
//				map.map.addAgent(expdrone2);
//				putResource(drone2, p2);
			}
		}
		
		/* 				 *
		 * Generate area *
		 * 				 */

		/* WATER */
		int sea = (world.center.equals(new Point(0,0))) ? 2 : 8;
		for (int j = sea; j > 0; j--) {
			double probability = (double)j * 0.12;
			if (dice.roll(1-probability)) {
				populate(Water.class, "polygon", j);
				break;
			}
		}

		
		for (int j = 1; j < 3; j++) {
			if (dice.roll(0.4))
				populate(Water.class, "polygon", j);
		}

		
		/* TREE */
		for (int j = 2; j >= 0; j--) {
			double probability = (double)j * 0.4;
			if (dice.roll(1-probability)) {
				populate(Tree.class, "polygon", j);
				break;
			}
		}
		
		for (int j = 0; j < random.nextInt((int)(World.DEFAULT*1.5)) + World.DEFAULT; j++) {
			if (dice.roll(0.4 * (j/2))) {
				populate(Tree.class, "circular", 0);
			}
		}
		
		
		/* GOLD */

		int gold = random.nextInt((int)(World.DEFAULT/10) +1);
		if (dice.roll(0.9 - gold * 0.22) && !world.center.equals(new Point(0,0)))
			populate(Gold.class, "polygon", random.nextInt(1) + 2);
		
		for (int j = 0; j < gold + 3; j++) {
			populate(Gold.class, "polygon", random.nextInt(1) + 1);
		}
		
		for (int j = 0; j < random.nextInt((int)(World.DEFAULT/6) + 1) + 6; j++) {
			populate(Gold.class, "circular", 0);
		}
		
		
	}

	public void populate(Class classname, String shape, int size) {
		try {
			Constructor<?> constructor = classname.getConstructor(Map.class, Point.class, String.class, int.class);
			Point p = getRandomPoint();
			int dist = (shape == "polygon") ? 2*size : size+1;
			while(!world.pointInWorldDistance(p, dist) && world.pointNearCenter(p)) {
				p = getRandomPoint();
			}
			Object res = constructor.newInstance(new Object[] { map, p, shape, size });
			addResource((Resource) res);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

package map;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import droneNode.ExpDrone;
import resources.*;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/** Agent used for the generation of content in a given world. */
public class Generator extends Agent {
	
	Map map2; // TEMP!
	Node map;
	String mapID;
	World world;
	UUID worldID;
	Random random;
	String seed;
	boolean b = false;
	
	public Generator(Map map2, Node map, UUID mapID, World world, String seed) {
		super(UUID.randomUUID().toString());
		this.map2 = map2;
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
		if (world.center.equals(new Point(0,0))) {
			Base base = new Base(world.center, "circular", 1);
			putResource(base, base.center);

			for (int i = -1; i < 1; i+=2) {
				Point p = new Point(world.center.x, world.center.y+i);
				Exp_drone drone = new Exp_drone(p, "circular", 1);
				ExpDrone expdrone = new ExpDrone(map2, p);
				map.addAgent(expdrone);
				putResource(drone, p);
				
////				Point p2 = new Point(map.base.center.x+i, map.base.center.y);
////				ExplorationDrone drone2 = new ExplorationDrone(map, p2, "circular", 1);
////				ExpDrone expdrone2 = new ExpDrone(map, p2);
////				map.map.addAgent(expdrone2);
////				putResource(drone2, p2);
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

		for (int j = 1; j < 2; j++) {
			if (dice.roll(0.3))
				populate(Water.class, "polygon", j);
		}

		/* ROCK */
		
		for (int j = 2; j >= 0; j--) {
			if (dice.roll(0.15)) {
				populate(Rock.class, "polygon", j);
				break;
			}
		}

		for (int j = 0; j < 3; j++) {
			if (dice.roll(0.5))
				populate(Rock.class, "polygon", random.nextInt(1));
		}

		
		/* TREE */
		
		for (int j = 1; j >= 0; j--) {
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
			populate(Gold.class, "polygon", random.nextInt(1) + 1);
		
		for (int j = 0; j < gold + 3; j++) {
			populate(Gold.class, "polygon", random.nextInt(1) + 1);
		}
		
		for (int j = 0; j < random.nextInt((int)(World.DEFAULT/6) + 1) + 6; j++) {
			populate(Gold.class, "circular", 0);
		}
		
		
	}

	/** Populates the current World with a given type, shape and size of a resource.
	 * @params */
	public void populate(Class classname, String shape, int size) {
		try {
			Constructor<?> constructor = classname.getConstructor(Point.class, String.class, int.class);
			Point p = getRandomPoint();
			int dist = (shape == "polygon") ? 2*size : size+1;
			while(!world.pointInWorldDistance(p, dist) && world.pointNearCenter(p)) {
				p = getRandomPoint();
			}
			Object res = constructor.newInstance(new Object[] {p, shape, size });
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
				(x - world.X()/2) + (world.center.x - Map.center.x),
				(y - world.Y()/2) + (world.center.y - Map.center.y));
		return p;
	}
	
	public void putResource(Resource resource, Point p) throws Exception {
		Tuple t = new Tuple(resource.type, (int) p.getX(), (int)p.getY());	
		put(t, Self.SELF);
	}
	
	/** Adds a resource cluster's tubles to the Map Tublespace.
	 * @param */
	public void addResource(Resource resource) throws Exception {
		for (Point p : getPoints(resource)) {
			if (world.pointInWorld(p) && !world.pointNearCenter(p)) {
				putResource(resource, p);
			}
		}
	}
	
	/** Gets all points associated with this Resource. */
	public List<Point> getPoints(Resource resource) {
		List<Point> list = new LinkedList<Point>();
		Point center = resource.center;
		int size = resource.size;
		int shape = resource.shape;
		list.add(center);

		// Circular
		if (shape == 0 || shape == 2) {
			for (int y = -size+center.y; y <= size+center.y; y++) {
				for (int x = -size+center.x; x <= size+center.x; x++) {
					Point p = new Point(x,y);
					if (p.distance(center) <= size) {
						list.add(p);
					}
				}
			}
		}

		// Polygon
		if (shape == 3) {
			Point[] reshaper = new Point[2];
			for (int k = 0; k < 2; k++) {
				//map.random x2
				reshaper[k] = new Point(random.nextInt(2*size+1)-size-1+center.x, random.nextInt(2*size+1)-size-1+center.y);
			}
			for (int y = (-2*size)+center.y; y <= (2*size)+center.y; y++) {
				for (int x = (-2*size)+center.x; x <= (2*size)+center.x; x++) {
					Point p = new Point(x,y);
					if (p.distance(center) <= size || p.distance(reshaper[0]) <= size || p.distance(reshaper[1]) <= size) {
						list.add(p);
					}
				}
			}
		}

		// Scatter
		if (shape == 2) {
			for (int i = 0; i < random.nextInt(size) + size/2; i++) {	//map.random
				if (list.size() > 2) {
					list.remove(random.nextInt(list.size()));			//map.random
				} else {
					break;
				}
			}
		}
		
		return list;
	}
	
}

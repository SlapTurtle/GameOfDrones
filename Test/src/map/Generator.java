package map;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;
import resources.Base;
import resources.Gold;
import resources.Resource;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import resources.*;

/** Agent used for the generation of content in a given world. */
public class Generator extends Agent {
	
	Template getT = new Template(new ActualTemplateField("generate"), Map.AnyWorld, Map.AnyString);
	Template getBounds = new Template(new ActualTemplateField("bounds"), new FormalTemplateField(int[].class));
	Template getPoints = new Template(Map.AnyInteger, Map.AnyInteger, new ActualTemplateField("listen"));

	int[] bounds;

	public Generator() {
		super("generator");
	}

	protected void doRun() {
		while(true) {
			try {
				// REQUEST
				Tuple request = get(getT, Self.SELF);
				World world = request.getElementAt(World.class, 1);
				Random random = new Random(request.getElementAt(String.class, 2).hashCode());			
				bounds = (int[])get(getBounds, Self.SELF).getElementAt(1);
				// PROCESS
				Tuple t = new Tuple("bounds", adjustBounds(bounds, world));
				put(t, Self.SELF);
				populateMap(world, random);
				addListeners(world);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int[] adjustBounds(int[] bounds, World world) {
		Point p = world.center;
		if (p.x < 0) {
			bounds[0] -= Map.DEFAULTGRID;
		} else if (p.x > 0) {
			bounds[1] += Map.DEFAULTGRID;
		} else if (p.y < 0) {
			bounds[2] -= Map.DEFAULTGRID;
		} else if (p.y > 0) {
			bounds[3] += Map.DEFAULTGRID;
		}
		return bounds;
	}


	/** Primary method used to initiate the content generation algorithm of the Generator Agent of a given World. */
	public void populateMap(World world, Random random) {
		Dice dice = new Dice(random);

		/* WATER */

		int sea = (world.center.equals(new Point(0,0))) ? 2 : 8;
		for (int j = sea; j > 0; j--) {
			double probability = (double)j * 0.12;
			if (dice.roll(1-probability)) {
				populate(Water.class, "polygon", j, world, random);
				break;
			}
		}

		for (int j = 1; j < 2; j++)
			if (dice.roll(0.3))
				populate(Water.class, "polygon", j, world, random);
		

		/* ROCK */
		
		for (int j = 2; j >= 0; j--)
			if (dice.roll(0.15)) {
				populate(Rock.class, "polygon", j, world, random);
				break;
			}
		

		for (int j = 0; j < 3; j++)
			if (dice.roll(0.5))
				populate(Rock.class, "polygon", random.nextInt(1), world, random);
		
		
		/* TREE */
		
		for (int j = 1; j >= 0; j--) {
			double probability = (double)j * 0.4;
			if (dice.roll(1-probability)) {
				populate(Tree.class, "polygon", j, world, random);
				break;
			}
		}
		
		for (int j = 0; j < random.nextInt((int)(World.DEFAULT*1.5)) + World.DEFAULT; j++)
			if (dice.roll(0.4 * (j/2)))
				populate(Tree.class, "circular", 0, world, random);
			
		
		
		/* GOLD */

		int gold = random.nextInt((int)(World.DEFAULT/10) +1);
		if (dice.roll(0.9 - gold * 0.22) && !world.center.equals(new Point(0,0)))
			populate(Gold.class, "polygon", random.nextInt(1) + 1, world, random);
		
		for (int j = 0; j < gold + 3; j++)
			populate(Gold.class, "polygon", random.nextInt(1) + 1, world, random);
		
		for (int j = 0; j < random.nextInt((int)(World.DEFAULT/6) + 1) + 6; j++)
			populate(Gold.class, "circular", 0, world, random);
	}

	/** Populates the current World with a given type, shape and size of a resource.
	 * @params */
	public void populate(Class<?> classname, String shape, int size, World world, Random random) {
		try {
			Constructor<?> constructor = classname.getConstructor(Point.class, String.class, int.class);
			Point p = getRandomPoint(world, random);
			int dist = (shape == "polygon") ? 2*size : size+1;
			while(!world.pointInWorldDistance(p, dist) && world.pointNearCenter(p))
				p = getRandomPoint(world, random);
			Object res = constructor.newInstance(new Object[] {p, shape, size });
			addResource((Resource) res, world, random);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Returns a random point inside the World associated with the Generator Agent.
	 * The point is represented using its generalized map coordinates. */
	public Point getRandomPoint(World world, Random random) {
		return translatePoint(random.nextInt(world.X()), random.nextInt(world.Y()), world);
	}

	/** Tanslates a point from its internal world coordinates to its generalized map coordinates.
	 * @param */
	public Point translatePoint(int x, int y, World world) {
		Point p = new Point(
				(x - world.X()/2) + world.center.x,
				(y - world.Y()/2) + world.center.y);
		return p;
	}

	public void putResource(Resource resource, Point p) throws Exception {
		if (queryp(new Template(Map.AnyString, new ActualTemplateField(p.x), new ActualTemplateField(p.y))) == null)
			put(new Tuple(resource.type, p.x, p.y), Self.SELF);
	}
	
	/** Adds a resource cluster's tubles to the Map Tublespace.
	 * @param */
	public void addResource(Resource resource, World world, Random random) throws Exception {
		for (Point p : resource.getPoints(random))
			if (world.pointInWorld(bounds, p) && !world.pointNearCenter(p))
				putResource(resource, p);
		
	}

	public void addListeners(World world) throws InterruptedException, IOException {
		LinkedList<Tuple> list = queryAll(new Template(getPoints.getElementAt(0), getPoints.getElementAt(1), Map.AnyString));
		for (Point p : World.getNeighbors(world.center, World.DEFAULT)) {
			Tuple tu = queryp(new Template(new ActualTemplateField(p.x),new ActualTemplateField(p.y),new FormalTemplateField(String.class)));
			if(!p.equals(world.center) && !p.equals(new Point(0,0)) && tu == null){
				put(new Tuple(p.x, p.y, "listen"), Self.SELF);
			}
		}
	}
}

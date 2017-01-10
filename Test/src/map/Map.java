package map;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.VirtualPort;
import UI.UI.GridDisplay;
import resources.Base;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class Map {
	
	public static final int EXP_HASHES = 10;
	public static final int EXP_HASHLENGTH = 16;
	public static final FormalTemplateField AnyString = new FormalTemplateField(String.class);
	public static final FormalTemplateField AnyInteger = new FormalTemplateField(Integer.class);
	public static final Template TEMPLATE_ALL = new Template(AnyString, AnyInteger, AnyInteger);
	
	public GridDisplay UI;
	public UUID ID;
	public Node map;
	VirtualPort port = new VirtualPort(8080);
	String seed;
	public Random random;
	Generator generator;
	World world;
	public Base base;
	Point center = new Point(0,0);
	int[] bounds;
	protected LinkedList<droneListener> listeners = new LinkedList<droneListener>();
	protected Hasher hasher;
	protected String[] hash = new String[EXP_HASHES];
	
	public Object render = new Object();
	Object syncRetrieval = new Object();
	
	/** Initialization of the Map object. Must be called upon construction.
	 * @param*/
	public void Init(String seed) {
		ID = UUID.randomUUID();
		map = new Node(ID.toString(), new TupleSpace());
		//map.addAgent(agent);
		this.seed = !(seed == null || seed.isEmpty()) ? seed : UUID.randomUUID().toString();
		random = new Random(this.seed.hashCode());
		hasher = new Hasher(this, random, this.seed);
		hash = hasher.expansionHashes(EXP_HASHLENGTH);
		map.start();
	}
	
	public Map(World world, String seed) {
		Init(seed);
		this.world = world;
		this.world.map = this;
		Generate(world, seed);
	}
	
	public Map(World world) {
		Init(null);
		this.world = world;
		this.world.map = this;
		Generate(world, seed);
	}
	
	public Map(String seed) {
		Init(seed);
		this.world = new World(new Point(0,0));
		this.world.map = this;
		Generate(world, seed);
	}
	
	public Map() {
		Init(null);
		this.world = new World(new Point(0,0));
		this.world.map = this;
		Generate(world, seed);
	}
	
	/** Generates a given World using the provided seed as a String.
	 * @param*/
	public void Generate(World world, String seed) {
		System.out.println("\nSeed: " + seed);
		// LEFT 0, RIGHT 1, UP 2, DOWN 3
		if (bounds == null) {
			center = new Point(0,0);
			bounds = new int[4];
			bounds[0] = -world.X() / 2;
			bounds[1] = world.X() / 2;
			bounds[2] = -world.Y() / 2;
			bounds[3] = world.Y() / 2;
		}
		generator = new Generator(this, ID, world, seed);
		map.addAgent(generator);
		addListeners(world);
	}

	public void addListeners(World world) {
		
		LinkedList<Point> dlist = new LinkedList<Point>();
		for (droneListener d : listeners) {
			dlist.add(d.center);
		}
		for (Point p : World.getNeighbors(world.center, World.DEFAULT)) {
			if (world.center.equals(new Point(0,0))) {
				droneListener a = new droneListener(this, p);
				map.addAgent(a);
				listeners.add(a);
				System.out.println("Added listener at " + p.x + ", " + p.y);
			} else if (!p.equals(new Point(0,0))) {
				boolean exists = false;
				for (Point dp : dlist) {
					if (p.x == dp.x && p.y == dp.y)
						exists = true;
				}
				if (!exists) {
					droneListener a = new droneListener(this, p);
					map.addAgent(a);
					listeners.add(a);
					System.out.println("Added listener at " + p.x + ", " + p.y);
				}
			}
		}
	}
	
	/** Expands the current playable map around a given point. World defaults to initial grid size.
	 * @param*/
	public void expandWorld(Point p) {
		int newOffset = Math.min(world.X(), world.Y());
		Point center = p;
		World newWorld = new World(center, newOffset);
		newWorld.map = this;
		newWorld.adjustBounds();
		Generate(newWorld, hasher.getExpansionHash(center));
		
		// TODO Add expansion in non-horizontal non-vertical directions
		
	}

	/** Expands the current playable map in a given direction. World defaults to initial grid size.
	 * @param*/
	public void expandWorld(int direction) {
		int newOffset = Math.min(world.X(), world.Y());
		int offsetX = 0, offsetY = 0;
		switch (direction) {
		case 0: offsetX = bounds[0] - newOffset; break;
		case 1: offsetX = bounds[1] + newOffset; break;
		case 2: offsetY = bounds[2] - newOffset; break;
		case 3: offsetY = bounds[3] + newOffset; break;
		}
		Point center = new Point(offsetX/2, offsetY/2);
		World newWorld = new World(center, newOffset);
		newWorld.map = this;
		newWorld.adjustBounds();
		Generate(newWorld, hasher.getExpansionHash(center));
		
		// TODO Add expansion in non-horizontal non-vertical directions
		
	}
	
	public void run() {
		int i = 0;
		while(i < 100) {
			synchronized (render) {
				render.notifyAll();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a linked list. */
	public LinkedList<Tuple> RetrieveTuples() {
		Retriever retriever = new Retriever(this);
		map.addAgent(retriever);
		synchronized (syncRetrieval) {
			try {
				syncRetrieval.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return retriever.Tuples;
	}

	public LinkedList<Tuple> RetrieveTuples(String TYPE) {
		Retriever retriever = new Retriever(this, TYPE);
		map.addAgent(retriever);
		synchronized (syncRetrieval) {
			try {
				syncRetrieval.wait();
			} catch (InterruptedException e) {
				
			}
		}
		return retriever.Tuples;
	}
	
	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a 2-dimensional int array. */
	public int[][] Retrieve() {
		int[][] N = new int[world.X()+1][world.Y()+1];
		
		for (int x = 0; x < world.X()+1; x++) {
			for (int y = 0; y < world.Y()+1; y++) {
				Tuple t = map.queryp(new Template(new ActualTemplateField(x+bounds[0]), new ActualTemplateField(y+bounds[2])));
				if (t != null) {
					System.out.println("Found match");
					N[x][y] = -1;
				}
			}
		}

		for (Tuple t : RetrieveTuples()) {
			if (N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] == -1) {
				if (t.getElementAt(String.class, 0) == "GOLD") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 1;
				} else if (t.getElementAt(String.class, 0) == "TREE") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 2;
				} else if (t.getElementAt(String.class, 0) == "BASE") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 3;
				} else if (t.getElementAt(String.class, 0) == "WATER") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 4;
				} else if (t.getElementAt(String.class, 0) == "EXPDRONE") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 5;
				} else if (t.getElementAt(String.class, 0) == "HARDRONE") {
					N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] = 6;
				}
			}
		}
		return N;
	}
	
	public static int getTupleX(Tuple t) {
		return t.getElementAt(Integer.class, 1);
	}
	
	public static int getTupleY(Tuple t) {
		return t.getElementAt(Integer.class, 2);
	}
	
	public UUID ID() {
		return ID;
	}
	
	
}
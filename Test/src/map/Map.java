package map;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.VirtualPort;
import map.HashRequest;
import resources.Base;
import droneNode.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Map extends Node {
	
	public static final int EXP_HASHES = 10;
	public static final int EXP_HASHLENGTH = 16;
	public static final FormalTemplateField AnyString = new FormalTemplateField(String.class);
	public static final FormalTemplateField AnyInteger = new FormalTemplateField(Integer.class);
	public static final FormalTemplateField AnyPoint = new FormalTemplateField(Point.class);
	public static final FormalTemplateField AnyWorld = new FormalTemplateField(World.class);
	public static final FormalTemplateField AnyRandom = new FormalTemplateField(Random.class);
	public static final Template TEMPLATE_ALL = new Template(AnyString, AnyInteger, AnyInteger);	

	public UUID ID;
	String seed;
	public Random random;
	Generator generator;
	World world;
	public Base base;
	Point center = new Point(0,0);
	int[] bounds;
	public LinkedList<AbstractDrone> drones = new LinkedList<AbstractDrone>();
	protected LinkedList<droneListener> listeners = new LinkedList<droneListener>();
	
	public Object render = new Object();
	Object syncRetrieval = new Object();

	public Map() {
		super("map", new TupleSpace());
		seed = UUID.randomUUID().toString();
		System.out.println("Seed: " + this.seed);
		world = new World(new Point(0,0));
		world.map = this;
		random = new Random(this.seed.hashCode());

		// Agents
		addAgent(new Generator());
		//addListeners(world);
		
		start();

		//Generate(world, seed);
	}

	/** Generates a given World using the provided seed as a String.
	 * @param*/
	public void Generate(World world, String seed) {
		// LEFT 0, RIGHT 1, UP 2, DOWN 3
		if (bounds == null) {
			center = new Point(0,0);
			bounds = new int[4];
			bounds[0] = -world.X() / 2;
			bounds[1] = world.X() / 2;
			bounds[2] = -world.Y() / 2;
			bounds[3] = world.Y() / 2;
		}
	}

	public void addListeners(World world) {
		LinkedList<Point> dlist = new LinkedList<Point>();
		for (droneListener d : listeners) {
			dlist.add(d.center);
		}
		for (Point p : World.getNeighbors(world.center, World.DEFAULT)) {
			if (world.center.equals(new Point(0,0))) {
				droneListener a = new droneListener(this, p);
				addAgent(a);
				listeners.add(a);
			} else if (!p.equals(new Point(0,0))) {
				boolean exists = false;
				for (Point dp : dlist) {
					if (p.x == dp.x && p.y == dp.y)
						exists = true;
				}
				if (!exists) {
					droneListener a = new droneListener(this, p);
					addAgent(a);
					listeners.add(a);
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
		addAgent(new Hasher());
		String identifier;
		put(new HashRequest(identifier = UUID.randomUUID().toString(), center, seed, Map.EXP_HASHLENGTH));
		Tuple t;
		try {
			t = get(new Template(new ActualTemplateField(identifier), Map.AnyString));
			Generate(newWorld, (String)t.getElementAt(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		addAgent(new Hasher());
		String identifier;
		put(new HashRequest(identifier = UUID.randomUUID().toString(), center, seed, Map.EXP_HASHLENGTH));
		Tuple t;
		try {
			t = get(new Template(new ActualTemplateField(identifier), Map.AnyString));
			System.out.println("HASH: " + (String)t.getElementAt(1));
			Generate(newWorld, (String)t.getElementAt(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		int i = 0;
		while(i < 100) {
			synchronized (render) {
				render.notifyAll();
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a linked list. */
	public LinkedList<Tuple> RetrieveTuples() {
		Retriever retriever = new Retriever(this);
		addAgent(retriever);
		synchronized (syncRetrieval) {
			try {
				syncRetrieval.wait(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return retriever.Tuples;
	}

	public LinkedList<Tuple> RetrieveTuples(String TYPE) {
		Retriever retriever = new Retriever(this, TYPE);
		addAgent(retriever);
		synchronized (syncRetrieval) {
			try {
				syncRetrieval.wait(100);
			} catch (InterruptedException e) {
				
			}
		}
		return retriever.Tuples;
	}

	public LinkedList<Point> RetrievePathableNeighbors(Point p) {
		NeighborRetriever retriever = new NeighborRetriever(this, p);
		addAgent(retriever);
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LinkedList<Point> list = retriever.neighbors;
		return list;
	}

	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a 2-dimensional int array. */
	public String[][] Retrieve(int size) {
		String[][] N = new String[size][size];
		String TRIGGER = "X";
		
		Point p = new Point(0,0);
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				N[x][y] = TRIGGER;
			}
		}
		LinkedList<Tuple> list = RetrieveTuples();
		for (Tuple t : list) {
			if (getTupleX(t)-p.x < size/2 && getTupleX(t)-p.x > -size/2 && getTupleY(t)-p.y < size/2 && getTupleY(t)-p.y > -size/2) {
				if (N[getTupleX(t)-p.x+size/2][getTupleY(t)-p.y+size/2] == TRIGGER) {
					N[getTupleX(t)-p.x+size/2][getTupleY(t)-p.y+size/2] = t.getElementAt(String.class, 0);
				}
			}
		}
		N[size/2][size/2] = "BASE";
		return N;
	}

	public int[][] Retrieve() {
		System.out.println("\nRendering Map...\n");
		int[][] N = new int[bounds[1]-bounds[0]+1][bounds[3]-bounds[2]+1];
		int TRIGGER = -1;
		for (int x = 0; x < world.X()+1; x++) {
			TRIGGER = 0; if (x == 0) { break; };
			for (int y = 0; y < world.Y()+1; y++) {
				Tuple t = queryp(new Template(new ActualTemplateField(x+bounds[0]), new ActualTemplateField(y+bounds[2])));
				if (t != null) {
					N[x][y] = TRIGGER;
				}
			}
		}
		LinkedList<Tuple> list = RetrieveTuples();
		for (Tuple t : list) {
			if (N[getTupleX(t)-bounds[0]][getTupleY(t)-bounds[2]] == TRIGGER) {
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
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

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

import javax.swing.plaf.synth.SynthComboBoxUI;

public class Map {
	
	UUID ID;
	Node map;
	VirtualPort port = new VirtualPort(8080);
	String seed;
	public Random random;
	Generator generator;
	World world;
	Base center;
	
	Object syncRetrieval = new Object();
	
	public void Init(String seed) {
		ID = UUID.randomUUID();
		map = new Node(ID.toString(), new TupleSpace());
		//map.addAgent(agent);
		this.seed = !(seed == null || seed.isEmpty()) ? seed : UUID.randomUUID().toString();
		random = new Random(this.seed.hashCode());
		
	}
	
	public Map(World world, String seed) {
		Init(seed);
		this.world = world;
		this.world.map = this;
		Generate(world);
	}
	
	public Map(World world) {
		Init(null);
		this.world = world;
		this.world.map = this;
		Generate(world);
	}
	
	public Map(String seed) {
		Init(seed);
		this.world = new World(random.nextInt(70) + 30);
		this.world.map = this;
		Generate(world);
	}
	
	public Map() {
		Init(null);
		this.world = new World(random.nextInt(70) + 30);
		this.world.map = this;
		Generate(world);	
	}
	
	public void Generate(World world) {
		System.out.println("Initial field of size x=" + world.X() + ", y=" + world.Y() + " initialized");
		System.out.println("Seed: " + seed);
		generator = new Generator(this, ID, world, seed);
		map.addAgent(generator);
		map.start();
	}
	
	public int[][] Retrieve() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int[][] N = new int[world.X()][world.Y()];
		
		Retriever retriever = new Retriever(this);
		map.addAgent(retriever);
		
		synchronized (syncRetrieval) {
			try {
				syncRetrieval.wait();
			} catch (InterruptedException e) {
				
			}
		}
		
		System.out.println("Retrieving Tuples for display");
		
		for (Tuple t : retriever.Tuples) {
//			System.out.println("retrieving: " + t.getElementAt(Integer.class, 1) + ", " + t.getElementAt(Integer.class, 2));
			if (t.getElementAt(String.class, 0) == "GOLD") {
				N[t.getElementAt(Integer.class, 1)][t.getElementAt(Integer.class, 2)] = 1;
			} else if (t.getElementAt(String.class, 0) == "TREE") {
				N[t.getElementAt(Integer.class, 1)][t.getElementAt(Integer.class, 2)] = 2;
			} else if (t.getElementAt(String.class, 0) == "BASE") {
				N[t.getElementAt(Integer.class, 1)][t.getElementAt(Integer.class, 2)] = 3;
			} else if (t.getElementAt(String.class, 0) == "WATER") {
				N[t.getElementAt(Integer.class, 1)][t.getElementAt(Integer.class, 2)] = 4;
			}
		}
		
		return N;
	}
	
	public UUID ID() {
		return ID;
	}
	
	
}

class Retriever extends Agent {
	
	Map map;
	String type;
	LinkedList<Tuple> Tuples;

	public Retriever(Map map, String type) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.type = type;
	}
	
	public Retriever(Map map) {
		super(UUID.randomUUID().toString());
		this.map = map;		
	}

	protected void doRun() {	
		Template T = (type == null) ? new Template(new FormalTemplateField(String.class), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class))
									: new Template(new ActualTemplateField(type), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		Tuples = queryAll(T);
		
		synchronized (map.syncRetrieval) {
			map.syncRetrieval.notifyAll();
		}
		
	}
	
	
	
}


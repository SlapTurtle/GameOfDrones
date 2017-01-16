package map;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import map.HashRequest;
import resources.Base;
import resources.Exp_drone;
import droneNode.*;
import launch.Main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Map extends Node {
	
	public static final int DEFAULTGRID = 40;
	public static final int EXP_HASHLENGTH = 16;
	public static final FormalTemplateField AnyString = new FormalTemplateField(String.class);
	public static final FormalTemplateField AnyInteger = new FormalTemplateField(Integer.class);
	public static final FormalTemplateField AnyPoint = new FormalTemplateField(Point.class);
	public static final FormalTemplateField AnyWorld = new FormalTemplateField(World.class);
	public static final Template TEMPLATE_ALL = new Template(AnyString, AnyInteger, AnyInteger);
	public static final Template TEMPLATE_EXPDRONE = new Template(new ActualTemplateField(Exp_drone.type), AnyInteger, AnyInteger);

//	public Object render = new Object();
//	Object syncRetrieval = new Object();

	public Map(String name, String seed, int port_int) {
		super(name, new TupleSpace());
		addPort(new VirtualPort(port_int));

		// Agents
		addAgent(new RetrieverNew());
		addAgent(new Hasher());
		addAgent(new Generator());
		addAgent(new droneListener());	

		// Generate map
		System.out.println("Seed: " + seed);
		put(new Tuple("seed", seed));
		put(new Tuple("bounds", new int[] { DEFAULTGRID/2, DEFAULTGRID/2, DEFAULTGRID/2, DEFAULTGRID/2 }));
		put(new Tuple("generate", new World(new Point(0,0)), seed));
		
		// Start
		AbstractDrone.self2map = new PointToPoint(name, new VirtualPortAddress(port_int));
		start();
	}

//	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a linked list. */
//	public LinkedList<Tuple> RetrieveTuples() {
//		Retriever retriever = new Retriever(this);
//		addAgent(retriever);
//		synchronized (syncRetrieval) {
//			try {
//				syncRetrieval.wait(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		return retriever.Tuples;
//	}
//
//	public LinkedList<Tuple> RetrieveTuples(String TYPE) {
//		Retriever retriever = new Retriever(this, TYPE);
//		addAgent(retriever);
//		synchronized (syncRetrieval) {
//			try {
//				syncRetrieval.wait(100);
//			} catch (InterruptedException e) {
//				
//			}
//		}
//		return retriever.Tuples;
//	}
//
//	public LinkedList<Point> RetrievePathableNeighbors(Point p) {
//		NeighborRetriever retriever = new NeighborRetriever(this, p);
//		addAgent(retriever);
//		try {
//			Thread.sleep(150);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		LinkedList<Point> list = retriever.neighbors;
//		return list;
//	}
//
//	/** (Asynchronous) Retrieves all Tuples in the Map Tublespace and returns as a 2-dimensional int array. */
//	public String[][] Retrieve(int size) {
//		String[][] N = new String[size][size];
//		String TRIGGER = "X";
//		
//		Point p = new Point(0,0);
//		
//		for (int x = 0; x < size; x++) {
//			for (int y = 0; y < size; y++) {
//				N[x][y] = TRIGGER;
//			}
//		}
//		LinkedList<Tuple> list = RetrieveTuples();
//		for (Tuple t : list) {
//			if (getTupleX(t)-p.x < size/2 && getTupleX(t)-p.x > -size/2 && getTupleY(t)-p.y < size/2 && getTupleY(t)-p.y > -size/2) {
//				if (N[getTupleX(t)-p.x+size/2][getTupleY(t)-p.y+size/2] == TRIGGER) {
//					N[getTupleX(t)-p.x+size/2][getTupleY(t)-p.y+size/2] = t.getElementAt(String.class, 0);
//				}
//			}
//		}
//		N[size/2][size/2] = "BASE";
//		return N;
//	}
//	
//	public static int getTupleX(Tuple t) {
//		return t.getElementAt(Integer.class, 1);
//	}
//	
//	public static int getTupleY(Tuple t) {
//		return t.getElementAt(Integer.class, 2);
//	}
	
	
}
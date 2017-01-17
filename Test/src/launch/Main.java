package launch;

import java.awt.Point;
import java.util.UUID;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Attribute;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import baseNode.*;
import droneNode.*;
import map.*;
import userInterface.*;

public class Main {

	public static final int DELAY = 500;
	public static final int FIELD = 40;
	
	static final int port_int = 8080;
	static final VirtualPort port = new VirtualPort(port_int);
	static final String baseID = "baseNode";
	static String mapID;
	static final String droneID = "droneNode";

	static final int exploreDrones = 2;
	static final int harvestDrones = 0;
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;
	static final String seed = null;

	public static void nicklas() throws InterruptedException{
		Node map = new Map(mapID = UUID.randomUUID().toString());
		
		// michaels stuff
		Node baseNode = new Node(baseID, new TupleSpace());
		baseNode.addPort(port);
		baseNode.addAgent(new MapMerger());
		baseNode.addAgent(new BaseAgent());
		baseNode.addAgent(new RetrieverNew());
		baseNode.put(new Tuple("BASE", 0, 0));
		baseNode.put(new Tuple("Radius", 0));
		baseNode.put(new Tuple("GoldCounter", startGoldCount));
		baseNode.put(new Tuple("TreeCounter", startTreeCount));
		baseNode.put(new Tuple("ExpDroneCounter", exploreDrones));
		baseNode.put(new Tuple("HarDroneCounter", harvestDrones));
		baseNode.put(new Tuple("mapEdge", 0));
		baseNode.start();
		
		//Drones
		AbstractDrone.self2base = new PointToPoint(baseID, new VirtualPortAddress(port_int));
		AbstractDrone.self2map = new PointToPoint(mapID, new VirtualPortAddress(port_int));
		int max = exploreDrones+harvestDrones;
		Node[] droneNodes = new Node[max];
		PointToPoint[] p2drones = new PointToPoint[max];
		for(int i = 0; i<max; i++){
			Point p = new Point(0,0);
			Node droneNode = new Node(droneID+i, new TupleSpace());
			droneNode.addPort(port);
			AbstractDrone AI = (i < exploreDrones) ? new ExpDrone(p) : new HarDrone(p);
			//AbstractDrone AI = new TestDrone(p);
			droneNode.addAgent(AI);
			droneNode.addAttribute(new Attribute("AI", AI)); //for UI Only
			droneNodes[i] = droneNode;
			p2drones[i] = new PointToPoint(droneID+i, new VirtualPortAddress(port_int));
			
			baseNode.put(new Tuple(AI.type, p.x, p.y));
			map.put(new Tuple(AI.type, p.x, p.y));
		}
		AbstractDrone.self2drone = p2drones;
		for(Node droneNode : droneNodes){
			droneNode.put(new Tuple("ready"));
			droneNode.start();
		}
		
		
	}

	public static void main(String[] args) throws InterruptedException{
<<<<<<< HEAD
		//nicklas();
		//michael();
		
		run();
	}
	
	public static void run() throws InterruptedException {
		nicklas();
=======
		nicklas();
		michael();
>>>>>>> refs/remotes/origin/Lam
	}
	
	public static void michael() throws InterruptedException {		
		//Map
		Node mapNode = new Node(mapID, new TupleSpace());
		mapNode.addPort(port);
		mapNode.addAgent(new RetrieverNew());
//		mapNode.addAgent(new Generator(seed)); //if null => random generator
//		mapNode.addAgent(new Hasher());
//		mapNode.addAgent(new Retriever());
		mapNode.start();
		
		//Base
		Node baseNode = new Node(baseID, new TupleSpace());
		baseNode.addPort(port);
		baseNode.addAgent(new MapMerger());
		baseNode.addAgent(new BaseAgent());
		baseNode.addAgent(new RetrieverNew());
		baseNode.put(new Tuple("BASE", 0, 0));
		baseNode.put(new Tuple("Radius", 0));
		baseNode.put(new Tuple("GoldCounter", startGoldCount));
		baseNode.put(new Tuple("TreeCounter", startTreeCount));
		baseNode.put(new Tuple("ExpDroneCounter", exploreDrones));
		baseNode.put(new Tuple("HarDroneCounter", harvestDrones));
		baseNode.put(new Tuple("mapEdge", 0));
		baseNode.start();
		
		//Drones
		AbstractDrone.self2base = new PointToPoint(baseID, new VirtualPortAddress(port_int));
		AbstractDrone.self2map = new PointToPoint(mapID, new VirtualPortAddress(port_int));
		int max = exploreDrones+harvestDrones;
		Node[] droneNodes = new Node[max];
		PointToPoint[] p2drones = new PointToPoint[max];
		for(int i = 0; i<max; i++){
			Point p = new Point(0,0);
			Node droneNode = new Node(droneID+i, new TupleSpace());
			droneNode.addPort(port);
			AbstractDrone AI = (i < exploreDrones) ? new ExpDrone(p) : new HarDrone(p);
			//AbstractDrone AI = new TestDrone(p);
			droneNode.addAgent(AI);
			droneNode.addAttribute(new Attribute("AI", AI)); //for UI Only
			droneNodes[i] = droneNode;
			p2drones[i] = new PointToPoint(droneID+i, new VirtualPortAddress(port_int));
			
			baseNode.put(new Tuple(AI.type, p.x, p.y));
			//map.put(new Tuple(AI.type, p.x, p.y));
		}
		AbstractDrone.self2drone = p2drones;
		for(Node droneNode : droneNodes){
			droneNode.put(new Tuple("ready"));
			droneNode.start();
		}
		
		//UI		
		//new Console(baseNode, mapNode, droneNodes, DELAY, FIELD);
	}
}
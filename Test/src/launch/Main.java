package launch;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import baseNode.*;
import droneNode.*;
import map.*; //TEMP
import mapNode.*;
import userInterface.*;

public class Main {

	public static final int DELAY = 500;
	public static final int FIELD = 40;
	
	static final int port_int = 8080;
	static final VirtualPort port = new VirtualPort(port_int);
	static final String baseID = "baseNode";
	static final String mapID = "mapNode";
	static final String droneID = "drone";
	static final String uiID = "console";

	static final int exploreDrones = 3;
	static final int harvestDrones = 3;
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;
	static final String seed = null;
	
	public static void main(String[] args) throws InterruptedException {		
		//Map
		Node mapNode = new Node(mapID, new TupleSpace());
		mapNode.addPort(port);
//		mapNode.addAgent(new Generator(seed)); //if null => random generator
//		mapNode.addAgent(new Hasher());
//		mapNode.addAgent(new Retriever());
		mapNode.start();
		
		//Base
		Node baseNode = new Node(baseID, new TupleSpace());
		baseNode.addPort(port);
		baseNode.addAgent(new MapMerger());
		baseNode.addAgent(new BaseAgent());
		baseNode.put(new Tuple("GoldCounter", startGoldCount));
		baseNode.put(new Tuple("TreeCounter", startTreeCount));
		baseNode.put(new Tuple("ExpDroneCounter", exploreDrones));
		baseNode.put(new Tuple("HarDroneCounter", harvestDrones));
		baseNode.start();
		
		
		//Drones
		int max = exploreDrones+harvestDrones;
		Node[] droneNodes = new Node[max];
		PointToPoint[] p2drones = new PointToPoint[max];
		for(int i = 0; i<max; i++){
			Node droneNode = new Node(droneID+i, new TupleSpace());
			droneNode.addPort(port);
//			Agent AI = (i > exploreDrones) ? new HarDrone() : new ExpDrone();
//			AI.setPostition(new Point(x?, y?));
//			drone.addAgent(AI);
			droneNodes[i] = droneNode;
			p2drones[i] = new PointToPoint(droneID+i, new VirtualPortAddress(port_int));
		}
		DroneAI.self2base = new PointToPoint(baseID, new VirtualPortAddress(port_int));
		DroneAI.self2map = new PointToPoint(mapID, new VirtualPortAddress(port_int));
		DroneAI.self2drone = p2drones;
		for(Node droneNode : droneNodes){
			droneNode.put(new Tuple("ready"));
			droneNode.start();
		}
		
		//UI
//		new Console(mapNode, baseNode, droneNodes).start();

//		//UI - query for all nodes to put a "ready" tuple in their own TupleSpace
//		//UI - reads position of baseNode and every droneNode
//		//UI - reads position of known resources from baseNode's TupleSpace
//		//UI - prints board
//		//UI - getall "ready" tuples + put for all "go!" tuple
		
		//TEMP
		Map map = new Map();
		new Console(map, DELAY, FIELD);
	}
}
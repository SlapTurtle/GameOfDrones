package Main;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Attribute;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import UI.*;
import map.*;
import baseAI.*;


public class Main {

	public static final int DELAY = 500;
	public static final int FIELD = 40;
	
	public static final int port_int = 8080;
	public static final VirtualPort port = new VirtualPort(port_int);
	public static final String baseID = "baseNode";
	public static final String mapID = "mapNode";
	public static final String droneID = "drone";
	public static final String uiID = "console";

	static int exploreDrones = 3;
	static int harvestDrones = 3;
	static String seed = null;
	
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
		baseNode.addAgent(new mapMerger());
		baseNode.addAgent(new BaseAgent());
		baseNode.start();
		baseNode.put(new Tuple("GoldCounter", 0));
		baseNode.put(new Tuple("TreeCounter", 0));
		
		//Drones
		PointToPoint p2base = new PointToPoint(baseID, new VirtualPortAddress(port_int));
		PointToPoint p2map = new PointToPoint(mapID, new VirtualPortAddress(port_int));
		int max = exploreDrones+harvestDrones;
		Node[] allDrones = new Node[max];
		for(int i = 0; i<max; i++){
			Node droneNode = new Node(droneID+i, new TupleSpace());
			droneNode.addPort(port);
			droneNode.addAttribute(new Attribute("p2base", p2base));
			droneNode.addAttribute(new Attribute("p2map", p2map));
//			Agent AI = (i > exploreDrones) ? new HarDrone() : new ExpDrone();
//			AI.setPostition(new Point(x?, y?));
//			drone.addAgent(AI);
			droneNode.start();
			droneNode.put(new Tuple("ready", droneID+i));
		}
		
		//UI
//		new Console(mapNode, baseNode, allDrones).start();

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
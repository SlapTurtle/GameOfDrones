package baseNode;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import droneNode.Drone;
import util.*;

public class Base extends Node {

	public Base(String name, VirtualPort port, int port_int, int startGoldCount, int startTreeCount, int exploreDrones, int harvestDrones, int retrieverCount) {
		// new Node(name, new TupleSpace())
		super(name, new TupleSpace());
		
		// Ports
		addPort(port);
		Drone.self2base = new PointToPoint(name, new VirtualPortAddress(port_int));
		
		// Agents
		addAgent(new MapMerger());
		addAgent(new HarvestAgent());
		for(int i = 0; i < retrieverCount; i++){
			addAgent(new Retriever());
		}
		
		// Setup
		if(exploreDrones > 0){
			//Locks for Exploration and Resource transitions
			put(new Tuple("ExpLock"));
			put(new Tuple("ResLock"));
		}
		put(new Tuple("BASE", 0, 0));
		put(new Tuple("Radius", 0));
		put(new Tuple("GoldCounter", startGoldCount));
		put(new Tuple("TreeCounter", startTreeCount));
		put(new Tuple("ExpDroneCounter", exploreDrones));
		put(new Tuple("HarDroneCounter", harvestDrones));
		put(new Tuple("mapEdge", 0));
		
		// Adds initially explored tiles
		for (int y = -1; y <= +1; y++) {
			for (int x = -1; x <= +1; x++) { 
				if (!(x == 0 && y == 0)) {
					put(new Tuple(x,y,MapMerger.ACTION_NEW));
				}
			}
		}
		
		// Go!
		start();
	}
}

package droneNode;

import java.awt.Point;
import java.util.LinkedList;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Attribute;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import baseNode.Base;
import droneNode.AbstractDrone;

public class Drone extends Node {
	public static PointToPoint self2base;
	public static PointToPoint self2map;
	public static LinkedList<PointToPoint> self2drone;
	
	public Drone(String name, String type, VirtualPort port, int port_int, Base baseNode) {
		// new Node(name, new TupleSpace())
		super(name, new TupleSpace());
		
		// Ports
		addPort(port);
		if(self2drone == null) self2drone = new LinkedList<PointToPoint>();
		self2drone.add(new PointToPoint(name, new VirtualPortAddress(port_int)));
		
		// Agents
		AbstractDrone AI;
		switch(type){
		default: AI = null;
		case "HARDRONE": AI = new HarDrone(new Point(0,0)); break;
		case "EXPDRONE": AI = new ExpDrone(new Point(0,0)); break;
		}
		addAgent(AI);
		
		// Add initial Location to Base
		baseNode.put(new Tuple(AI.type, 0, 0, AI.id));
		
		// For UI Only
		addAttribute(new Attribute("AI", AI));
		
		// Go!
		start();
	}
}

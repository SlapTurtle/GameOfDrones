package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.Attribute;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;
import org.cmg.resp.topology.VirtualPort;
import org.cmg.resp.topology.VirtualPortAddress;

import baseNode.Base;
import droneNode.AbstractDrone;
import map.Map;
import resources.Expdrone;
import resources.Hardrone;

public class Drone extends Node {
	public static PointToPoint self2base;
	public static PointToPoint self2map;
	public static LinkedList<PointToPoint> self2drone;
	
	public static int count = 0;
	public Drone(String name, String type, VirtualPort port, int port_int, Base base, Map map) throws InterruptedException, IOException {
		// new Node(name, new TupleSpace())
		super(name, new TupleSpace());
		
		// Ports
		addPort(port);
		if(self2drone == null) self2drone = new LinkedList<PointToPoint>();
		self2drone.add(new PointToPoint(name, new VirtualPortAddress(port_int)));
		
		// Agents
		Point q = null;
		if(type.equals(Hardrone.type)){
			switch(count++){
			default: q = new Point(0,1); break;
			case 1: q = new Point(0,-1); break;
			case 2: q = new Point(1,-0); break;
			case 3: q = new Point(-1,0); break;
			}
		}
		
		Point p = (type.equals(Hardrone.type)) ? q : new Point(0,0);
		AbstractDrone AI;
		switch(type){
		default: AI = null;
		case Hardrone.type: AI = new HarDrone(p, name); break;
		case Expdrone.type: AI = new ExpDrone(p, name); break;
		}
		addAgent(AI);
		
		if(AI.type.equals(Hardrone.type)){
			this.put(new Tuple("next_move", p.x, p.y), Self.SELF);
		}
		
		// Add initial Location to Base
		base.put(new Tuple(AI.type, p.x, p.y, AI.id));
		map.put(new Tuple(AI.type, p.x, p.y, AI.id));
		this.put(new Tuple(p),Self.SELF);
		
		// Go!
		start();
	}
}

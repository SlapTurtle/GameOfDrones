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

import droneNode.Drone;
import resources.Expdrone;
import util.Retriever;

import java.awt.Point;

public class Map extends Node {
	
	public static final int DEFAULTGRID = 40;
	public static final int EXP_HASHLENGTH = 16;
	public static final FormalTemplateField AnyString = new FormalTemplateField(String.class);
	public static final FormalTemplateField AnyInteger = new FormalTemplateField(Integer.class);
	public static final FormalTemplateField AnyPoint = new FormalTemplateField(Point.class);
	public static final FormalTemplateField AnyWorld = new FormalTemplateField(World.class);
	public static final Template TEMPLATE_ALL = new Template(AnyString, AnyInteger, AnyInteger);
	public static final Template TEMPLATE_EXPDRONE = new Template(new ActualTemplateField(Expdrone.type), AnyInteger, AnyInteger, AnyString);

	public Map(String name, String seed, VirtualPort port, int port_int, int retrieverCount) {
		super(name, new TupleSpace());
		
		// Ports
		addPort(port);
		Drone.self2map = new PointToPoint(name, new VirtualPortAddress(port_int));

		// Agents
		addAgent(new DroneListener());
		addAgent(new Hasher());
		addAgent(new Generator());
		for(int i = 0; i < retrieverCount; i++){
			addAgent(new Retriever());
		}

		// Generate map
		System.out.println("Seed: " + seed);
		put(new Tuple("seed", seed));
		put(new Tuple("bounds", new int[] { -Map.DEFAULTGRID/2, Map.DEFAULTGRID/2, -Map.DEFAULTGRID/2, Map.DEFAULTGRID/2 }));
		put(new Tuple("generate", new World(new Point(0,0)), seed));
		
		// Go!
		start();
	}	
}
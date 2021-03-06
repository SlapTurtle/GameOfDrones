package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

public abstract class AbstractDrone extends Agent {
	public String type;
	public String id;
	public Point position;
	
	public AbstractDrone(Point position, String type, String id) {
		super(id);
		this.type = type;
		this.id = id;
		this.position = position;
		
	}
	
	
	@Override
	protected final void doRun() throws Exception {
		
		while(true){
			try {
				get(new Template(new ActualTemplateField("go")), Self.SELF);
				move(moveDrone());
				droneAction();
				put(new Tuple("ready"),Self.SELF);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	

	//Main move method for drones
	protected abstract Point moveDrone() throws Exception;
	//Secondary effect (harvest, explore etc.)
	protected abstract void droneAction() throws Exception;

	protected final boolean move(Point p) throws InterruptedException, IOException {
		if (p==null || p.distance(position) > 1.21) return false;
		Template template = new Template(   
						new ActualTemplateField(type),
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class),
						new ActualTemplateField(id));
		position = new Point(p.x, p.y);
		get(template, Drone.self2base);
		put(new Tuple(type, p.x, p.y, id), Drone.self2base);
		get(template, Drone.self2map);
		put(new Tuple(type, p.x, p.y, id), Drone.self2map);
		get(new Template(new FormalTemplateField(Point.class)), Self.SELF);
		put(new Tuple(p), Self.SELF);
		return true;
	}
	
	protected final LinkedList<Point> getNeighborPoints(Point p) {
		return getNeighborPoints(p, 1);
	}
	protected final LinkedList<Point> getNeighborPoints(Point p, int dist) {
		LinkedList<Point> list = new LinkedList<Point>();
		for (int y = p.y-dist; y <= p.y+dist; y++)
			for (int x = p.x-dist; x <= p.x+dist; x++) 
				if (!(x == p.x && y == p.y))
					list.add(new Point(x,y));
		return list;
	}
}


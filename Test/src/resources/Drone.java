package resources;

import java.awt.Point;
import java.util.Random;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;
import map.*;
import util.Position;

public class Drone extends Agent {

	protected Map map;
	public String TYPE;
	public UUID ID;
	public Position position = new Position();
	
	public Drone(Map map, Point position) {
		super(UUID.randomUUID().toString());
		this.ID = UUID.fromString(this.name);
		this.map = map;
		this.position = new Position(position.x, position.y);
		this.map.drones.add(this);
	}

	protected void doRun() {
		Random r = new Random();
		Dice dice = new Dice(r);
		int dir = r.nextInt(4);
		try {
			explore();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			
			map.RetrievePathableNeighbors(position);
			
			if (dice.roll(0.4)) {
				move(dir);
			} else {
				move(r.nextInt(4));
			}
		}
	}
	
	private void explore() throws Exception {
		for (Point p : World.getNeighbors(position.toPoint())) {
			Template t = new Template(new ActualTemplateField(p.x), new ActualTemplateField(p.y));
			boolean b = (queryp(t) == null) ? put(new Tuple(p.x, p.y), Self.SELF) : false;
		}
	}
	
	private void move(int dir) {
		int[] xy = getDirection(dir, position.x, position.y);
		move(new Point(xy[0], xy[1]));
		//map.UI.move(ID, dir);
	}

	protected void move(Point p) {
		if (p.distance(position.toPoint()) > 1.21)
			return;
		try {
			Template template = new Template(new ActualTemplateField(TYPE),
					new ActualTemplateField(position.x),
					new ActualTemplateField(position.y));
			get(template, Self.SELF);
			int[] xy = new int[]{p.x,p.y};
			position.move(xy[0], xy[1]);
			Tuple t2 = new Tuple(TYPE, xy[0], xy[1]);
			put(t2, Self.SELF);
			
			//map.UI.moveDrone(map.random.nextInt(10), 0);
			
			explore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int[] getDirection(int dir, int x, int y){
		switch(dir){
			case 0: x-=1; break; // LEFT
			case 1: x+=1; break; // RIGHT
			case 2: y-=1; break; // UP
			case 3: y+=1; break; // DOWN
		}
		return new int[]{x,y};
	}
}


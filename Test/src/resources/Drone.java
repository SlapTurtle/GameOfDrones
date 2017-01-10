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

public class Drone extends Agent {

	Map map;
	protected String TYPE;
	Point position = new Point();
	
	public Drone(Map map, Point position) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.position = position;
	}
	
	protected void doRun() {
		Random r = new Random();
		try {
			explore();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean b = false;
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			if (r.nextInt(5) <= 1) {
				if (b) {
					move(1);
				} else {
					move(2);
				}
				b = !b;
			} else {
				move(r.nextInt(4));
			}
		}
	}
	
	private void explore() throws Exception {
		for (Point p : World.getNeighbors(position)) {
			Template t = new Template(new ActualTemplateField(p.x), new ActualTemplateField(p.y));
			boolean b = (queryp(t) == null) ? put(new Tuple(p.x, p.y), Self.SELF) : false;
		}
	}
	
	private void move(int dir) {
		int[] xy = getDirection(dir, position.x, position.y);
		move(new Point(xy[0], xy[1]));
	}
	
	private void move(Point p) {
		if (p.distance(position) > 1.21)
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


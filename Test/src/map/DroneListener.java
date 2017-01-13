package map;

import java.awt.Point;
import java.util.LinkedList;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.Tuple;

public class DroneListener extends Agent {
	
	Map map;
	Point center;

	public DroneListener(Map map, Point center) {
		super("DroneListener"+center.x+center.y);
		this.map = map;
		this.center = center;
	}

	protected void doRun() throws Exception {
		boolean b = true;
		while(b) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
				}
			}
			LinkedList<Tuple> drones = map.RetrieveTuples("EXPDRONE");
			if (drones != null) {
				for (Tuple t : drones) {
					Point p = new Point(Map.getTupleX(t), Map.getTupleY(t));
					if (b && p.distance(center) <= (World.DEFAULT-2)) {
						//System.out.println("DRONE " + p.x + "," + p.y + " IN RANGE OF " + center.x + "," + center.y + " [" + p.distance(center) + "]");
						map.expandWorld(center);
						b = false;
					}
				}
			}	
		}	
	}
}

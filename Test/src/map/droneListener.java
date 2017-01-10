package map;

import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.Tuple;

import resources.ExplorationDrone;

public class droneListener extends Agent {
	
	Map map;
	Point center;

	public droneListener(Map map, Point center) {
		super(UUID.randomUUID().toString());
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
						System.out.println("DRONE " + p.x + "," + p.y + " IN RANGE OF " + center.x + "," + center.y + " [" + p.distance(center) + "]");
						map.expandWorld(center);
						b = false;
					}
				}
			}
			
		}
		
		
		
	}

	
	
}

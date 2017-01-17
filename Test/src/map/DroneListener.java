package map;

import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

public class DroneListener extends Agent {

	Template getPoints = new Template(Map.AnyInteger, Map.AnyInteger, Map.AnyString);
	
	public DroneListener() {
		super(UUID.randomUUID().toString());
	}

	protected void doRun() {
		while(true) {
			try {
				Thread.sleep(50);
				LinkedList<Tuple> l = queryAll(getPoints);
				for(Tuple t : l) {
					Point p = new Point(t.getElementAt(Integer.class, 0), t.getElementAt(Integer.class, 1));
					if (t.getElementAt(String.class, 2) == "listen" && !p.equals(new Point(0,0))) {
						LinkedList<Tuple> drones = queryAll(Map.TEMPLATE_EXPDRONE);
						for (Tuple d : drones) {
							Point dp = new Point((int)d.getElementAt(1), (int)d.getElementAt(2));
							if (p.distance(dp) <= Map.DEFAULTGRID-2) {
								expandWorld(p);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean expandWorld(Point p) {
		World world = new World(p, Map.DEFAULTGRID);
		try {
			Tuple t = get(new Template(new ActualTemplateField(p.x), new ActualTemplateField(p.y), new ActualTemplateField("listen")), Self.SELF);
			put(new Tuple(t.getElementAt(Integer.class, 0), t.getElementAt(Integer.class, 1), "unlisten"), Self.SELF);
			String identifier;
			String seed = (String)query(new Template(new ActualTemplateField("seed"), Map.AnyString), Self.SELF).getElementAt(1);
			put(new HashRequest(identifier = UUID.randomUUID().toString(), p, seed, Map.EXP_HASHLENGTH), Self.SELF);
			get(new Template(new ActualTemplateField(identifier), Map.AnyString), Self.SELF);
			put(new Tuple("generate", world, seed), Self.SELF);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}

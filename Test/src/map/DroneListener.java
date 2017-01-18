package map;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import launch.Main;

public class DroneListener extends Agent {
	
	public static final int sensitivity = (Map.DEFAULTGRID/2) + 5;
	Template getAnyListner = new Template(Map.AnyInteger, Map.AnyInteger, Map.AnyString);
	Template getOnlyListener = new Template(Map.AnyInteger, Map.AnyInteger, new ActualTemplateField("listen"));
	
	public DroneListener() {
		super(UUID.randomUUID().toString());
	}

	protected void doRun() throws InterruptedException, IOException {
		while(true) {
			LinkedList<Tuple> l = queryAll(getOnlyListener);
			LinkedList<Tuple> drones = queryAll(Map.TEMPLATE_EXPDRONE);
			for(Tuple t : l) {
				Point p = new Point(t.getElementAt(Integer.class, 0), t.getElementAt(Integer.class, 1));
				if (!p.equals(new Point(0,0))) {
					for (Tuple d : drones) {
						Point dp = new Point((int)d.getElementAt(1), (int)d.getElementAt(2));
						if (p.distance(dp) <= sensitivity) {
							expandWorld(p);
							break;
						}
					}
				}
			}
		}
	}

	private boolean expandWorld(Point p) {
		World world = new World(p, Map.DEFAULTGRID);
		try {
			get(new Template(new ActualTemplateField(p.x), new ActualTemplateField(p.y), new ActualTemplateField("listen")), Self.SELF);
			put(new Tuple(p.x, p.y, "unlisten"), Self.SELF);
			String seed = query(new Template(new ActualTemplateField("seed"), Map.AnyString), Self.SELF).getElementAt(String.class, 1);
//			String identifier = UUID.randomUUID().toString();
			put(new HashRequest(p, seed, Map.EXP_HASHLENGTH, world), Self.SELF);
//			String hash = get(new Template(new ActualTemplateField(identifier), Map.AnyString), Self.SELF).getElementAt(String.class, 1);
//			put(new Tuple("generate", world, hash), Self.SELF);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
}

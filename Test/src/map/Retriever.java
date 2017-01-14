package map;

import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import resources.Resource;

/** Agent for retrieving Tuples from the Map Tublespace. */
class Retriever extends Agent {
	
	String type;
	protected LinkedList<Tuple> Tuples;

	public Retriever(String type) {
		super(UUID.randomUUID().toString());
		this.type = type;
	}

	public Retriever() {
		super(UUID.randomUUID().toString());	
	}
	
	protected void doRun() {	
		Template T = (type == null) ? Map.TEMPLATE_ALL
									: new Template(new ActualTemplateField(type), Map.AnyInteger, Map.AnyInteger);
		Tuples = queryAll(T);
		synchronized (Map.syncRetrieval) {
			Map.syncRetrieval.notifyAll();
		}
	}
}

class NeighborRetriever extends Retriever {

	LinkedList<Point> neighbors = new LinkedList<Point>();
	Point point;
	
	public NeighborRetriever(Point p) {
		super();
		this.point = p;
	}

	protected void doRun() {
		for(Point p : World.getNeighbors(point)) {
			if (p.distance(point) < 1.1) {
				Tuple t = queryp(templateFromPoint(p));
				if (t != null) {
					if (Resource.isPathable(t.getElementAt(String.class, 0))) {
						this.neighbors.add(new Point(Map.getTupleX(t), Map.getTupleY(t)));
					}
				} else {
					this.neighbors.add(p);
				}
			}	
		}
		synchronized (Map.syncRetrieval) {
			Map.syncRetrieval.notifyAll();
		}
	}
	
	private Template templateFromPoint(Point p) {
		return new Template(Map.AnyString, new ActualTemplateField(p.x), new ActualTemplateField(p.y));
	}
	
}


/** Legacy Agent used for deletion of Tuples in the Map Tublespace. */
class Bulldozer extends Agent {
	
	Map map;
	String type;
	LinkedList<Tuple> Tuples;

	public Bulldozer(Map map, String type) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.type = type;
	}
	
	public Bulldozer(Map map) {
		super(UUID.randomUUID().toString());
		this.map = map;		
	}

	protected void doRun() {
		Template T = (type == null) ? Map.TEMPLATE_ALL
									: new Template(new ActualTemplateField(type), Map.AnyInteger, Map.AnyInteger);
		while(queryp(Map.TEMPLATE_ALL) != null) {
			getAll(T);
		}
	}
}
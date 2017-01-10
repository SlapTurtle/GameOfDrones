package map;

import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

/** Agent for retrieving Tuples from the Map Tublespace. */
class Retriever extends Agent {
	
	Map map;
	String type;
	LinkedList<Tuple> Tuples;

	public Retriever(Map map, String type) {
		super(UUID.randomUUID().toString());
		this.map = map;
		this.type = type;
	}
	
	public Retriever(Map map) {
		super(UUID.randomUUID().toString());
		this.map = map;		
	}

	protected void doRun() {	
		Template T = (type == null) ? Map.TEMPLATE_ALL
									: new Template(new ActualTemplateField(type), Map.AnyInteger, Map.AnyInteger);
		Tuples = queryAll(T);
		synchronized (map.syncRetrieval) {
			map.syncRetrieval.notifyAll();
		}	
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
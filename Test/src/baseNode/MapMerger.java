package baseNode;

import java.util.LinkedList;
import java.util.Scanner;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import map.Map;

public class MapMerger extends Agent {
	public static final String ACTION_NEW = "new";
	public static final String ACTION_OLD = "old";
	public static final String MAP_EDGE = "mapEdge";
	
	public MapMerger() {
		super("MapMerger");
	}

	@Override
	protected void doRun() throws Exception {
		while(true){
			get(new Template(new ActualTemplateField("goMM")), Self.SELF);
			Tuple trange = query(new Template(new ActualTemplateField(MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF);
			int range = trange.getElementAt(Integer.class, 1) + 1;
			
			//checks that a ring exists
			LinkedList<Tuple> list = queryAll(new Template(Map.AnyInteger, Map.AnyInteger, new ActualTemplateField(ACTION_OLD)));
			LinkedList<Tuple> get = new LinkedList<Tuple>();
			for(Tuple tu : list){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				boolean bx = (Math.abs(x) == range && Math.abs(y) <= range);
				boolean by = (Math.abs(y) == range && Math.abs(x) <= range);
				if(bx || by) get.add(tu);
				else if(Math.abs(x) < range && Math.abs(y) < range) get(searchXY(x,y),Self.SELF);
			}
			if(get.size() == 8*(range)){
				//gets all tuples in the ring.
				for(Tuple tu : get){
					int x = tu.getElementAt(Integer.class, 0);
					int y = tu.getElementAt(Integer.class, 1);
					get(searchXY(x,y),Self.SELF);
				}
				
				//increases count
				trange = get(new Template(new ActualTemplateField(MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF);
				put(new Tuple(MAP_EDGE, range),Self.SELF);
			}
			put(new Tuple("readyMM"),Self.SELF);
		}
	}
	
	private Template searchXY(int x, int y){
		return new Template(new ActualTemplateField(x), new ActualTemplateField(y), new ActualTemplateField(ACTION_OLD));
	}
}

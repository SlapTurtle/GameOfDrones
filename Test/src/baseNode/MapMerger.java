package baseNode;

import java.util.LinkedList;

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
			boolean b = true;
			LinkedList<Tuple> list = queryAll(new Template(Map.AnyInteger, Map.AnyInteger, new ActualTemplateField(ACTION_OLD)));
			LinkedList<Tuple> get = new LinkedList<Tuple>();
			for(Tuple tu : list){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				if(Math.abs(x)==range || Math.abs(y)==range){
					boolean bx = (Math.abs(x) == range && y <= range && y >= -range);
					boolean by = (Math.abs(y) == range && x <= range && x >= -range);
					b = bx || by;
					get.add(tu);
				}
				else if(Math.abs(x) <= range-1 && Math.abs(y) <= range-1){
					get(searchXY(x,y),Self.SELF);
				}
				if(!b) break;
			}
			if(b && get.size() > 0){
				//gets all tuples in the ring.
				for(Tuple tu : get){
					int x = tu.getElementAt(Integer.class, 0);
					int y = tu.getElementAt(Integer.class, 1);
					get(searchXY(x,y),Self.SELF);
				}
				
				//increases count
				trange = get(new Template(new ActualTemplateField(MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF);
				int i = trange.getElementAt(Integer.class, 1) + 1;
				put(new Tuple(MAP_EDGE, i),Self.SELF);
				System.out.println("MAPEDGE"+ i);
			}
			put(new Tuple("readyMM"),Self.SELF);
		}
	}
	
	private Template searchXY(int x, int y){
		return new Template(new ActualTemplateField(x), new ActualTemplateField(y), new ActualTemplateField(ACTION_OLD));
	}
}

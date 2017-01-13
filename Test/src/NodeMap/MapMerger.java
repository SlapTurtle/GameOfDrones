package NodeMap;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

public class MapMerger extends Agent {
	public static final String ACTION_NEW = "new";
	public static final String MAP_EDGE = "mapedge";
	
	public MapMerger() {
		super("MapMerger");
	}

	@Override
	protected void doRun() throws Exception {
		
		put(new Tuple(MAP_EDGE, 0),Self.SELF);
		
		while(true){
			Tuple tu = query(new Template(new ActualTemplateField(MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF);
			double range = tu.getElementAt(Integer.class, 1);
			
			//checks that a ring exists
			for(int i = 1; i<360; i++){
				int x = (int) (range*Math.cos(deg2rad(i)));
				int y = (int) (range*Math.sin(deg2rad(i)));
				query(searchXY(x,y), Self.SELF);
			}
			
			//gets all tuples in the ring.
			for(int i = 1; i<360; i++){
				int x = (int) (range*Math.cos(deg2rad(i)));
				int y = (int) (range*Math.sin(deg2rad(i)));
				getp(searchXY(x,y));
			}
			
			//increases count
			tu = get(new Template(new ActualTemplateField(MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF);
			int i = tu.getElementAt(Integer.class, 1) + 1;
			put(new Tuple(MAP_EDGE, i),Self.SELF);
		}
	}
	
	private double deg2rad(float deg){
		return deg*(180/Math.PI);
	}
	
	private Template searchXY(int x, int y){
		return new Template(new ActualTemplateField(ACTION_NEW), new ActualTemplateField(x), new ActualTemplateField(y));
	}
}

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

import resources.Resource;

public class RetrieverNew extends Agent {
	
	public RetrieverNew() {
		super(UUID.randomUUID().toString());
	}

	@Override
	protected void doRun() throws Exception {
		Template t = new Template(
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class),
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class)
					);
		
		while(true) {
			Tuple in = get(t, Self.SELF);
			String order = in.getElementAt(String.class, 0);
			String id = in.getElementAt(String.class, 1);
			int x = in.getElementAt(Integer.class, 2);
			int y = in.getElementAt(Integer.class, 3);
			
			LinkedList<Point> list;
			switch(order){
			default: list = new LinkedList<Point>(); break;
			case "neighbours_all": list = getNeighbours(x,y); break;
			case "neighbours_pathable": list = getPathableNeighbours(x,y); break;
			}
			put(new Tuple(order, id, list), Self.SELF);			
		}
	}
	
	private LinkedList<Point> getPathableNeighbours(int x, int y) {
		LinkedList<Point> list = new LinkedList<Point>();
		Tuple res[] = {
				getp(templateXY(x+1,y)),
				getp(templateXY(x-1,y)),
				getp(templateXY(x,y+1)),
				getp(templateXY(x,y-1))				
		};
		for(int i = 0; i<res.length; i++){
			if(res[i] == null || Resource.isPathable(res[i].getElementAt(String.class, 0))) {
				int dx,dy;
				switch(i){
				default: dx = 1; dy = 0; break;
				case 1: dx = -1; dy = 0; break;
				case 2: dx = 0; dy = 1; break;
				case 3: dx = 0; dy = -1; break;
				}
				list.add(new Point(x+dx,y+dy));
			}
		}		
		return list;
	}
	
	private LinkedList<Point> getNeighbours(int x0, int y0) {
		LinkedList<Point> list = new LinkedList<Point>();
		for (int y = y0-1; y <= y0+1; y++) {
			for (int x = x0-1; x <= x0+1; x++) { 
				if (!(x == x0 && y == y0)) {
					Tuple tu = getp(templateXY(x+1,y));
					if(tu != null) {
						list.add(new Point(tu.getElementAt(Integer.class, 1), tu.getElementAt(Integer.class, 2)));
					}
				}
			}
		}
		return list;
	}	
	
	private Template templateXY(int x, int y) {
		return new Template(
				new FormalTemplateField(String.class),
				new ActualTemplateField(x),
				new ActualTemplateField(y)
			);
	}
}

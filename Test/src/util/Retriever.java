package util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.UUID;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import droneNode.HarDrone;
import resources.Empty;
import resources.Resource;

public class Retriever extends Agent {
	
	public Retriever() {
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
			
			Object list;
			switch(order){
			default: list = null; break;
			case "neighbours_explore" : list = getNeighboursExplore(x,y); break;
			case "neighbours_all": list = getNeighbours(x,y); break;
			case "neighbours_pathable": list = getPathableNeighbours(x,y); break;
			}
			put(new Tuple(order, id, list), Self.SELF);			
		}
	}	
	
	private LinkedList<Tuple> getNeighboursExplore(int x0, int y0) {
		LinkedList<Tuple> list = new LinkedList<Tuple>();
		for (int y = y0-1; y <= y0+1; y++) {
			for (int x = x0-1; x <= x0+1; x++) { 
				if (!(x == x0 && y == y0)) {
					Tuple tu = queryp(templateXYExplore(x,y));
					if(tu != null) {
						list.add(tu);
					}
					else{
						list.add(new Tuple(x, y, Empty.type));
					}
				}
			}
		}
		return list;
	}
	
	private LinkedList<Point> getPathableNeighbours(int x, int y) {
		LinkedList<Point> list = new LinkedList<Point>();
		Tuple res[] = {
				queryp(templateXY(x+1,y)),
				queryp(templateXY(x-1,y)),
				queryp(templateXY(x,y+1)),
				queryp(templateXY(x,y-1))				
		};
		Tuple dro[] = {
				queryp(templateXYDrone(x+1,y)),
				queryp(templateXYDrone(x-1,y)),
				queryp(templateXYDrone(x,y+1)),
				queryp(templateXYDrone(x,y-1))				
		};
		for(int i = 0; i<4; i++){
			if(	(res[i] == null || Resource.isPathable(res[i].getElementAt(String.class, 0)))	&&
				(dro[i] == null || Resource.isPathable(dro[i].getElementAt(String.class, 0))) 	){
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
	
	private LinkedList<Tuple> getNeighbours(int x0, int y0) {
		LinkedList<Tuple> list = new LinkedList<Tuple>();
		for (int y = y0-1; y <= y0+1; y++) {
			for (int x = x0-1; x <= x0+1; x++) { 
				if (!(x == x0 && y == y0)) {
					Tuple tu = queryp(templateXY(x,y));
					if(tu != null) {
						list.add(tu);
					}
					else{
						list.add(new Tuple(Empty.type, x, y));
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
	
	private Template templateXYExplore(int x, int y) {
		return new Template(
				new ActualTemplateField(x),
				new ActualTemplateField(y),
				new FormalTemplateField(String.class)
			);
	}
	
	private Template templateXYDrone(int x, int y) {
		return new Template(
				new ActualTemplateField(HarDrone.type),
				new ActualTemplateField(x),
				new ActualTemplateField(y),
				new FormalTemplateField(String.class)
			);
	}
}

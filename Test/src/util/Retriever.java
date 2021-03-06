package util;

import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.UUID;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import baseNode.MapMerger;
import map.Map;
import resources.Hardrone;
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
			
			Object response;
			switch(order){
			default: response = null; break;
			case "neighbours_explore" : response = getNeighboursExplore(x,y); break;
			case "neighbours_all": response = getNeighbours(x,y); break;
			case "neighbours_pathable": response = getPathableNeighbours(x,y); break;
			case "single_pathable": response = getSinglePathable(x,y); break;
			}
			put(new Tuple(order, id, response), Self.SELF);			
		}
	}	
	
	private int getSinglePathable(int x, int y) throws InterruptedException, IOException {
		boolean b = false;
		
		Tuple res = queryp(new Template(Map.AnyString, new ActualTemplateField(x), new ActualTemplateField(y)));
		Tuple res2 = queryp(new Template(Map.AnyString, new ActualTemplateField(x), new ActualTemplateField(y), Map.AnyInteger));
		Tuple dro = queryp(new Template(Map.AnyString, new ActualTemplateField(x), new ActualTemplateField(y), Map.AnyString));
		Tuple exp = queryp(new Template(new ActualTemplateField(x), new ActualTemplateField(y), Map.AnyString));
		int range = query(new Template(new ActualTemplateField(MapMerger.MAP_EDGE), Map.AnyInteger),Self.SELF).getElementAt(Integer.class, 1);
		if(	(res == null || Resource.isPathable(res.getElementAt(String.class, 0)))	&&
			(res2 == null || Resource.isPathable(res2.getElementAt(String.class, 0))) &&
			(dro == null || Resource.isPathable(dro.getElementAt(String.class, 0))) &&
			(exp != null || (Math.abs(x) <= range && Math.abs(y) <= range))	){
				b = true;
			}
		return (b) ? 1:0 ;
	}

	private LinkedList<Tuple> getNeighboursExplore(int x0, int y0) {
		LinkedList<Tuple> re = new LinkedList<Tuple>();
		LinkedList<Tuple> list = new LinkedList<Tuple>();
		list = queryAll(new Template(Map.AnyInteger, Map.AnyInteger, Map.AnyString));
		Iterator<Tuple> it = list.iterator(); 
		while(it.hasNext()){
			Tuple tu = it.next();
			int x = tu.getElementAt(Integer.class, 0);
			int y = tu.getElementAt(Integer.class, 1);
			if(!(y <= y0+1 && y >= y0-1 && x <= x0+1 && x >= x0-1) || (x==x0 && y==y0)){
				it.remove();
			}
		}
		for (int y = y0-1; y <= y0+1; y++) {
			for (int x = x0-1; x <= x0+1; x++) { 
				if (!(x == x0 && y == y0)) {
					Tuple tu = null;
					for(Tuple t : list){
						if(x == t.getElementAt(Integer.class, 0) && (y == t.getElementAt(Integer.class, 1))){
							tu = t;
						}
					}
					if(tu != null) {
						re.add(tu);
					}
					else{
						re.add(new Tuple(x, y, Empty.type));
					}
				}
			}
		}
		return re;
	}
	
	private LinkedList<Point> getPathableNeighbours(int x0, int y0) throws InterruptedException, IOException {
		LinkedList<Point> re= new LinkedList<Point>();
		
		LinkedList<Tuple> listres = queryAll(new Template(Map.AnyString, Map.AnyInteger, Map.AnyInteger));		
		Iterator<Tuple> itres = listres.iterator();
		while(itres.hasNext()){
			Tuple tu = itres.next();
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			if(!((x == x0+1 && y == y0) || (x == x0-1 && y == y0) || (y == y0+1 && x == x0) || (y == y0-1 && x == x0))){
				itres.remove();
			}
		}
		LinkedList<Tuple> listres2 = queryAll(new Template(Map.AnyString, Map.AnyInteger, Map.AnyInteger, Map.AnyInteger));		
		Iterator<Tuple> itres2 = listres2.iterator();
		while(itres2.hasNext()){
			Tuple tu = itres2.next();
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			if(!((x == x0+1 && y == y0) || (x == x0-1 && y == y0) || (y == y0+1 && x == x0) || (y == y0-1 && x == x0))){
				itres2.remove();
			}
		}
		LinkedList<Tuple> listdrone = queryAll(new Template(new ActualTemplateField(Hardrone.type), Map.AnyInteger, Map.AnyInteger, Map.AnyString));
		Iterator<Tuple> itdrone = listdrone.iterator();
		while(itdrone.hasNext()){
			Tuple tu = itdrone.next();
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			if(!((x == x0+1 && y == y0) || (x == x0-1 && y == y0) || (y == y0+1 && x == x0) || (y == y0-1 && x == x0))){
				itdrone.remove();
			}
		}
		LinkedList<Tuple> listexp = queryAll(new Template(Map.AnyInteger, Map.AnyInteger, Map.AnyString));
		Iterator<Tuple> itexp = listexp.iterator();
		while(itexp.hasNext()){
			Tuple tu = itexp.next();
			int x = tu.getElementAt(Integer.class, 0);
			int y = tu.getElementAt(Integer.class, 1);
			if(!((x == x0+1 && y == y0) || (x == x0-1 && y == y0) || (y == y0+1 && x == x0) || (y == y0-1 && x == x0))){
				itexp.remove();
			}
		}
		int range = query(new Template(new ActualTemplateField(MapMerger.MAP_EDGE), Map.AnyInteger),Self.SELF).getElementAt(Integer.class, 1);
		for(int i = 0; i<4; i++){
			int dx,dy;
			switch(i){
			default: dx = 1; dy = 0; break;
			case 1: dx = -1; dy = 0; break;
			case 2: dx = 0; dy = 1; break;
			case 3: dx = 0; dy = -1; break;
			}
			Tuple res = null;
			for(Tuple t : listres){
				if(x0 + dx == t.getElementAt(Integer.class, 1) && (y0 + dy == t.getElementAt(Integer.class, 2))){
					res = t;
				}
			}
			Tuple res2 = null;
			for(Tuple t : listres2){
				if(x0 + dx == t.getElementAt(Integer.class, 1) && (y0 + dy == t.getElementAt(Integer.class, 2))){
					res2 = t;
				}
			}
			Tuple dro = null;
			for(Tuple t : listdrone){
				if(x0 + dx == t.getElementAt(Integer.class, 1) && (y0 + dy == t.getElementAt(Integer.class, 2))){
					dro = t;
				}
			}
			Tuple exp = null;
			for(Tuple t : listexp){
				if(x0 + dx == t.getElementAt(Integer.class, 0) && (y0 + dy == t.getElementAt(Integer.class, 1))){
					exp = t;
				}
			}
			if(	(res == null || Resource.isPathable(res.getElementAt(String.class, 0)))		&&
				(res2 == null || Resource.isPathable(res2.getElementAt(String.class, 0)))	&&
				(dro == null || Resource.isPathable(dro.getElementAt(String.class, 0))) 	&&
				(exp != null || (Math.abs(x0 + dx) <= range && Math.abs(y0 + dy) <= range))	){
				re.add(new Point(x0+dx,y0+dy));
			}
		}
		return re;
	}
	
	private LinkedList<Tuple> getNeighbours(int x0, int y0) {
		LinkedList<Tuple> re = new LinkedList<Tuple>();
		LinkedList<Tuple> list = new LinkedList<Tuple>();
		list = queryAll(new Template(Map.AnyString, Map.AnyInteger, Map.AnyInteger));
		Iterator<Tuple> it = list.iterator(); 
		while(it.hasNext()){
			Tuple tu = it.next();
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			if(!(y <= y0+1 && y >= y0-1 && x <= x0+1 && x >= x0-1) || (x==x0 && y==y0)){
				it.remove();
			}
		}
		for (int y = y0-1; y <= y0+1; y++) {
			for (int x = x0-1; x <= x0+1; x++) { 
				if (!(x == x0 && y == y0)) {
					Tuple tu = null;
					for(Tuple t : list){
						if(x == t.getElementAt(Integer.class, 1) && (y == t.getElementAt(Integer.class, 2))){
							tu = t;
						}
					}
					if(tu != null) {
						re.add(tu);
					}
					else{
						re.add(new Tuple(Empty.type, x, y));
					}
				}
			}
		}
		return re;
	}	
}

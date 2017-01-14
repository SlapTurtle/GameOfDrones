package baseNode;

import map.Map; //TEMP

import java.awt.Point;
import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

public class BaseAgent extends Agent {
	
	LinkedList<Tuple> gold = new LinkedList<Tuple>();
	LinkedList<Tuple> tree = new LinkedList<Tuple>();
	LinkedList<Point> pg = new LinkedList<Point>();
	LinkedList<Point> pt = new LinkedList<Point>();
	Point center = new Point(0,0);
	
	public BaseAgent() {
		super("BaseAgent");
	}

	@Override
	protected void doRun() throws Exception {
		while(true){
			//Await Request from Harvester Drone
		}
	}
	
	public void getResource(){
		int tempx;
		int tempy;
		
		Template tpg = new Template(new ActualTemplateField("GOLD"), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		gold = queryAll(tpg);
			for (Tuple tu : gold) {
				tempx = tu.getElementAt(Integer.class, 1);
				tempy = tu.getElementAt(Integer.class, 2);
				pg.add(new Point(tempx, tempy));
		}
		
		Template tpt = new Template(new ActualTemplateField("GOLD"), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		tree = queryAll(tpt);
			for (Tuple tu : gold) {
				tempx = tu.getElementAt(Integer.class, 1);
				tempy = tu.getElementAt(Integer.class, 2);
				pt.add(new Point(tempx, tempy));
		}
	}
	
//	public LinkedList<Point> sendHarvester(){
//		LinkedList<Point> path;
//		if(gold.isEmpty() && tree.isEmpty()){
//			getResource();
//			sendHarvester();
//		}
//		else if (gold.isEmpty()){
//			while (!pt.isEmpty()){
//				path = getPath(center,pt.getFirst());
//				pt.removeFirst();
//				return path;
//			}
//			
//		}
//		else {
//			while(!pg.isEmpty()){
//				path = getPath(center, pg.getFirst());
//				pg.removeFirst();
//				return path;
//			}
//		}
//		return null;
//	}
//	
//	public LinkedList<Point> getPath(Point p1, Point p2){
//		//A* algorithm
//		return null;
//	}
}	


package baseAI;
import map.Map;
import resources.Gold;

import java.awt.Point;
import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.Tuple;



public class BaseAgent extends Agent {
	protected int radius = 2;
	protected int hRadius = 10;
	Map map = new Map();
	private LinkedList<Tuple> gold = new LinkedList<Tuple>();
	private LinkedList<Tuple> tree = new LinkedList<Tuple>();
	LinkedList<Point> pg = new LinkedList<Point>();
	LinkedList<Point> pt = new LinkedList<Point>();
	
	public BaseAgent() {
		super("BaseAgent");
	}

	@Override
	protected void doRun() throws Exception {
		
	}
//	public void sendExpDrone(){
//		
//	}
	
	public void getGold(){
		int tempx;
		int tempy;
		
		gold = map.RetrieveTuples("GOLD");
		for (int i = 0; i<gold.size(); i++){
			tempx = map.getTupleX(gold.get(i));
			tempy = map.getTupleY(gold.get(i));
			pg.add(new Point(tempx, tempy));
		}
		
	}
	
	public void getTree(){
		int tempx;
		int tempy;
		tree = map.RetrieveTuples("TREE");
			for (int i = 0; i<tree.size(); i++){
				tempx = map.getTupleX(tree.get(i));
				tempy = map.getTupleY(tree.get(i));
				pt.add(new Point(tempx, tempy));
			}
	}
	
//	public Point[] sendHarvester(){
//		if(gold.isEmpty() && tree.isEmpty()){
//			getGold();
//			getTree();
//			sendHarvester();
//		}
//		else if (gold.isEmpty()){
//			
//		}
//	}
	
//	public Point[] getPath(Point p1, Point p2){
//		
//	}
}	


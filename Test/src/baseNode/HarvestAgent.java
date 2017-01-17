package baseNode;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

public class HarvestAgent extends Agent {
	
	LinkedList<Point> pg = new LinkedList<Point>();
	LinkedList<Point> pt = new LinkedList<Point>();
	Point center = new Point(0,0);
	
	public HarvestAgent() {
		super("HarvestAgent");
		pg = new LinkedList<Point>();
		pt = new LinkedList<Point>();
	}

	@Override
	protected void doRun() throws Exception {
		Template in = new Template(new ActualTemplateField("order"), new FormalTemplateField(String.class));
		while(true){
			Tuple tu = get(in, Self.SELF);
			String order = tu.getElementAt(String.class, 0);
			String id = tu.getElementAt(String.class, 1);
			put(new Tuple(order, id, getResourcePoint()),Self.SELF);
		}
	}
	
	private void getResources(){
		Template tpg = new Template(new ActualTemplateField("GOLD"), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		
		LinkedList<Tuple> gold = queryAll(tpg);
			for (Tuple tu : gold) { 
				pg.add(new Point(tu.getElementAt(Integer.class, 1), tu.getElementAt(Integer.class, 2)));
		}
		pg = quickSort(pg);
		
		Template tpt = new Template(new ActualTemplateField("TREE"), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		LinkedList<Tuple> tree = queryAll(tpt);
			for (Tuple tu : tree) {
				pt.add(new Point(tu.getElementAt(Integer.class, 1), tu.getElementAt(Integer.class, 2)));
		}
		pt = quickSort(pt);
	}
	
	public Point getResourcePoint(){		
		if (!pg.isEmpty()){
			return pg.removeFirst();
		}
		else if (!pt.isEmpty()){
			return pt.removeFirst();
		}	
		getResources();
		return null;
	}
	
	
	private static LinkedList<Point> quickSort(LinkedList<Point> points){
		if (points.size() <= 1) return points;
		
		Point basePoint=new Point(0,0);
        int pivot=new Random().nextInt(points.size());
        Point pivotPoint=points.get(pivot);
        double pivotValue = pivotPoint.distance(basePoint);

        LinkedList<Point> S1 = new LinkedList<Point>();
        LinkedList<Point> S2 = new LinkedList<Point>();
        LinkedList<Point> pivotValues = new LinkedList<Point>();


        for (Point point:points) {
        	double value=point.distance(basePoint);
        	
        	if (value == pivotValue) pivotValues.add(point);
            else if (value < pivotValue) S1.add(point);
            else S2.add(point);
        }

        LinkedList<Point> lower = quickSort(S1);
        LinkedList<Point> higher = quickSort(S2);

        return joinLists(lower, pivotValues, higher);
    }
	
	private static LinkedList<Point> joinLists (LinkedList<Point> lower, LinkedList<Point> mid, LinkedList<Point> higher) {
		for (Point p:mid) {
			lower.addLast(p);
		}
		for (Point p:higher) {
			lower.addLast(p);
		}
		return lower;
	}
}	


package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import util.AStarPoint;

import resources.*;

//evade move

//multiple harvester drones
//exception in get new moves

public class HarDrone extends AbstractDrone {
	public static final String type = "HARDRONE";
	public static int droneCounter = 0;
	
	LinkedList<Point> path;
	Point resourcePoint;
	boolean deliverResource;
	
	public HarDrone(Point position) {
		super(position, type, type + droneCounter++);
		path = new LinkedList<Point>();
		deliverResource=false;
	}
	
	@Override
	protected Point moveDrone() throws InterruptedException, IOException{
			//drone is at base and it needs new moves
			if (!path.isEmpty()) 
				return regularMove();
			getNewMoves();
			return null;
			
	}
	
	@Override
	protected void droneAction() {
		harvest();
	}
	protected void harvest() {
		Template t = new Template(
				new FormalTemplateField(String.class),
				new ActualTemplateField(resourcePoint.x),
				new ActualTemplateField(resourcePoint.y)
		);
		if (super.position.equals(resourcePoint)) { //if drone is at same position as resourcer
			try {
				Tuple tup=get(t,Drone.self2base);
				put(tup,Self.SELF);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(deliverResource) { //if drone is beside base and it just came home from collecting resource
			try {
				Tuple tup = get(t,Self.SELF);
				String resource=(String) tup.getElementAt(0);
				switch (resource) {
				case Gold.type: incrementGold(); break;
				case Tree.type: incrementTree(); break;
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			deliverResource=false;
		}	
	}
	
	private Point regularMove() {
		Point p= path.removeFirst();
		if (path.isEmpty()) deliverResource=true;
		return p;
	}
	
	//TODO handling exception: what to do?
	private void getNewMoves(){
			Point target=null;
			ArrayList<Point> moves=null;
			while (moves==null) {
				try {
					target = getNewTarget();
					if (target==null) return; //no ressources available
					moves = aStar(position, target);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
			
			//way out
			path.addAll(moves);
			//omit start position (already here)
			path.remove();
			
			//mark position of resource
			resourcePoint=path.getLast();
			
			//turn moves list around for route back to base
			Collections.reverse(moves);
			
			//omit start position of way back route (resource point)
			moves.remove(0);
			
			//way home
			path.addAll(moves);
	}

	private void evade (ArrayList<Object> list) {
		//if hardrone at postion targets current as next
			//while (not move to side)
				//move back
			//move to side
		//else 
			//stand still
	}
	
	private void increment (String material) {
		Template t=new Template(
				new ActualTemplateField(material),
				new FormalTemplateField(Integer.class)
		);
		try {
			Tuple tup=get(t,Drone.self2base);
			int resourceCounter=(Integer) tup.getElementAt(1);
			System.out.println("Updated base resources. Before " + material + ": " + resourceCounter + " now " + material + ": " + (++resourceCounter));
			put(new Tuple(tup.getElementAt(0), resourceCounter ),Drone.self2base);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void incrementGold() {
		increment("GoldCounter");
	}

	private void incrementTree() {
		increment("TreeCounter");
	}
	//help functions
	
	private Point getNewTarget() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("order"), new ActualTemplateField(id), new FormalTemplateField(Point.class));
		put(new Tuple("order",id), Drone.self2base);
		Tuple tu = get(tp, Drone.self2base);
		return tu.getElementAt(Point.class, 2);
	}
	
	//TODO what does this do?
	//TODO Drone.self2base?: general point 2 point
	private LinkedList<Point> getPathablePoints(Point p) throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("neighbours_pathable"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		put(new Tuple("neighbours_pathable",id, p.x, p.y), Drone.self2base);
		Tuple tu = get(tp, Drone.self2base);
		return tu.getElementAt(LinkedList.class, 2);
	}
	
	private ArrayList<Point> aStar(Point pointStart, Point pointEnd) throws InterruptedException, IOException{
		//Declaration of lists
		LinkedList<AStarPoint> closedSet = new LinkedList<AStarPoint>();
		LinkedList<AStarPoint> openSet = new LinkedList<AStarPoint>();
		
		//conversion of Points to AstarPoints
		AStarPoint start = new AStarPoint(pointStart.x,pointStart.y);
		AStarPoint end = new AStarPoint(pointEnd.x,pointEnd.y);
		
		//init of start node
		start.gscore=0;
		start.fscore=distance(pointStart, pointEnd);
		//Init of openSet
		openSet.add(start);
		
		
		while (!openSet.isEmpty()){
			AStarPoint current;
			
			//Getting node with lowest fScore
			Iterator<AStarPoint> it = openSet.iterator();
			current=it.next();
			while(it.hasNext()) {
				AStarPoint temp = it.next();
				if(temp.fscore<current.fscore) current=temp;
			}
			
			if ((current.fscore-current.gscore)==1) {
				AStarPoint endPoint=new AStarPoint(pointEnd.x,pointEnd.y);
				endPoint.cameFrom=current;
				return reconstructPath(endPoint);
			}	
			
			//moving current to closedSet
			openSet.remove(current);
			closedSet.add(current);
			
			//hacks to get AStarPoint neigbors list
			LinkedList<AStarPoint> neighbors = AStarPoint.convertPointList(getPathablePoints(current)); //retriever
			
			for(AStarPoint neighbor : neighbors){
				//ignores already visited points.
				if(closedSet.contains(neighbor)) continue;
				
				int tentativeGscore = current.gscore + 1;
				
				//if neighbor is not registered before
				if(!openSet.contains(neighbor)) openSet.add(neighbor);
				
				//if neighbor is registered before
				//investigate if this path is better
				else if(tentativeGscore>=neighbor.gscore) continue;
				
				//if neighbor is not registered before or this is a better path than the excisting
				//calculate following:
				
				neighbor.cameFrom=current;
				neighbor.gscore=tentativeGscore;
				
				neighbor.fscore=neighbor.gscore+distance(neighbor, pointEnd);
				
			}
			
		}
		
		return null;
	}
	
	private boolean droneAtPosition (Point p) {
		Template tp = new Template(new ActualTemplateField("drone_at_position"), new ActualTemplateField(id), new FormalTemplateField(boolean.class));
		try {
			put(new Tuple("drone_at_position",id, p.x, p.y), Drone.self2base);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tuple tu=null;
		try {
			tu = get(tp, Drone.self2base);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tu.getElementAt(boolean.class, 2);
	}
	
	private int distance(Point start, Point end){
		int dx=Math.abs(end.x-start.x);
		int dy=Math.abs(end.y-start.y);
		return dx+dy;
	}

	private static ArrayList<Point> reconstructPath(AStarPoint end) {
		ArrayList<AStarPoint> path = new ArrayList<AStarPoint>();
		AStarPoint current=end;
		path.add(current);
		while(current.cameFrom!=null){
			current=current.cameFrom;
			path.add(0,current);
		}
		return AStarPoint.convertToPointList(path);
	}
}

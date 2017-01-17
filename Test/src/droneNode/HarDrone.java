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

import util.AStarPoint;

//test regular move
//resource part: general discussion which ressources: represented many places?
//evade move


//DONE:
//if astar can't find path keep trying with ned point. Each point temporarily deleted. Added again when ressources from larger area is used
//General a-star (distance calculation) : implemented with get pathable points
//reconstruction of a star path

//exception in get new moves

public class HarDrone extends AbstractDrone {
	public static final String type = "HARDRONE";
	public static int droneCounter = 0;
	
	LinkedList<Point> path;
	Point resourcePoint;
	
	public HarDrone(Point position) {
		super(position, type, type + droneCounter++);
		path = new LinkedList<Point>();
	}
	
	@Override
	protected Point moveDrone() throws InterruptedException, IOException{
			//drone is at base and it needs new moves
			if (path.isEmpty()) {
				unloadResource();
				getNewMoves();
			}
			Point regularPoint=path.getFirst();
			//if (p.isAvailable) {
				return path.removeFirst();
			//}
			//else {
				//return evadeMove(regularPoint);
			//}	
	}
	
	@Override 
	protected void harvest() {
		if (super.position.equals(resourcePoint)) {
			//TODO
			//get resource tuple from base tuple space
			//put resource tuple in own tuple space or represent it in another way for later storing in base
		}	
	}
	
	//TODO handling exception: what to do?
	private void getNewMoves(){
			Point target=null;
			ArrayList<Point> moves=null;
			while (moves==null) {
				try {
					target = getNewTarget();
					moves = aStar(position, target);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
			path.addAll(moves);
			Collections.reverse(moves);
			path.addAll(moves);
			this.resourcePoint=moves.get(0);
	}
	
	private Point regularMove() {
		return null; 
	}
	
	private Point evadeMove (Point regularPoint) {
		//TODO
		//if other drones next point is point where i am standing
			//if possible move to side: not player or eg. lake
			//else move backwards
		//else stand still this round
		return null;
	}
	
	private void unloadResource() {
		//TODO
		//delete resource storing
		//increment base resourcer counter
	}
	
	
	
	
	
	/* Help methods */
	
	//TODO what does this do?
	//TODO self2base?: general point 2 point
	private LinkedList<Point> getPathablePoints() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("neighbours_pathable"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		put(new Tuple("neighbours_pathable",id, position.x, position.y), self2base);
		Tuple tu = get(tp,AbstractDrone.self2base);
		return tu.getElementAt(LinkedList.class, 2);
	}
	
	private Point getNewTarget() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("order"), new ActualTemplateField(id), new FormalTemplateField(Point.class));
		put(new Tuple("order",id), self2base);
		Tuple tu = get(tp,AbstractDrone.self2base);
		return tu.getElementAt(Point.class, 2);
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
			
			if(current.equals(end)) return reconstructPath(current);
			
			//moving current to closedSet
			openSet.remove(current);
			closedSet.add(current);
			
			//hacks to get AStarPoint neigbors list
			LinkedList<AStarPoint> neighbors = AStarPoint.convertPointList(getPathablePoints()); //retriever
			
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

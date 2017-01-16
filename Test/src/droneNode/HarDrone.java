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

import map.Map; //TEMP
import util.AStarPoint;

//test regular move
//do resource part: //TODO get resource tuple from base ts and put in own ts: 	//if (.isEmpty()) hasHarvested=true;
	//in get new moves: //TODO delete (get) resource from own ts //TODO increment (get->put) specific resource counter for base
//evade move

//if astar can't find path

//exception in get new moves

public class HarDrone extends AbstractDrone {
	public static final String type = "HARDRONE";
	public static int droneCounter = 0;
	
	boolean hasHarvested;
	LinkedList<Point> path;
	
	public HarDrone(Point position) {
		super(position, type, type + droneCounter++);
		hasHarvested = false;
		path = new LinkedList<Point>();
	}
	
	@Override
	protected Point moveDrone() throws InterruptedException, IOException{
			//drone is at base and it needs new moves
			if (path.isEmpty()) 
				getNewMoves();
			
			return regularMove();
	}
	
	private Point regularMove() {
		return path.removeFirst();
	}
	
	private Point getNewTarget() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("order"), new ActualTemplateField(id), new FormalTemplateField(Point.class));
		put(new Tuple("order",id), self2base);
		Tuple tu = get(tp,AbstractDrone.self2base);
		return tu.getElementAt(Point.class, 2);
	}
	
	//TODO handling exception: what to do?
	private void getNewMoves(){
			Point target=null;
			ArrayList moves=null;
			try {
				target = getNewTarget();
				moves = aStar(position, target);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
			path.addAll(moves);
			
			Collections.reverse(moves);
			path.addAll(moves);
	}
	
	//TODO what does this do?
	//TODO self2base?: general point 2 point
	private LinkedList<Point> getPathablePoints() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("neighbours_pathable"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		put(new Tuple("neighbours_pathable",id, position.x, position.y), self2base);
		Tuple tu = get(tp,AbstractDrone.self2base);
		return tu.getElementAt(LinkedList.class, 2);
	}
	
	private void evade (ArrayList<Object> list) {
		//list is used so if list when drode returning it uses that
		//and if drone is gathering it uses that list
		
		//decide if drone is winner
		//if loser step aside 
		
		//if winner continue
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

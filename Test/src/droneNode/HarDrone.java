package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import map.Map; //TEMP
import util.AStarPoint;
import util.Position;

public class HarDrone extends Drone {
	public static final String type = "HARDRONE";
	public static int droneCounter = 0;
	
	boolean hasHarvested;
	LinkedList<Point> pathOut;
	LinkedList<Point> pathHome;
	
	public HarDrone(Point position) {
		super(position, type, type + droneCounter++);
		hasHarvested = false;
		pathOut = new LinkedList<Point>();
		pathHome = new LinkedList<Point>();
	}
	
	@Override
	protected Point moveDrone() throws InterruptedException, IOException{
			Point target;	
			
			if (pathOut.isEmpty() && pathHome.isEmpty()) { //drone is at base, need new target
				while(pathOut.isEmpty()){
					target = getNewTarget();
					pathOut.addAll(aStar(position, target));
				}
				//TODO delete resource from own ts
				//TODO increment specific resource counter for base
				return null;
			}
			
			else if (!pathOut.isEmpty()) { //on way out
				//TODO evade
				target = pathOut.removeFirst();
				/*if(map.isDroneAt(target)) {
					
					
					//pathOut.add(0, target);
					//pathOut.add(0, position);
				}	
				else {
					if (!pathHome.contains(target)) //if needed for evade scenario*/
						pathHome.addFirst(target);
					
					//TODO cut resource tuple from base ts and place in drone ts
					
					if (pathOut.isEmpty()) hasHarvested=true;
				//}
			}
			
			else { //on way home
				target = pathHome.removeFirst();
				
				/*if(map.isDroneAt(target)) {
					//TODO evade
					
					pathHome.add(0, target);
					pathHome.add(0, position);
				}
				else {
				
				}*/
			}

			return target;
	}
	
	private Point getNewTarget() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("order"), new ActualTemplateField(id), new FormalTemplateField(Point.class));
		put(new Tuple("order",id), self2base);
		Tuple tu = get(tp,Drone.self2base);
		return tu.getElementAt(Point.class, 2);
	}
	private LinkedList<Point> getPathablePoints(){
		return null;
	}
	
	private void evade (ArrayList<Object> list) {
		//list is used so if list when drode returning it uses that
		//and if drone is gathering it uses that list
		
		//decide if drone is winner
		//if loser step aside 
		
		//if winner continue
	}
	
	private static ArrayList<Point> aStar(Point pointStart, Point pointEnd){
		//Declaration of lists
		LinkedList<AStarPoint> closedSet = new LinkedList<AStarPoint>();
		LinkedList<AStarPoint> openSet = new LinkedList<AStarPoint>();
		
		//conversion of Points to AstarPoints
		AStarPoint start = new AStarPoint(pointStart.x,pointStart.y);
		AStarPoint end = new AStarPoint(pointEnd.x,pointEnd.y);
		
		//init of start node
		start.gscore=0;
		start.fscore=start.distance(end);
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
			LinkedList<AStarPoint> neighbors = AStarPoint.convertPointList(map.RetrievePathableNeighbors(current)); //retriever
			
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
				neighbor.fscore=neighbor.gscore+neighbor.distance(end);
				
			}
			
		}
		
		return null;
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

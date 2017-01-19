package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;

import util.AStarPoint;

import resources.*;

//michael deadlock: slave/stall
//kan vi sikre at en drones run bliver kørt før en anden?

//teste med multiple harvester drones
//exception in get new moves

public class HarDrone extends AbstractDrone {
	public static final String type = "HARDRONE";
	public static int droneCounter = 0;
	
	LinkedList<Point> path;
	Point resourcePoint;
	boolean deliverResource;
	boolean slave;
	boolean stall;
	
	public HarDrone(Point position) {
		super(position, type, type + droneCounter++);
		path = new LinkedList<Point>();
		deliverResource=false;
		slave=false;
		stall=false;
	}
	
	@Override
	protected Point moveDrone() throws InterruptedException, IOException{
			
		
		/*if (stall) return null;
		if (slave)
			evade();
			*/
		
		if (!path.isEmpty()) {
			if (!isPositionOccupied())
				return regularMove();
			return evade();
		}
		getNewMoves();//drone is at base and it needs new moves
		return null;	
	}
	
	@Override
	protected void droneAction() throws InterruptedException, IOException {
		harvest();
		//update next position in own tuple space
		Tuple tup = get(new Template(new ActualTemplateField ("next_move"), new FormalTemplateField(Point.class)),Self.SELF);
		putNextMoveInTupleSpace();
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

	private boolean isPositionOccupied() throws InterruptedException, IOException {
		put(new Tuple(path.get(0),super.id),Drone.self2base);
		
		Template t= new Template(
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(String.class)
		);
		
		Tuple tup=get(t,Drone.self2base);
		int answer=(Integer) tup.getElementAt(0);
		if (answer==1) return true;
		return false;
	}
	
	private Point evade () throws InterruptedException, IOException {
		Point returnPoint=null;
		Point opponentMovePosition=getOpponentMovePosition(); 
		if (opponentMovePosition==null) return null; //if oponent doesn't have any planned moves stand still
		if (opponentMovePosition.equals(super.position)) { //if opponents next move targets this drones current position
			returnPoint=moveToSide(opponentMovePosition); //try move to side
			if (returnPoint==null) 
				returnPoint=moveBackwards(opponentMovePosition); //if not move to side try move backwards
		}
		if (returnPoint!=null) { //if we do any temporary move
			this.path.addFirst(super.position); //add current position to path so that it can move bak to original path later
		}
		//if opponent drone does not targets this drones position
		//or this drone can't move either back or to one of the sides: stand still: returnPoint=null
		return returnPoint;  
	}
	
	private Point getOpponentMovePosition() throws InterruptedException, IOException {
		Point opponentPosition;
		Point opponentNextMoveposition;
		
		for (PointToPoint ptp: Drone.self2drone) {
			opponentPosition=getOpponentPosition(ptp);
			if (opponentPosition==null) continue; //should not happen
			if (opponentPosition.equals(path.getFirst())) {//this is drone standing in position we want to move to
				return getOpponentNextMove(ptp);
			}	
			
		}
		return null; //should not happen
	}
	
	private Point getOpponentNextMove(PointToPoint ptp) throws InterruptedException, IOException {
		Template t= new Template(
				new ActualTemplateField ("next_move"),
				new FormalTemplateField(Point.class)
		);
		Tuple tup=query(t,ptp);
		return (Point) tup.getElementAt(1);
	}
	
	private Point getOpponentPosition(PointToPoint ptp) throws InterruptedException, IOException {
		Template t= new Template(
				new FormalTemplateField(Point.class)
		);
		
		Tuple tup=query(t,ptp);
		return (Point) tup.getElementAt(0);
	}
	
	private Point moveBackwards(Point opponentPos) throws InterruptedException, IOException {
		int dx=opponentPos.x-super.position.x;
		
		Point backwardsPosition;
		if (dx==1) { //opponent drone is to the right of this drone
			backwardsPosition=new Point(super.position.x-1,super.position.y); //try move to the left
		}
		else if (dx==-1){ //opponent drone is to the left of this drone
			backwardsPosition=new Point(super.position.x+1,super.position.y); //try move to the right
		}
		else { //opponent drone is either above or under this drone
			int dy=opponentPos.y-super.position.y;
			if (dy==1){ //opponent drone is under this drone
				backwardsPosition=new Point(super.position.x,super.position.y-1); //try move up
			}
			else {//opponent drone is above this drone
				backwardsPosition=new Point(super.position.x,super.position.y+1); //try move down
			}
		}
		if (checkPosition(backwardsPosition.x,backwardsPosition.y)) return backwardsPosition;
		return null;
	}
	
	private Point moveToSide(Point opponentPos) throws InterruptedException, IOException {
		int dx=Math.abs(opponentPos.x-super.position.x);
		
		//drodes are side by side
		if (dx==1) {
			//try move up
			if (checkPosition(super.position.x,super.position.y-1)) {
				return new Point(super.position.x,super.position.y-1);
			}
			//try move down
			if (checkPosition(super.position.x,super.position.y+1)) {
				return new Point(super.position.x,super.position.y+1);
			}
		}
		else { //drones are above/beneath each other
			//try move right
			if (checkPosition(super.position.x+1,super.position.y)) {
				return new Point(super.position.x+1,super.position.y);
			}
			//try move left
			if (checkPosition(super.position.x-1,super.position.y)) {
				return new Point(super.position.x-1,super.position.y);
			}
		}
		return null;
	}
	
	//returns true if position is pathable
	private boolean checkPosition (int x, int y) throws InterruptedException, IOException {
		String order="single_pathable";
		put (new Tuple(order,super.id,x,y),Drone.self2base);
		Template t=new Template(
				new ActualTemplateField(order),
				new ActualTemplateField(super.id),
				new FormalTemplateField(Object.class)
		);
		Tuple tup=get(t,Drone.self2base);
		int answer=(int) tup.getElementAt(2);
		if (answer==1) return true; 
		return false;
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
	
	protected void putNextMoveInTupleSpace() throws InterruptedException, IOException {
		try {
			Point p=path.getFirst();
			put(new Tuple("next_move",p),Self.SELF);
		} catch (NoSuchElementException e) {
		}
		
	}
	//help functions
	
	private Point getNewTarget() throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("order"), new ActualTemplateField(id), new FormalTemplateField(Point.class));
		put(new Tuple("order",id), Drone.self2base);
		Tuple tu = get(tp, Drone.self2base);
		return tu.getElementAt(Point.class, 2);
	}
	
	private boolean getSinglePathable(Point p) throws InterruptedException, IOException{
		Template tp = new Template(new ActualTemplateField("single_pathable"), new ActualTemplateField(id), new FormalTemplateField(Integer.class));
		put(new Tuple("single_pathable",id, p.x, p.y), Drone.self2map);
		Tuple tu = get(tp, Drone.self2base);
		return (tu.getElementAt(Integer.class, 2) == 1) ;
	}
	
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

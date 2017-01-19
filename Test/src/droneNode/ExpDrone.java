package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.PointToPoint;

import baseNode.MapMerger;
import resources.Empty; 

public class ExpDrone extends AbstractDrone {
	public static final String type = "EXPDRONE";
	public static int DroneCounter = 0;
	
	protected Point radiusPoint;
	protected boolean returnToBase;
	private boolean beenHereBefore;
	private boolean returnToCirculation;
	int radius;
	
	public ExpDrone(Point position) {
		super(position, type, type + DroneCounter++);
		radius = 0;
		radiusPoint = new Point(radius, 0);
		returnToBase = false;
		beenHereBefore = false;
		returnToCirculation = true;
	}
	
	@Override
	protected void droneAction() {
		try {
			explore();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * main function to move exp drone
	 * @param d
	 * @param dir
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Override
	protected Point moveDrone() throws Exception {
		
		Point nP = new Point(position.x, position.y);
		
		if(returnToBase) return returnToBase(nP);
		if(returnToCirculation) return returnToCirculation(nP);
		if(nP.equals(radiusPoint) && beenHereBefore) returnToBase=true;		
		if(nP.equals(radiusPoint) && !beenHereBefore) beenHereBefore=true;
		
		int q = getQuadrant(nP);
		int dir=0;
		Point[] posArr = getFieldsToCheck(nP);
		dir = getDirFromRadius(posArr[1],posArr[0], radius);
		//new place for drone to be is called NP
		switch(q){
			case 1 : if(dir<0) nP.move(nP.x-1,nP.y);
					 else      nP.move(nP.x, nP.y+1);
					 break;
			
			case 2 : if(dir<0) nP.move(nP.x, nP.y-1); 
					 else      nP.move(nP.x-1, nP.y);
					 break;
			case 3 : if(dir<0) nP.move(nP.x+1, nP.y); 
					 else      nP.move(nP.x, nP.y-1); 
					 break;
			case 4 : if(dir<0) nP.move(nP.x, nP.y+1);
					 else      nP.move(nP.x+1, nP.y); 
			         break;
			default: nP.move(nP.x, nP.y);
					 break;
		}
		return nP; 
	}

	/**
	 * Moves drone to circulation, then sets beenHereBefore boolean true, to ensure that
	 * the drone knows when to return to base
	 * @param nextPoint
	 * @return moved nextPoint
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private Point returnToCirculation(Point nP) throws InterruptedException, IOException { 
		if(nP.x==this.radius){
			returnToCirculation=false;
			beenHereBefore=true;
		}
		else{
			nP.move(nP.x+1,nP.y);
		}
		return nP;
	}

	/**
	 * Moves the drone towards the base, when there sets booleans and increases map.radius as well as moves raduisPoint. 
	 * @param nextPoint
	 * @return Moved nextPoint
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private Point returnToBase(Point nP) throws InterruptedException, IOException {
		if(nP.x>=1){
			nP.move(nP.x-1, nP.y);
		}
		else{ 
			returnToBase=false; 
			beenHereBefore=false;
			returnToCirculation=true;
			radius = getNewRadius();
		}
		return nP;
	}

	/**
	 * Gets the direction using {@link #pythagoras(Point)}method
	 * @param p1
	 * @param p2
	 * @param radius
	 * @return int of what direction to move. 1 for up, -1 for left
	 */
	private int getDirFromRadius(Point p1, Point p2, int radius){
		int dir=0;
		double c1 = 0;
		double c2 = 0;
		c1 = pythagoras(p1);
		c2 = pythagoras(p2);
		
		if(Math.abs(radius-c1)>Math.abs(radius-c2)){
			dir=-1;
		}
		else{
			dir=1;
		}
		return dir;
	}
	
	
	/**
	 * Takes p.x and p.y and returns radius using pythagoras
	 * @param p
	 * @return squared c in pythagoras
	 */
	private double pythagoras(Point p){
		int a=p.x;
		int b=p.y;
		
		return Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
	}
	
	
	
	/**
	 * 
	 * @param dir
	 * @param p
	 * @return
	 */
	private Point[] getFieldsToCheck(Point p){
		Point[] arr = new Point[2];
		int q = getQuadrant(p);
		switch(q){
			case 1 : arr[0]= new Point(p.x-1, p.y);
					 arr[1]= new Point(p.x, p.y+1);
					 break;
			
			case 2 : arr[0]= new Point(p.x, p.y-1);
			 		 arr[1]= new Point(p.x-1, p.y);
					 break;
			case 3 : arr[0]= new Point(p.x+1, p.y);
			 		 arr[1]= new Point(p.x, p.y-1);
					 break;
			
			case 4 : arr[0]= new Point(p.x, p.y+1);
			 		 arr[1]= new Point(p.x+1, p.y);
					 break;
	
			default :
					break;
		}
		
		return arr;
	}

	/**
	 * gets what quadrant the position is in, too use for switches
	 * @param p
	 * @return
	 */
	private int getQuadrant(Point p) {
		int q=0;
		
		if(p.x>=0 && p.y>=0) q=1;
		else if(p.x<=0 && p.y>=0) q=2;
		else if(p.x<=0 && p.y<=0) q=3;
		else if(p.x>=0 && p.y<=0) q=4;
		
		return q;
	}
	
	private int getNewRadius() throws InterruptedException, IOException {
		Template tp = new Template(new ActualTemplateField("Radius"), new FormalTemplateField(Integer.class));
		Tuple tu = get(tp, Drone.self2base);
		int radius = tu.getElementAt(Integer.class, 1) + 2;
		put(new Tuple("Radius", radius),Drone.self2base);
		radiusPoint.move(radius, radiusPoint.y);
		return radius;
	}
	
	private void explore() throws Exception {
		//add explored location
		//gets explorer lock
		Tuple lock = get(new Template(new ActualTemplateField("ExpLock")),Drone.self2base);
		LinkedList<Tuple> baseExplore = getNeighboursExplore(position);
		int range = getMapEdgeRadius(); 
		for(Tuple tu : baseExplore){
			if(tu.getElementAt(String.class, 2).equals(Empty.type)){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				if(Math.abs(x) > range || Math.abs(y) > range){					
					put(new Tuple(x,y,MapMerger.ACTION_NEW), Drone.self2base);
				}
			}
		}
		//gives back explorer lock
		put(lock, Drone.self2base);
		//add resources
		//gets resources lock
		lock = get(new Template(new ActualTemplateField("ResLock")),Drone.self2base);
		LinkedList<Tuple> map = getNeighbours(position, true);
		LinkedList<Tuple> base = getNeighbours(position, false);
		Iterator<Tuple> iMap = map.iterator();
		Iterator<Tuple> iBase = base.iterator();
		while(iBase.hasNext()){
			Tuple tb = iBase.next();
			Tuple tm = iMap.next();
			String strb = tb.getElementAt(String.class, 0);
			String strm = tm.getElementAt(String.class, 0);
			int x = tm.getElementAt(Integer.class, 1); //same x and y because of the nature of the retriever agent.
			int y = tm.getElementAt(Integer.class, 2); //hence no need to check for it.
			if(strb.equals(Empty.type) && !strm.equals(Empty.type)) {
				Template tp = new Template(new ActualTemplateField(strm), new ActualTemplateField(x),new ActualTemplateField(y));
				put(get(tp, Drone.self2map), Drone.self2base);
			}
		}
		//gives back resources lock
		put(lock, Drone.self2base);
	}
	
	private int getMapEdgeRadius() throws InterruptedException, IOException{
		return query(new Template(new ActualTemplateField(MapMerger.MAP_EDGE), new FormalTemplateField(Integer.class)),Drone.self2base).getElementAt(Integer.class, 1);
	}
	
	private LinkedList<Tuple> getNeighboursExplore(Point position) throws InterruptedException, IOException{
		put(new Tuple("neighbours_explore", id, position.x, position.y), Drone.self2base);
		Template tp = new Template(new ActualTemplateField("neighbours_explore"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		LinkedList<Tuple> list = get(tp, Drone.self2base).getElementAt(LinkedList.class, 2);
		return list;
	}
	
	private LinkedList<Tuple> getNeighbours(Point position, boolean bool) throws InterruptedException, IOException{
		PointToPoint p2p = (bool) ? Drone.self2map : Drone.self2base;
		put(new Tuple("neighbours_all", id, position.x, position.y), p2p);
		Template tp = new Template(new ActualTemplateField("neighbours_all"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		LinkedList<Tuple> list = get(tp, p2p).getElementAt(LinkedList.class, 2);
		return list;
	}
}
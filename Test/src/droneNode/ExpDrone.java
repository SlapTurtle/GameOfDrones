package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import baseNode.MapMerger;

public class ExpDrone extends AbstractDrone {
	public static final String type = "EXPDRONE";
	public static int DroneCounter = 0;
	
	protected Point radiusPoint;
	protected boolean returnToBase;
	private boolean beenHereBefore;
	private boolean returnToCirculation;
	
	private int radius = 0;
		
	public ExpDrone(Point position) {
		super(position, type, type + DroneCounter++);
		radiusPoint = new Point(radius, 0);
		returnToBase = false;
		beenHereBefore = false;
		returnToCirculation = false;
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
		
		Point d = new Point(position.x, position.y);
		Point nP = new Point(d.x, d.x);
		
		if(returnToBase) return returnToBase(nP);
		
		if(returnToCirculation) return returnToCirculation(nP);
		//if drone is on (radius,0) and been here before.
		if(d.equals(radiusPoint) && beenHereBefore) returnToBase=true;
		
		if(d.equals(radiusPoint) && !beenHereBefore) beenHereBefore=true;
		
		int q = getQuadrant(d);
		int dir=0;
		Point[] posArr = getFieldsToCheck(d);
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
		explore(nP);
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
	
	//updates base's radius and applies it to next exploration route
	private int getNewRadius() throws InterruptedException, IOException {
		Template tp = new Template(new ActualTemplateField("Radius"), new FormalTemplateField(Integer.class));
		Tuple tu = get(tp,AbstractDrone.self2base);
		int radius = tu.getElementAt(Integer.class, 1) + 2;
		put(new Tuple("Radius", radius),self2base);
		radiusPoint.move(radius, radiusPoint.y);
		return radius;
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
	
	private void explore(Point position) throws Exception {
		for (Point p : getNeighbors(position)) {
			Template t0 = new Template(new FormalTemplateField(String.class), new ActualTemplateField(p.x), new ActualTemplateField(p.y));
			Template t1 = new Template(new ActualTemplateField(MapMerger.MAP_EDGE), new FormalTemplateField(Integer.class));
			int radius = query(t1, self2base).getElementAt(Integer.class, 1);
			if(p.distance(new Point(0,0)) > radius && queryp(t0) == null)
				put(new Tuple(MapMerger.ACTION_NEW, p.x, p.y), self2map);
		}
	}

	@Override
	protected void harvest() {
		//do nothing
		//this is not a harvester
	}
}
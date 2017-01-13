package droneNode;

import java.awt.Point;

import map.Map;
import resources.*;
import util.Position;

public class ExpDrone extends DroneAI {
	public static int DroneCounter = 0;
	
	protected Position radiusPoint;
	protected boolean returnToBase = false;
	private boolean beenHereBefore = false;
	private boolean returnToCirculation = false;
	
	private int radius;
		
	public ExpDrone(Map map, Point position) {
		super(map, position, "EXPDRONE" + DroneCounter++);
		this.radiusPoint = new Position(map.radius, 0);
		this.radius=map.radius;
	}

	@Override
	protected void doRun() {
		while(true) {
			synchronized (map.render) {
				try {
					map.render.wait();
				} catch (InterruptedException e) {
					
				}
			}
			
			
			
			move(moveDrone(new Position(position.x,position.y),this.radius));
			//move(0);
		}
	}

	/**
	 * Help function to move drone
	 * @param d
	 * @param dir
	 * @return
	 */
	private Point moveDrone(Position d, int radius){
		Point nP = new Point(d.getX(), d.getY());
		
		if(returnToBase) return returnToBase(nP);
		
		if(returnToCirculation) return returnToCirculation(nP);
		//if drone is on (radius,0) and been here before.
		if(d.equals(radiusPoint) && beenHereBefore) returnToBase=true;
		
		if(d.equals(radiusPoint) && !beenHereBefore) beenHereBefore=true;
		
		int q = getQuadrant(d);
		int dir=0;
		Position[] posArr = getFieldsToCheck(d);
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
	 */
	private Point returnToCirculation(Point nP) {
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
	 */
	private Point returnToBase(Point nP) {
		if(nP.x>=1){
			nP.move(nP.x-1, nP.y);
		}
		else{ 
			returnToBase=false; 
			beenHereBefore=false;
			returnToCirculation=true;
			map.radius+=2;
			this.radius=map.radius;
			radiusPoint.move(map.radius, radiusPoint.getY());
		}
		return nP;
	}

	/**
	 * Gets the direction using {@link #pythagoras(Position)}method
	 * @param p1
	 * @param p2
	 * @param radius
	 * @return int of what direction to move. 1 for up, -1 for left
	 */
	private int getDirFromRadius(Position p1, Position p2, int radius){
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
	private double pythagoras(Position p){
		int a=p.getX();
		int b=p.getY();
		
		return Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
	}
	
	
	
	/**
	 * 
	 * @param dir
	 * @param p
	 * @return
	 */
	private Position[] getFieldsToCheck(Position p){
		Position[] arr = new Position[2];
		int q; 
		q=getQuadrant(p);
		switch(q){
			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
					 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 2 : arr[0]= new Position(p.getX(), p.getY()-1);
			 		 arr[1]= new Position(p.getX()-1, p.getY());
					 break;
			case 3 : arr[0]= new Position(p.getX()+1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()-1);
					 break;
			
			case 4 : arr[0]= new Position(p.getX(), p.getY()+1);
			 		 arr[1]= new Position(p.getX()+1, p.getY());
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
	private int getQuadrant(Position p) {
		int q=0;
		
		if(p.getX()>0 && p.getY()>=0) q=1;
		else if(p.getX()<=0 && p.getY()>0) q=2;
		else if(p.getX()<0 && p.getY()<=0) q=3;
		else if(p.getX()>=0 && p.getY()<0) q=4;
		
		return q;
	}
	
}
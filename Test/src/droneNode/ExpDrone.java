package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;

import baseNode.MapMerger;
import resources.Empty;
import util.Position;

public class ExpDrone extends AbstractDrone {
	public static final String type = "EXPDRONE";
	public static int DroneCounter = 0;
	
	protected Position radiusPoint;
	protected boolean returnToBase;
	private boolean beenHereBefore;
	private boolean returnToCirculation;
	
	private int radius = 0;
		
	public ExpDrone(Point position) {
		super(position, type, type + DroneCounter++);
		radiusPoint = new Position(radius, 0);
		returnToBase = false;
		beenHereBefore = false;
		returnToCirculation = false;
	}

	/**
	 * Help function to move drone
	 * @param d
	 * @param dir
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Override
	protected Point moveDrone() throws Exception {
		
		Position d = new Position(position.x, position.y);
		Point nP = new Point(d.getX(), d.getY());
		
		if(returnToBase) return returnToBase(nP);		
		if(returnToCirculation) return returnToCirculation(nP);
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
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private Point returnToCirculation(Point nP) throws InterruptedException, IOException {
		radius = getNewRadius(); 
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
			radiusPoint.set(radius, radiusPoint.y);
		}
		return nP;
	}
	
	//updates base's radius and applies it to next exploration route
	private int getNewRadius() throws InterruptedException, IOException {
		Template tp = new Template(new ActualTemplateField("Radius"), new FormalTemplateField(Integer.class));
		Tuple tu = get(tp,AbstractDrone.self2base);
		int radius = tu.getElementAt(Integer.class, 1) + 2;
		put(new Tuple("Radius", radius),self2base);
		return radius;
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
		int q = getQuadrant(p);
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
		
		if(p.getX()>=0 && p.getY()>=0) q=1;
		else if(p.getX()<=0 && p.getY()>=0) q=2;
		else if(p.getX()<=0 && p.getY()<=0) q=3;
		else if(p.getX()>=0 && p.getY()<=0) q=4;
		
		return q;
	}
	
	@Override
	public void move(Point p) {
		try{
			super.move(p);
			explore(p);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void explore(Point position) throws Exception {
		//add explored location
		LinkedList<Tuple> baseExplore = getNeighboursExplore(position);
		int range = getRadius(); 
		for(Tuple tu : baseExplore){
			if(tu.getElementAt(String.class, 2).equals(Empty.type)){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				if(Math.abs(x) > range || Math.abs(y) > range){
					put(new Tuple(x,y,MapMerger.ACTION_NEW), self2base);
				}
			}
		}
		
		//add resources
		LinkedList<Tuple> map = getNeighbours(position, true);
		LinkedList<Tuple> base = getNeighbours(position, false);
		System.out.println(map);
		System.out.println(base);
		for(Tuple tb : base){
			String strb = tb.getElementAt(String.class, 0);
			int xb = tb.getElementAt(Integer.class, 1);
			int yb = tb.getElementAt(Integer.class, 2);
			if(strb.equals(Empty.type)){
				for(Tuple tm : map){
					String strm = tm.getElementAt(String.class, 0);
					int xm = tm.getElementAt(Integer.class, 1);
					int ym = tm.getElementAt(Integer.class, 2);
					if(xb==xm && yb==ym && !strm.equals(Empty.type)){
						System.out.println(strb);
						put(tm, self2base);
					}
				}
			}
		}
	}
	
	private int getRadius() throws InterruptedException, IOException{
		return query(new Template(new ActualTemplateField(MapMerger.MAP_EDGE), new FormalTemplateField(Integer.class)),self2base).getElementAt(Integer.class, 1);
	}
	
	private LinkedList<Tuple> getNeighboursExplore(Point position) throws InterruptedException, IOException{
		put(new Tuple("neighbours_explore", id, position.x, position.y), self2base);
		Template tp = new Template(new ActualTemplateField("neighbours_explore"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		LinkedList<Tuple> list = get(tp, self2base).getElementAt(LinkedList.class, 2);
		return list;
	}
	
	private LinkedList<Tuple> getNeighbours(Point position, boolean bool) throws InterruptedException, IOException{
		PointToPoint p2p = (bool) ? self2map : self2base;
		put(new Tuple("neighbours_all", id, position.x, position.y), p2p);
		Template tp = new Template(new ActualTemplateField("neighbours_all"), new ActualTemplateField(id), new FormalTemplateField(LinkedList.class));
		LinkedList<Tuple> list = get(tp, p2p).getElementAt(LinkedList.class, 2);
		return list;
	}
}
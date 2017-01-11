package gatheringAI;

import java.awt.Point;
import java.util.LinkedList;


public class AStar {
	
	private LinkedList<Point> closedSet = new LinkedList<Point>();
	private LinkedList<Point> openSet = new LinkedList<Point>();
	private LinkedList<Point> cameFrom = new LinkedList<Point>();
	
	private Point start;
	private Point end;
	
	public AStar(Point start, Point end){
		this.start=start;
		this.end=end;
		openSet.add(start);
	}
	
	
	
	
}

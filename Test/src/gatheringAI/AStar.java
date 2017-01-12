package gatheringAI;

import java.awt.Point;
import java.util.LinkedList;

import map.Map;
import map.World;


public class AStar {
	
	private LinkedList<Point> closedSet = new LinkedList<Point>();
	private LinkedList<Point> openSet = new LinkedList<Point>();
	private LinkedList<Point> cameFrom = new LinkedList<Point>();
	
	private Point start; //0,0
	private Point end; //goal,goal
	private Point current;
	
	private int offset;
	
	private Map map; 
	private boolean flying;
	
	public AStar(Point start, Point end,int offset, Map map){
		this.start=start;
		this.end=end;
		openSet.add(start);
		current=start;
		this.offset=offset;
		System.out.println(start());
	}
	
	public LinkedList<Point> start(){
		
		int [][] gScore = new int[Math.abs(end.x)+1][Math.abs(end.y)+1];
		int tentativeGScore = 1;
		gScore[end.x][end.y]=1;
		LinkedList<Point> neighbors = new LinkedList<Point>();
		current=lowestGScore(gScore);
		while(!openSet.isEmpty()){
			if(current==end){
				return reconstructPath();
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			neighbors = map.RetrievePathableNeighbors(current);
			if(flying) neighbors=World.getNeighbors(current);
			
			
			
			for(Point p1 : neighbors){
				if(closedSet.contains(p1))continue;
				
				tentativeGScore=gScore[current.x][current.y]+1;
				
				if(!openSet.contains(p1)) openSet.add(p1);
				
				else if(tentativeGScore>=gScore[p1.x][p1.y]) continue;
				
				cameFrom.add(p1);
				gScore[p1.x][p1.y] = tentativeGScore;
			}
		}
		
		return cameFrom;
	}

	

	private Point lowestGScore(int[][] gScore) {
		Point p=new Point(openSet.getFirst());
		int min=gScore[p.x][p.y];
		
		for(Point p1 : openSet){
			if(gScore[p1.x][p1.y]<min && gScore[p1.x][p1.y]!=0){
				p=p1;
				min=gScore[p1.x][p1.y];
			}
		}
				
		return p;
	}
	
	
	private LinkedList<Point> reconstructPath() {
		LinkedList<Point> path = new LinkedList<Point>();
		return cameFrom;
	}
}

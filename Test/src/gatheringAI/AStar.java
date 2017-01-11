package gatheringAI;

import java.awt.Point;
import java.util.LinkedList;

import map.World;


public class AStar {
	
	private LinkedList<Point> closedSet = new LinkedList<Point>();
	private LinkedList<Point> openSet = new LinkedList<Point>();
	private LinkedList<Point> cameFrom = new LinkedList<Point>();
	
	private Point start; //0,0
	private Point end; //goal,goal
	private Point current;
	
	public AStar(Point start, Point end){
		this.start=start;
		this.end=end;
		openSet.add(start);
		current=start;
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
			
			neighbors = World.getNeighbors(current);
			LinkedList<Point> tempNeighbors = new LinkedList<Point>();
			for(Point p1 : neighbors){
				if(p1.x<0 || p1.y<0 ){
					tempNeighbors.add(p1);
				}
			}
			for(Point p1 : tempNeighbors){
				neighbors.remove(p1);
			}
			
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

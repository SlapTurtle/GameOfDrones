package gatheringAI;

import java.awt.Point;

public class Main {
	
	public static void main(String [] args){
		Point start = new Point(0,0);
		Point end = new Point(9,9);
		
		AStar a = new AStar(start,end);
	}
}

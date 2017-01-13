package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class AStarPoint extends Point {
	public double fscore;
	public int gscore;
	public AStarPoint cameFrom;
	
	public AStarPoint (int x, int y) {
		super(x,y);
		
	}
	
	public static LinkedList<AStarPoint> convertPointList (LinkedList<Point> pointList) {
		LinkedList<AStarPoint> aStarList = new LinkedList<AStarPoint>();
		for (Point point:pointList){
			aStarList.add(new AStarPoint(point.x,point.y));
		}
		return aStarList;
	}
	
	//Converter to Point
	public static ArrayList<Point> convertToPointList (ArrayList<AStarPoint> aPointList) {
		ArrayList<Point> pointList = new ArrayList<Point>();
		for (AStarPoint aPoint : aPointList){
			pointList.add(new Point(aPoint.x,aPoint.y));
		}
		return pointList;
	}
}

package userInterface;

import java.awt.Point;

import droneNode.DroneAI;
import map.Map;
import userInterface.UI.GridDisplay;


public final class UIcontrol {
	protected Map map;
	protected GridDisplay gridDisplay;
	
	public UIcontrol(Map map, GridDisplay ui) {
		this.map = map;
		this.gridDisplay = ui;
	}
	
	public void move(String id, Point p) {
		gridDisplay.moveDrone(id, p);
	}
	public void move(String id, int dir){
		System.out.println("moving " + id.toString() + " in dir " + dir);
		gridDisplay.moveDrone(id, dir);
	}
	
	public void delete(){
		
	}
	
	public static void addVision(){
		
	}
	
	public String getID(Point p){
		for (DroneAI d : map.drones) {
			if (d.position.equals(p)) {
				return d.id;
			}
		}
		return null;
	}
	
	
}

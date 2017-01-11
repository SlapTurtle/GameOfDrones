package UI;

import java.awt.Point;
import java.util.UUID;


import UI.UI.GridDisplay;
import map.Map;
import resources.Drone;


public final class UIcontrol {
	protected Map map;
	protected GridDisplay gridDisplay;
	
	public UIcontrol(Map map, GridDisplay ui) {
		this.map = map;
		this.gridDisplay = ui;
	}
	
	public void move(UUID id, Point p) {
		gridDisplay.moveDrone(id, p);
	}
	public void move(UUID id, int dir){
		System.out.println("moving " + id.toString() + " in dir " + dir);
		gridDisplay.moveDrone(id, dir);
	}
	
	public void delete(){
		
	}
	
	public static void addVision(){
		
	}
	
	public UUID getID(Point p){
		for (Drone d : map.drones) {
			if (d.position.equals(p)) {
				return d.ID;
			}
		}
		return null;
	}
	
	
}

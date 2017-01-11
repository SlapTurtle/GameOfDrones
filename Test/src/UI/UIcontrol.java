package UI;

import java.awt.Point;
import java.util.UUID;


import UI.UI.GridDisplay;


public final class UIcontrol { 
	protected GridDisplay gridDisplay;
	
	public UIcontrol(GridDisplay ui) {
		this.gridDisplay = ui;
	}
	
	public void move(UUID id, Point p) {
		gridDisplay.moveDrone(id, p);
	}
	public void move(UUID id, int dir){
		gridDisplay.moveDrone(id, dir);
	}
	
	public void delete(){
		
	}
	
	public static void addVision(){
		
	}
	
	
}

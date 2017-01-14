package userInterface;

import java.awt.Point;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import baseNode.*;
import droneNode.*;
import map.*;

public class Console implements Runnable {
	String[][] board;
	int delay;
	int size;
	Node base;
	Node map;
	Node[] drones;	
	UserInterfaceAgent UserInterfaceAgent;

	public Console(Node baseNode, Node mapNode, Node[] droneNodes, int delay, int size) {
		base = baseNode;
		map = mapNode;
		drones = droneNodes;
		this.delay = delay;
		this.size = size;
		if(baseNode != null) {
			UserInterfaceAgent = new UserInterfaceAgent();
			baseNode.addAgent(UserInterfaceAgent);
		}
		run();
	}

	public void run() {
		try {
			Template rdy = new Template(new ActualTemplateField("ready"));
			Tuple go = new Tuple("go");
			while(true) {
				//wait for all things to be ready
				//base.get(rdy);
				//map.get(rdy);
				//System.out.println("UI gets");
				for(Node drone : drones){
					drone.get(rdy);
				}
				//System.out.println("UI Renders");
				Thread.currentThread().sleep(delay);
				
				//renders new image
				render();
				
				//tells everything to make next move
				//base.put(go);
				//map.put(go);
				//System.out.println("UI puts");
				for(Node drone : drones){
					drone.put(go);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Displays a given map's current state to the console.
	 * Avoid invoking while other instances of the method are running.
	 * @param*/
	public void render() {
		board = UserInterfaceAgent.getMap();
		int offsetx = (UserInterfaceAgent != null) ? -UserInterfaceAgent.bounds[1] : 0 ;
		int offsety = (UserInterfaceAgent != null) ? -UserInterfaceAgent.bounds[3] : 0 ;
		for(Node drone : drones){
			AbstractDrone AI = (AbstractDrone) drone.getAttribute("AI").getValue();
			Point p = AI.position;
			board[p.x + offsetx][p.y + offsety] = AI.type; 
		}
		System.out.println("\n\n\n\n\n");
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				char c = '.';
				if (board[x][y] != null && board[x][y] != "-") {
					switch (board[x][y]) {
					case "TEST": c = 'J'; break;
					case "BASE": c = 'B'; break;
					case "GOLD": c = 'G'; break;
					case "ROCK": c = 'R'; break;
					case "EXPDRONE": c = 'E'; break;
					case "HARDRONE": c = 'H'; break;
					case "TREE": c = 'T'; break;
					case "WATER": c = 'W'; break;
					default: 	 c = ' '; break;
					}
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
}

package userInterface;

import java.awt.Point;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import baseNode.*;
import droneNode.*;
import mapNode.*;

public class Console implements Runnable {
	String[][] board;
	int delay;
	int size;
	Node base;
	Node map;
	Node[] drones;

	public Console(Node baseNode, Node mapNode, Node[] droneNodes, int delay, int size) {
		board = new String[size][size];
		base = baseNode;
		map = mapNode;
		drones = droneNodes;
		this.delay = delay;
		this.size = size;
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
				for(Node drone : drones){
					drone.get(rdy);
				}
				
				//renders new image
				render();
				
				//tells everything to make next move
				base.put(go);
				map.put(go);
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
		board = new String[size][size];//map.Retrieve(size);
		for(Node drone : drones){
			DroneAI AI = (DroneAI) drone.getAttribute("AI").getValue();
			Point p = AI.position;
			//TODO: Offset
			board[p.x][p.y] = (AI instanceof ExpDrone) ? "EXPDRONE" : "HARDRONE"; 
		}
		System.out.println("\n\n\n\n\n");
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				char c = '.';
				if (board[x][y] != null && board[x][y] != "-") {
					switch (board[x][y]) {
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

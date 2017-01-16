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
			int i = 0;
			while(true) {
				//wait for all things to be ready
				//System.out.println("UI gets");				
				//map.get(rdy);
				//base.get(rdy);
				for(Node drone : drones){
					drone.get(rdy);
				}
				
				//System.out.println("UI Renders");
				Thread.currentThread().sleep(delay);
				
				//renders new image
				render();				
				
				//tells everything to make next move
				//System.out.println("UI puts");
				//map.put(go);
				//base.put(go);
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
	 * @param
	 * @throws InterruptedException */
	public void render() throws InterruptedException {
		//puts and gets UI handshake
		Template rdyUI = new Template(new ActualTemplateField("readyUI"));
		Tuple goUI = new Tuple("goUI");
		base.put(goUI);
		base.get(rdyUI);
		
		board = UserInterfaceAgent.getMap();
		int offsetx = -UserInterfaceAgent.bounds[1];
		int offsety = -UserInterfaceAgent.bounds[3];
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				char c = '.';
				switch (board[x][y]) {
				case "BASE": c = 'B'; break;
				case "GOLD": c = 'G'; break;
				case "ROCK": c = 'R'; break;
				case "EXPDRONE": c = 'E'; break;
				case "HARDRONE": c = 'H'; break;
				case "TREE": c = 'T'; break;
				case "WATER": c = 'W'; break;
				default: 	 c = '.'; break;
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n\n\n");
	}
	
}

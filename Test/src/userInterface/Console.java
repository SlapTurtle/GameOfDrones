package userInterface;

import java.awt.Point;
import java.io.IOException;
import java.util.Scanner;

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
				render();
				new Scanner(System.in).nextLine();
				for(Node drone : drones){
					drone.put(go);
				}
				for(Node drone : drones){
					drone.get(rdy);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Displays a given map's current state to the console.
	 * Avoid invoking while other instances of the method are running.
	 * @param
	 * @throws InterruptedException 
	 * @throws IOException */
	public void render() throws InterruptedException, IOException {
		//puts and gets UI handshake
		System.out.println(UserInterfaceAgent.queryAllTuples(true, false, false, false));
		Template rdyMM = new Template(new ActualTemplateField("readyMM"));
		Tuple goUI = new Tuple("goUI");
		base.put(goUI);
		base.get(rdyMM);
		
		board = UserInterfaceAgent.getMap();
		int offsetx = -UserInterfaceAgent.bounds[1];
		int offsety = -UserInterfaceAgent.bounds[3];
		int b1 = UserInterfaceAgent.bounds[0]-UserInterfaceAgent.bounds[1]+1;
		int b2 = UserInterfaceAgent.bounds[2]-UserInterfaceAgent.bounds[3]+1;
		
		System.out.println("----");
		for (int y = 0; y < b2; y++) {
			for (int x = 0; x < b1; x++) {
				char c = 'X';
				if(board[x][y] != null) {
					switch (board[x][y]) {
					case "BASE": c = 'B'; break;
					case "GOLD": c = 'G'; break;
					case "ROCK": c = 'R'; break;
					case "EXPDRONE": c = 'E'; break;
					case "HARDRONE": c = 'H'; break;
					case "TREE": c = 'T'; break;
					case "WATER": c = 'W'; break;
					case "EMPTY": c = '.'; break;
					default: 	 c = '.'; break;
					}
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}

		System.out.println("----");
		System.out.println("\n\n\n\n\n");
	}
	
}

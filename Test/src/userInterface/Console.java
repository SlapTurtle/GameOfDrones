package userInterface;

import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import baseNode.*;
import droneNode.*;
import resources.Gold;

public class Console implements Runnable {
	Node base;
	Node map;
	LinkedList<Drone> drones;
	int delay;
	int size;
	
	String[][] board;
	UserInterfaceAgent UserInterfaceAgentBase;

	public Console(Node base, Node map, LinkedList<Drone> drones, int delay, int size) {
		this.base = base;
		this.map = map;
		this.drones = drones;
		this.delay = delay;
		this.size = size;
		
		UserInterfaceAgentBase = new UserInterfaceAgent();
		base.addAgent(UserInterfaceAgentBase);
		
		run();
	}

	public void run() {
		try {
			Template rdy = new Template(new ActualTemplateField("ready"));
			Template rdyUI = new Template(new ActualTemplateField("readyUI"));
			Template rdyMM = new Template(new ActualTemplateField("readyMM"));
			Tuple go = new Tuple("go");
			Tuple goUI = new Tuple("goUI");
			Tuple goMM = new Tuple("goMM");
			
			while(true) {
				//delays the whole process
				Thread.sleep(delay);
				//puts go for UserInterfaceAgent
				base.put(goUI);
				base.get(rdyUI);
				//puts go for MapMerger
				base.put(goMM);
				base.get(rdyMM);
				//Prints board
				PrintString();
				//puts go signal to all drones
				for(Drone drone : drones){
					drone.put(go);
				}
				
				//gets ready signal from all drones
				Long t = System.currentTimeMillis();
				for(Drone drone : drones){
					drone.get(rdy);
				}
				System.out.println((System.currentTimeMillis()-t));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PrintString() throws InterruptedException, IOException {
		//1=explore : 2=resource : 3=drones : 4=baseField
		//System.out.println(UserInterfaceAgentBase.queryAllTuples(true, false, false, false));
		//System.out.println(UserInterfaceAgentMap.queryAllTuples(false, true, false, false));
		
		board = UserInterfaceAgentBase.getMap();
		int b1 = UserInterfaceAgentBase.bounds[0]-UserInterfaceAgentBase.bounds[1]+1;
		int b2 = UserInterfaceAgentBase.bounds[2]-UserInterfaceAgentBase.bounds[3]+1;
		
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
					default: 	 c = '?'; break;
					}
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n\n\n");
	}	
}

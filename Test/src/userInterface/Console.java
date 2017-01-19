package userInterface;

import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import baseNode.*;
import droneNode.*;

public class Console implements Runnable {
	
	Node base;
	Node map;
	LinkedList<Drone> drones;
	int delay;
	String seed;
	boolean displayTime;
	
	String[][] board;
	UserInterfaceAgent UserInterfaceAgentBase;

	public Console(Node base, Node map, LinkedList<Drone> drones, int delay, String seed, boolean displayTime) {
		this.base = base;
		this.map = map;
		this.drones = drones;
		this.delay = delay;
		this.seed = seed;
		this.displayTime = displayTime;
		
		UserInterfaceAgentBase = new UserInterfaceAgent();
		base.addAgent(UserInterfaceAgentBase);
		
		run();
	}

	public void run() {
		Template rdy = new Template(new ActualTemplateField("ready"));
		Template rdyMM = new Template(new ActualTemplateField("readyMM"));
		Tuple go = new Tuple("go");
		Tuple goMM = new Tuple("goMM");
		
		Long t = System.currentTimeMillis();
		Thread gt, dt;
		while(true) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			if(displayTime){
				System.out.println("Time: "+(System.currentTimeMillis() - t));
				System.out.println("Seed: "+seed);
				t = System.currentTimeMillis();
			}
			gt = new Thread(() -> {
				try {
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
					for(Drone drone : drones){
						drone.get(rdy);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			dt = new Thread(() -> {
				try {
					//waits for delay
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			try {
				dt.start();
				gt.start();
				dt.join();
				gt.join();
				dt.stop();
				gt.stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
				char c = '+';
				if(board[x][y] != null) {
					switch (board[x][y]) {
					case "BASE": c = 'B'; break;
					case "GOLD": c = 'G'; break;
					case "ROCK": c = 'R'; break;
					case "EXPDRONE": c = 'E'; break;
					case "HARDRONE": c = 'H'; break;
					case "TREE": c = 'T'; break;
					case "WATER": c = 'W'; break;
					case "EMPTY": c = ' '; break;
					default: 	 c = '?'; break;
					}
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}	
}

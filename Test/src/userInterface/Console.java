package userInterface;

import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

import baseNode.*;
import droneNode.*;
import map.Map;

public class Console implements Runnable {
	
	Node base;
	Node map;
	LinkedList<Drone> drones;
	int delay;
	String seed;
	
	String[][] board;
	UserInterfaceAgent UserInterfaceAgentBase;

	public Console(Node base, Node map, LinkedList<Drone> drones, int delay, String seed) {
		this.base = base;
		this.map = map;
		this.drones = drones;
		this.delay = delay;
		this.seed = seed;
		
		UserInterfaceAgentBase = new UserInterfaceAgent();
		base.addAgent(UserInterfaceAgentBase);
		
		run();
	}

	public void run() {
		try{
			Template rdy = new Template(new ActualTemplateField("ready"));
			Template rdyMM = new Template(new ActualTemplateField("readyMM"));
			Tuple go = new Tuple("go");
			Tuple goMM = new Tuple("goMM");
			
			Long t = System.currentTimeMillis();
			Thread gt, dt;
			while(true) {
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				System.out.print(" | Gold: "+base.query(new Template(new ActualTemplateField("GoldCounter"),Map.AnyInteger)).getElementAt(Integer.class, 1));
				System.out.print(" | Tree: "+base.query(new Template(new ActualTemplateField("TreeCounter"),Map.AnyInteger)).getElementAt(Integer.class, 1));
				System.out.print(" | Time/move: "+(System.currentTimeMillis() - t)+" ms");
				System.out.print(" | Seed: "+seed);
				System.out.println(" | ");
				t = System.currentTimeMillis();		
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
		} catch(Exception e) {}
	}

	public void PrintString() throws InterruptedException, IOException {
		
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

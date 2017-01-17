package launch;

import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.topology.VirtualPort;
import baseNode.*;
import droneNode.*;
import map.*;
import userInterface.*;

public class Main {

	public static final int DELAY = 100;
	public static final int FIELD = 40;
	
	static final int port_int = 8080;
	public static final VirtualPort port = new VirtualPort(port_int);
	static final String baseID = "baseNode";
	static final String mapID = "mapNode";
	static final String droneID = "droneNode";
	
	static final String seed = UUID.randomUUID().toString();
	
	static final int exploreDrones = 2;
	static final int harvestDrones = 1;
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;

	public static void run() throws InterruptedException{
		//Map
		Map map = new Map(mapID, seed, port, port_int);
		
		//Base
		Base base = new Base(baseID, port, port_int, startGoldCount, startTreeCount, exploreDrones, harvestDrones);
		
		//Drones
		LinkedList<Drone> drones = new LinkedList<Drone>();
		
		for(int i = 0; i<exploreDrones + harvestDrones; i++){
			String type = (i < exploreDrones) ? "EXPDRONE" : "HARDRONE";
			Drone droneNode = new Drone(droneID+i, type, port, port_int, base, map);
			drones.add(droneNode);
		}
		
		//UI
		new Console(base, map, drones, DELAY, FIELD);
		
		
	}

	public static void main(String[] args) throws InterruptedException{
		run();
	}
}
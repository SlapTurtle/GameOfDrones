package launch;

import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.topology.VirtualPort;
import baseNode.*;
import droneNode.*;
import map.*;
import userInterface.*;

public class Main {
	//Node Configurations.
	static final int port_int = 8080;
	static final VirtualPort port = new VirtualPort(port_int);
	static final String baseID = "baseNode";
	static final String mapID = "mapNode";
	static final String droneID = "droneNode";
	
	//Map generate
	static final String seed = UUID.randomUUID().toString();
	
	//Base
	static final int exploreDrones = 2;
	static final int harvestDrones = 1;
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;
	
	//UI
	static final int minDelay = 300; //will exceed due to computing power at some point
	static final boolean displayTime = true; //displays actual time in milliseconds.

	public static void run() throws InterruptedException{
		//Map
		Map map = new Map(mapID, seed, port, port_int, exploreDrones);
		
		//Base
		Base base = new Base(baseID, port, port_int, startGoldCount, startTreeCount, exploreDrones, harvestDrones, exploreDrones+harvestDrones);
		
		//Drones
		LinkedList<Drone> drones = new LinkedList<Drone>();
		
		for(int i = 0; i<exploreDrones + harvestDrones; i++){
			String type = (i < exploreDrones) ? ExpDrone.type : HarDrone.type;
			Drone droneNode = new Drone(droneID+i, type, port, port_int, base, map);
			drones.add(droneNode);
		}
		
		//UI
		new Console(base, map, drones, minDelay, displayTime);
		
		
	}

	public static void main(String[] args) throws InterruptedException{
		run();
	}
}
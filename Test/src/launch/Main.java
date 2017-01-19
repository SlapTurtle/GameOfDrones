package launch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;
import org.cmg.resp.topology.VirtualPort;
import baseNode.*;
import droneNode.*;
import map.*;
import resources.Expdrone;
import resources.Hardrone;
import userInterface.*;

public class Main {
	//Node Configurations.
	static final int port_int = 8080;
	static final VirtualPort port = new VirtualPort(port_int);
	static final String baseID = "baseNode";
	static final String mapID = "mapNode";
	static final String droneID = "droneNode";
	
	//Map generate
//	static final String seed = "yourseed";
	static final String seed = UUID.randomUUID().toString();
	
	//Base
	public static final int exploreDrones = 2;
	public static final int harvestDrones = 4;
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;
	
	//UI
	static final int minDelay = 50; //will exceed due to computing power at some point
	static final boolean displayTime = true; //displays actual time in milliseconds.

	public static void run() throws InterruptedException, IOException{
		//Map
		Map map = new Map(mapID, seed, port, port_int, exploreDrones);
		
		//Base
		Base base = new Base(baseID, port, port_int, startGoldCount, startTreeCount, exploreDrones, harvestDrones, exploreDrones+harvestDrones);
		
		//Drones
		LinkedList<Drone> drones = new LinkedList<Drone>();
		
		for(int i = 0; i < exploreDrones + harvestDrones; i++){
			String type = (i < exploreDrones) ? Expdrone.type : Hardrone.type;
			Drone droneNode = new Drone(droneID+i, type, port, port_int, base, map);
			drones.add(droneNode);
		}
		
		//UI
		new Console(base, map, drones, minDelay, seed, displayTime);
		
		
	}

	public static void main(String[] args) throws InterruptedException, IOException{
		run();
	}
}
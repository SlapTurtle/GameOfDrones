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
	//This string can be set whatever is liked. The same seed, will always generate the same map.
	//Leave the seed as null to randomly generate a seed.
	//static String seed = null;
	//SUGGESTED SEED:
	static String seed = "22c9e190-232a-4b33-ba76-022efa516fd0";
	
	
	//Base
	public static final int exploreDrones = 3;
	public static final int harvestDrones = 2; //MAX 4 due to spawn points
	static final int startGoldCount = 0;
	static final int startTreeCount = 0;
	
	//UI
	static final int minDelay = 300; //will exceed due to computing power at some point

	public static void run() throws InterruptedException, IOException{
		if(seed == null)
			seed = UUID.randomUUID().toString();
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
		new Console(base, map, drones, minDelay, seed);
		
		
	}

	public static void main(String[] args) throws InterruptedException, IOException{
		run();
	}
}
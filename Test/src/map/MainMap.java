package map;

import org.cmg.resp.comp.Node;

import droneNode.DroneAI;
import userInterface.Console;

public class MainMap {

	public static final int DELAY = 500;
	public static final int FIELD = 40;

	public static void main(String[] args) throws InterruptedException {
		Map map = new Map();
		new Console(null, null, new Node[]{}, DELAY, FIELD);
	}	 
}
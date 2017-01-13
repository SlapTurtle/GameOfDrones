package map;

import UI.Console;
import map.Map;

public class Main {

	public static final int DELAY = 500;
	public static final int FIELD = 40;

	public static void main(String[] args) throws InterruptedException {
		Map map = new Map();
		new Console(map, DELAY, FIELD);
	}	 

}
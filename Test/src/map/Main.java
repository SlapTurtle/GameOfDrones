package map;

import java.io.IOException;

public class Main {

	public static final int DELAY = 200;

	public static void main(String[] args) throws InterruptedException {
		Map map = new Map();
		Thread.sleep(100);
		display(map);

		while(true) {
			synchronized (map.render) {
				map.render.notifyAll();
			}
			Thread.sleep(DELAY);
			display(map, 30);
		}
	}

	/** Displays a given map's current state to the console.
	 * Avoid invoking while other instances of the method are running.
	 * @param*/
	static void display(Map map, int size) {
		System.out.flush();
		String[][] N = map.Retrieve(size);
		System.out.println("\n\n\n\n\n");
		for (int y = 0; y < N.length; y++) {
			for (int x = 0; x < N.length; x++) {
				char c = ' ';
				if (N[x][y] != "-") {
					switch (N[x][y]) {
					case "BASE": c = 'B'; break;
					case "GOLD": c = 'G'; break;
					case "EXPDRONE": c = 'E'; break;
					case "TREE": c = 'T'; break;
					case "WATER": c = 'W'; break;
					default: 	 c = ' '; break;
					}
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
	static void display(Map map) {
		String[][] N = map.Retrieve(40);
		System.out.println("\n");
		for (int y = 0; y < N.length; y++) {
			for (int x = 0; x < N.length; x++) {
				char c;
				switch (N[x][y]) {
				case "BASE": c = 'B'; break;
				case "GOLD": c = 'G'; break;
				case "EXPDRONE": c = 'E'; break;
				case "TREE": c = 'T'; break;
				default: 	 c = ' '; break;
				}
				
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	 

}

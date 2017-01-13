package userInterface;

import map.Map;

public class Console implements Runnable {

	Map map;
	String[][] board;
	int delay;
	int size;

	public Console(Map map, int delay, int size) {
		this.map = map;
		board = new String[size][size];
		this.delay = delay;
		this.size = size;
		run();
	}

	public void run() {
		try {
			while(true) {
				Thread.sleep(delay);
				render();
				synchronized (map.render) {
					map.render.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Displays a given map's current state to the console.
	 * Avoid invoking while other instances of the method are running.
	 * @param*/
	public void render() {
		board = map.Retrieve(size);
		System.out.println("\n\n\n\n\n");
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				char c = ' ';
				if (board[x][y] != "-") {
					switch (board[x][y]) {
					case "BASE": c = 'B'; break;
					case "GOLD": c = 'G'; break;
					case "ROCK": c = 'R'; break;
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
	
}

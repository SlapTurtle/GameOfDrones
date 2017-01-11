package map;

public class Main {

	public static final int DELAY = 500;

	public static void main(String[] args) throws InterruptedException {
		Map map = new Map();
		Thread.sleep(100);
		display(map);

//		while(true) {
//			synchronized (map.render) {
//				map.render.notifyAll();
//			}
//			Thread.sleep(DELAY);
//			display(map);
//		}
	}

	/** Displays a given map's current state to the console.
	 * Do not invoke while other instances of the same method are running.
	 * @param*/
	static void display(Map map) {
		int[][] N = map.Retrieve();
		System.out.println("\n");
		for (int y = 0; y < map.bounds[3]-map.bounds[2]; y++) {
			for (int x = 0; x < map.bounds[1]-map.bounds[0]; x++) {
				char c;
				switch (N[x][y]) {
				case -1: c = '.'; break;
				case 1:  c = 'G'; break;
				case 2:  c = 'T'; break;
				case 3:  c = 'B'; break;
				case 4:  c = 'W'; break;
				case 5:  c = 'D'; break;
				case 6:  c = 'R'; break;
				default: c = ' '; break;
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}

}

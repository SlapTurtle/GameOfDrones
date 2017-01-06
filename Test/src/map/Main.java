package map;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		World world = new World(30);
		//Map map = new Map(world, "f40562ff-e6a4-469c-aaf2-4baa15c2f701");
		Map map = new Map(world);
		//Map map = new Map(world, "5fbd53c6-d677-4ee0-a054-6e96a093fc2e");
		//Map map = new Map(world, "969af951-255d-491a-94e2-1262ff90c17f");
		//Map map = new Map(world, "ac0a6337-edc0-4077-ba5d-6d85730492df");
		Thread.sleep(100);
		int[][] N = map.Retrieve();
		
		System.out.println("\nRendering Map...\n");
		Thread.sleep(50);
		
		for (int y = 0; y < map.world.Y(); y++) {
			for (int x = 0; x < map.world.X(); x++) {
				char c;
				
//				if (x == map.world.X() / 2 && y == map.world.Y() / 2) {
//					c = 'B';
//				} else {
					switch (N[x][y]) {
					case 1: c = 'G'; break;
					case 2: c = 'T'; break;
					case 3: c = 'B'; break;
					case 4: c = 'W'; break;
					default: c = '.'; break;
					}
//				}
				
				System.out.print(c + " ");
			}
			System.out.println();
		}
		

	}

}

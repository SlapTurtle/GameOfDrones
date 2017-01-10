package map;

import java.util.LinkedList;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Map map = new Map(new World(20), "hej");
		Thread.sleep(100);
		display(map);

		int i = 0;
		while(i < 100) {
			synchronized (map.render) {
				map.render.notifyAll();
			}
			Thread.sleep(500);
			display(map);
			i++;
		}
	}

	/** Displays a given map's current state to the console.
	 * Do not invoke while other instances of the same method are running.
	 * @param*/
	static void display(Map map) {		
		LinkedList<Tuple> drones = map.RetrieveTuples("EXPDRONE");
		int[][] N = map.Retrieve();
		System.out.println("\nRendering Map...\n");
		for (int y = 0; y < map.bounds[3]-map.bounds[2]; y++) {
			for (int x = 0; x < map.bounds[1]-map.bounds[0]; x++) {
				char c;
				boolean d = false;
				for (Tuple t : drones) {
					if (Map.getTupleX(t) == x && Map.getTupleY(t) == y) {
						d = true;
						System.out.print('D'  + " ");
					}
				}
				if (!d && map.map.queryp(new Template(new ActualTemplateField(x-map.world.X()/2), new ActualTemplateField(y-map.world.Y()/2))) != null) {
					switch (N[x][y]) {
					case 1: c = 'G'; break;
					case 2: c = 'T'; break;
					case 3: c = 'B'; break;
					case 4: c = 'W'; break;
					case 5: c = 'D'; break;
					default: c = '.'; break;
					}
				} else {
					c = 'X';
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}

}

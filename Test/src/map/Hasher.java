package map;

import java.awt.Point;
import java.util.Random;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

/** Class responsible for hash functions associated with world expansion. */
public class Hasher extends Agent{

	Template getT = new Template(new ActualTemplateField("hash"), Map.AnyPoint, Map.AnyString, Map.AnyInteger, Map.AnyWorld);

	public Hasher() {
		super("hasher");
	}

	protected void doRun() throws Exception {
		while(true) {
			// REQUEST
			Tuple request = get(getT, Self.SELF);
			Point center = request.getElementAt(Point.class, 1);
			String seed = request.getElementAt(String.class, 2);
			int hashlength = request.getElementAt(Integer.class, 3);
			World world = request.getElementAt(World.class, 4);
			// PROCESS
			String hash = getExpansionHash(seed, center, hashlength);
			// RESPONSE
			Tuple response = new Tuple("generate", world, hash);
			put(response, Self.SELF);
		}
	}

	public String getExpansionHash(String seed, Point center, int hashlength) {
		int newHash = h(Math.abs(seed.hashCode()), center.x, 3, 2*center.y, seed.hashCode());
		return generateHash(newHash, hashlength);
	}

	public String generateHash(int newHash, int length) {
		Random r = new Random(newHash);
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder();
	    for( int j = 0; j < length; j++ ) 
	    	sb.append( AB.charAt( r.nextInt(AB.length()) ) );
		return sb.toString();
	}

	public int h(int A, int x, int B, int y, int C) {
		return (y != C) ? (A / (x + B)) % (C - y) : (A / (x + B)) % (C*3 - y*2 + 1);
	}
	
	
}

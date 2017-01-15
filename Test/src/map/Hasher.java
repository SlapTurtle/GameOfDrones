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

	Template getT = new Template(new ActualTemplateField("hash"), Map.AnyString, Map.AnyPoint, Map.AnyString, Map.AnyInteger);

	public Hasher() {
		super("hasher");
	}

	protected void doRun() throws Exception {
		while(true) {
			// REQUEST
			Tuple request = get(getT, Self.SELF);
			String target = request.getElementAt(String.class, 1);
			Point center = request.getElementAt(Point.class, 2);
			String seed = request.getElementAt(String.class, 3);
			int hashlength = request.getElementAt(Integer.class, 4);
			// PROCESS
			String hash = getExpansionHash(seed, center, hashlength);
			// RESPONSE
			Tuple response = new Tuple(target, hash);
			put(response, Self.SELF);
		}
	}

	public String getExpansionHash(String seed, Point center, int hashlength) {
		int newHash = h(Math.abs(seed.hashCode()), center.y-center.x, center.x+3, 2*center.y-1, seed.hashCode());
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
		
		return (A / (x + B)) % (C - y);
	}
	
	
}

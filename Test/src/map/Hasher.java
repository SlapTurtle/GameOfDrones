package map;

import java.awt.Point;
import java.util.Random;

/** Class responsible for hash functions associated with world expansion. */
public class Hasher {

	Map map;
	Random random;
	String seed;

	public Hasher(Map map, Random random, String seed) {
		this.map = map;
		this.random = random;
		this.seed = seed;
	}

	public String[] expansionHashes(int length) {
		String[] hash = new String[length];
		for (int i = 0; i < length; i++) {
			hash[i] = generateHash(random, length);
		}
		return hash;
	}

	public String getExpansionHash(Point center) {
		int index = Math.abs(h(seed.hashCode(), center.x, 11, center.y, 7, map.hash.length));
		int newHash = -1*h(Math.abs(map.hash[index].hashCode()), center.y-center.x, index+3, 2*center.y-1, seed.hashCode(), 0);
		Random r = new Random(newHash);
		return generateHash(r, Map.EXP_HASHLENGTH);
	}

	public String generateHash(Random r, int length) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder();
	    for( int j = 0; j < length; j++ ) 
	    	sb.append( AB.charAt( r.nextInt(AB.length()) ) );
		return sb.toString();
	}
	
	public int h(int A, int x, int B, int y, int C, int D) {
		
		return (D != 0) ? (A / (x + B)) % (y + C) % D : (A / (x + B)) % (C - y);
	}
	
	
}

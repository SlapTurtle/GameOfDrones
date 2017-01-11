package map;

import java.util.Random;

public final class Dice {
	
	Random random;
	
	public Dice(Random random) {
		this.random = random;
	}

	public boolean roll(double probability) {
		probability = (probability > 1) ? 1 : probability;
		return (random.nextInt(100) <= (int)(probability*100));
	}

}
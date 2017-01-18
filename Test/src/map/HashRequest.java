package map;

import java.awt.Point;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

public class HashRequest extends Tuple {
	
	public HashRequest(Point center, String seed, int hashlength, World world) {
		super("hash", center, seed, hashlength, world);
	}

}
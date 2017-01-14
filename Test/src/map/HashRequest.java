package map;

import java.awt.Point;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;

public class HashRequest extends Tuple {
	
	public HashRequest(String identifier, Point center, String seed, int hashlength) {
		super("hash", identifier, center, seed, hashlength);
	}

}
package droneNode;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import resources.*;

public class TestDrone extends Drone {
	static int i = 0;
	public TestDrone(Point position) {
		super(position, "TEST", "TEST" + i++);
	}
	
	int j = 0;
	protected Point moveDrone() throws Exception{
		//put request in map
		put(new Tuple("test", getName(), position.x, position.y),Drone.self2map);
		//get answer fom map
		Template tp = new Template(
				new ActualTemplateField("test"),
				new ActualTemplateField(getName()),
				new FormalTemplateField(Resource.class),
				new FormalTemplateField(Resource.class),
				new FormalTemplateField(Resource.class),
				new FormalTemplateField(Resource.class));
		Tuple tu = get(tp, self2map);
//		String s = id+"-------" + "\n";
//		s += "move: " + j++ + " : "+tu.getElementAt(Resource.class, 4).getClass().toString() + "\n";
//		s += "f: "+ position.x + position.y + " - t: " + (position.x+1) + position.y + "\n";
//		s += "-------";
//		System.out.println(s);
		return new Point(position.x+1, position.y);
	}
}

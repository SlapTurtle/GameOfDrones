package mapNode;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import resources.Resource;

public class RetrieverNew extends Agent {
	public static int counter = 0;
	
	public RetrieverNew() {
		super("Retriver" + counter++);
	}

	@Override
	protected void doRun() throws Exception {
		Template t = new Template(
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class),
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class)
					);
		
		while(true){
			Tuple in = get(t, Self.SELF);
			String order = in.getElementAt(String.class, 0);
			String id = in.getElementAt(String.class, 1);
			int x = in.getElementAt(Integer.class, 2);
			int y = in.getElementAt(Integer.class, 3);
			
			Resource[] list = null;
			switch(order){
			case "neighbour_all": list = getNeighbours(x,y,false);
			case "neighbour_pathable": list = getNeighbours(x,y,true);
			}
			put(new Tuple(order, id, list),Self.SELF);
			
		}
	}
	
	private Resource[] getNeighbours(int x, int y, boolean bool) {
		String res[] = {
				getp(templateXY(x+1,y)).getElementAt(String.class, 0),
				getp(templateXY(x-1,y)).getElementAt(String.class, 0),
				getp(templateXY(x,y+1)).getElementAt(String.class, 0),
				getp(templateXY(x,y-1)).getElementAt(String.class, 0)				
		};
		if(bool) ;//check for pathability
		
		return (res.length == 4) ? null : null;
	}
	
	private Template templateXY(int x, int y) {
		return new Template(
				new FormalTemplateField(String.class),
				new ActualTemplateField(x),
				new ActualTemplateField(y)
			);
	}

}

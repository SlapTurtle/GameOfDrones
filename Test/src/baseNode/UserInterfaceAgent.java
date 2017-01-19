package baseNode;

import java.io.IOException;
import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import map.Map;
import resources.Empty;

public class UserInterfaceAgent extends Agent {
	
	public int[] bounds = new int[]{0,0,0,0};
	public String[][] board;
	
	public UserInterfaceAgent() {
		super("UserInterfaceAgent");
	}

	@Override
	protected void doRun() throws Exception {}
	
	public String[][] getMap() throws InterruptedException, IOException{
		Template tm = new Template(
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(String.class));
		Template tr = new Template(
				new FormalTemplateField(String.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class));
		Template tt = new Template(
				new FormalTemplateField(String.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class));
		Template td = new Template(
				new FormalTemplateField(String.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(String.class));
		//Bounds minimum is range
		int range = query(new Template(new ActualTemplateField(MapMerger.MAP_EDGE), new FormalTemplateField(Integer.class)),Self.SELF).getElementAt(Integer.class, 1);
		bounds[0] = (range+1 > bounds[0]) ? range+1 : bounds[0];
		bounds[1] = (-range-1 < bounds[1]) ? -range-1 : bounds[1];
		bounds[2] = (range+1 > bounds[2]) ? range+1 : bounds[2];
		bounds[3] = (-range-1 < bounds[3]) ? -range-1 : bounds[3];
		//update bounds to latest
		LinkedList<Tuple> list = queryAll(tm);
		for(Tuple tu : list){
			if(tu != null){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				bounds[0] = (x >= bounds[0]) ? x+1 : bounds[0];
				bounds[1] = (x <= bounds[1]) ? x-1 : bounds[1];
				bounds[2] = (y >= bounds[2]) ? y+1 : bounds[2];
				bounds[3] = (y <= bounds[3]) ? y-1 : bounds[3];
				}
		}
		//create board
		board = new String[bounds[0]-bounds[1]+1][bounds[2]-bounds[3]+1];
		//add all explored field within mapedge
		for(int y = bounds[3]; y<bounds[2]-bounds[3]+1; y++){
			for(int x = bounds[1]; x<bounds[0]-bounds[1]+1; x++){
				boolean bx = (Math.abs(x) == range && y <= range && y >= -range);
				boolean by = (Math.abs(y) == range && x <= range && x >= -range);
				if((Math.abs(x) <= range && Math.abs(y) <= range)|| bx || by) {
					board[x-bounds[1]][y-bounds[3]] = "EMPTY";
				}
			}
		}
		//add all explored fields outside of mapEdge
		for(Tuple tu : list){
			if(tu != null){
				int x = tu.getElementAt(Integer.class, 0);
				int y = tu.getElementAt(Integer.class, 1);
				board[x-bounds[1]][y-bounds[3]] = Empty.type;
			}
		}
		//add all resources
		list = queryAll(tr);
		for(Tuple tu : list){
			String str = tu.getElementAt(String.class, 0);
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			board[x-bounds[1]][y-bounds[3]] = str;
		}
		//add more resources
		list = queryAll(tt);
		for(Tuple tu : list){
			String str = tu.getElementAt(String.class, 0);
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			board[x-bounds[1]][y-bounds[3]] = str;
		}
		//add drones
		list = queryAll(td);
		for(Tuple tu : list){
			String str = tu.getElementAt(String.class, 0);
			int x = tu.getElementAt(Integer.class, 1);
			int y = tu.getElementAt(Integer.class, 2);
			board[x-bounds[1]][y-bounds[3]] = str;
		}
		//always base on-top
		board[-bounds[1]][-bounds[3]] = "BASE";
		return board;
	}
}

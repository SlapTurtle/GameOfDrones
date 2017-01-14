package baseNode;

import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

public class UserInterfaceAgent extends Agent {

	public UserInterfaceAgent() {
		super("UserInterfaceAgent");
	}

	@Override
	protected void doRun() throws Exception {
		//does nothing
	}
	
	public String[][] getMap(){
		Template tp = new Template(
				new FormalTemplateField(String.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class));
		
		LinkedList<Tuple> list = queryAll(tp);
		int[] bounds = new int[]{0,0,0,0};
		for(Tuple tu : list){
			if(tu != null){
				int x = tu.getElementAt(Integer.class, 1);
				int y = tu.getElementAt(Integer.class, 2);
				bounds[0] = (x > bounds[0]) ? x : bounds[0];
				bounds[1] = (x < bounds[1]) ? x : bounds[1];
				bounds[2] = (y > bounds[2]) ? y : bounds[2];
				bounds[3] = (y < bounds[3]) ? y : bounds[3];
			}
		}
		String[][] board = new String[bounds[0]-bounds[1]+1][bounds[2]-bounds[3]+1];
		for(Tuple tu : list){
			if(tu != null){
				String type = tu.getElementAt(String.class, 0);
				int x = tu.getElementAt(Integer.class, 1);
				int y = tu.getElementAt(Integer.class, 2);
				board[x][y] = type;
			}
		}
		for(String[] row : board){
			for(String s : row){
				s = (s == null) ? "EMPTY" : s;
			}
		}
		return board;
	}
}

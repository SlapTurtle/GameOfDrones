package baseNode;

import java.util.LinkedList;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.topology.Self;

import resources.Empty;

public class UserInterfaceAgent extends Agent {
	
	public int[] bounds = new int[]{0,0,0,0};
	
	public UserInterfaceAgent() {
		super("UserInterfaceAgent");
	}

	@Override
	protected void doRun() throws Exception {
		Template tp = new Template(new ActualTemplateField(MapMerger.ACTION_NEW), new FormalTemplateField(Integer.class), new FormalTemplateField(Integer.class));
		while(true){
//			get(new Template(new ActualTemplateField("goUI")), Self.SELF);
//			LinkedList<Tuple> list = getAll(tp);
//			for(Tuple t : list) {
//				int x = t.getElementAt(Integer.class, 1);
//				int y = t.getElementAt(Integer.class, 2);
//				put(new Tuple(MapMerger.ACTION_OLD,x,y),Self.SELF);
//				//add update to UI
//			}
//			put(new Tuple("readyUI"),Self.SELF);
		}
	}
	
	public String[][] getMap(){
		Template tp = new Template(
				new FormalTemplateField(String.class),
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class));
		
		LinkedList<Tuple> list = queryAll(tp);
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
		System.out.println("" + board.length + ":" + board[0].length);
		for(Tuple tu : list){
			if(tu != null){
				String type = tu.getElementAt(String.class, 0);
				int x = tu.getElementAt(Integer.class, 1);
				int y = tu.getElementAt(Integer.class, 2);
				board[x-bounds[1]][y-bounds[3]] = type;
			}
		}
		for(int y = 0; y<bounds[2]-bounds[3]+1; y++){
			for(int x = 0; x<bounds[0]-bounds[1]+1; x++){
				board[x][y] = (board[x][y] == null) ? Empty.type : board[x][y];
			}
		}
		return board;
	}
}



import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.Self;

import map.*;

public class PoC {
	static int LB=0;
	static int UB=10;
	static int SIZE=UB-LB;
	static int MIDDLE=((UB-LB)/2);
	static TupleSpace ts=new TupleSpace();
	
	public static final FormalTemplateField ANYINTEGER = new FormalTemplateField(Integer.class);
	public static final FormalTemplateField ANYSTRING = new FormalTemplateField(String.class);
	
	
	// The main method will create some nodes, tuple spaces and agents
	public static void main(String[] argv) {
		// We create here a single node
		Node node = new Node("goD",ts);
		
		//We initialize the board and print it 
		//init
		init(ts);
		
		//We create a single drone
		Agent drone = new Drone("drone");
		
		// We add agent to the node
		node.addAgent(drone);
		
		// We start the node
		node.start();
	}
	
	private static void init2 (TupleSpace ts){
		Map map = new Map();
		
	}
	
	private static void init(TupleSpace ts) {
		//creating fields for SIZE x SIZE grid
		/*
		 * Types:
		 * A: air
		 * G: gold
		 * W: wood
		 * B: base
		 * D: drone
		 * 3rd information is field information
		 * 4th information is field information known by drone
		 */
		
		for (int i=LB;i<UB;i++) {
			for (int j=LB;j<UB;j++) {
				String res=".";
				
				//We replace air with gold in field with 2% chance
				if (twoPercent()) res="G";
				
				//We replace air with tree in field with 2% chance
				else if (twoPercent()) res="T";
				
				//Spawn base in middle
				//Spawn drone just north of base
				if (j==MIDDLE) {
					if (i==1 | i==2 | i==3) {
						ts.put((new Tuple(i,j,"G","U")));
						continue;					}
					if (i==MIDDLE) { //put base field in tuple space
						ts.put((new Tuple(i,j,"B","B")));
						continue;
					}
					else if (i==MIDDLE-1) { //put drone field in tuple space
						ts.put((new Tuple(i,j,"D","D")));
						continue;
					}
				}
				
				//put air/resource field in tuplespace
				ts.put(new Tuple (i,j,res,"U"));
			}
		}
	}
	
	private static void printDevineBoard (TupleSpace ts){
		String[][] boardKnown=new String[SIZE][SIZE];
		String[][] boardDrone=new String[SIZE][SIZE];
		
		Template t=new Template(
				ANYINTEGER,
				ANYINTEGER,	
				ANYSTRING,
				ANYSTRING
			);
			LinkedList<Tuple> ls=ts.queryAll(t);
			
			for(Tuple tup:ls) {
				int x=(int) tup.getElementAt(0);
				int y=(int) tup.getElementAt(1);
				String res=(String) tup.getElementAt(2);
				String droneKnowledge=(String) tup.getElementAt(3);
				boardDrone[x][y]=droneKnowledge;
				boardKnown[x][y]=res;
			}
		
			for (int i=LB;i<UB;i++){
				for (int j=LB;j<UB;j++) {
					//System.out.print(boardKnown[i][j]);
					System.out.printf("%-2s", boardKnown[i][j]);
				}
				
				System.out.print("    ");
				
				for (int j=LB;j<UB;j++) {
					//System.out.print(boardDrone[i][j]);
					System.out.printf("%-2s", boardDrone[i][j]);
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
			
	}
	
	
	
	
	//printing board with "god knowledge"
	private static void printBoard (TupleSpace ts) {
		String[][] board=new String[SIZE][SIZE];
		
		Template t=new Template(
			ANYINTEGER,
			ANYINTEGER,	
			ANYSTRING,
			ANYSTRING
		);
		LinkedList<Tuple> ls=ts.queryAll(t);
		
		for(Tuple tup:ls) {
			int x=(int) tup.getElementAt(0);
			int y=(int) tup.getElementAt(1);
			String res=(String) tup.getElementAt(2);
			board[x][y]=res;
		}
		
		for (int i=LB;i<UB;i++){
			for (int j=LB;j<UB;j++) {
				System.out.print(board[i][j]);
			}
			
			System.out.println();
		}
		System.out.println();
	}
	
	//printing board with drone knowledge
	private static void printKnownBoard (TupleSpace ts) {
		String[][] board=new String[SIZE][SIZE];
		
		Template t=new Template(
			ANYINTEGER,
			ANYINTEGER,	
			ANYSTRING,
			ANYSTRING
		);
		LinkedList<Tuple> ls=ts.queryAll(t);
		
		for(Tuple tup:ls) {
			int x=(int) tup.getElementAt(0);
			int y=(int) tup.getElementAt(1);
			String res=(String) tup.getElementAt(3);
			board[x][y]=res;
		}
		
		for (int i=LB;i<UB;i++){
			for (int j=LB;j<UB;j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static boolean twoPercent () {
		if (Math.random()*100<2) return true;
		return false;
	}

	public static class Drone extends Agent {
		// This constructor records the name of the agent
		public Drone(String name) {
			super(name);
			
		}

		// This is the function invoked when the agent starts running in a node
		@Override
		protected void doRun() {
			Scanner scanner=new Scanner(System.in);
			
			
			explore();
			printDevineBoard(ts);
			
<<<<<<< HEAD
			scanner.nextLine();
			
			move();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			
			move();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			move();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			move2();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			move2();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			move2();
			explore();
			printDevineBoard(ts);
			
			System.exit(0);
=======
			int dir = scanner.nextInt();
			while(true){
				moveMultipleDirecitons(dir);
				explore();
				printDevineBoard(ts);
				dir = scanner.nextInt();
			}
>>>>>>> refs/remotes/origin/joepadde
		}
		
		private void explore () {
			try {
				Template t=new Template(
						ANYINTEGER,
						ANYINTEGER,	
						new ActualTemplateField("D"),
						ANYSTRING
				);
				Tuple tup=queryp(t);
				
				int i=(int) tup.getElementAt(0);
				int j=(int) tup.getElementAt(1);
				
				//up
				t=new Template (
					new ActualTemplateField(i-1),
					new ActualTemplateField(j),
					ANYSTRING,
					ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//down 
				t=new Template (
						new ActualTemplateField(i+1),
						new ActualTemplateField(j),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//right
				t=new Template (
						new ActualTemplateField(i),
						new ActualTemplateField(j+1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//left
				t=new Template (
						new ActualTemplateField(i),
						new ActualTemplateField(j-1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//upright
				t=new Template (
						new ActualTemplateField(i-1),
						new ActualTemplateField(j+1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//upleft
				t=new Template (
						new ActualTemplateField(i-1),
						new ActualTemplateField(j-1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//downleft
				t=new Template (
						new ActualTemplateField(i+1),
						new ActualTemplateField(j-1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
				//downright
				t=new Template (
						new ActualTemplateField(i+1),
						new ActualTemplateField(j+1),
						ANYSTRING,
						ANYSTRING
				);
				tup=getp(t);
				if (((String)tup.getElementAt(3)).equals("U")) {
					put(new Tuple(tup.getElementAt(0),tup.getElementAt(1),tup.getElementAt(2),tup.getElementAt(2)),Self.SELF);
				}
				else {
					put(tup,Self.SELF);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		private void move() {
			
			try{
				Template t=new Template(
						ANYINTEGER,
						ANYINTEGER,	
						new ActualTemplateField("D"),
						ANYSTRING
					);
				Tuple tup;
				tup = get(t,Self.SELF); 
				
				int i;
				int j;
				
				i=(int) tup.getElementAt(0);
				j= (int) tup.getElementAt(1);
				
				//put "." in drones former spot
				put(new Tuple(i,j,".","."), Self.SELF);
				
				t=new Template(
						new ActualTemplateField(i-1),
						new ActualTemplateField(j),	
						ANYSTRING,
						ANYSTRING
				);
				
				tup = get(t,Self.SELF); 
				
				put(new Tuple(i-1,j,"D","D"), Self.SELF);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		private void move2() {
			try{
				Template t=new Template(
						ANYINTEGER,
						ANYINTEGER,	
						new ActualTemplateField("D"),
						ANYSTRING
					);
				Tuple tup;
				tup = get(t,Self.SELF); 
				
				int i;
				int j;
				
				i=(int) tup.getElementAt(0);
				j= (int) tup.getElementAt(1);
				
				//put "A" in drones former spot
				put(new Tuple(i,j,".","."), Self.SELF);
				
				t=new Template(
						new ActualTemplateField(i+1),
						new ActualTemplateField(j),	
						ANYSTRING,
						ANYSTRING
				);
				
				tup = get(t,Self.SELF); 
				
				put(new Tuple(i+1,j,"D","D"), Self.SELF);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		private void moveMultipleDirecitons(int dir) {

			try{
				//Template for Drone position. 
				Template t=new Template(
						ANYINTEGER,
						ANYINTEGER,	
						new ActualTemplateField("D"),
						ANYSTRING
					);
				
				//gets tuple of drone position
				Tuple tup;
				tup = get(t,Self.SELF); 
				
				int i;
				int j;
				
				//sets int i and j to current position of drone
				i=(int) tup.getElementAt(0);
				j= (int) tup.getElementAt(1);
				
				//Sets i or j to the right direction, switches on int dir. See method getDirection.
				int[] arr = getDirection(dir,i,j); 
				
				//put "." in drones former spot, unless it's the base
				if(i==MIDDLE && j==MIDDLE){
					put(new Tuple(i,j,"B","B"), Self.SELF);
				}
				else{
					put(new Tuple(i,j,".","."), Self.SELF);
				}
				
				//gets template for where to put drone
				t=getDirectionTemplate(arr);
				

				tup = get(t,Self.SELF); 
				
				//puts new position of drone to the map
				put(new Tuple(arr[0],arr[1],"D","D"), Self.SELF);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static Template getDirectionTemplate(int[] arr){
		
		Template t=new Template(
				new ActualTemplateField(arr[0]),
				new ActualTemplateField(arr[1]),	
				ANYSTRING,
				ANYSTRING
		);
		
		
		return t;
		
	}
	
	private static int[] getDirection(int dir, int i, int j){
		switch(dir){
			case 0  : i=i-1;
					  break; //move up
					  
			case 1  : i=i+1;
					  break; //move down
					  
			case 2  : j=j+1;
					  break; //move right
					  
			case 3  : j=j-1;
					  break; //move left
					  
			default : i=i;
					  j=j;
					  break; //default for security
		}
		
		return new int[]{i,j};
	}
	
	
}
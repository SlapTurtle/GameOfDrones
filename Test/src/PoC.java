

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

public class PoC {
	static int LB=0;
	static int UB=10;
	static int SIZE=UB-LB;
	static int MIDDLE=((UB-LB)/2);
	static TupleSpace ts=new TupleSpace();
	
	
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
					if (i==1) {
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
				new FormalTemplateField(Integer.class),
				new FormalTemplateField(Integer.class),	
				new FormalTemplateField(String.class),
				new FormalTemplateField(String.class)
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
			new FormalTemplateField(Integer.class),
			new FormalTemplateField(Integer.class),	
			new FormalTemplateField(String.class),
			new FormalTemplateField(String.class)
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
			new FormalTemplateField(Integer.class),
			new FormalTemplateField(Integer.class),	
			new FormalTemplateField(String.class),
			new FormalTemplateField(String.class)
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
			
			//initial explore
		}

		// This is the function invoked when the agent starts running in a node
		@Override
		protected void doRun() {
			Scanner scanner=new Scanner(System.in);
			
			explore();
			printDevineBoard(ts);
			
			scanner.nextLine();
			
			move();
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			
			moveMultipleDirecitons(0);
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			moveMultipleDirecitons(0);
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			moveMultipleDirecitons(3);
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			moveMultipleDirecitons(3);
			explore();
			printDevineBoard(ts);
			scanner.nextLine();
			moveMultipleDirecitons(1);
			explore();
			printDevineBoard(ts);
			moveMultipleDirecitons(1);
			explore();
			printDevineBoard(ts);
			moveMultipleDirecitons(2);
			explore();
			printDevineBoard(ts);
			moveMultipleDirecitons(2);
			explore();
			printDevineBoard(ts);
		}
		
		private void explore () {
			try {
				Template t=new Template(
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class),	
						new ActualTemplateField("D"),
						new FormalTemplateField(String.class)
				);
				Tuple tup=queryp(t);
				
				int i=(int) tup.getElementAt(0);
				int j=(int) tup.getElementAt(1);
				
				//up
				t=new Template (
					new ActualTemplateField(i-1),
					new ActualTemplateField(j),
					new FormalTemplateField(String.class),
					new FormalTemplateField(String.class)
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
						new ActualTemplateField(i-1),
						new ActualTemplateField(j),
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class),	
						new ActualTemplateField("D"),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class),	
						new ActualTemplateField("D"),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
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
						new FormalTemplateField(Integer.class),
						new FormalTemplateField(Integer.class),	
						new ActualTemplateField("D"),
						new FormalTemplateField(String.class)
					);
				Tuple tup;
				tup = get(t,Self.SELF); 
				
				int i;
				int j;
				
				i=(int) tup.getElementAt(0);
				j= (int) tup.getElementAt(1);
				
				
				
				//put "." in drones former spot
				put(new Tuple(i,j,".","."), Self.SELF);
				if(dir==0){ //move up
				t=new Template(
						new ActualTemplateField(i+1),
						new ActualTemplateField(j),	
						new FormalTemplateField(String.class),
						new FormalTemplateField(String.class)
				);
				}
				if(dir==1){ //move down
					t=new Template(
							new ActualTemplateField(i-1),
							new ActualTemplateField(j),	
							new FormalTemplateField(String.class),
							new FormalTemplateField(String.class)
					);
				}
				if(dir==2){ //move right
					t=new Template(
							new ActualTemplateField(i),
							new ActualTemplateField(j+1),	
							new FormalTemplateField(String.class),
							new FormalTemplateField(String.class)
					);
				}
				if(dir==3){ //move left
					t=new Template(
							new ActualTemplateField(i),
							new ActualTemplateField(j-1),	
							new FormalTemplateField(String.class),
							new FormalTemplateField(String.class)
					);
				}
				
				tup = get(t,Self.SELF); 
				switch(dir){
					case 0: put(new Tuple(i+1,j,"D","D"), Self.SELF); //move up
							break;				
					case 1: put(new Tuple(i-1,j,"D","D"), Self.SELF); //move down
							break;
					case 2: put(new Tuple(i,j+1,"D","D"), Self.SELF); //move right
							break;
					case 3: put(new Tuple(i,j-1,"D","D"), Self.SELF); //move left
							break;
					default : put(new Tuple(i,j,"D","D"), Self.SELF); //do nothing
							break;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
}
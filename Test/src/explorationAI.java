//<<<<<<< HEAD
///*
// *  @Author: Benjamin Lam, s153486
// */
//public class explorationAI {
//	
//	int radius;
//
//	public explorationAI(int r){
//		this.radius=r;
//		
//		//gets closest unknown point.
//		int[] arr= getPointInUnknown();
//		int i=arr[0];
//		int j=arr[1];
//		
//		Position p = new Position(5,5); 
//		
//		findNextPoint(p);
//		
//	}
//	
//	private void findNextPoint(Position p) {
//		// init of hypotenuse
//		int hyp;
//		//find hypotenus by using Pythagoras
//		hyp = getHypotenuse(p);
//		
//		//check if go up or go left
//		checkDirection(hyp, this.radius);
//		
//	}
//
//
//	private int getDirection(Position p, int radius){
//		int r;
//		
//		
//		
//		return r;
//	}
//	
//	private int pythagoras(Position p){
//		int a=p.getX();
//		int b=p.getY();
//		
//		r= (int) Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
//	}
//	
//	
//	
//	private int[] getPointInUnknown(){
//		int[] a = new int[2];
//		return a;
//	}
//	/*private Position[] getFieldsToCheck(int dir, Position p){
//		Position[] arr = new Position[2];
//		switch(dir){
//			case 0 : arr[0]= new Position(p.getX()-1, p.getY());
//					 arr[1]= new Position(p.getX(), p.getY()+1);
//					 break;
//			
//			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
//			 		 arr[1]= new Position(p.getX(), p.getY()-1);
//					 break;
//			case 2 : arr[0]= new Position(p.getX()-1, p.getY());
//			 		 arr[1]= new Position(p.getX(), p.getY()+1);
//					 break;
//			
//			case 3 : arr[0]= new Position(p.getX()-1, p.getY());
//			 		 arr[1]= new Position(p.getX(), p.getY()+1);
//					 break;
//	
//			default :
//					break;
//		}
//		
//		return arr;
//	}
//	*/
//
//	
//	
//}
//=======
/////*
//// *  @Author: Benjamin Lam, s153486
//// */
////public class explorationAI {
////	
////	int radius;
////
////	public explorationAI(int r){
////		this.radius=r;
////		
////		//gets closest unknown point.
////		int[] arr= getPointInUnknown();
////		int i=arr[0];
////		int j=arr[1];
////		
////		Position p = new Position(5,5); 
////		
////		findNextPoint(p);
////		
////	}
////	
////	private void findNextPoint(Position p) {
////		// init of hypotenuse
////		int hyp;
////		//find hypotenus by using Pythagoras
////		hyp = getHypotenuse(p);
////		
////		//check if go up or go left
////		checkDirection(hyp, this.radius);
////		
////	}
////
////
////	private int getDirection(Position p, int radius){
////		int r;
////		
////		
////		
////		return r;
////	}
////	
////	private int pythagoras(Position p){
////		int a=p.getX();
////		int b=p.getY();
////		
////		r= (int) Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
////	}
////	
////	
////	
////	private int[] getPointInUnknown(){
////		int[] a = new int[2];
////		return a;
////	}
////	/*private Position[] getFieldsToCheck(int dir, Position p){
////		Position[] arr = new Position[2];
////		switch(dir){
////			case 0 : arr[0]= new Position(p.getX()-1, p.getY());
////					 arr[1]= new Position(p.getX(), p.getY()+1);
////					 break;
////			
////			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
////			 		 arr[1]= new Position(p.getX(), p.getY()-1);
////					 break;
////			case 2 : arr[0]= new Position(p.getX()-1, p.getY());
////			 		 arr[1]= new Position(p.getX(), p.getY()+1);
////					 break;
////			
////			case 3 : arr[0]= new Position(p.getX()-1, p.getY());
////			 		 arr[1]= new Position(p.getX(), p.getY()+1);
////					 break;
////	
////			default :
////					break;
////		}
////		
////		return arr;
////	}
////	*/
////
////	
////	
////
////	private int getDir(int i, int j){
////		int dir=0;
////		
////		//switch()
////		
////		return dir;
////	}
////	
////	// TODO intital move process. Move drone to this destination. Use move in a specific direction.
////	private void initMove(int i, int j) {
////		// TODO Auto-generated method stub
////		
////	}
////
////}
//>>>>>>> refs/remotes/origin/joepadde

//import util.Position;
//
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
//		radius=6;
//	}
//	
//	public static void main(String [] args){
//		int radius=6;
//		Position d = new Position(6,0);
//		Position[] posArr = getFieldsToCheck(d);
//		Position p1 = new Position(5,1); 
//		Position p2 = new Position(4,0);
//		int dir = 0;
//		
//		System.out.println("d" + d);
//		System.out.println("up" + p1);
//		System.out.println("left" + p2);
//		
//		for(int i = 0; i<48; i++){
//			posArr = getFieldsToCheck(d);
//			dir = getDirFromRadius(posArr[1],posArr[0], radius);
//			System.out.println(dir);
//			moveDrone(d,dir);
//			System.out.println("d" + d);
//			System.out.println("up" + posArr[1]);
//			System.out.println("left" + posArr[0]);
//		}
//		
//	}
//	
//	
//	/**
//	 * Help function to test
//	 * @param p
//	 * @param dir
//	 * @return
//	 */
//	private static Position moveFieldsToCheck(Position p, int dir){
//		if(dir<0){
//			p.setX(p.getX()-1);
//		}
//		else{
//			p.setY(p.getY()+1);
//		}		
//		return p; 
//	}
//	/**
//	 * Help function to move drone
//	 * @param d
//	 * @param dir
//	 * @return
//	 */
//	private static Position moveDrone(Position d, int dir){
//		int q = getQuadrant(d);
//		switch(q){
//			case 1 : if(dir<0) interQuadrantMoving(d,d.getX()-1,d.getY());
//					 else      interQuadrantMoving(d,d.getX(),d.getY()+1);
//					 break;
//			
//			case 2 : if(dir<0) interQuadrantMoving(d,d.getX(),d.getY()-1);
//					 else      interQuadrantMoving(d,d.getX()-1,d.getY());
//					 break;
//			case 3 : if(dir<0) interQuadrantMoving(d,d.getX()+1,d.getY());
//					 else      interQuadrantMoving(d,d.getX(),d.getY()-1);
//					 break;
//			case 4 : if(dir<0) interQuadrantMoving(d,d.getX(),d.getY()+1);
//					 else      interQuadrantMoving(d,d.getX()+1,d.getY());
//			         break;
//			default: interQuadrantMoving(d,d.getX(),d.getY());
//					 break;
//		}		
//		return d; 
//	}
//
//	/**
//	 * help function for moveDrone
//	 * @param p
//	 * @param x
//	 * @param y
//	 * @return Position
//	 */
//	private static Position interQuadrantMoving(Position p, int x, int y){
//		p.setX(x);
//		p.setY(y);
//		return p;
//	}
//	
//	/**
//	 * Gets the direction using {@link #pythagoras(Position)}method
//	 * @param p1
//	 * @param p2
//	 * @param radius
//	 * @return int of what direction to move. 1 for up, -1 for left
//	 */
//	private static int getDirFromRadius(Position p1, Position p2, int radius){
//		int dir=0;
//		double c1 = 0;
//		double c2 = 0;
//		c1 = pythagoras(p1);
//		c2 = pythagoras(p2);
//		
//		if(Math.abs(radius-c1)>Math.abs(radius-c2)){
//			dir=-1;
//		}
//		else{
//			dir=1;
//		}
//		return dir;
//	}
//	
//	
//	/**
//	 * Takes p.x and p.y and returns radius using pythagoras
//	 * @param p
//	 * @return squared c in pythagoras
//	 */
//	private static double pythagoras(Position p){
//		int a=p.getX();
//		int b=p.getY();
//		
//		return Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
//	}
//	
//	
//	
//	private int[] getPointInUnknown(){
//		int[] a = new int[2];
//		return a;
//	}
//	
//	/**
//	 * 
//	 * @param dir
//	 * @param p
//	 * @return
//	 */
//	private static Position[] getFieldsToCheck(Position p){
//		Position[] arr = new Position[2];
//		int q; 
//		q=getQuadrant(p);
//		switch(q){
//			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
//					 arr[1]= new Position(p.getX(), p.getY()+1);
//					 break;
//			
//			case 2 : arr[0]= new Position(p.getX(), p.getY()-1);
//			 		 arr[1]= new Position(p.getX()-1, p.getY());
//					 break;
//			case 3 : arr[0]= new Position(p.getX()+1, p.getY());
//			 		 arr[1]= new Position(p.getX(), p.getY()-1);
//					 break;
//			
//			case 4 : arr[0]= new Position(p.getX(), p.getY()+1);
//			 		 arr[1]= new Position(p.getX()+1, p.getY());
//					 break;
//	
//			default :
//					break;
//		}
//		
//		return arr;
//	}
//
//	private static int getQuadrant(Position p) {
//		int q=0;
//		
//		if(p.getX()>0 && p.getY()>=0) q=1;
//		else if(p.getX()<=0 && p.getY()>0) q=2;
//		else if(p.getX()<0 && p.getY()<=0) q=3;
//		else if(p.getX()>=0 && p.getY()<0) q=4;
//		
//		return q;
//	}
//	
//
//	
//	
//}

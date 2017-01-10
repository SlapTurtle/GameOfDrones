/*
 *  @Author: Benjamin Lam, s153486
 */
public class explorationAI {
	
	int radius;

	public explorationAI(int r){
		this.radius=r;
		
		//gets closest unknown point.
		int[] arr= getPointInUnknown();
		int i=arr[0];
		int j=arr[1];
		
		radius=5;
	}
	
	public static void main(String [] args){
		int radius=5;
		Position d = new Position(5,0);
		Position[] posArr = new Position[] {new Position(5,1),new Position(4,0)};
		Position p1 = new Position(5,1); 
		Position p2 = new Position(4,0);
		int dir = 0;
		
		System.out.println("d" + d);
		System.out.println("up" + p1);
		System.out.println("left" + p2);
		
		for(int i = 0; i<20; i++){
			posArr = getFieldsToCheck(d);
			dir = getDirFromRadius(posArr[0],posArr[1], radius);
			System.out.println(dir);
			moveDrone(d,dir);
			System.out.println("d" + d);
			System.out.println("up" + posArr[0]);
			System.out.println("left" + posArr[1]);
		}
		
	}
	
	
	/**
	 * Help function to test
	 * @param p
	 * @param dir
	 * @return
	 */
	private static Position moveFieldsToCheck(Position p, int dir){
		if(dir<0){
			p.setX(p.getX()-1);
		}
		else{
			p.setY(p.getY()+1);
		}		
		return p; 
	}
	/**
	 * Help function to move drone
	 * @param d
	 * @param dir
	 * @return
	 */
	private static Position moveDrone(Position d, int dir){
		if(dir<0){
			d.setX(d.getX()-1);
		}
		else{
			d.setY(d.getY()+1);
		}		
		return d; 
	}

	
	/**
	 * Gets the direction using {@link #pythagoras(Position)}method
	 * @param p1
	 * @param p2
	 * @param radius
	 * @return int of what direction to move. 1 for up, -1 for left
	 */
	private static int getDirFromRadius(Position p1, Position p2, int radius){
		int dir=0;
		double c1 = 0;
		double c2 = 0;
		c1 = pythagoras(p1);
		c2 = pythagoras(p2);
		
		if(Math.abs(radius-c1)>Math.abs(radius-c2)){
			dir=-1;
		}
		else{
			dir=1;
		}
		return dir;
	}
	
	
	/**
	 * Takes p.x and p.y and returns radius using pythagoras
	 * @param p
	 * @return squared c in pythagoras
	 */
	private static double pythagoras(Position p){
		int a=p.getX();
		int b=p.getY();
		
		return Math.sqrt(Math.pow(a,2)+Math.pow(b, 2));
	}
	
	
	
	private int[] getPointInUnknown(){
		int[] a = new int[2];
		return a;
	}
	
	/**
	 * 
	 * @param dir
	 * @param p
	 * @return
	 */
	private static Position[] getFieldsToCheck(Position p){
		Position[] arr = new Position[2];
		int q; 
		q=getQuadrant(p);
		switch(q){
			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
					 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 2 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()-1);
					 break;
			case 3 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 4 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
	
			default :
					break;
		}
		
		return arr;
	}

	private static int getQuadrant(Position p) {
		int q=0;
		
		if(p.getX()>0){
			// x>0 and y>0 --> 1.st q
			if(p.getY()>0){
				q=1;
			}
			// x>0 and y<0 --> 4.th q
			if(p.getY()<0){
				q=4;
			}
		}
		//x<0
		else{
			// x<0 and y>0 --> 2.sn q
			if(p.getY()>0){
				q=2;
			}
			// x<0 and y<0 --> 3.rd q
			if(p.getY()<0){
				q=3;
			}
		}
		
		return q;
	}
	

	
	
}

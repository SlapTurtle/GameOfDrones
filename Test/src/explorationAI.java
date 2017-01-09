/*
 *  @Author: Benjamin Lam, s153486
 */
public class explorationAI {
	

	public explorationAI(){
		
		//gets closest unknown point.
		int[] arr= getPointInUnknown();
		int i=arr[0];
		int j=arr[1];
		
		//move drone to this destination (i,j)
		initMove(i,j);
		
		
		// get dir
		
	}
	
	private int[] getPointInUnknown(){
		int[] a = new int[2];
		return a;
	}

	
	
	private Position[] getFieldsToCheck(int dir, Position p){
		Position[] arr = new Position[2];
		switch(dir){
			case 0 : arr[0]= new Position(p.getX()-1, p.getY());
					 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 1 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()-1);
					 break;
			case 2 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
			
			case 3 : arr[0]= new Position(p.getX()-1, p.getY());
			 		 arr[1]= new Position(p.getX(), p.getY()+1);
					 break;
	
			default :
					break;
		}
		
		return arr;
	}
	
	// TODO intital move process. Move drone to this destination. Use move in a specific direction.
	private void initMove(int i, int j) {
		// TODO Auto-generated method stub
		
	}
}

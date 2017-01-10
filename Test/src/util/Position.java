package util;

public class Position {
	private int x;
	private int y;
	
	//Constructor
	public Position(int x, int y){
		this.setX(x);
		this.setY(y);
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setXandY(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public String toString(){
		return "["+this.x + "," + this.y + "]";
	}
	
	/*
	 * DOT EQUALS 
	 */
	public boolean equalsX(int newX){
		return (this.x==newX);
	}
	
	public boolean equalsY(int newY){
		return (this.y==newY);
	}

	


}

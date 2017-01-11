package util;

import java.awt.Point;

public class Position {
	public int x;
	public int y;
	
	//Constructors
	public Position(int x, int y){
		this.setX(x);
		this.setY(y);
	}
	
	public Position(){
		this.x=0;
		this.y=0;
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
	
	public void move(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	
	/*
	 * DOT EQUALS 
	 */
	public boolean equals(Position p){
		return (this.x==p.x && this.y==p.y);
	}
	
	public boolean equalsX(int newX){
		return (this.x==newX);
	}
	
	public boolean equalsY(int newY){
		return (this.y==newY);
	}

	/*
	 * Translators
	 */

	public String toString(){
		return "["+this.x + "," + this.y + "]";
	}
	
	public Point toPoint(){
		return new Point(this.x,this.y);
	}

}

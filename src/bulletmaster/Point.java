package bulletmaster;

import java.awt.Rectangle;

public class Point {
	//This is my version of the java point class
	//This uses ints instead of doubles
	private int x;
	private int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void move(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dX, int dY){
		this.x = this.x+dX;
		this.y = this.y+dY;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isInside(Rectangle rect){
		if (this.x>rect.getMaxX()||this.x<rect.getMinX()||this.y>rect.getMaxY()||this.y<rect.getMinY()){
			return false;
		} else return true;
		
	}
	
	
	
}

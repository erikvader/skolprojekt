package main;

import java.awt.Point;

public class Ormdel{

	private int x, y;//, oldX, oldY;

	public Ormdel(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Ormdel(Ormdel o){
		this.x = o.getX();
		this.y = o.getY();
	}
	
	public void move(int x, int y){
		//this.oldX = this.x;
		//this.oldY = this.y;
		this.x = x;
		this.y = y;
	}
	
	public void move(Ormdel o){
		this.move(o.getX(), o.getY());
	}
	
	public Point getPoint(){
		return new Point(x, y);
	}
	
	/*
	public void move(Ormdel o){
		this.move(o.getOldX(), o.getOldY());
		
	}
	*/
	/*
	public int getOldX() {
		return oldX;
	}

	public int getOldY() {
		return oldY;
	}
	*/
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
	
}

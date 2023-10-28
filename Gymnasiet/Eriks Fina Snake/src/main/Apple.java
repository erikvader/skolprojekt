package main;

public class Apple{
	
	private int x, y;

	public Apple(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Apple(Apple a){
		this.x = a.getX();
		this.y = a.getY();
	}

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
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
	
}

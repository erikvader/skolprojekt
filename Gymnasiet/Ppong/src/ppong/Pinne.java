package ppong;

import java.awt.Color;
import java.awt.Graphics;

public class Pinne extends GameObject{

	protected boolean leftSide;
	
	protected boolean up, down;
	
	/**
	 * g�r en pinne
	 * @param name: unikt namn
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param leftSide: vilken sida
	 */
	public Pinne(String name, double x, double y, double width, double height, Color color, boolean leftSide) {
		super(name, x, y, width, height, color);
		this.leftSide = leftSide;
		speed = 3;
	}
	
	/**
	 * uppdaterar den
	 */
	@Override
	public void tick() {
		if(up){
			vy = -speed;
		}else if(down){
			vy = speed;
		}else{
			vy = 0;
		}
		
		//flytta sig
		super.tick();
		
		//check bounds
		checkBounds();
	}
	
	/**
	 * kollar och korrigerar ifall pinnen krockar med undre och �vre v�ggarna
	 */
	protected void checkBounds(){
		if(x < 0){
			x = 0;
			vx = 0;
		}
		if((x+width) > PongGame.WIDTH){
			x = PongGame.WIDTH-width;
			vx = 0;
		}
		
		if(y < 0){
			y = 0;
			vy = 0;
		}
		if((y+height) > PongGame.HEIGHT){
			y = PongGame.HEIGHT-height;
			vy = 0;
		}
	}
	
	/**
	 * s�ger att vi nu �ker ned�t
	 * @param b
	 */
	public void setDown(boolean b){
		down = b;
	}
	
	/**
	 * s�ger att vi �ker upp�t
	 * @param b
	 */
	public void setUp(boolean b){
		up = b;
	}
	
	/**
	 * stannar pinnen
	 */
	public void stop(){
		vy = 0;
	}

	/**
	 * returnar true ifall pinnen �r p� v�nster sida.
	 * @return
	 */
	public boolean isLeftSide(){return leftSide;}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
}

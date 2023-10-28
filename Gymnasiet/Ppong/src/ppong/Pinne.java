package ppong;

import java.awt.Color;
import java.awt.Graphics;

public class Pinne extends GameObject{

	protected boolean leftSide;
	
	protected boolean up, down;
	
	/**
	 * gör en pinne
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
	 * kollar och korrigerar ifall pinnen krockar med undre och övre väggarna
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
	 * säger att vi nu åker nedåt
	 * @param b
	 */
	public void setDown(boolean b){
		down = b;
	}
	
	/**
	 * säger att vi åker uppåt
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
	 * returnar true ifall pinnen är på vänster sida.
	 * @return
	 */
	public boolean isLeftSide(){return leftSide;}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
}

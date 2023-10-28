package game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;


public class Spring extends MapObject{

	private BufferedImage[] sprites;
	private int currentFrame = 0;
	private boolean bouncing = false;
	private int bounceCounter = 0;
	private double speed = -6;
	
	public Spring(TileMap tm) {
		super(tm);
		
		width = 30;
		height = 30;
		cheight = 13;
		cwidth = 9;
		
		sprites = Content.spring1; //0: full, 1: half, 2: down
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
	}
	
	public void setSpeed(double s){speed = s;}
	public double getSpeed(){return speed;}
	
	public boolean isBouncing(){return bouncing;}
	public void setBouncing(){bouncing = true;}
	
	public void draw(Graphics2D g){
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}
	
	public void update(Section s){
		if(!isInsideSection(s)) return;
		
		if(bouncing){
			bounceCounter++;
			if(bounceCounter == 1){
				currentFrame = 2;
			}else if(bounceCounter == 21){
				currentFrame = 0;
			}else if(bounceCounter == 120){
				bounceCounter = 0;
				bouncing = false;
				currentFrame = 0;
			}else if(bounceCounter % 15 == 0){
				currentFrame = currentFrame == 0 ? 1:0;
			}
			
		}
		animation.setFrame(currentFrame);
	}

}

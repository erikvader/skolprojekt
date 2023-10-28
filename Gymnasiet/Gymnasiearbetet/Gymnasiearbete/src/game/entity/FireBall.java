package game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(TileMap tm, boolean right) {
		
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		// load sprites
		sprites = Content.FireBall[0];
		hitSprites = Content.FireBall[1];
			
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(4);
		
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(4);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update(Section s) {
		if(!isInsideSection(s)) return;
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) { //checktilemapcollision sätter dx till 0 om man krockar i något (för dy också)
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}



















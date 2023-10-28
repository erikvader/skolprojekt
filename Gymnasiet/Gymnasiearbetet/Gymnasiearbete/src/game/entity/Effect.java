package game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.handlers.Content;
import game.tileMap.TileMap;

public class Effect extends MapObject{ 
	
	protected BufferedImage[] sprites;
	
	protected boolean remove;
	
	public Effect(TileMap tm, int x, int y, BufferedImage[] sprites, int delay){
		super(tm);
		
		this.x = x;
		this.y = y;
		
		this.sprites = sprites;
		height = sprites[0].getHeight();
		width = sprites[0].getWidth();
		
		animation = new Animation();
		animation.setFrames(this.sprites);
		animation.setDelay(delay);
		
		facingRight = true;
		
	}
	
	public Effect(TileMap tm, int x, int y) {
		this(tm, x, y, Content.Explosion1, 4);
		
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
	
}


















package game.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.handlers.Content;
import game.tileMap.TileMap;

public class Scenery extends MapObject{
	
	private boolean show = true;
	private float curOpacity = 1, stepOpacity = 0.03f;
	
	public Scenery(TileMap tm, double x, double y, String sprite, int width, int height) {
		super(tm);
		
		this.x = x;
		this.y = y;
		
		BufferedImage[] sprites = Content.loadRow(sprite, width, height, 0, 1);
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		this.width = width;
		this.height = height;
		cwidth = width;
		cheight = height;
		facingRight = true;
		animation.update();

	}
	
	public void hide(){
		show = false;
	}
	
	public void show(){
		show = true;
	}
	
	public void fadeIn(){
		curOpacity = 0;
		stepOpacity = Math.abs(stepOpacity);
		show();
	}
	
	public void fadeOut(){
		curOpacity = 1;
		stepOpacity = -Math.abs(stepOpacity);
	}
	
	private void updateOpacity(){
		curOpacity += stepOpacity;
		if(curOpacity > 1){
			curOpacity = 1;
		}else if(curOpacity < 0){
			curOpacity = 0;
			hide();
		}
	}

	@Override
	public void draw(Graphics2D g){
		if(!show) return;
		setMapPosition();
		if(notOnScreen()) return;
		animation.update();
		updateOpacity();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, curOpacity));
		super.draw(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}

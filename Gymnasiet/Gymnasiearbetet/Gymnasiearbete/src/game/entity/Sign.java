package game.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.handlers.Content;
import game.hud.Conversation;
import game.tileMap.TileMap;


public class Sign extends MapObject {
	
	private Conversation conversation;

	public Sign(TileMap tm, Conversation c) {
		this(tm, c, Content.sign1);
	}
	
	public Sign(TileMap tm, Conversation c, BufferedImage[] frames){
		super(tm);
		
		conversation = c;
		
		height = width = 30;
		cheight = cwidth = 30;
		
		animation = new Animation();
		animation.setFrames(frames);
		animation.setDelay(-1);
	}
	
	public Conversation getDialog(){
		return conversation;
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}

}

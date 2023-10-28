package game.entity.level4_boss.fackverk;

import java.awt.Graphics2D;

import game.entity.Animation;
import game.entity.MapObject;
import game.handlers.Content;
import game.tileMap.TileMap;

public class TransitionBoss extends MapObject{

	private Animation ligger;/*, pratar, river;*/
	
	public TransitionBoss(TileMap tm){
		super(tm);
		
		facingRight = true;
		
		ligger = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/transition_boss.png", 140, 121, 0, 1), -1);
		//pratar = new Animation(Content.enkelnot, -1);
		//river = new Animation(Content.alsenholtFire, -1);
		
		cwidth = cheight = 1;
		
		setObjectAnimation(ligger);
		
	}
	
	/*
	public void setPratar(){
		setObjectAnimation(pratar);
	}
	
	public void setRiver(){
		setObjectAnimation(river);
	}
	*/
	
	public void update(){
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}
}

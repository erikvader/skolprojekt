package game.entity.items;

import java.awt.Graphics2D;

import game.entity.MapObject;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class ThrowableItem extends MapObject{

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	protected boolean remove = false;
	
	protected double xFriction;
	protected double moveSpeedUp;
	//movespeed är left och right
	
	protected boolean beingHeld = false;
	protected boolean wasThrown = false;
	
	public ThrowableItem(TileMap tm) {
		super(tm);
	}
	
	public boolean plzRemove(){return remove;}
	
	public void shootAngle(double angle){
		dx = Math.cos(Math.toRadians(angle))*moveSpeed;
		dy = -Math.sin(Math.toRadians(angle))*moveSpeed;
	}
	
	public void launch(int dir){
		wasThrown = true;
		//falling = true;
		dy = dx = 0;
		if(dir == UP){
			dy = moveSpeedUp;
		}else if(dir == DOWN){
			dy = 0;
		}else if(dir == LEFT){
			dx = -moveSpeed;
		}else if(dir == RIGHT){
			dx = moveSpeed;
		}
	}
	
	protected void getNextPosition(){
		// movement
		if(dx < 0){
			dx += xFriction;
			if(dx > 0){
				dx = 0;
			}
		}else if(dx > 0){
			dx -= xFriction;
			if(dx < 0){
				dx = 0;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void setBeingHeld(boolean b){beingHeld = b;}
	public boolean isBeingHeld(){return beingHeld;}
	
	public void remove(){remove = true;}
	
	public void update(Section s){
		if(s.fellOutside(this)) {
			remove = true;
			return;
		}
		/*
		if(!isInsideSection(s)){
			remove = true;
			return;
		}
		*/
		
		// update position
		if(!isBeingHeld()){
			getNextPosition();
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		}
		
		// update animation
		animation.update();
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}

}

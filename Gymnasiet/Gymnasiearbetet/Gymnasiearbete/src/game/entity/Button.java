package game.entity;

import java.awt.Graphics2D;

import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Button extends MapObject{

	private boolean pressed = false;
	
	private Animation[] animations;
	
	public static final int UNPRESSED = 0;
	public static final int PRESSED = 1;
	
	private int curState = UNPRESSED;
	
	private boolean remove = false;
	
	public Button(TileMap tm) {
		super(tm);
		
		width = 25;
		height = 27;
		
		cheight = 25;
		cwidth = 27;
		
		animations = new Animation[2];
		animations[0] = new Animation(Content.button1[0], -1);
		animations[1] = new Animation(Content.button1[1], -1);
		
		setObjectAnimation(animations[curState]);
		
	}
	
	private void setCorrectAnimation(){
		if(pressed){
			if(curState != PRESSED){
				setObjectAnimation(animations[PRESSED]);
				curState = PRESSED;
			}
		}else{
			if(curState != UNPRESSED){
				setObjectAnimation(animations[UNPRESSED]);
				curState = UNPRESSED;
			}
		}
	}
	
	public boolean plzRemove(){return remove;}
	
	public void setPressed(boolean pressed){
		this.pressed = pressed;
	}
	
	public void hit(){
		setPressed(!pressed);
	}
	
	public boolean isPressed(){return pressed;}
	
	public void update(Section s){
		setCorrectAnimation();
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}

}

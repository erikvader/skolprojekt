package game.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.handlers.Content;
import game.tileMap.TileMap;


public class Door extends MapObject{

	private int sourceSection;
	private int target;
	
	public static final int MODE_SECTION = 0; //borde heta MODE_DOOR
	public static final int MODE_STATE = 1;
	private int switchMode = MODE_SECTION;
	
	//animations
	private Animation closed;
	private Animation opened;
	private Animation lock;
	
	public static final int DOOR1 = 0;
	public static final int DOOR_SPIRAL = 1;
	
	private boolean locked = false;
	
	private int score = -1;
	private char[] scores = {'F', 'E', 'D', 'C', 'B', 'A'};
	private Font scoreFont = new Font("Arial", Font.BOLD, 20);
	
	public Door(TileMap tm, int source, int doorSprites) {
		super(tm);
		
		sourceSection = source;
		
		animation = new Animation();
		if(doorSprites == DOOR1)
			setSpritesDoor1();
		else if(doorSprites == DOOR_SPIRAL)
			setSpritesSpiral();
		
		setClosed();
		
		width = 90;
		height = 60;
		cwidth = 34;
		cheight = 45;
		
		facingRight = true;
	}
	
	public Door(TileMap tm, int source){
		this(tm, source, DOOR1);
	}
	
	private void setSpritesSpiral(){
		closed = new Animation(Content.doorSpiral[0], -1);
		opened = new Animation(Content.doorSpiral[1], -1);
		lock = new Animation(Content.doorSpiral[2], -1);
	}
	
	private void setSpritesDoor1(){
		closed = new Animation(Content.door1[0], -1);
		opened = new Animation(Content.door1[1], -1);
		lock = new Animation(Content.door1[2], -1);
	}
	
	public int getTarget(){return target;}
	public int getMode(){return switchMode;}
	public int getSourceSection(){return sourceSection;}
	public boolean isLocked(){return locked;}
	
	public void setDoorTarget(int doorIndex){
		switchMode = MODE_SECTION;
		target = doorIndex;
	}
	
	public void setStateTarget(int stateIndex){
		switchMode = MODE_STATE;
		target = stateIndex;
	}
	
	public void setOpened(){
		animation = opened;
	}
	
	public void setClosed(){
		animation = closed;
	}
	
	public void setLocked(){
		animation = lock;
		locked = true;
	}
	
	public void unlock(){
		locked = false;
		setClosed();
	}
	
	public void setScore(int s){
		score = s;
	}
	
	public void update(){
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
		
		if(score != -1){
			g.setColor(Color.BLACK);
			g.setFont(scoreFont);
			g.drawString(Character.toString(scores[score]), (int)(xmap+x-7), (int)(ymap+y-13));
		}
	}

}

package game.entity.enemies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Spider extends Enemy{
	
	private int counter = 0;
	private int pauseCounter = 60;
	private double stringY;
	private double originY;
	private double targetY;
	private boolean attacking, playerInRange;
	
	private Player p;
	private Rectangle detectionBox;
	
	private double moveUpSpeed, moveDownSpeed;

	public Spider(TileMap tm, Player p, ArrayList<Effect> ep) {
		super(tm, ep);
		
		damage = 1;
		health = maxHealth = 1;
		
		width = height = 30;
		cwidth = 28;
		cheight = 20;
		
		BufferedImage[] sprites = Content.Spider;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		facingRight = true;
		
		moveDownSpeed = 2;
		moveUpSpeed = 1;
		
		this.p = p;
	}

	@Override
	public boolean attacks(Player p) {
		return intersects(p);
	}
	
	public void setPosesTile(double x, double height, int originY){
		this.x = x;
		this.originY = (originY)*30+cheight/2;
		this.targetY = height+this.originY;
		this.stringY = (originY)*30;
		setPosition(x, this.originY);
	}
	
	/*
	public void setPoses(double x, double targetY, double originY){
		this.targetY = targetY;
		this.stringY = originY;
		setPosition(x, stringY);
	}
	*/
	
	public void setDetectionbox(int width, int height){
		detectionBox = new Rectangle((int)(x-width/2), (int)originY, width, height);
	}
	
	@Override
	public void getNextPosition() {
		if(attacking){
			if(y < targetY){
				dy = moveDownSpeed;
			}else{
				dy = 0;
				if(!playerInRange){
					counter++;
					if(counter >= pauseCounter){
						counter = 0;
						attacking = false;
					}
				}else{
					counter = 0;
				}
			}
		}else{
			if(y > originY){
				dy = -moveUpSpeed;
			}else{
				dy = 0;
			}
		}
	}
	
	private void playerUpdates(){
		if(p.intersects(detectionBox)){
			attacking = true;
			playerInRange = true;
		}else{
			playerInRange = false;
		}
	}
	
	@Override
	public void update(Section s) {
		if(!isInsideSection(s)) return;
		setMapPosition();
		if(notOnScreen()) return;
		
		playerUpdates();
		
		//update pos
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			flinchTimer++;
			if(flinchTimer > 24) {
				flinching = false;
				flinchTimer = 0;
			}
		}
		
		// update animation
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(4));
		g.drawLine((int)(x+xmap), (int)(y+ymap), (int)(x+xmap), (int)(stringY+ymap));
		
		super.draw(g);
	}

}

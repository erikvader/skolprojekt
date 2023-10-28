package game.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.Tile;
import game.tileMap.TileMap;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Slugger(TileMap tm, ArrayList<Effect> ep) {
		
		super(tm, ep);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 1;
		damage = 1;
		
		// load sprites
		sprites = Content.Slugger[0];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(18);
		
		right = false;
		left = true;
		facingRight = false;
		
	}
	
	@Override
	public void update(Section s) {
		if(!isInsideSection(s)) return;
		setMapPosition();
		if(notOnScreen()) return;
		
		// update position
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
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		//check if about to fall
		if(left){
			if(tileMap.getType((int)((y+cheight/2+3)/30), (int)((x-cwidth/2)/30)) != Tile.BLOCKED){
				right = true;
				left = false;
				facingRight = true;
			}
		}else if(right){
			if(tileMap.getType((int)((y+cheight/2+3)/30), (int)((x+cwidth/2)/30)) != Tile.BLOCKED){
				right = false;
				left = true;
				facingRight = false;
			}
		}
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		if(notOnScreen()) return; 
		
		super.draw(g);
		
	}

	@Override
	public boolean attacks(Player p) {
		return intersects(p);
	}
	
}












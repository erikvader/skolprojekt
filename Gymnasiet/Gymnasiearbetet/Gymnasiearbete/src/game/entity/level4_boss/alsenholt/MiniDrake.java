package game.entity.level4_boss.alsenholt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.entity.Animation;
import game.entity.Player;
import game.entity.enemies.Enemy;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class MiniDrake extends Enemy {
	
	private BufferedImage[] sprites;
	private boolean shooting = false;
	
	public MiniDrake(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 2;
		maxSpeed = 1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 28;
		cheight = 24;
		
		health = maxHealth = 1;
		damage = 1;
		
		// load sprites
		sprites = Content.alsenholtMini;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(8);
		
		left = true;
		facingRight = false;
		
	}
	
	@Override
	public void update(Section s) {
		if(!isInsideSection(s)) return;
		
		// update position
		if(!shooting){
			getNextPosition();
			checkTileMapCollision();
		}else{
			xtemp = x+dx;
			ytemp = y+dy;
		}
		setPosition(xtemp, ytemp);
		
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
		/*
		//check if about to fall
		if(!falling){
			if(left){
				if(tileMap.getType((int)((y+cheight/2+3)/30), (int)((x-cwidth/2)/30)) == Tile.NORMAL){
					right = true;
					left = false;
					facingRight = true;
				}
			}else if(right){
				if(tileMap.getType((int)((y+cheight/2+3)/30), (int)((x+cwidth/2)/30)) == Tile.NORMAL){
					right = false;
					left = true;
					facingRight = false;
				}
			}
		}
		*/
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
		if(shooting){
			return false;
		}else{
			return intersects(p);
		}
	}
	
	@Override
	public void hit(int damage) { 
		health -= damage;
		if(health < 0) health = 0;
	}
	
	public int getHealth(){return health;}
	
	public void shoot(double x, double y){
		//kopa av EnemyProjectile:shootTarget()
		double dist = Math.sqrt(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2));
		double skala = dist / moveSpeed;
		
		dx = (x-this.x)/skala;
		dy = (y-this.y)/skala;
		
		shooting = true;
	}
	
	public boolean getShooting(){return shooting;}
	public void setDead(){dead = true;}
}












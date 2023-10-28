package game.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.items.CalculatorItem;
import game.entity.items.ThrowableItem;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.Tile;
import game.tileMap.TileMap;

public class Calculator extends Enemy {
	
	private BufferedImage[] sprites;
	
	private ArrayList<EnemyProjectile> ep;
	private ArrayList<ThrowableItem> ti;
	private ArrayList<Enemy> enemies;
	
	private int shootCounterTarget = 200;
	private int shootCounter = 0;
	
	public Calculator(TileMap tm, ArrayList<EnemyProjectile> ep, ArrayList<Effect> e, ArrayList<ThrowableItem> ti, ArrayList<Enemy> enemies) {
		
		super(tm, e);
		
		this.ti = ti;
		this.ep = ep;
		this.enemies = enemies;
		
		moveSpeed = 0.5;
		maxSpeed = 0.5;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 18;
		cheight = 21;
		
		health = maxHealth = 2;
		damage = 1;
		
		// load sprites
		sprites = Content.Calculator;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(10);
		
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
			if(flinchTimer > 10) {
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
		
		//shoot or not
		shootCounter++;
		if(shootCounter % shootCounterTarget == 0){
			shootCounter = 0;
			BlackProjectile bp = new BlackProjectile(tileMap);
			double a = facingRight ? 0 : 180;
			bp.shootAngle(a);
			bp.setPosition(x, y);
			ep.add(bp);
		}
		
	}
	
	public void draw(Graphics2D g) {
		if(flinching){ 
			if(flinchTimer/6 % 2 == 0){ 
				return;
			}
		}
		
		setMapPosition();
		
		if(notOnScreen()) return; 
		
		super.draw(g);
		
	}

	@Override
	public boolean attacks(Player p) {
		return intersects(p);
	}
	
	@Override
	public void die() {
		CalculatorItem ci = new CalculatorItem(tileMap, enemies, effects);
		ci.setPosition(x, y);
		ci.shootAngle(90d);
		ti.add(ci);
		
		super.die();
	}
	
}












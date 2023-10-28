package game.entity.level2_boss;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class TauProjectile extends EnemyProjectile{

	private boolean dirRight;
	private ArrayList<EnemyProjectile> enemyProjectiles;
	
	protected TauProjectile(TileMap tm, boolean right, ArrayList<EnemyProjectile> ep) {
		super(tm);
		
		enemyProjectiles = ep;
		
		dirRight = right;
		facingRight = true;
		
		damage = 1;
		
		//moveSpeed = 2;
		stopSpeed = 0.02;
		fallSpeed = 0.2;
		maxFallSpeed = 2.5;
		
		cwidth = 13; //15
		cheight = 13; //15
		width = 15;
		height = 15;
		
		sprites = new Animation(Content.tau, -1);
		hitSprites = new Animation();
		
		setObjectAnimation(sprites);
		
		//sprites.setEmpty();
		hitSprites.setEmpty();
		//animation.setEmpty();
		
		
	}

	@Override
	public boolean hits(MapObject mo) {
		return mo.intersects(getRectangle());
	}
	
	@Override
	protected void getNextPosition(){
		//x
		if(dirRight){
			if(dx < stopSpeed){
				dx = 0;
			}else{
				dx -= stopSpeed;
			}
		}else{
			if(dx > -stopSpeed){
				dx = 0;
			}else{
				dx += stopSpeed;
			}
		}
		
		//y
		dy += fallSpeed;
		if(dy > maxFallSpeed)
			dy = maxFallSpeed;
	}
	
	public void shoot(double speed){
		dx = speed;
		if(!dirRight) dx *= -1;
	}
	
	public void spawnPies(){
		PIProjectile p = new PIProjectile(tileMap, false);
		p.setPosition(x, y);
		p.shoot(2);
		p.shootUp(-4.5);
		enemyProjectiles.add(p);
		
		p = new PIProjectile(tileMap, true);
		p.setPosition(x, y);
		p.shoot(2);
		p.shootUp(-4.5);
		enemyProjectiles.add(p);
	}
	
	@Override
	public void update(Section s) {
		if(!isInsideSection(s)) {
			remove = true;
			return;
		}

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(collidedWithMap && !hit) { 
			setHit();
			spawnPies();
		}
		
		animation.update();
		
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		/*
		setMapPosition();
		
		g.setColor(Color.BLACK);
		g.fillRect((int)(x+xmap-width/2.0), (int)(y+ymap-height/2.0), width, height);
		*/
	}
	
}

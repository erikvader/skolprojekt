package game.entity.level4_boss.alsenholt;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class GroundFire extends EnemyProjectile{
	
	public GroundFire(TileMap tm, boolean right){
		super(tm);
		
		facingRight = right;
		
		damage = 1;
		
		moveSpeed = 2;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		dy = 4;
		
		sprites = new Animation(Content.alsenholtFire, 5);
		hitSprites = new Animation(Content.FireBall[1], 5);
		
		setObjectAnimation(sprites);
		cwidth = 20; //23
		cheight = 26;
	}
	
	public void update(Section s) {
		if(!isInsideSection(s)) return;
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) { 
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}

	@Override
	public boolean hits(MapObject mo) {
		if(hit) return false;
		boolean r = intersects(mo);
		//if(r) setHit();
		return r;
	}
	
}

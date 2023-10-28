package game.entity.level1_boss.alsenholt;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class ProjectileFire extends EnemyProjectile{
	
	public ProjectileFire(TileMap tm, double speed){
		super(tm);
		
		damage = 1;
		moveSpeed = speed;
		
		sprites = new Animation(Content.FireBall[0], 5);
		//hitSprites = new AnimationSet(Content.FireBall[1], 5);
		
		setObjectAnimation(sprites);
		cwidth = 12; //14
		cheight = 12; //14
	}
	
	public void update(Section s) {
		if(!isInsideSection(s)) {
			remove = true;
			return;
		}

		/*
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) { 
			setHit();
		}
		*/
		
		xtemp = x+dx;
		ytemp = y+dy;
		setPosition(xtemp, ytemp);
		
		animation.update();
		/*
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		*/
	}

	@Override
	public boolean hits(MapObject mo) {
		boolean r = intersects(mo);
		//if(r) setHit();
		return r;
	}
	
}

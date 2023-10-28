package game.entity.enemies;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class BlackProjectile extends EnemyProjectile{

	protected BlackProjectile(TileMap tm) {
		super(tm);
		
		damage = 1;
		
		moveSpeed = 2;
		
		sprites = new Animation(Content.blackParticle, -1);
		hitSprites = new Animation(Content.Explosion3, 6);
		setObjectAnimation(sprites);
		
		cwidth = cheight = 5;
		
	}
	
	public void update(Section s) {
		if(!isInsideSection(s)) {
			remove = true;
			return;
		}

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
	public void setHit() {
		super.setHit();
	}

	@Override
	public boolean hits(MapObject mo) {
		if(hit) return false;
		boolean h = intersects(mo);
		if(h) setHit();
		return h;
	}

}

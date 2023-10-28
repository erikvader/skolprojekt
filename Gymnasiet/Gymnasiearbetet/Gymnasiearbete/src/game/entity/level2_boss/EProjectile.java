package game.entity.level2_boss;

import java.awt.Graphics2D;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class EProjectile extends EnemyProjectile{

	protected EProjectile(TileMap tm) {
		super(tm);
		
		damage = 1;
		
		moveSpeed = 3;
		
		cwidth = 13;//15
		cheight = 13;//15
		width = 15;
		height = 15;
		
		facingRight = true;
		
		sprites = new Animation(Content.e, -1);
		hitSprites = new Animation();
		
		setObjectAnimation(sprites);
		
		//sprites.setEmpty();
		hitSprites.setEmpty();
		//animation.setEmpty();
	}

	@Override
	public boolean hits(MapObject mo) {
		if(hit) return false;
		boolean h = intersects(mo);
		//if(h) setHit();
		return h;
	}

	@Override
	public void update(Section s) {
		if(!isInsideSection(s)) {
			remove = true;
			return;
		}

		//checkTileMapCollision();
		xtemp = x + dx;
		ytemp = y + dy;
		setPosition(xtemp, ytemp);
		
		/*
		if(collidedWithMap && !hit) { 
			setHit();
		}
		*/
		
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

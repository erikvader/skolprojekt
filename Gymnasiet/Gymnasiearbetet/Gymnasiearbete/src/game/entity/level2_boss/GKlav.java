package game.entity.level2_boss;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class GKlav extends EnemyProjectile{

	
	protected GKlav(TileMap tm, boolean rightDir) {
		super(tm);
		
		damage = 1;
		moveSpeed = 2;
		
		width = 60;
		height = 60;
		cheight = 56; //60
		cwidth = 23; //27
		
		facingRight = true;
		
		sprites = new Animation(Content.gklav, -1);
		hitSprites = new Animation();
		hitSprites.setEmpty();
		
		setObjectAnimation(sprites);
		
		y = 9*30-30;
		
		if(rightDir){
			x = -27/2.0;
			dx = moveSpeed;
		}else{
			x = 16*30+27/2.0;
			dx = -moveSpeed;
		}
		
		
	}
	
	@Override
	public void update(Section s) {
		animation.update();
		
		setPosition(x+dx, y);
		
		//if utanför
		if(x < -27/2.0 || x > 16*30+27/2.0){
			remove = true;
		}
	}

	@Override
	public boolean hits(MapObject mo) {
		return intersects(mo);
	}

}

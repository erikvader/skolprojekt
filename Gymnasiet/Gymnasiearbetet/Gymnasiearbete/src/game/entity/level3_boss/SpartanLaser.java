package game.entity.level3_boss;

import game.entity.enemies.enemyProjectile.EnemyProjectileLaser;
import game.tileMap.TileMap;

public class SpartanLaser extends EnemyProjectileLaser{

	public SpartanLaser(TileMap tm) {
		super(tm, 60, 600);
	}
	
	@Override
	protected void constructPath() {
		double tri = 30;
		path.reset();
		path.moveTo(0, 0);
		path.lineTo(-bredd/2, tri);
		path.lineTo(-bredd/2, tri+length);
		path.lineTo(bredd/2, tri+length);
		path.lineTo(bredd/2, tri);
		path.closePath();
		
	}

}

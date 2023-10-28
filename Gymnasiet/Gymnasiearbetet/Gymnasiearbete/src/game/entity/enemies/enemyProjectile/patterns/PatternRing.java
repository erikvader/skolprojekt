package game.entity.enemies.enemyProjectile.patterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.TileMap;

public class PatternRing extends PatternBase{
	
	public PatternRing(TileMap tm) {
		super(tm);
	}

	public PatternRing(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternRing(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternRing(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	
	public void shootRing(double startAngle, double angleInterval, int antal){
		double step = angleInterval / antal;
		EnemyProjectile e;
		for(int i = 0; i < antal; i++){
			e = getProj();
			e.shootAngle(startAngle+i*step);
			projs.add(e);
		}
		
		ep.addAll(projs);
		
		begin = true;
	}

}

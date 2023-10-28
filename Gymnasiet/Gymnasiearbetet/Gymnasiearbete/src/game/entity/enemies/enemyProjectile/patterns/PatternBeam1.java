package game.entity.enemies.enemyProjectile.patterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.TileMap;

/*
 * Skjuter en "beam" från en punkt. varje projektil skjuts samtidigt
 * men har olika hastigher.
 */
public class PatternBeam1 extends PatternBase{

	public PatternBeam1(TileMap tm) {
		super(tm);
	}

	public PatternBeam1(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternBeam1(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternBeam1(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	
	public void shootBeam(double tx, double ty, int antal, double startSpeed, double stepSpeed){
		EnemyProjectile e;
		for(int i = 0; i < antal; i++){
			e = getProj();
			//e.setPosition(x, y);
			e.setSpeed(startSpeed+i*stepSpeed);
			e.shootTarget(tx, ty);
			projs.add(e);
		}
		
		begin = true;
		ep.addAll(projs);
	}

}

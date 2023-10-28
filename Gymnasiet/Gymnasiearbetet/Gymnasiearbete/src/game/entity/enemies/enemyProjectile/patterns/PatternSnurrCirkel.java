package game.entity.enemies.enemyProjectile.patterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileCircle;
import game.tileMap.TileMap;

public class PatternSnurrCirkel extends PatternBase{
	
	public PatternSnurrCirkel(TileMap tm) {
		super(tm);
	}

	public PatternSnurrCirkel(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternSnurrCirkel(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternSnurrCirkel(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	
	@Override
	protected EnemyProjectileCircle getProj() {
		EnemyProjectileCircle e = new EnemyProjectileCircle(this);
		e.setPosition(x, y);
		return e;
	}
	
	public void shootSnurrCirkel(int antal, double radius, double vinkelSpeed){
		double step = 360 / antal;
		EnemyProjectileCircle e;
		for(int i = 0; i < antal; i++){
			e = getProj();
			e.shootCircle(radius, step*i);
			e.setVinkelSpeed(vinkelSpeed);
			projs.add(e);
		}
		
		ep.addAll(projs);
		
		begin = true;
	}
	
	public void shootTarget(double xi, double yi){
		for(EnemyProjectile e : projs)
			e.shootTarget(xi, yi);
	}
	
}

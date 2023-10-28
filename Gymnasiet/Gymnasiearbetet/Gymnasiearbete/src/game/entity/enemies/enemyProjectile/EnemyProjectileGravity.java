package game.entity.enemies.enemyProjectile;

import java.awt.image.BufferedImage;

import game.tileMap.TileMap;

public class EnemyProjectileGravity extends EnemyProjectile{

	//friction
	private double frictionX = 0;
	private boolean gravity = false;
	
	public EnemyProjectileGravity(TileMap tm) {
		super(tm);
	}
	
	public EnemyProjectileGravity(EnemyProjectile ep) {
		super(ep);
	}

	public EnemyProjectileGravity(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
	}

	public EnemyProjectileGravity(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
	}

	public void setGravity(double fallSpeed, double maxFallSpeed){
		this.fallSpeed = fallSpeed;
		this.maxFallSpeed = maxFallSpeed;
		gravity = true;
	}
	
	public void setFrictionX(double frictX){
		frictionX = frictX;
	}
	
	@Override
	protected void getNextPosition() {
		if(gravity){
			dy += fallSpeed;
			if(dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
		
		if(dx > 0){
			dx -= frictionX;
			if(dx < 0)
				dx = 0;
		}else if(dx < 0){
			dx += frictionX;
			if(dx > 0)
				dx = 0;
		}
	}

}

package game.entity.enemies.enemyProjectile;

import java.awt.image.BufferedImage;

import game.tileMap.TileMap;

public class EnemyProjectileAccel extends EnemyProjectile{
	
	private double dir, acc;
	private boolean constant, relative;

	public EnemyProjectileAccel(TileMap tm) {
		super(tm);
	}
	
	public EnemyProjectileAccel(EnemyProjectile ep) {
		super(ep);
	}

	public EnemyProjectileAccel(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
	}

	public EnemyProjectileAccel(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
	}
	
	/**
	 * accelerera �t samma absoluta h�ll hela tiden
	 * <br>
	 * om maxSpeed �r 0 s� finns det ingen hastighetsgr�ns.
	 * 
	 * @param dir
	 */
	public void setAccelerationConstant(double dir, double acc, double maxSpeed){
		this.maxSpeed = maxSpeed;
		this.dir = dir;
		this.acc = acc;
		constant = true;
	}
	
	/**
	 * accelererar relativt till den nuvarande f�rdriktningen
	 * <br>
	 * om maxSpeed �r 0 s� finns det ingen hastighetsgr�ns.
	 * 
	 * @param dir
	 */
	public void setAccelerationRelative(double dir, double acc, double maxSpeed){
		this.maxSpeed = maxSpeed;
		this.dir = dir;
		this.acc = acc;
		relative = true;
	}
	
	@Override
	protected void getNextPosition() {
		if(constant){
			if(getSpeed() < Math.abs(maxSpeed) || maxSpeed == 0){
				accelerateDir(dir, acc);
			}
		}else if(relative){
			double cur = getDir();
			if(getSpeed() < Math.abs(maxSpeed) || maxSpeed == 0){
				accelerateDir(cur+dir, acc);
			}
		}
	}
	
	@Override
	protected void checkBounce() {
		//kanske �ndra s� att den bouncar r�tt
		super.checkBounce();
	}

}

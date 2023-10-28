package game.entity.enemies.enemyProjectile;

import java.awt.image.BufferedImage;

import game.tileMap.TileMap;

public class EnemyProjectileWobbly extends EnemyProjectile{
	
	//grejer
	private boolean wobbly = false;
	private double limitG, limitS;
	private double curDir, step;
	private boolean increase;

	public EnemyProjectileWobbly(TileMap tm) {
		super(tm);
	}
	
	public EnemyProjectileWobbly(EnemyProjectile ep) {
		super(ep);
	}

	public EnemyProjectileWobbly(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
	}

	public EnemyProjectileWobbly(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
	}
	
	public void shootWobbly(double limitGreater, double limitSmaller, double step, boolean increase){
		wobbly = true;
		limitG = limitGreater;
		limitS = limitSmaller;
		this.step = step;
		this.increase = increase;
		
		if(increase){
			curDir = limitS-step;
		}else{
			curDir = limitG+step;
		}
		
	}
	
	public void shootWobbly2(double dir, double offset, double step, boolean increase){
		shootWobbly(dir+offset, dir-offset, step, increase);
	}
	
	public void stopWobbly(){wobbly = false;}

	@Override
	protected void getNextPosition() {
		if(wobbly){
			//ska byta håll eller inte
			if(increase){
				curDir += step;
				if(curDir >= limitG){
					increase = !increase;
					//curDir = limitG
				}
			}else{
				curDir -= step;
				if(curDir <= limitS){
					increase = !increase;
					//curDir = limitS
				}
			}
			
			//sätt hastigheten rätt
			shootAngle(curDir);
			
		}
	}

}

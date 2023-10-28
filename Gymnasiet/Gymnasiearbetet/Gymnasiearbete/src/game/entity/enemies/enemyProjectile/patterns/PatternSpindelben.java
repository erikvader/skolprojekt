package game.entity.enemies.enemyProjectile.patterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileGravity;
import game.tileMap.TileMap;

public class PatternSpindelben extends PatternBase{

	public PatternSpindelben(TileMap tm) {
		super(tm);
	}

	public PatternSpindelben(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternSpindelben(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternSpindelben(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	
	public void shootSpindel(double[] xPunkter, double yPunkt, double fallSpeed, double maxFallSpeed, int xtime){
		//hitta y
		//double yAcc = (2*((yPunkt-y)-e.getDY()*time))/(time*(time-1));
		
		for(int i = 0; i < xPunkter.length; i++){
			EnemyProjectileGravity e = getGrav();
			e.setPosition(x, y);
			
			//hitta x
			double deltax = xPunkter[i]-x;
			double a = -(2*deltax)/(xtime*xtime);
			double vNoll = -a*(xtime+0.5); //var -0.5 innan, då behövdes -a
			
			e.setFrictionX(Math.abs(a));
			e.setGravity(fallSpeed, maxFallSpeed);
			e.setVector(vNoll /*-a*/, -moveSpeed); //"-a" är där för att EP uppdaterar hastigheten precis innan första förflyttningen
			
			projs.add(e);
		}
		
		begin = true;
		ep.addAll(projs);
	}
	
	private EnemyProjectileGravity getGrav(){
		return new EnemyProjectileGravity(this);
	}

}

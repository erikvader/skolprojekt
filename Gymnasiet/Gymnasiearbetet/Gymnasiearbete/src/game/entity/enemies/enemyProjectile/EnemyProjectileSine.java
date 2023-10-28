package game.entity.enemies.enemyProjectile;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import game.tileMap.TileMap;

public class EnemyProjectileSine extends EnemyProjectile{

	private boolean sine = false;
	private double sineAngle, distOnLine = 0; 
	private double sineConstant; // konstanten framför, om negativ så åker den åt andra hållet
	private double sinePeriod; //uträknad periodkonstant
	private Point2D.Double orgCord;
	
	public EnemyProjectileSine(TileMap tm) {
		super(tm);
	}

	public EnemyProjectileSine(EnemyProjectile ep) {
		super(ep);
	}

	public EnemyProjectileSine(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
	}

	public EnemyProjectileSine(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
	}
	
	public void setOrgPosition(double x, double y) {
		super.setPosition(x, y);
		orgCord = new Point2D.Double(x, y);
	}
	
	/**
	 * sinePeriod i pixlar
	 * 
	 * @param angle
	 * @param sineConstant
	 * @param sinePeriod
	 */
	public void shootSine(double angle, double sineConstant, double sinePeriod){
		sine = true;
		sineAngle = Math.toRadians(angle);
		this.sineConstant = sineConstant;
		this.sinePeriod = (2*Math.PI)/sinePeriod;
	}
	
	public void stopSine(){sine = false;}
	
	@Override
	protected void getNextPosition() {
		if(sine){
			distOnLine += moveSpeed;
			double distFromLine = sineConstant*-Math.sin(sinePeriod*distOnLine);
			double x1 = distFromLine*-Math.sin(sineAngle);
			double y1 = distFromLine*Math.cos(sineAngle);
			double x2 = distOnLine*Math.cos(sineAngle);
			double y2 = distOnLine*-Math.sin(sineAngle);
			
			x = orgCord.getX()+x2-x1;
			y = orgCord.getY()+y2+y1;
		}
		
		//System.out.println(x);
	}
	
	@Override
	protected void checkBounce() {
		//kanske ändra så att den bouncar rätt
		super.checkBounce();
	}

}

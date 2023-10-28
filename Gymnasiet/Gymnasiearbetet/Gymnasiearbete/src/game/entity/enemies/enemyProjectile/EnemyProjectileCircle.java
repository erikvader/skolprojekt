package game.entity.enemies.enemyProjectile;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import game.tileMap.TileMap;

public class EnemyProjectileCircle extends EnemyProjectile{
	
	private Point2D.Double orgCord;
	private boolean circle, expand;
	private double radius, vinkelHastighet, curVinkel, expandDist;
	private boolean /*vinkelSpeed,*/ constantSpeed, radiusUpdated;

	public EnemyProjectileCircle(TileMap tm) {
		super(tm);
	}
	
	public EnemyProjectileCircle(EnemyProjectile ep){
		super(ep);
	}

	public EnemyProjectileCircle(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
	}

	public EnemyProjectileCircle(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
	}
	
	public void shootCircle(double dist, double curVinkelDeg){
		circle = true;
		orgCord = new Point2D.Double(x, y);
		//setPosition(x, y);
		//this.clockwise = clockwise;
		setRadius(dist);
		this.curVinkel = Math.toRadians(curVinkelDeg);
		
	}
	
	public void setExpand(double dist){
		expand = true;
		expandDist = dist;
	}
	
	public void stopExpand(){expand = false;}
	
	//en fixerad längd av cirkeln
	public void setConstantSpeed(double speed){
		resetSpeeds();
		constantSpeed = true;
		moveSpeed = speed;
		vinkelHastighet = speed/getRadius();
	}
	
	//vinkeln i Grader
	public void setVinkelSpeed(double vinkel){
		resetSpeeds();
		//vinkelSpeed = true;
		vinkelHastighet = Math.toRadians(vinkel);
	}
	
	//period i ticks
	public void setVinkelSpeedPeriodtid(double period){
		setVinkelSpeed((360)/period);
	}
	
	private void resetSpeeds(){
		/*vinkelSpeed = */constantSpeed = false;
	}

	public void setRadius(double rad){
		radius = rad;
		radiusUpdated = true;
	}
	
	public double getRadius(){return radius;}
	public void stopCircle(){circle = false;}
	public void continueCircle(){circle = true;}
	
	public double calcRadius(){
		return Math.sqrt(Math.pow(x-orgCord.getX(), 2)+Math.pow(y-orgCord.getY(), 2));
	}
	
	@Override
	protected void getNextPosition() {
		if(circle){
			if(expand){
				setRadius(expandDist+radius);
			}
			
			if(constantSpeed && radiusUpdated){
				radiusUpdated = false;
				setConstantSpeed(moveSpeed);
			}
			
			curVinkel += vinkelHastighet;
			
			x = Math.cos(curVinkel)*radius + orgCord.getX();
			y = -Math.sin(curVinkel)*radius + orgCord.getY();
			
			orgCord.setLocation(orgCord.getX()+dx, orgCord.getY()+dy);
		}
	}

}

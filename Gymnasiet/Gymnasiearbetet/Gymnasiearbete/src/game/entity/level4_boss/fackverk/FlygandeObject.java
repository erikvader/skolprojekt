package game.entity.level4_boss.fackverk;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.Enemy;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.patterns.PatternSnurrCirkel;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class FlygandeObject extends Enemy{
	
	protected ArrayList<Effect> effects;
	protected ArrayList<EnemyProjectile> enemyProjectiles;
	protected Player player;
	
	protected Point restingPos;
	protected boolean isHome = false;
	
	//destionation
	protected boolean shootDestination = false;
	protected Point destination = new Point(0, 0);
	
	//floating
	private boolean floating = false;
	private double periodY, periodX;
	private int tick = 0;
	private double floatSpeed = 0.5;
	
	protected static EnemyProjectile cloneProj1;

	public FlygandeObject(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<Effect> ef) {
		super(tm);
		
		enemyProjectiles = ep;
		this.player = player;
		this.effects = ef;
		
		damage = 1;
		moveSpeed = 1;
		
		periodX = (Math.random()*0.06 + 0.07) * ((int)(Math.random()*2) == 0 ? 1 : -1); //0.06, 0.07
		periodY = (Math.random()*0.06 + 0.07) * ((int)(Math.random()*2) == 0 ? 1 : -1);
		
		cloneProj1 = getProj1();
		enemyProjectiles.remove(enemyProjectiles.size()-1); //ta bort den tomma som inte används
	}
	
	protected EnemyProjectile getProj1(){
		EnemyProjectile ep = new EnemyProjectile(tileMap, Content.laserBoll, -1, 14, 14, 2.4);
		//ep.setPosition(restingPos);
		ep.setPosition(-500, -500);
		ep.setHitOnPlayer = false;
		ep.checkCollisionWithMap = false;
		enemyProjectiles.add(ep);
		return ep;
	}
	
	protected EnemyProjectile getProj2(){
		EnemyProjectile ep = new EnemyProjectile(tileMap, Content.laserBollYellow, -1, 14, 14, 2.4);
		//ep.setPosition(restingPos);
		ep.setPosition(-500, -500);
		ep.setHitOnPlayer = false;
		ep.checkCollisionWithMap = false;
		enemyProjectiles.add(ep);
		return ep;
	}
	
	protected EnemyProjectile spawnBlixt(double x){
		EnemyProjectile ep = new EnemyProjectile(tileMap, Content.blixt, 6, 26, 270, 500);
		ep.setPosition(x, 11*30-135);
		ep.setHitOnPlayer = false;
		ep.setHitOnCollideMap = false;
		ep.checkCollisionWithMap = false;
		ep.removeIfOutsideSection = false;
		ep.shootDestination(x, (11+4)*30+15);
		ep.setWarning(Content.varning, -1, x, (11+9)*30-12.5, 100);
		ep.setExpiration(40);
		enemyProjectiles.add(ep);
		return ep;
	}
	
	protected EnemyProjectile spawnLaserV(double x, int delay, int expiration){
		EnemyProjectile ep = new EnemyProjectile(tileMap, Content.laserv, -1, 15, 270, 500);
		ep.setPosition(x, 11*30-135);
		ep.setHitOnPlayer = false;
		ep.setHitOnCollideMap = false;
		ep.checkCollisionWithMap = false;
		ep.removeIfOutsideSection = false;
		ep.shootDestination(x, (11+4)*30+15);
		ep.setWarning(Content.arrowsv, -1, x, (11+4)*30+15, delay);
		ep.setExpiration(expiration);
		enemyProjectiles.add(ep);
		return ep;
	}
	
	protected EnemyProjectile spawnLaserH(double y, int delay, int expiration){
		EnemyProjectile ep = new EnemyProjectile(tileMap, Content.laserh, -1, 480, 15, 500);
		ep.setPosition(17*30+10-240, y);
		ep.setHitOnPlayer = false;
		ep.setHitOnCollideMap = false;
		ep.checkCollisionWithMap = false;
		ep.removeIfOutsideSection = false;
		ep.shootDestination(17*30+10+240, y);
		ep.setWarning(Content.arrowsh, -1, 17*30+10+240, y, delay);
		ep.setExpiration(expiration);
		enemyProjectiles.add(ep);
		return ep;
	}
	
	protected EnemyProjectile spawnSnurrRing(){
		PatternSnurrCirkel psc //= new PatternSnurrCirkel(tileMap, Content.laserBoll, -1, 14, 14, 2.4, enemyProjectiles);
			= new PatternSnurrCirkel(cloneProj1, enemyProjectiles);
		psc.setHitOnPlayer = false;
		psc.checkCollisionWithMap = false;
		psc.setPosition(x, y);
		psc.shootSnurrCirkel(10, 20, 5);
		//psc.shootTarget(player.getx(), player.gety());
		enemyProjectiles.add(psc);
		return psc;
	}
	
	public void setFloat(boolean b){
		floating = b;
		tick = 0;
	}
	
	private void shootTarget(double x, double y){
		double dist = Math.sqrt(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2));
		double skala = dist / moveSpeed;
		
		dx = (x-this.x)/skala;
		dy = (y-this.y)/skala;
	}
	
	public void stopShootDestination(){
		shootDestination = false;
	}
	
	public void gotoDestination(double x, double y){
		isHome = false;
		shootTarget(x, y);
		shootDestination = true;
		destination.setLocation(x, y);
	}
	
	public void gotoDestination(Point p){
		gotoDestination(p.x, p.y);
	}
	
	public void gotoDestination(Point2D.Double p){
		gotoDestination(p.getX(), p.getY());
	}
	
	public void goHome(){
		gotoDestination(restingPos);
		isHome = true;
	}
	
	public boolean isGotoDestination(){return shootDestination;}
	
	protected void checkShootDestination(){
		if(!shootDestination) return;
		
		double distToDestination = Math.sqrt(Math.pow(destination.x-x, 2) + Math.pow(destination.y-y, 2));
		
		if(distToDestination <= 0.1){
			setPosition(destination);
			shootDestination = false;
			dx = dy = 0;
		}else if(distToDestination < moveSpeed){
			dx = destination.x-x;
			dy = destination.y-y;
		}
	}
	
	public void getNextPosition(){
		if(floating){
			tick++;
			dx = floatSpeed*Math.sin(tick*periodX);
			dy = floatSpeed*Math.sin(tick*periodY);
		}
	}
	
	@Override
	public void update(Section s) {
		getNextPosition();
		checkShootDestination();
		setPosition(x+dx, y+dy);
		
	}

	@Override
	public boolean attacks(Player p) {
		return intersects(p);
	}
	
	@Override
	public void hit(int damage) {
		super.hit(damage);
	}

	@Override
	public void die() {
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(notOnScreen()) return; 
		
		super.draw(g);
	}
	
}

package game.entity.enemies.enemyProjectile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import game.entity.Animation;
import game.entity.MapObject;
import game.handlers.Calc;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class EnemyProjectile extends MapObject {
	
	protected boolean hit;
	protected boolean remove;
	protected Animation sprites;
	protected Animation hitSprites;
	
	protected int damage;
	
	//flags
	public boolean setHitOnPlayer = true;
	public boolean setHitOnCollideMap = true;
	public boolean checkCollisionWithMap = true;
	public boolean removeIfOutsideSection = true;
	public boolean setBounce = false;
	
	protected int expiration = -1;
	
	protected boolean shootDestination = false;
	protected Point destination = new Point(0, 0);
	
	//bounce
	private double lastDX, lastDY;
	
	//warning
	private boolean warning = false;
	private double wdx, wdy;
	private int warningCounter;
	private Animation warningAnim;
	private double wx, wy;
	
	private Point2D.Double preSetPoint;
	
	public EnemyProjectile(TileMap tm) {
		
		super(tm);
		
		facingRight = true;
		
		animation = new Animation();
		animation.setEmpty();
		
		hitSprites = new Animation();
		hitSprites.setEmpty();
		
		preSetPoint = new Point2D.Double();
		
	}
	
	public EnemyProjectile(EnemyProjectile ep){
		this(ep.tileMap);
		
		this.sprites = new Animation(ep.sprites);
		this.hitSprites = new Animation(ep.hitSprites);
		this.cwidth = ep.cwidth;
		this.cheight = ep.cheight;
		this.moveSpeed = ep.moveSpeed;
		
		setObjectAnimation(this.sprites);
		
		this.damage = ep.damage;
		this.expiration = ep.expiration;
		this.setHitOnCollideMap = ep.setHitOnCollideMap;
		this.setHitOnPlayer = ep.setHitOnPlayer;
		this.checkCollisionWithMap = ep.checkCollisionWithMap;
		this.removeIfOutsideSection = ep.removeIfOutsideSection;
		this.setBounce = ep.setBounce;
	}
	
	public EnemyProjectile(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed){
		this(tm);
		//facingRight = true;
		//animation = new Animation();
		this.sprites = new Animation(sprites, delay);
		setObjectAnimation(this.sprites);
		
		this.moveSpeed = moveSpeed;
		this.cheight = cheight;
		this.cwidth = cwidth;
		
		damage = 1;
	}
	
	public EnemyProjectile(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed){
		this(tm, sprites, delay, cwidth, cheight, moveSpeed);
		this.hitSprites = new Animation(hitSprites, hitDelay);
	}
	
	public void shootAngle(double angle){
		dx = dy = 0;
		accelerateDir(angle, moveSpeed);
	}
	
	public void accelerateDir(double angle, double acc){
		dx += Math.cos(Math.toRadians(angle))*acc;
		dy += -Math.sin(Math.toRadians(angle))*acc;
	}
	
	public void shootTarget(double x, double y){
		double dist = Math.sqrt(Math.pow(x-this.x, 2)+Math.pow(y-this.y, 2));
		double skala = dist / moveSpeed;
		
		dx = (x-this.x)/skala;
		dy = (y-this.y)/skala;
	}
	
	public void shootDestination(double x, double y){
		shootTarget(x, y);
		shootDestination = true;
		destination.setLocation(x, y);
	}
	
	public void shootPreSetDestination(){
		shootDestination(preSetPoint.getX(), preSetPoint.getY());
	}
	
	public void preSetShootDestination(double x, double y){
		preSetPoint.setLocation(x, y);
	}
	
	public void preSetShootDestination(Point2D.Double p){
		preSetPoint = p;
	}
	
	//efter att du har skjutit den någonstans
	public void setWarning(BufferedImage[] sprites, int delay, double xpos, double ypos, int shootDelay){
		warning = true;
		wdx = dx;
		wdy = dy;
		dx = dy = 0;
		warningCounter = shootDelay;
		wx = xpos;
		wy = ypos;
		warningAnim = new Animation(sprites, delay);
	}
	
	/**
	 * en delay innan den skjuts
	 * 
	 * @param delay
	 */
	public void setDelay(int delay){
		setWarning(new BufferedImage[]{new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)}, -1, 0, 0, delay);
	}
	
	public double getDir(){
		return Calc.calcDir(dx, dy, getSpeed());
	}
	
	public double getSpeed(){
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		setObjectAnimation(hitSprites);
		dx = 0;
		//dy också 0?
		//cwidth = cheight = 0;
	}
	
	public int getDamage(){return damage;}
	public void setDamage(int damage){this.damage = damage;}
	public boolean shouldRemove() { return remove; }
	public void setRemove(){remove = true;}
	public void setSpeed(double s){moveSpeed = s;}
	public boolean isAtDestination(){return (!shootDestination && x == destination.x && y == destination.y);}
	
	public void setExpiration(int ticks){
		expiration = ticks;
	}
	
	public void stop(){
		dx = dy = 0;
		shootDestination = false;
	}
	
	public boolean hits(MapObject mo){
		if(hit) return false;
		boolean h = intersects(mo);
		if(h && setHitOnPlayer) setHit();
		return h;
	}
	
	protected void getNextPosition(){
		
	}
	
	protected void checkBounce(){
		if(setBounce){
			if(collidedWithMapLeft || collidedWithMapRight){
				dx = -lastDX;
			}
			if(collidedWithMapBottom || collidedWithMapTop){
				dy = -lastDY;
			}
		}
	}
	
	protected void checkExpiration(){
		if(expiration > 0){
			expiration--;
		}
		if(expiration == 0){
			setHit();
			expiration--;
		}
	}
	
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
	
	protected void checkWarning(){
		if(!warning) return;
		
		warningAnim.update();
		
		warningCounter--;
		if(warningCounter <= 0){
			warningCounter = 0;
			dx = wdx;
			dy = wdy;
			warning = false;
		}
		
	}
	
	public void update(Section s) {
		if(!isInsideSection(s) && removeIfOutsideSection){
			remove = true;
			return;
		}
		
		checkWarning();
		if(!warning){
			checkExpiration();
			getNextPosition();
			checkShootDestination();
		}
		
		lastDX = dx;
		lastDY = dy;
		if(checkCollisionWithMap){
			checkTileMapCollision();
		}else{
			xtemp = x+dx;
			ytemp = y+dy;
		}
		checkBounce();
		setPosition(xtemp, ytemp);
		
		if(collidedWithMap && setHitOnCollideMap && !hit){ //checktilemapcollision sätter dx till 0 om man krockar i något (för dy också)
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		if(warning){
			g.drawImage(warningAnim.getImage(), (int)(wx + xmap - warningAnim.getWidth() / 2), (int)(wy + ymap - warningAnim.getHeight() / 2), null);
		}
		
		if(notOnScreen()) return;
		
		super.draw(g);
		
	}

}

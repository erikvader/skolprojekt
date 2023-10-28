package game.entity.enemies.enemyProjectile.patterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.Section;
import game.tileMap.TileMap;

/*
 * skjuter en "beam" som i PatternBeam1 fast med skillnaded att denna inte skjuter alla samtidigt.
 */
public class PatternBeam2 extends PatternBase{

	private MapObject target;
	private int delay;
	private double startSpeed, stepSpeed;
	private int antal, hasShot;
	private double startAngle, stepAngle;
	private boolean isAngle = false;
	
	public PatternBeam2(TileMap tm) {
		super(tm);
	}

	public PatternBeam2(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternBeam2(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternBeam2(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	
	public void shootBeam(MapObject p, int antal, double startSpeed, double stepSpeed, int delay){
		target = p;
		this.delay = delay;
		this.antal = antal;
		this.stepSpeed = stepSpeed;
		this.startSpeed = startSpeed;
		hasShot = 0;
		begin = true;
	}
	
	public void shootBeamAngle(double startAngle, double stepAngle, int antal, double startSpeed, double stepSpeed, int delay){
		shootBeam(null, antal, startSpeed, stepSpeed, delay);
		isAngle = true;
		this.startAngle = startAngle;
		this.stepAngle = stepAngle;
	}
	
	@Override
	public void update(Section s) {
		if(!begin) return;
		
		if(counter % delay == 0 && hasShot < antal){
			EnemyProjectile e = getProj();
			e.setPosition(x, y);
			projs.add(e);
			ep.add(e);
			e.setSpeed(startSpeed+hasShot*stepSpeed);
			hasShot++;
			if(!isAngle){
				e.shootTarget(target.getx(), target.gety());
			}else{
				e.shootAngle(startAngle);
				startAngle += stepAngle;
			}
		}
		
		super.update(s);
	}
}

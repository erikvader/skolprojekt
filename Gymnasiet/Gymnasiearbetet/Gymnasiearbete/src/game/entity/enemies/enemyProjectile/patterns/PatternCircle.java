package game.entity.enemies.enemyProjectile.patterns;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Calc;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class PatternCircle extends PatternBase{

	private double radius;
	private double cdelay;
	private MapObject target;
	
	public PatternCircle(TileMap tm) {
		super(tm);
	}
	
	public PatternCircle(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternCircle(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternCircle(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}

	public void shootCircle(double radius, int antal, double delay, MapObject p){
		projs.clear();
		this.radius = radius;
		cdelay = delay;
		target = p;
		
		//spawna allt
		for(int i = 0; i < antal; i++){
			EnemyProjectile c = getProj();
			Point2D.Double point = Calc.getCirclePoint(x, y, this.radius, (i+1)*(360.0/antal));
			c.shootDestination(point.getX(), point.getY());
			projs.add(c);
		}
		
		begin = true;
		
		ep.addAll(projs);
	}
	
	@Override
	public void update(Section s) {
		super.update(s);
		
		//counter++;
		
		if(counter == cdelay){
			for(EnemyProjectile ee : projs)
				ee.shootTarget(target.getx(), target.gety());
		}
		
	}
	

}

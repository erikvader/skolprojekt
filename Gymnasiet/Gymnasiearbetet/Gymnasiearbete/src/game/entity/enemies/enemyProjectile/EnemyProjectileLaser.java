package game.entity.enemies.enemyProjectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import game.entity.MapObject;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class EnemyProjectileLaser extends EnemyProjectile{

	protected Path2D path;
	private double angle;
	protected double length;
	protected double bredd;
	//moveSpeed
	private Color color = Color.RED;
	private double defaultAngle = 270; //rakt ner
	protected AffineTransform posTransform;
	
	public EnemyProjectileLaser(TileMap tm, double bredd, double length) {
		super(tm);
		
		path = new Path2D.Double();
		
		this.bredd = bredd;
		this.length = length;
	
		cwidth = width = 1;
		cheight = height = 1;
		
		setHitOnCollideMap = false;
		setHitOnPlayer = false;
		checkCollisionWithMap = false;
		
		posTransform = new AffineTransform();
		
		setAngle(270);
		
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	protected void constructPath(){
		path.reset();
		path.moveTo(-bredd/2, -length/2);
		path.lineTo(-bredd/2, length/2);
		path.lineTo(bredd/2, length/2);
		path.lineTo(bredd/2, -length/2);
		path.closePath();
		
	}
	
	public void shoot(){
		shootAngle(angle);
	}
	
	public void setAngle(double angle){
		this.angle = angle;
		AffineTransform angleTransform = new AffineTransform();
		//angleTransform.translate(x, y);
		angleTransform.rotate(Math.toRadians(defaultAngle - angle));
		//angleTransform.translate(-bredd/2, -length/2);
		
		constructPath(); //resetar den typ
		
		path.transform(angleTransform);
	}
	
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		posTransform = new AffineTransform();
		posTransform.translate(x, y);
	}
	
	@Override
	public void update(Section s) {
		super.update(s);
		//constructPath();
		
	}
	
	@Override
	public boolean hits(MapObject mo) {
		return path.createTransformedShape(posTransform).intersects(mo.getRectangle());
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		//super.draw(g);
		
		g.setColor(color);
		AffineTransform at = new AffineTransform(posTransform);
		at.translate(xmap, ymap);
		g.fill(path.createTransformedShape(at));
	}

}

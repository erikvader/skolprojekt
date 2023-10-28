package game.entity.level2_boss;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class SineWave extends EnemyProjectile{

	private boolean rightDir = false; //åker åt höger
	private boolean stop = false;
	private ArrayList<Double> points;
	private int accCounter = 0;
	
	public SineWave(TileMap tm, boolean rightDir) {
		super(tm);
		
		this.rightDir = rightDir;
		
		damage = 1;
		
		moveSpeed = 0.005;
		maxSpeed = 3; //2.3
		
		dx = 1.5 * (rightDir ? 1 : -1);
		
		sprites = new Animation(Content.sineWave, -1);
		hitSprites = new Animation();
		
		hitSprites.setEmpty();
		
		setObjectAnimation(sprites);
		
		cwidth = width; //width på en
		cheight = height;
		
		points = new ArrayList<Double>(10);
		
		//lägg till första
		points.add(getSinePos());
		
	}
	
	private double getSinePos(){
		if(rightDir){
			return (0-getWidth()/2.0);
		}else{
			return (16*30+getWidth()/2.0);
		}
	}
	
	@Override
	public boolean hits(MapObject mo) {
		if(testCollision(mo, mo.gety()-mo.getCHeight()/2.0)){
			return true;
		}
		
		if(testCollision(mo, mo.gety()+mo.getCHeight()/2.0)){
			return true;
		}
		
		return false;
	}
	
	private boolean underKurva(double x){
		for(double d : points){
			if(x >= d-width/2 && x <= d+width/2){
				return true;
			}
		}
		return false;
	}
	
	private boolean testCollision(MapObject mo, double py){
		
		//om ens under en kurva
		if(!underKurva(mo.getx()))
			return false;
		
		//finns ens i närheten
		if((y-py)/(height/2.0) > 1){ //ASIN måste vara <= ett
			return false;
		}
		
		//y = sy + height/2 * -sin((2*pi/199)*(x - sx))
		
		//period = width
		double k = (2*Math.PI)/width;
		double asin = Math.asin((y-py)/(height/2.0));
		double sx = points.get(0)-width/2.0;
		
		double x1 = (asin)/k+sx;
		double x2 = (Math.PI-asin)/k+sx;
		//x1, x2 är vinklar den den intersectar
		
		//kolla if intersectar
		//få x till längst ut på vänster
		while(x1 >= 0 && x2 >= 0){
			x1 -= width;
			x2 -= width;
		}
		
		//leta åt höger
		double pleft = mo.getx()-mo.getCWidth()/2.0;
		double pright = mo.getx()+mo.getCWidth()/2.0;

		double leftx = points.get(0)-width/2;
		double rightx = points.get(points.size()-1)+width/2;
		
		while(x1 < 16*30 || x2 < 16*30){
			if((x1 >= pleft && x1 <= pright) || (x2 >= pleft && x2 <= pright)){ //if x1 eller x2 inter player
				if((x1 >= leftx && x1 <= rightx) || (x2 >= leftx && x2 <= rightx)){ //if x1 eller x2 är inom gränserna
					return true;
				}
			}
			x1 += width;
			x2 += width;
		}

		return false;
		
	}
	
	public void stop(){
		stop = true;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	@Override
	public void update(Section s) {
		
		//if(points.get(0) >= 60) return;
		
		accCounter++;
		
		//accelerera
		if(accCounter % 3 == 0){
			if(dx > 0){
				dx += moveSpeed;
				if(dx > maxSpeed)
					dx = maxSpeed;
			}else if(dx < 0){
				dx -= moveSpeed;
				if(dx < -maxSpeed)
					dx = -maxSpeed;
			}
		}
		
		//flytta på dem
		double d;
		for(int i = 0; i < points.size(); i++){
			d = points.get(i);
			points.set(i, d+dx);
		}
		
		//if utanför
		if(rightDir){
			d = points.get(points.size()-1);
			if(d-width/2 >= 16*30){
				points.remove(points.size()-1);
			}
		}else{
			d = points.get(0);
			if(d+width/2 < 0){
				points.remove(0);
			}
		}
		
		//lägga till fler
		if(!stop){
			if(rightDir){
				d = points.get(0);
				if(d-width/2 > 0){
					points.add(0, getSinePos());
				}
			}else{
				d = points.get(points.size()-1);
				if(d+width/2 < 16*30){
					points.add(points.size(), getSinePos());
				}
			}
		}else{
			if(points.size() == 0){
				remove = true;
			}
		}
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		//super.draw(g);
		setMapPosition();
		
		//rita tills är utanför
		for(Double d : points){
			g.drawImage(animation.getImage(), (int)(d+xmap-width/2), (int)(y+ymap-height/2), null);
		}
		
		//debugga
		/*
		g.setColor(Color.RED);
		for(int i = 0; i < 16*30; i++){
			double grej = y-50*Math.sin((2*Math.PI/199)*(i-points.get(0)-width/2));
			g.fillRect((int)(i+xmap), (int)grej, 1, 1);
		}
		*/
		
	}
	

}

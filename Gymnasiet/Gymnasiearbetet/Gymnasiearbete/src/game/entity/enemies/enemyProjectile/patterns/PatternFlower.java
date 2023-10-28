package game.entity.enemies.enemyProjectile.patterns;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileCircle;
import game.handlers.Calc;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class PatternFlower extends PatternBase{

	private MapObject target;
	private double[] punkter = { //index, x
			-35, -30.44, -19.84, -6.5, 7.12, 19.63, 30.23, 35
	};
	//private final double w = 50;
	private final double h = 70;
	private double snurrSpeed = -1;
	
	private boolean shootAll = false;
	private int allStart;
	
	protected ArrayList<EnemyProjectileCircle> cProjs = new ArrayList<EnemyProjectileCircle>();
	
	/**
	 * rätt så mycket anpassad till alsenholt2_2
	 * 
	 * @param tm
	 */
	public PatternFlower(TileMap tm) {
		super(tm);
	}
	
	public PatternFlower(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
	}

	public PatternFlower(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternFlower(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}

	public void spawnFlower(MapObject p, double x, double y){
		cProjs.clear();
		target = p;
		setPosition(x, y);
		
		//spawna allt
		//35^2 = 1.96y^2 + x^2
		//y = +-sqrt((35^2-x^2)/1.96)
		double[] ypoints = new double[punkter.length];
		for(int i = 0; i < punkter.length; i++){
			ypoints[i] = Math.sqrt((Math.pow(h/2.0, 2)-Math.pow(punkter[i], 2))/1.96);
		}
		
		for(int i = 0; i < punkter.length*2-2; i++){
			int r = i + (i>=punkter.length ? -(2*(i-7)) : 0); //det indexet vill vill ha. vi går fram och tillbaka på X utana tt ta pubnkterna längst ut
			
			double s = i>=punkter.length ? -1 : 1; //ifall den ska vara på andra sidan
			Point2D.Double[] m = new Point2D.Double[4];//meh, det funkar
			m[0] = new Point2D.Double(x+punkter[r]+35, y+ypoints[r]*s);
			m[1] = new Point2D.Double(x+punkter[r]*(-1)-35, y-ypoints[r]*s);
			m[2] = new Point2D.Double(x-ypoints[r]*s*-1, y+punkter[r]*-1-35);
			m[3] = new Point2D.Double(x+ypoints[r]*s*-1, y+punkter[r]+35);
			
			for(int j = 0; j < 4; j++){
				EnemyProjectileCircle e = new EnemyProjectileCircle(this);
				e.setSpeed(2.4);
				e.setPosition(x, y);
				e.preSetShootDestination(m[j]);
				double radius = Calc.calcLength(m[j], e.getPosDouble());
				e.shootCircle(radius, Calc.calcDir(m[j].getX()-x, m[j].getY()-y, radius));
				e.setVinkelSpeed(snurrSpeed);
				e.stopCircle();
				cProjs.add(e);
			}
			
		}
		
		begin = true;
		
		ep.addAll(cProjs);
	}
	
	@Override
	public void update(Section s) {
		//super.update(s);

		if(!begin) return;
		/*
		EnemyProjectileCircle e;
		for(int i = 0; i < cProjs.size(); i++){
			e = cProjs.get(i);
			if(e.shouldRemove()){
				cProjs.remove(i);
				i--;
			}
		}
		
		//ska tas bort
		if(cProjs.size() == 0){
			remove = true;
		}
		*/
		EnemyProjectileCircle e;
		boolean attTaBort = true;
		for(int i = 0; i < cProjs.size(); i++){
			e = cProjs.get(i);
			if(!e.shouldRemove()){
				attTaBort = false;
				break;
			}
		}
		
		if(attTaBort){
			remove = true;
		}
		
		counter++;
		
		//skjut ut dem i början
		int a = 8;
		if((counter-1) % a == 0 && counter < a*14+1){
			int start = (counter-1)/a;
			start *= 4;
			for(int i = start; i < start+4; i++){
				cProjs.get(i).shootPreSetDestination();
			}
		}
		
		//shoot
		if(shootAll){
			int b = 7;
			if((counter-1-allStart) % b == 0 && counter-allStart < b*14+1){
				int start = (counter-1-allStart)/b;
				start *= 4;
				for(int i = start; i < start+4; i++){
					e = cProjs.get(i);
					e.setSpeed(3.4);
					e.shootTarget(target.getx(), target.gety());
				}
			}
		}
		
	}
	
	public void shootAll(){
		shootAll = true;
		stopSnurr();
		allStart = counter;
	}
	
	public void stopSnurr(){
		for(int i = 0; i < cProjs.size(); i++){
			cProjs.get(i).stopCircle();
		}
	}
	
	public void startSnurr(){
		EnemyProjectileCircle e;
		for(int i = 0; i < cProjs.size(); i++){
			e = cProjs.get(i);
			e.setVinkelSpeed(snurrSpeed);
			e.continueCircle();
		}
	}
	
	/**
	 * 1 blir anti-klocka
	 * 
	 * @param dir
	 */
	public void snurra(int dir){
		snurrSpeed = (int)Math.abs(snurrSpeed)*dir;
	} 
	
	@Override
	public void draw(Graphics2D g) {
		/*
		setMapPosition();
		g.setColor(Color.BLUE);
		g.drawOval((int)(xmap+x-w/2), (int)(y+ymap), (int)w, (int)h);
		g.drawOval((int)(xmap+x-w/2), (int)(y-h+ymap), (int)w, (int)h);
		g.drawOval((int)(xmap+x), (int)(y-w/2+ymap), (int)h, (int)w);
		g.drawOval((int)(xmap+x-h), (int)(y-w/2+ymap), (int)h, (int)w);
		*/
	}
	

}

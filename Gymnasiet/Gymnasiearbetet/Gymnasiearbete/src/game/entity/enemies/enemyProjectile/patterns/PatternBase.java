package game.entity.enemies.enemyProjectile.patterns;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class PatternBase extends EnemyProjectile{

	protected ArrayList<EnemyProjectile> projs = new ArrayList<EnemyProjectile>();
	protected ArrayList<EnemyProjectile> ep;
	
	protected int counter = 0;
	protected boolean begin = false;
	
	public PatternBase(TileMap tm) {
		super(tm);
	}

	public PatternBase(EnemyProjectile ep, ArrayList<EnemyProjectile> enemyP) {
		super(ep);
		this.ep = enemyP;
	}

	public PatternBase(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep){
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed);
		this.ep = ep;
	}

	public PatternBase(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed);
		this.ep = ep;
	}
	
	protected EnemyProjectile getProj(){
		EnemyProjectile c = new EnemyProjectile(this);
		c.setPosition(x, y);
		return c;
	}
	
	@Override
	public void update(Section s) {
		//super.update(s);
		if(!begin) return;
		
		EnemyProjectile e;
		for(int i = 0; i < projs.size(); i++){
			e = projs.get(i);
			if(e.shouldRemove()){
				projs.remove(i);
				i--;
			}
		}
		
		//ska tas bort
		if(projs.size() == 0){
			remove = true;
		}
		
		counter++;
		
	}
	
	@Override
	public boolean hits(MapObject mo) {
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		//super.draw(g);
		/*for(EnemyProjectile e : cirkeln)
			e.draw(g);
		*/
	}
}

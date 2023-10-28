package game.entity.enemies.enemyProjectile.patterns;

import java.util.ArrayList;

import game.entity.Animation;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.Section;
import game.tileMap.TileMap;

/*
 * skjuter en "beam" som i PatternBeam1 fast med skillnaded att denna inte skjuter alla samtidigt.
 */
public class PatternSplittaRing extends PatternBase{

	private EnemyProjectile p;
	private int delay;
	private int antal;
	
	private Animation stor;
	
	public PatternSplittaRing(TileMap tm) {
		super(tm);
	}

	public PatternSplittaRing(EnemyProjectile ep, Animation stor, ArrayList<EnemyProjectile> enemyP) {
		super(ep, enemyP);
		this.stor = stor;
	}

	/*
	public PatternSplittaRing(TileMap tm, BufferedImage[] sprites, int delay, BufferedImage[] hitSprites, int hitDelay,
			int cwidth, int cheight, double moveSpeed, ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, hitSprites, hitDelay, cwidth, cheight, moveSpeed, ep);
	}

	public PatternSplittaRing(TileMap tm, BufferedImage[] sprites, int delay, int cwidth, int cheight, double moveSpeed,
			ArrayList<EnemyProjectile> ep) {
		super(tm, sprites, delay, cwidth, cheight, moveSpeed, ep);
	}
	*/
	
	public void shootSplitta(int antal, int delay, double tx, double ty){
		this.delay = delay;
		this.antal = antal;
		begin = true;
		
		EnemyProjectile p = getProj();
		//p.setPosition(x, y);
		p.shootDestination(tx, ty);
		p.setObjectAnimation(stor);
		p.setHitbox(20, 20);
		projs.add(p);
		ep.add(p);
		this.p = p;
	}
	
	@Override
	public void update(Section s) {
		if(!begin) return;
		
		if(p.isAtDestination()){
			if(counter >= delay){
				p.setRemove();
				PatternRing r = new PatternRing(p, ep);
				r.setPosition(p.getPosDouble());
				double sa = Math.random()*360;
				r.shootRing(sa, 360, antal);
				ep.add(r);
			}
		}else{
			counter--;
		}
		
		super.update(s);
	}
}

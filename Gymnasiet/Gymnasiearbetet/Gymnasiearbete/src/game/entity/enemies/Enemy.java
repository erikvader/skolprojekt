package game.entity.enemies;

import java.util.ArrayList;

import game.entity.Effect;
import game.entity.MapObject;
import game.entity.Player;
import game.tileMap.Section;
import game.tileMap.TileMap;

public abstract class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	
	//protected ArrayList<EnemyProjectile> enemyProjectiles;
	protected ArrayList<Effect> effects;
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public Enemy(TileMap tm, ArrayList<Effect> e) {
		super(tm);
		effects = e;
	}
	
	/*
	public Enemy(TileMap tm, ArrayList<EnemyProjectile> ep) {
		super(tm);
		enemyProjectiles = ep;
	}
	*/
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public void getNextPosition() {
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	/**
	 * skada fienden
	 * @param damage
	 */
	public void hit(int damage) { 
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = 0;
	}
	
	public void update(Section s) {}
	
	/**
	 * gör någonting när fienden dör
	 */
	public void die(){
		if(effects != null){
			effects.add(new Effect(tileMap, (int)x, (int)y));
		}
	} 
	
	/**
	 * kollar ifall fienden SKA göra skada på spelaren
	 * @param p
	 * @return
	 */
	public abstract boolean attacks(Player p); //checks if this enemy will deal damage to the player this update
	
	/**
	 * GÖR själva attacken på spelaren.
	 * @param p
	 */
	public void attack(Player p){
		p.hit(damage);
	}
}















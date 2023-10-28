package game.entity.level1_boss.alsenholt;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Player;
import game.entity.enemies.Enemy;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.level4_boss.alsenholt.GroundFire;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Drake extends Enemy{
	
	private Animation[] sprites;
	
	//private ArrayList<Enemy> enemies;
	//private ArrayList<Effect> effects;
	private ArrayList<EnemyProjectile> enemyProjectiles;
	
	private final int ANIMATION_IDLE = 0;
	private final int ANIMATION_WALKING = 1;
	private final int ANIMATION_SHOOTING = 2;
	private final int ANIMATION_CRYING = 3;
	private final int ANIMATION_LAST_ATTACK = 4; //kryss för ögon
	private final int ANIMATION_FALLING = 5;
	private final int ANIMATION_BACKAR = 6;
	
	private int curAction = ANIMATION_IDLE;
	
	private Player player;
	
	private boolean started = false;
	
	//phases
	private final int PHASE_NOTHING = 0;
	private final int PHASE_1_1 = 1;
	
	private int currentPhase = PHASE_1_1;
	
	private int counter = 0;
	
	//shot
	private boolean hasShot = false;
	private int shotCounterTarget = 20;
	private int shotModulo = 0;
	
	//jump
	private int jumpDelay = 400;
	private int jumpDelayCounter = 0;
	
	public Drake(TileMap tm, Player p, ArrayList<EnemyProjectile> ep/*, ArrayList<Enemy> enemies, ArrayList<Effect> effects*/) {
		super(tm);
		
		enemyProjectiles = ep;
		
		player = p;
		//this.enemies = enemies;
		//this.effects = effects;
		
		width = height = 5*30;
		cwidth = 124;
		cheight = 136; //140
		
		health = maxHealth = 3;
		
		sprites = new Animation[7];
		
		sprites[ANIMATION_IDLE] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt/boss.png", 5*30, 5*30, 0, 13), 6);
		sprites[ANIMATION_IDLE].setSpecificDelay(0, 20);
		sprites[ANIMATION_SHOOTING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt/boss.png", 5*30, 5*30, 1, 1), -1);
		sprites[ANIMATION_WALKING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt/boss.png", 5*30, 5*30, 2, 16), 4);
		sprites[ANIMATION_CRYING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt/boss.png", 5*30, 5*30, 3, 1), -1);
		sprites[ANIMATION_FALLING] = sprites[ANIMATION_SHOOTING];
		sprites[ANIMATION_LAST_ATTACK] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt/boss.png", 5*30, 5*30, 4, 1), -1);
		sprites[ANIMATION_BACKAR] = sprites[ANIMATION_WALKING];
		
		animation = new Animation();
		setAnimation(ANIMATION_IDLE);
		
		facingRight = false;
		
		fallSpeed = 0.1;
		maxFallSpeed = 5;
		damage = 1;
		moveSpeed = maxSpeed = stopSpeed = 1;
		jumpStart = -6.6;
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(sprites[curAction]);
		
	}
	
	public void setLastTalk(){
		stop();
		facingRight = true;
		setAnimation(ANIMATION_IDLE);
		currentPhase = PHASE_NOTHING;
		started = false;
	}
	
	public void setLastTalk2(){
		setAnimation(ANIMATION_LAST_ATTACK);
	}
	
	public void begin(){
		started = true;
	}
	public boolean hasBegun(){return started;}
	
	@Override
	public void getNextPosition() {
		if(left){
			dx = -moveSpeed;
		}else if(right){
			dx = moveSpeed;
		}else{
			dx = 0;
		}
		
		if(falling){
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	@Override
	public void update(Section s) {
		
		//movement
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		animation.update();
		
		if(!started) return;
		
		//handle Phases
		counter++;
		
		if(hasShot){
			if(counter % shotCounterTarget == shotModulo){
				hasShot = false;
			}
		}
		
		if(currentPhase == PHASE_1_1){
			//fram tillbaka
			left = right = false;
			if(!falling){
				double v = Math.cos(counter*0.05);
				if(v > 0){
					right = true;
					left = false;
				}else if(v < 0){
					left = true;
					right = false;
				}
			}
			
			if(counter % 199 == 0){
				attack1();
				hasShot = true;
				shotModulo = counter % shotCounterTarget;
			}
			
			if(counter % 327 == 0){
				attack2();
			}
			
			if(counter % 470 == 0){
				attack3();
			}
			
			//räkna ut hopp
			jumpDelayCounter++;
			if(jumpDelayCounter >= jumpDelay){
				double p = (jumpDelayCounter-jumpDelay)*(1.0/(800-jumpDelay)); //på tick 800 är det 1%
				if((Math.random()*100) <= p){
					//System.out.println(jumpDelayCounter + " " + p);
					dy = jumpStart;
					jumpDelayCounter = 0;
				}
			}
			
		}
		
		if(hasShot){
			if(curAction != ANIMATION_SHOOTING)
				setAnimation(ANIMATION_SHOOTING);
		}else if(falling){
			if(curAction != ANIMATION_FALLING)
				setAnimation(ANIMATION_FALLING);
		}else if(left || right){
			if(curAction != ANIMATION_WALKING)
				setAnimation(ANIMATION_WALKING);
		}else{
			if(curAction != ANIMATION_IDLE)
				setAnimation(ANIMATION_IDLE);
		}
		
	}
	
	private void attack1(){
		shootPF(1.1).shootAngle(180);
		shootPF(0.7).shootAngle(215);
		shootPF(0.7).shootAngle(145);
	}
	
	private void attack2(){
		GroundFire gf = new GroundFire(tileMap, false);
		gf.setPosition(x-30, y);
		enemyProjectiles.add(gf);
	}
	
	private void attack3(){
		shootPF(2).shootTarget(player.getx(), player.gety());
	}
	
	private ProjectileFire shootPF(double speed){
		ProjectileFire pf = new ProjectileFire(tileMap, speed);
		pf.setPosition(x-30, y);
		enemyProjectiles.add(pf);
		return pf;
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}
	
	@Override
	public boolean attacks(Player p) {
		return intersects(p);
	}

	@Override
	public void attack(Player p) {
		p.hit(damage, true, -8, -5, true);
	}
	
	@Override
	public void hit(int damage) {
		//super.hit(damage);
	}
	
}

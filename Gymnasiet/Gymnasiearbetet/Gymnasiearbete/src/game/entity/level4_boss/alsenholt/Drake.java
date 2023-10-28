package game.entity.level4_boss.alsenholt;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.Enemy;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.level1_boss.alsenholt.ProjectileFire;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Drake extends Enemy{
	
	private Animation[] sprites;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Effect> effects;
	private ArrayList<EnemyProjectile> enemyProjectiles;

	private final int ANIMATION_IDLE = 0;
	private final int ANIMATION_WALKING = 1;
	private final int ANIMATION_SHOOTING = 2;
	private final int ANIMATION_CRYING = 3;
	private final int ANIMATION_LAST_ATTACK = 4;
	private final int ANIMATION_FALLING = 5;
	private final int ANIMATION_BACKAR = 6;
	
	private int curAction = ANIMATION_IDLE;
	
	private Player player;
	
	private boolean started = false;
	
	//phases
	private final int PHASE_NOTHING = 0;
	private final int PHASE_1_1 = 1;
	private final int PHASE_1_2 = 6;
	private final int PHASE_2 = 2;
	private final int PHASE_TAKE_DAMAGE = 3;
	private final int PHASE_3 = 4;
	private final int PHASE_LAST_ATTACK = 5;
	
	private int currentPhase = PHASE_1_1;
	
	private int counter = 0;
	
	private MiniDrake miniDrake;
	
	private boolean krockat = false;
	
	//phase attack2
	private double angle = 180;
	
	//attack4
	private boolean loopTimer = false;
	private int toSkip = 0;
	
	public Drake(TileMap tm, Player p, ArrayList<EnemyProjectile> ep, ArrayList<Enemy> enemies, ArrayList<Effect> effects) {
		super(tm);
		
		enemyProjectiles = ep;
		
		player = p;
		this.enemies = enemies;
		this.effects = effects;
		
		width = height = 5*30;
		cwidth = 124;
		cheight = 136; //140
		
		health = maxHealth = 3;
		
		sprites = new Animation[7];
		
		sprites[ANIMATION_IDLE] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/boss2.png", 5*30, 5*30, 0, 13), 6);
		sprites[ANIMATION_IDLE].setSpecificDelay(0, 20);
		sprites[ANIMATION_SHOOTING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/boss2.png", 5*30, 5*30, 1, 1), -1);
		sprites[ANIMATION_WALKING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/boss2.png", 5*30, 5*30, 2, 16), 4);
		sprites[ANIMATION_CRYING] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/boss2.png", 5*30, 5*30, 3, 1), -1);
		sprites[ANIMATION_FALLING] = sprites[ANIMATION_SHOOTING];
		sprites[ANIMATION_LAST_ATTACK] = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/boss2.png", 5*30, 5*30, 4, 1), -1);
		sprites[ANIMATION_BACKAR] = sprites[ANIMATION_WALKING];
		
		animation = new Animation();
		setAnimation(ANIMATION_IDLE);
		
		facingRight = false;
		
		fallSpeed = 0.1;
		maxFallSpeed = 5;
		damage = 5;
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(sprites[curAction]);
		
	}
	
	public void begin(){
		started = true;
	}
	public boolean hasBegun(){return started;}
	public boolean hasKrockat(){return krockat;}
	
	public boolean isLast_attack(){return currentPhase == PHASE_LAST_ATTACK;}
	
	@Override
	public void getNextPosition() {
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
		
		if(miniDrake != null){
			if(miniDrake.getHealth() == 0 && !miniDrake.getShooting()){
				miniDrake.shoot(x-30, y);
			}else if(miniDrake.getShooting()){
				if(miniDrake.intersects(this)){
					for(int a = -30; a <= 30; a+=30){
						for(int b = 45; b >= -15; b-=30){
							effects.add(new Effect(tileMap, (int)x+a, (int)(y+b), Content.Explosion2, 8));
						}
					}
					
					miniDrake.setDead();
					miniDrake = null;
					health -= 1;
					currentPhase = PHASE_TAKE_DAMAGE;
					counter = 0;
				}
			}
		}
		
		if(falling){
			setAnimation(ANIMATION_FALLING);
		}
		
		//handle Phases
		counter++;
		if(currentPhase == PHASE_1_1){
			if(counter == 1060){ //end phase
				currentPhase = PHASE_1_2;
				counter = 0;
			}else if(counter < 1000){
				if(counter%50 == 30) setAnimation(ANIMATION_SHOOTING);
				attack1();
				if(counter%50 == 15) setAnimation(ANIMATION_IDLE);
				attack5();
			}else if(counter == 1000){
				setAnimation(ANIMATION_IDLE);
			}
		}else if(currentPhase == PHASE_1_2){
			if(counter == 1){
				setAnimation(ANIMATION_SHOOTING);
			}else if(counter == 750){
				sendMini();
			}
			attack2();
		}else if(currentPhase == PHASE_2){
			if(counter == 1){
				setAnimation(ANIMATION_SHOOTING);
			}else if(counter == 80){
				setAnimation(ANIMATION_IDLE);
			}
			if(counter == 600){
				sendMini();
			}
			attack3();
		}else if(currentPhase == PHASE_3){
			if(counter == 1){
				setAnimation(ANIMATION_SHOOTING);
			}
			if(counter == 855){
				sendMini();
			}
			attack4();
		}else if(currentPhase == PHASE_LAST_ATTACK){
			if(counter == 1)
				setAnimation(ANIMATION_BACKAR);
				damage = 5;
			if(counter <= 60){
				dx = 1;
			}else if(counter == 61){
				if(counter == 61){
					setAnimation(ANIMATION_WALKING);
				}
				dx = -2;
			}else if(counter > 61){
				if(dx == 0){
					setAnimation(ANIMATION_LAST_ATTACK);
					currentPhase = PHASE_NOTHING;
					krockat = true;
				}
			}
		}else if(currentPhase == PHASE_TAKE_DAMAGE){
			if(counter == 1){
				setAnimation(ANIMATION_CRYING);
			}else{
				setPosition(Math.sin((counter/60.0)*8)*15+29*30, y);
			}
			if(counter == 225){
				setPosition(29*30, y);
				counter = 0;
				if(health == 2){
					currentPhase = PHASE_2;
				}else if(health == 1){
					currentPhase = PHASE_3;
				}else if(health == 0){
					currentPhase = PHASE_LAST_ATTACK;
				}
			}
		}
		
	}
	
	private void sendMini(){
		miniDrake = new MiniDrake(tileMap);
		miniDrake.setPosition(x-30, y);
		enemies.add(miniDrake);
	}

	private void attack5(){
		if(counter % 200 >= 130 && counter % 7 == 0){
			double speed = 2*(((counter%200)-130)/69.0)+2;
			shootPF(speed).shootTarget(player.getx(), player.gety());
		}
	}
	
	private void attack4(){
		if(counter == 1){
			angle = 90;
		}
		if(counter % 2 == 0){
			if(angle <= 194 && angle >= 175 && loopTimer == false){
				toSkip = 194-(int)(Math.random()*(20.0));
				loopTimer = true;
			}else{
				loopTimer = false;
			}
			if(toSkip >= angle && toSkip < angle+10){
				
			}else{
				shootPF(1).shootAngle(angle);
				shootPF(1).shootAngle(angle+5);
			}
			angle -= 10;
			if(angle < 0) angle += 360;
		}
		
	}
	
	private void attack3(){
		if(counter <= 80 && counter % 2 == 0){
			double tx = (Math.random()*5+26)*30;
			shootPF(6).shootTarget(tx, 0);
		}
		if(counter > 80 && (counter % 26 == 0 || counter % 9 == 0)){
			double tx = 18*30+7+(Math.random()*(9*30-14));
			double s;
			if(counter % 9 == 0){ //en långsam
				s = (Math.random()*20)/10+1;
			}else{//minde chans för en snabb
				s = (Math.random()*20)/10+3;
			}
			ProjectileFire pf = shootPF(s);
			pf.setPosition(tx, 0);
			pf.shootAngle(270);
		}
	}
	
	private void attack1(){
		if(counter % 50 == 0){
			GroundFire gf = new GroundFire(tileMap, false);
			gf.setPosition(x-30, y);
			enemyProjectiles.add(gf);
		}
	}
	
	private void attack2(){
		if(counter == 1){
			angle = 90;
		}
		if(counter % 15 == 0){
			shootPF(4).shootAngle(angle);
			shootPF(4).shootAngle(angle+180);
			shootPF(4).shootAngle(angle+270);
			shootPF(4).shootAngle(angle+90);
			shootPF(4).shootAngle(angle+45);
			shootPF(4).shootAngle(angle+225);
			shootPF(4).shootAngle(angle+315);
			shootPF(4).shootAngle(angle+135);
			angle -= 15;
			if(angle < 0) angle += 360;
		}
		if(counter % 180 == 0){
			shootPF(6).shootTarget(player.getx(), player.gety());
		}
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
		//p.hit(damage, true, -5, -4, true);
		super.attack(p);
	}

	@Override
	public void hit(int damage) {
		//super.hit(damage);
	}
	
}

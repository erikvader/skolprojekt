package game.entity.level2_boss;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.audio.JukeBox;
import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.Enemy;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.items.ThrowableItem;
import game.entity.items.mattias.DelatMedNollItem;
import game.gameState.levels.level2.Level2_BossState;
import game.handlers.Content;
import game.main.GamePanel;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Mattias extends Enemy {
	
	private ArrayList<EnemyProjectile> enemyProjectiles;
	private ArrayList<ThrowableItem> throwableItems;
	private Player player;
	
	private boolean defeated = false;
	private boolean started = false;
	private boolean intro = false;
	
	//animations
	private Animation[] animations;
	private final int ANIMATION_NORMAL = 0;
	private final int ANIMATION_SMOKES = 1;
	private final int ANIMATION_LAUGHS = 2;
	
	private int curAction = ANIMATION_NORMAL;
	
	//phases
	private final int PHASE_NOTHING = 0;
	private final int PHASE_SMOKES = 1; //tar skada
	private final int PHASE_1 = 2;
	private final int PHASE_1_ATTACK_1 = 3; //spam e
	private final int PHASE_2_ATTACK_1 = 4; //sinus
	private final int PHASE_2 = 5;
	private final int PHASE_3 = 6;
	
	private int currentPhase = PHASE_NOTHING;
	private int lastPhase = PHASE_NOTHING;
	
	private int counter = 0;
	
	private Point restingPos;
	
	private DelatMedNollItem delatMedNoll;
	private boolean throwNoll = false;
	
	private Keyboard keyboard;
	private MattiasCounter timer;
	
	//movements
	private boolean middle = false; //move towards restingPos
	//private boolean isInMiddle = false; //för att kolla ifall man har flyttats färdigt till mitten
	private double leftEdge = 70, rightEdge = GamePanel.WIDTH-70;
	private int wobbleCounter = 0;
	private boolean wobble = false;
	//left, right
	
	//attacks
	//phase_1_attack_1
	private int[] p_1_1 = {-40, 40, 40, 5, 0}; //0 = left, 1 = right, 2 = current, 3 = step, 4 = dir (0 -)

	//phase_2_attack_1
	private SineWave p_2_1_left, p_2_1_right;
	
	//shoot gklav
	private boolean gklavDir = false;

	public Mattias(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<ThrowableItem> ti, ArrayList<Effect> ef) {
		super(tm, ef);
		
		enemyProjectiles = ep;
		throwableItems = ti;
		this.player = player;
		
		width = 90;
		height = 100;
		cwidth = 50;
		cheight = 100;
		
		facingRight = true;
		
		health = maxHealth = 3;
		
		moveSpeed = maxSpeed = stopSpeed = 1;
		
		//init animationer
		BufferedImage[][] sheet = Content.load("/Resources/Sprites/Enemies/mattias/mattis.png", 90, 100, new int[]{1, 1, 1});
		animations = new Animation[3];
		animations[ANIMATION_NORMAL] = new Animation(sheet[0], -1);
		animations[ANIMATION_SMOKES] = new Animation(sheet[2], -1);
		animations[ANIMATION_LAUGHS] = new Animation(new BufferedImage[]{sheet[0][0], sheet[1][0]}, 10);
		
		setAnimation(ANIMATION_NORMAL);
		
		restingPos = new Point(8*30, 2*30+15);
		
		keyboard = new Keyboard(tileMap);
		keyboard.setPosition(-500, -500);
		
		timer = new MattiasCounter(tileMap);
		
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(animations[curAction]);
		
	}
	
	public void playIntro(){intro = true;}
	public boolean isDonePlayIntro(){return !intro;}
	public boolean isDefeated(){return defeated;}
	
	public void setWobble(boolean w){
		wobble = w;
		if(!w) wobbleCounter = 0;
	}
	
	public void begin(){
		started = true;
		currentPhase = PHASE_1; //PHASE_1
		//moveToMiddle();
	}
	
	private void goRandomDir(){
		left = (int)(Math.random()*2) == 0 ? true : false;
		right = !left;
		setWobble(true);
	}
	
	private void moveToMiddle(){
		middle = true;
		//isInMiddle = false;
		left = right = false;
		setWobble(false);
		
	}
	
	@Override
	public void stop() {
		dx = dy = 0;
		setWobble(false);
		left = right = false;
		middle = false;
	}
	
	@Override
	public void getNextPosition() {
		
		if(middle){
			double dist = Math.sqrt(Math.pow(x-restingPos.getX(), 2) + Math.pow(y-restingPos.getY(), 2));
			if(dist < maxSpeed){
				middle = false;
				dx = dy = 0;
				//isInMiddle = true;
				setPosition(restingPos);
			}else{
				dx = ((restingPos.getX() - x) * maxSpeed) / dist;
				dy = ((restingPos.getY() - y) * maxSpeed) / dist;
			}
			
		}else if(left || right){
			if(x <= leftEdge){
				left = false;
				right = true;
			}else if(x >= rightEdge){
				left = true;
				right = false;
			}
		
			if(left){
				dx -= moveSpeed;
				if(dx < -maxSpeed) 
					dx = -maxSpeed;
			}else if(right){
				dx += moveSpeed;
				if(dx > maxSpeed) 
					dx = maxSpeed;
			}
			
		}
		
		//wobbla upp och ner
		if(wobble){
			wobbleCounter++;
			dy = (restingPos.getY() + 10*Math.sin(wobbleCounter/12.0)) - y;
		}
		
	}
	
	@Override
	public void update(Section s) {
		animation.update();
		if(keyboard != null) keyboard.update();
		if(timer != null) timer.update();
		
		if(intro){
			introSequence();
			x += dx;
			y += dy;
			setPosition(x, y);
			return;
		}
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if(!started) return;
		
		//skadedes, till nästa fas
		if(delatMedNoll != null && delatMedNoll.damages(this)){
			counter = 0;
			delatMedNoll.remove();
			delatMedNoll = null;
			currentPhase = PHASE_SMOKES;
			throwNoll = false;
			stopSinus();
			health--;
			if(health == 2){
				lastPhase = PHASE_2;
			}else if(health == 1){
				lastPhase = PHASE_3;
			}else if(health == 0){
				
			}
			
		}
		
		//vunnit sista fasen
		if(currentPhase == PHASE_3 && counter > 1){ //så att timer kan spawnas
			if(timer.getTime() == 0){
				defeated = true;
				started = false;
				keyboard = null;
				stop();
				return;
			}
		}
		
		counter++;
		
		if(currentPhase == PHASE_1){
			if(counter == 1){
				goRandomDir();
			}
			
			if(counter == 30){
				if(throwNoll){
					spawnDelatMedNoll();
					throwNoll = false;
				}
			}
			
			if(counter % 90 == 0){
				shootPi();
			}
			
			if(counter % 151 == 0){
				shootE();
			}
			
			if(counter == 600){
				lastPhase = currentPhase;
				currentPhase = PHASE_1_ATTACK_1;
				counter = 0;
				throwNoll = true;
				moveToMiddle();
			}
		}else if(currentPhase == PHASE_2){
			if(counter == 1){
				goRandomDir();
			}
			
			if(counter == 30){
				if(throwNoll){
					spawnDelatMedNoll();
					throwNoll = false;
				}
			}
			
			if(counter % 90 == 0){
				shootTau();
			}
			
			if(counter % 101 == 0){
				shootE();
			}
			
			if(counter == 600){
				lastPhase = currentPhase;
				currentPhase = PHASE_2_ATTACK_1;
				counter = 0;
				throwNoll = true;
				moveToMiddle();
			}
		}else if(currentPhase == PHASE_1_ATTACK_1){
			if(counter == 1 && middle){ //inte där än
				counter--;
			}else{
				phase_1_attack_1();
				if(counter == 650){ 
					currentPhase = lastPhase;
					counter = 0;
					 //reset
					p_1_1[4] = 0;
					p_1_1[2] = p_1_1[1];
				}
			}
		}else if(currentPhase == PHASE_2_ATTACK_1){
			if(counter == 1 && middle){ //inte där än
				counter--;
			}else{
				phase_2_attack_1();
				if(counter == 800){ 
					currentPhase = lastPhase;
					counter = 0;
					 //reset
					stopSinus();
				}
			}
		}else if(currentPhase == PHASE_SMOKES){
			if(counter == 1){
				setAnimation(ANIMATION_SMOKES);
				stop();
			}else if(counter == 200){
				moveToMiddle();
				setAnimation(ANIMATION_NORMAL);
			}else if(counter == 201 && middle){ // inte framme
				counter--;
			}else if(counter == 201){
				currentPhase = lastPhase;
				counter = 0;
			}
		}else if(currentPhase == PHASE_3){
			if(counter == 1){
				spawnKeyboard();
				spawnTimer();
				setWobble(true);
				JukeBox.stop("mattias_background");
				JukeBox.play("mattias_last");
				Level2_BossState.playMusic = false;
				Level2_BossState.playLast = true;
			}
			
			//gklav
			if(counter == 30 || (counter-30) % 300 == 0){
				shootGKlav(gklavDir);
				gklavDir = !gklavDir;
				if(counter >= 60*50-30){
					shootGKlav(gklavDir);
				}
			}
			
			//cirkel
			if(counter % 600 == 0){
				shootCirkel();
			}
			if(counter >= 60*40 && counter % 600 == 299){
				shootCirkel();
			}
			
			//falling
			if(counter >= 60*6 && counter % 60 == 0){
				shootFallingNot();
			}
			if(counter >= 60*12 && counter % 60 == 29){
				shootFallingNot();
			}
			if(counter >= 60*30 && counter % 60 == 12){
				shootFallingNot();
			}
			
			//sidleds
			if(counter >= 60*45 && counter % 100 == 0){
				shootSnedNot();
			}
		}
		
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if(keyboard != null)
			keyboard.draw(g);
		
		if(timer != null)
			timer.draw(g);
	}

	@Override
	public boolean attacks(Player p) {
		return false;
	}
	
	@Override
	public void hit(int damage) {
		/*
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) defeated = true;
		striked = true;
		*/
	}
	
	///////attakcks//////
	private void stopSinus(){
		if(p_2_1_left != null && p_2_1_right != null){
			p_2_1_left.stop();
			p_2_1_right.stop();
			p_2_1_left = p_2_1_right = null;
		}
	}
	
	private void shootPi(){
		double speed = Math.random()+1.3;
		PIProjectile p = new PIProjectile(tileMap, false);
		p.setPosition(x, y+30);
		p.shoot(speed);
		enemyProjectiles.add(p);
		
		p = new PIProjectile(tileMap, true);
		p.setPosition(x, y+30);
		p.shoot(speed);
		enemyProjectiles.add(p);
		
	}
	
	private void shootTau(){
		double speed = Math.random()+1.3;
		TauProjectile p = new TauProjectile(tileMap, false, enemyProjectiles);
		p.setPosition(x, y+30);
		p.shoot(speed);
		enemyProjectiles.add(p);
		
		p = new TauProjectile(tileMap, true, enemyProjectiles);
		p.setPosition(x, y+30);
		p.shoot(speed);
		enemyProjectiles.add(p);
		
	}
	
	private void shootE(){
		EProjectile e = new EProjectile(tileMap);
		e.setPosition(x, y);
		e.shootTarget(player.getx(), player.gety());
		enemyProjectiles.add(e);
	}
	
	private void shootE(double angle, double s){
		EProjectile e = new EProjectile(tileMap);
		e.setPosition(x, y);
		e.setSpeed(s);
		e.shootAngle(angle);
		enemyProjectiles.add(e);
	}
	
	private void shootGKlav(boolean dir){
		GKlav g = new GKlav(tileMap, dir);
		enemyProjectiles.add(g);
	}
	
	private void shootCirkel(){
		for(int i = 0; i < 5; i++){
			addRandNot().shootAngle(225+22.5*i);
		}
	}
	
	private void shootFallingNot(){
		EnemyProjectile ep = addRandNot();
		double rand = (Math.random()*(16*30-(37.5*2)))+37.5;
		ep.setPosition(rand, -15);
		ep.shootAngle(270);
	}
	
	private void shootSnedNot(){
		double randy = Math.random()*4*30+(10*30-5*30);
		int randDir = (int)(Math.random()*2);
		EnemyProjectile ep = addRandNot();
		ep.setSpeed(2.2);
		if(randDir == 0){
			ep.setPosition(-15, randy);
			ep.shootAngle(0);
		}else{
			ep.setPosition(16*30+15, randy);
			ep.shootAngle(180);
		}
	}
	
	private EnemyProjectile addRandNot(){
		return (int)(Math.random()*2) == 0 ? addDubbelnot() : addEnkelnot();
	}
	
	private DubbelnotProjectile addDubbelnot(){
		DubbelnotProjectile dn = new DubbelnotProjectile(tileMap);
		dn.setPosition(x, y);
		enemyProjectiles.add(dn);
		return dn;
	}
	
	private EnkelnotProjectile addEnkelnot(){
		EnkelnotProjectile en = new EnkelnotProjectile(tileMap);
		en.setPosition(x, y);
		enemyProjectiles.add(en);
		return en;
	}
	
	private void spawnDelatMedNoll(){
		if(delatMedNoll != null) return;
		delatMedNoll = new DelatMedNollItem(tileMap, effects);
		delatMedNoll.setPosition(x,  y);
		double angle = Math.random()*180+180;
		delatMedNoll.shootAngle(angle);
		throwableItems.add(delatMedNoll);
	}
	
	private void spawnKeyboard(){
		//keyboard = new Keyboard(tileMap);
		keyboard.setPosition(x, y);
		
		for(int i = 0; i <= 6; i++){
			effects.add(new Effect(tileMap, (int)(x-90+30*i), (int)(y+40), Content.Smoke1, 5));
		}
	}
	
	private void spawnTimer(){
		//timer = new MattiasCounter(tileMap);
		timer.start();
	}
	
	private void phase_1_attack_1(){
		if(counter % 10 == 0){
			if(p_1_1[4] == 0){ //minus
				p_1_1[2] -= p_1_1[3]; //steppa till vänster
				if(p_1_1[2] < p_1_1[0]){ //om den är över gränsen
					p_1_1[4] = 1; //byt håll
				}
			}else if(p_1_1[4] == 1){ //plus
				p_1_1[2] += p_1_1[3]; //steppa till höger
				if(p_1_1[2] > p_1_1[1]){ //om den är över gränsen
					p_1_1[4] = 0; //byt håll
				}
			}
			
			for(int i = p_1_1[2]; i < 360; i += 60){
				shootE(i, 1);
			}
		}
	}
	
	private void phase_2_attack_1(){
		if(counter == 1){
			setWobble(true);
			//spawna dem
			//->
			p_2_1_left = new SineWave(tileMap, true);
			p_2_1_left.setY(9*30-100-5+1);
			enemyProjectiles.add(p_2_1_left);
			
			//<-
			p_2_1_right = new SineWave(tileMap, false);
			p_2_1_right.setY(9*30+1);
			enemyProjectiles.add(p_2_1_right);
			
		}
	}
	
	public void kill(){
		dead = true;
	}
	
	@Override
	public void die() {
		for(int i = 0; i < 2; i++){
			effects.add(new Effect(tileMap, (int)(x-25+50*i), (int)(y), Content.Explosion2, 8));
		}
	}
	
	//spelar introsekvensen, typ som ett event
	private void introSequence(){
		counter++;
		if(counter == 1){
			JukeBox.play("mattias_laugh");
			setAnimation(ANIMATION_LAUGHS);
			setPosition(-30, 60);
			dx = 4;
			dy = 0.7;
		}else if(counter == 160){
			setPosition(GamePanel.WIDTH+30, 10);
			dx = -3.4;
			dy = 1.1;
		}else if(counter == 340){
			setPosition(8*30, -50);
			dx = 0;
			dy = 2;
		}else if(counter > 340 && counter < 340+120){
			dy -= 0.01597;
		}else if(counter == 340+120){
			setPosition(restingPos);
			dy = 0;
			counter = 0;
			intro = false;
			setWobble(true);
			setAnimation(ANIMATION_NORMAL);
		}
	}

}

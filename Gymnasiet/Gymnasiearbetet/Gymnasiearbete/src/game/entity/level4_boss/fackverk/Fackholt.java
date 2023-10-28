package game.entity.level4_boss.fackverk;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.MapObject;
import game.entity.Player;
import game.entity.Scenery;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileAccel;
import game.entity.enemies.enemyProjectile.EnemyProjectileCircle;
import game.entity.enemies.enemyProjectile.patterns.PatternFlower;
import game.handlers.Calc;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Fackholt extends FlygandeObject {
	
	private LeftHand leftHand;
	private RightHand rightHand;
	private HealthBar healthBar;
	
	private boolean defeated = false;
	private boolean started = false;
	private boolean intro = false;
	
	//animations
	private Animation[] animations;
	private final int ANIMATION_NORMAL = 0;
	private final int ANIMATION_LASER = 1;
	
	private int curAction = ANIMATION_NORMAL;
	
	private boolean laser = false;
	private boolean devil = false;
	
	private Animation diadem;
	
	//phases
	private final int PHASE_NOTHING = 0;
	private final int PHASE_1 = 1;
	private final int PHASE_2 = 2;
	private final int PHASE_HELL = 3;
	private final int PHASE_3 = 4;
	
	private int currentPhase = PHASE_NOTHING;
	
	private int counter = 0;
	
	//0 = spiral, 1 = random, 2 = targeted, 3 = cool spiral
	private int[] counters = {-1, -1, -1, -1}; //counters för huvudets attacker;
	private double spiralCur = 180;
	
	private double coolSpiralCur = 180;
	private int coolAntal = 1;
	
	private boolean handsOnMe = true;
	private boolean handsSnurra = false;
	private double snurraAng = 0;
	private double snurraRadius = 100;
	
	private Scenery pentagramScen;
	private PatternFlower flower;
	
	//random attacks
	private int randomAttack = -1;
	private int lastRandomAttack = -1;
	private final int randomAttackDelay = 20;
	private final int guaranteedDmg = 3; //3
	private int nonDmgAttacks = 0;
	private boolean[][] dmgAttacks = {
			{false, true, false, false}, //phase 1
			{true, false, false, true}, //phase 2
			{true, false, false, true}, //phase 3
	};
	
	//laser
	private int laserDelay, laserCounter, laserFastest;
	private boolean laserV = true;

	public Fackholt(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<Effect> ef) {
		super(tm, ep, player, ef);
		
		width = 82;
		height = 94;
		cwidth = 82;
		cheight = 94;
		
		facingRight = true;
		
		health = maxHealth = 33; //multipel av 3
		
		moveSpeed = maxSpeed = stopSpeed = 1;
		
		//init animationer
		BufferedImage[][] sheet = Content.load("/Resources/Sprites/Enemies/alsenholt2/alsenholt.png", 82, 94, new int[]{1, 1, 1});
		animations = new Animation[2];
		animations[ANIMATION_NORMAL] = new Animation(sheet[0], -1);
		animations[ANIMATION_LASER] = new Animation(sheet[1], -1);
		
		
		diadem = new Animation(Content.loadRow("/Resources/Sprites/Enemies/alsenholt2/diadem.png", 82, 94, 0, 1), -1);
		//animation = new Animation();
		//animation.setEmpty();
		setAnimation(ANIMATION_NORMAL);
		
		restingPos = new Point(25*30, 13*30);
		
		leftHand = new LeftHand(tileMap, ep, player, ef);
		rightHand = new RightHand(tileMap, ep, player, ef);
		healthBar = new HealthBar(maxHealth);
		
		pentagramScen = new Scenery(tileMap, restingPos.getX(), restingPos.getY(), "/Resources/Sprites/Enemies/alsenholt2/pentagram.png", 128, 128);
		pentagramScen.hide();
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(animations[curAction]);
		
	}
	
	private void setPentagram(boolean b){
		if(!b){ 
			//pentagramScen.setPosition(-300, -300);
			pentagramScen.fadeOut();
			setFloat(true);
		}else{
			pentagramScen.fadeIn();
			setFloat(false);
			goHome();
		}
	}
	
	public void playIntro(){intro = true;}
	public boolean isDonePlayIntro(){return !intro;}
	public boolean isDefeated(){return defeated;}
	
	public void begin(){
		started = true;
		currentPhase = PHASE_1; //PHASE_1
		setFloat(true);
		leftHand.begin();
		rightHand.begin();
	}
	
	@Override
	public void getNextPosition() {
		super.getNextPosition();
	}
	
	@Override
	public void update(Section s) {
		//animation.update();
		
		if(handsSnurra){
			snurraAng += -0.7;
		}
		
		if(leftHand != null){
			leftHand.update(s);
			if(handsOnMe) leftHand.setPosition(x-45, y+20);
			if(handsSnurra) leftHand.setPosition(Calc.getCirclePoint(x, y, snurraRadius, snurraAng+180));
		}
		
		if(rightHand != null){
			rightHand.update(s);
			if(handsOnMe) rightHand.setPosition(x+50, y+20);
			if(handsSnurra) rightHand.setPosition(Calc.getCirclePoint(x, y, snurraRadius, snurraAng));
		}
		
		healthBar.setHealth(health);
		
		if(intro){
			introSequence();
		}
		
		//update position
		super.update(s);

		if(!started) return;
		
		checkPhases();
		
		counter++;
		
		checkAttacks();
		checkBackgroundAttacks();
		
		//uppdatera huvudets attacker
		shootSpiral();
		shootRandom();
		shootPlayer();
		shootCoolSpiral();
		
		//uppdatera allt
		if(currentPhase == PHASE_1){ //PHASE 1
			if(randomAttack == 0){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_RAND_ATTACK);
					getNothingHand().changePhase(Hand.PHASE_SHOOT_RANDOM);
				}else if(randomEnd(300)){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 1){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_SMASH_PLAYER);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 2){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_STRAFE_SHOOT);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 3){
				if(counter <= 401){
					diginovablixt();
				}else if(counter >= 541){
					randomAttack = -1;
				}
			}
		}else if(currentPhase == PHASE_2){
			if(randomAttack == 0){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_SLAP);
				}else if(counter == 210){
					getFreeHand().changePhase(Hand.PHASE_SHOOT_FALLING);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 1){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_PAC_MAN);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 2){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_RAND_ATTACK);
					getNothingHand().changePhase(Hand.PHASE_SHOOT_SPLITTA);
				}else if(randomEnd(300)){
					randomAttack = -1;
					setHandsToNothing();
				}
			}else if(randomAttack == 3){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_SHOOT_RINGAR);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
			}
		}else if(currentPhase == PHASE_HELL){
			if(counter == 20){
				devil = true;
			}else if(counter == 100){
				setPentagram(true);
			}else if(counter == 150){
				spawnFlower();
			}else if(counter == 300){
				leftHand.stop();
				rightHand.stop();
				leftHand.gotoDestination(Calc.getCirclePoint(x, y, snurraRadius, 180));
				rightHand.gotoDestination(Calc.getCirclePoint(x, y, snurraRadius, 0));
			}else if(counter == 400){
				flower.snurra(1);
				flower.startSnurr();
				handsSnurra = true;
			}else if(counter == 460){
				leftHand.changePhase(Hand.PHASE_HELL_CIRCLE);
				rightHand.changePhase(Hand.PHASE_HELL_CIRCLE);
			}else if(counter == 461){
				if(!bothHandsCanChange()){
					counter--;
				}
			}else if(counter == 480){
				flower.shootAll();
			}else if(counter == 760){
				counters[3] = 0;
				coolAntal = 1;
			}else if(counter == 800){
				leftHand.changePhase(Hand.PHASE_HELL_COOL);
				rightHand.changePhase(Hand.PHASE_HELL_COOL);
			}else if(counter == 1060){
				coolAntal = 2;
			}else if(counter == 1580){
				coolAntal = 9;
				leftHand.setPhaseNothing();
				rightHand.setPhaseNothing();
			}else if(counter == 2000){
				counters[3] = -1;
				handsSnurra = false;
				leftHand.goHome();
				rightHand.goHome();
			}else if(counter == 2001){
				if(leftHand.isGotoDestination() || rightHand.isGotoDestination())
					counter--;
			}else if(counter == 2100){ //3600
				initLaserkanon(80, 60);
				leftHand.setFloat(true);
				rightHand.setFloat(true);
			}else if(counter > 2100 && counter <= 2100+1200){
				laserkanon();
			}else if(counter == 2100+1200+200){
				setPentagram(false);
				resetPhase();
				currentPhase = PHASE_3;
				counters[0] = 0;
			}
		}else if(currentPhase == PHASE_3){
			if(randomAttack == 0){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_CORNER_SHIELD);
					getFreeHand().changePhase(Hand.PHASE_SHOOT_SPLITTA);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				} 
			}else if(randomAttack == 1){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_SHOOT_BEAMS);
				}else if(counter == 20){
					getNothingHand().changePhase(Hand.PHASE_SHOOT_BEAMS);
				}else if(randomEnd(600)){
					randomAttack = -1;
					setHandsToNothing();
				} 
			}else if(randomAttack == 2){
				if(counter % 130 == 0){
					shootRing2();
				}else if(randomEnd(500)){
					randomAttack = -1;
				} 
			}else if(randomAttack == 3){
				if(counter == 1){
					getRandomHand().changePhase(Hand.PHASE_CORNER_SHIELD);
					getFreeHand().changePhase(Hand.PHASE_CORNER_SHIELD);
				}else if(bothHandsCanChange()){
					randomAttack = -1;
					setHandsToNothing();
				}
				if(counter % 60 == 0){
					double pos = Math.random()*(14*30-25)+18*30+12.5;
					spawnBlixt(pos);
					//spawnLaserH((11+9)*30-15, 60, 26);
				}
			}
		}
		
		//System.out.println("rand "+randomAttack+" : counter "+counter+" : "+leftHand.canChange()+" "+rightHand.canChange());
		
		//update animations
		if(laser){
			if(curAction != ANIMATION_LASER){
				setAnimation(ANIMATION_LASER);
			}
		}else{
			if(curAction != ANIMATION_NORMAL){
				setAnimation(ANIMATION_NORMAL);
			}
		}
		
	}
	
	private Hand getRandomHand(){
		int a = (int)(Math.random()*2);
		return a == 0 ? leftHand : rightHand;
	}
	
	private Hand getFreeHand(){
		if(leftHand.canChange())
			return leftHand;
		else if(rightHand.canChange())
			return rightHand;
		return null;
	}
	
	private Hand getNothingHand(){
		if(leftHand.doesNothing())
			return leftHand;
		else if(rightHand.doesNothing())
			return rightHand;
		return null;
	}
	
	private void setHandsToNothing(){
		leftHand.setPhaseNothing();
		rightHand.setPhaseNothing();
	}
	
	private boolean bothHandsCanChange(){
		return leftHand.canChange() && rightHand.canChange();
	}
	
	private boolean randomEnd(int minDuration){
		return randomEnd(minDuration, counter);
	}
	
	private boolean randomEnd(int minDuration, int counter){
		if(counter >= minDuration){
			double p = (counter-minDuration)*(1.0/(300)); //300 tick efter minDuration är det 1%
			if((Math.random()*100) <= p){
				return true;
			}
		}
		return false;
	}
	
	private void checkBackgroundAttacks(){
		if(currentPhase == PHASE_1){
			if(randomAttack != 3){
				if(counters[2] <= -1){ //laser
					counters[2]--;
					if(randomEnd(450, Math.abs(counters[2]))){ //random starta
						counters[2] = 0;
					}
				}
				
				if(counters[0] <= -1){
					counters[0]--;
					if(randomEnd(300, Math.abs(counters[0]))){ //random starta
						counters[0] = 0;
					}
				}else{
					if(randomEnd(250, Math.abs(counters[0]))){ //random sluta
						counters[0] = -1;
					}
				}
			}else{
				counters[0] = -1;
			}
		}else if(currentPhase == PHASE_2){
			if(counters[1] <= -1){
				counters[1]--;
				if(randomEnd(400, Math.abs(counters[1]))){ //random starta
					counters[1] = 0;
				}
			}else{
				if(randomEnd(350, Math.abs(counters[1]))){ //random sluta
					counters[1] = -1;
				}
			}
		}else if(currentPhase == PHASE_3){
			if(counters[2] <= -1){ //laser
				counters[2]--;
				if(randomEnd(550, Math.abs(counters[2]))){ //random starta
					counters[2] = 0;
				}
			}
			
			if(counters[1] >= 0){
				if(randomEnd(400, Math.abs(counters[1]))){ //random sluta
					counters[1] = -1;
					counters[0] = 0;
				}
			}
			if(counters[0] >= 0){
				if(randomEnd(400, Math.abs(counters[0]))){ //random sluta
					counters[0] = -1;
					counters[1] = 0;
				}
			}
		}
	}
	
	private void checkAttacks(){
		//hitta random attack
		if(randomAttack == -1){
			counter = 1;
			randomAttack = -2;
		}
		if(randomAttack == -2){
			if(counter >= randomAttackDelay){
				randomAttack = -3;
			}
		}
		if(randomAttack == -3){
			counter = 1;
			int nummer = 0;
			int dmgIndex = 0;
			if(currentPhase == PHASE_1){
				nummer = 4;
				dmgIndex = 0;
			}else if(currentPhase == PHASE_2){
				nummer = 4;
				dmgIndex = 1;
			}else if(currentPhase == PHASE_3){
				nummer = 4;
				dmgIndex = 2;
			}
			
			do{
				randomAttack = (int)(Math.random()*nummer);
			}while(randomAttack == lastRandomAttack || (nonDmgAttacks >= guaranteedDmg ? !dmgAttacks[dmgIndex][randomAttack] : false));
			//om vi ska ha en garanterad dmg så fortsätter vi sålänge arrayen ger oss false
			//System.out.println(randomAttack);
			lastRandomAttack = randomAttack;
			
			if(dmgAttacks[dmgIndex][randomAttack]){
				nonDmgAttacks = 0;
			}else{
				nonDmgAttacks++;
			}
			
		}
	}
	
	private void checkPhases(){
		if(currentPhase == PHASE_1 && health <= maxHealth*(2/3.0) && randomAttack == -1){
			resetPhase();
			currentPhase = PHASE_2;
		}else if(currentPhase == PHASE_2 && health <= maxHealth*(1/3.0) && randomAttack == -1){
			resetPhase();
			currentPhase = PHASE_HELL;
		}
	}
	
	private void resetPhase(){
		resetBackgroundAttacks();
		randomAttack = lastRandomAttack = -1;
		counter = 0;
	}
	
	private void resetBackgroundAttacks(){
		for(int i = 0; i <  counters.length; i++){
			counters[i] = -1;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		
		super.draw(g);
		/*
		g.setColor(Color.BLACK);
		g.fillRect((int)(xmap+x-width/2), (int)(ymap+y-height/2), width, height);
		*/
		
		if(leftHand != null)
			leftHand.draw(g);
		
		if(rightHand != null)
			rightHand.draw(g);
		
		pentagramScen.draw(g);
		
		if(started)
			healthBar.draw(g);
		
		if(devil)
			g.drawImage(diadem.getImage(), (int)(xmap+x-diadem.getWidth()/2), (int)(ymap+y-diadem.getHeight()/2-7), null); //blev inte exakt av någon anledning (y alltså)
	}

	@Override
	public boolean attacks(Player p) {
		return super.attacks(p) || leftHand.attacks(p) || rightHand.attacks(p);
	}
	
	@Override
	public boolean intersects(MapObject o) {
		return super.intersects(o) || leftHand.intersects(o) || rightHand.intersects(o);
	}
	
	@Override
	public boolean intersects(Rectangle r) {
		return super.intersects(r) || leftHand.intersects(r) || rightHand.intersects(r);
	}
	
	@Override
	public void hit(int damage) {
		if((currentPhase == PHASE_1 && health > (2/3.0)*maxHealth) ||
			(currentPhase == PHASE_2 && health > maxHealth*(1/3.0)) ||
			(currentPhase == PHASE_3 && health > 0)
		){
			//flasha rött
			health -= damage;
			if(health < 0) health = 0;
			if(health == 0) setDefeated();
		}
	}
	
	private void setDefeated(){
		defeated = true;
		effects.add(new Effect(tileMap, (int)(leftHand.getx()), (int)(leftHand.gety()), Content.Explosion2, 8));
		effects.add(new Effect(tileMap, (int)(rightHand.getx()), (int)(rightHand.gety()), Content.Explosion2, 8));
		leftHand.setDefeat();
		rightHand.setDefeat();
		currentPhase = PHASE_NOTHING;
		started = false;
		setFloat(false);
		goHome();
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
			goHome();
			handsOnMe = true;
		}else if(counter == 2){
			if(shootDestination){
				counter--;
			}
		}else if(counter == 62){
			handsOnMe = false;
			leftHand.goHome();
			rightHand.goHome();
		}else if(counter == 92){
			intro = false;
			counter = 0;
		}
	}
	
	////////ATTACKER///////
	private void shootRing2(){
		for(double i = 0; i < 360; i+=(360.0/10)){
			for(int j = 0; j < 2; j++){
				EnemyProjectileCircle e = new EnemyProjectileCircle(cloneProj1);
				e.setPosition(restingPos);
				e.shootCircle(0, i+30);
				e.setVinkelSpeed(0.4 * (j == 0 ? 1 : -1));
				e.setExpand(1.7);
				enemyProjectiles.add(e);
			}
		}
	}
	
	private void initLaserkanon(int start, int fastest){
		laserDelay = start;
		laserFastest = fastest;
		laserCounter = 0;
	}
	
	private void laserkanon(){ //start(laserdelay) 130; laserFastest 80:
		laserCounter++;
		if(laserCounter == laserDelay){
			if(laserDelay > laserFastest)
				laserDelay -= 3;
			laserCounter = 0;
			boolean on = (int)(Math.random()*2) == 0 ? true : false;
			int d = (int)(laserDelay-26);
			int e = 26;//(int)(laserDelay*(1/3.0)); 
			if(laserV){
				if(on){
					spawnMassaLaserV(player.getx(), d, e);
				}else{
					spawnMassaLaserV(player.getx()-30, d, e);
				}
			}else{
				if(on){
					spawnLaserH((11+9)*30-15, d, e);
				}else{
					spawnLaserH((11+7)*30, d, e);
				}
			}
			laserV = !laserV;
		}
	}
	
	private void spawnMassaLaserV(double start, int delay, int expiration){
		double space = 60;
		//åt höger
		for(double i = start; i < 17*30+460; i+=space){
			spawnLaserV(i, delay, expiration);
		}
		//åt vänster
		for(double i = start-space; i > 17*30; i-=space){
			spawnLaserV(i, delay, expiration);
		}
	}
	
	private void diginovablixt(){
		if((counter-1) % 100 == 0){
			//slumpa några hål av 16 platser
			boolean[] pla = new boolean[16]; //true betyder tom
			int utbytta = 0;
			do{
				int d = (int)(Math.random()*16);
				if(!pla[d]){ //inte tagen
					pla[d] = true;
					utbytta++;
				}
			}while(utbytta < 5);
			
			//spawn dem
			for(int i = 0; i < pla.length; i++){
				if(!pla[i]){
					spawnBlixt(18*30+12.5+10+25*i);
				}
			}
		}
	}
	
	private void spawnFlower(){
		PatternFlower pf = new PatternFlower(cloneProj1, enemyProjectiles);
		pf.spawnFlower(player, restingPos.getX(), restingPos.getY());
		enemyProjectiles.add(pf);
		flower = pf;
	}
	
	private void shootSpiral(){
		if(counters[0] <= -1) return;
		counters[0]++;
		if(counters[0] % 14 == 0){
			spiralCur += 25;
			EnemyProjectile e = getProj1();
			e.setPosition(restingPos);
			e.setSpeed(3);
			e.shootAngle(spiralCur);
		}
	}
	
	private void shootRandom(){
		if(counters[1] <= -1) return;
		counters[1]++;
		if(counters[1] % 15 == 0){
			EnemyProjectile e = getProj1();
			double rand = Math.random()*360;
			e.setPosition(restingPos);
			e.setSpeed(2);
			e.shootAngle(rand);
		}
	}
	
	private void shootPlayer(){
		if(counters[2] <= -1) return;
		counters[2]++;
		if(counters[2] == 1){
			laser = true;
		}else if(counters[2] % 2 == 0 && counters[2] >= 40){
			for(double i = restingPos.getX()-5; i <= restingPos.getX()+19; i+=24){
				EnemyProjectile e = getProj1();
				e.setSpeed(3.8);
				e.setPosition(i, restingPos.getY()-4);
				e.shootTarget(player.getx(), player.gety());
			}
		}
		if(counters[2] >= 90){
			counters[2] = -1;
			laser = false;
		}
	}
	
	private void shootCoolSpiral(){
		if(counters[3] <= -1) return;
		counters[3]++;
		if(counters[3] % 5 == 0){
			coolSpiralCur += 20;
			for(double i = 0; i < 360; i+=(360/coolAntal)){
				EnemyProjectileAccel e = new EnemyProjectileAccel(cloneProj1);
				e.setSpeed(2.3);//2.5
				e.setPosition(restingPos);
				e.setAccelerationConstant(Calc.fixAngle(coolSpiralCur+i+180), 0.05, 2.7);
				e.shootAngle(coolSpiralCur+i);
				enemyProjectiles.add(e);
			}
		}
	}
	
}

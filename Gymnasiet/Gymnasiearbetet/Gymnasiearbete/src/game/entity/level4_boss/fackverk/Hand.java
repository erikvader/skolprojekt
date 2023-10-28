package game.entity.level4_boss.fackverk;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.Player;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileCircle;
import game.entity.enemies.enemyProjectile.EnemyProjectileGravity;
import game.entity.enemies.enemyProjectile.EnemyProjectileSine;
import game.entity.enemies.enemyProjectile.EnemyProjectileWobbly;
import game.entity.enemies.enemyProjectile.patterns.PatternBeam1;
import game.entity.enemies.enemyProjectile.patterns.PatternBeam2;
import game.entity.enemies.enemyProjectile.patterns.PatternCircle;
import game.entity.enemies.enemyProjectile.patterns.PatternRing;
import game.entity.enemies.enemyProjectile.patterns.PatternSplittaRing;
import game.handlers.Calc;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Hand extends FlygandeObject{
	
	//animation
	private Animation[] animations;
	public static final int ANIMATION_NORMAL = 0;
	public static final int ANIMATION_FIST = 1;
	
	private int curAction = ANIMATION_NORMAL;
	
	//phase
	public static final int PHASE_NOTHING = 0;
	public static final int PHASE_RAND_ATTACK = 1; //står stilla i sin home och skjuter något random
	public static final int PHASE_SMASH_PLAYER = 2;
	public static final int PHASE_STRAFE_SHOOT = 3;
	public static final int PHASE_SLAP = 4;
	public static final int PHASE_CORNER_SHIELD = 5;
	public static final int PHASE_PAC_MAN = 6; //från I wanna kill the guy
	public static final int PHASE_SHOOT_SPLITTA = 7;
	public static final int PHASE_SHOOT_FALLING = 8;
	public static final int PHASE_SHOOT_RANDOM = 9;
	public static final int PHASE_SHOOT_RINGAR = 10;
	public static final int PHASE_SHOOT_BEAMS = 11;
	public static final int PHASE_HELL_CIRCLE = 12;
	public static final int PHASE_HELL_COOL = 13;
	public static final int PHASE_SINUS_LUFTUS = 14;
	
	private int curPhase = PHASE_NOTHING;
	
	protected boolean isLeft;
	private boolean canChange = true;
	private int counter = 0;
	
	//saker till rand attack
	private int rand_last = -1, rand_cur = -1, rand_amount = 3;
	
	private double orgMoveSpeed;
	
	//PHASE STRAFE SHOOT
	protected Point2D.Double pointStrafeShoot1;
	protected Point2D.Double pointStrafeShoot2;
	
	//PHASE_CORENER_SHIELD
	private ArrayList<EnemyProjectileCircle> shield;
	
	//PHASE_SHOOT_FALLING
	protected double leftSpeed, rightSpeed;
	
	//PHASE_SHOOT_WAVE
	//protected double waveInterval, waveStart;
	
	//PHASE_SHOOT_RANDOM
	protected double randStart;
	
	//PHASE_HELL_CIRCLE
	protected double hellSpiralCur = 0;
	
	public Hand(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<Effect> ef) {
		super(tm, ep, player, ef);
		
		setPosition(-500, -500);
		
		cwidth = width = 50;
		cheight = height = 50;
		
		animation = new Animation();
		animations = new Animation[2];
		BufferedImage[][] sheet = Content.load("/Resources/Sprites/Enemies/alsenholt2/hand.png", 50, 50, new int[]{1, 1});
		animations[ANIMATION_NORMAL] = new Animation(sheet[0], -1);
		animations[ANIMATION_FIST] = new Animation(sheet[1], -1);
		setAnimation(ANIMATION_NORMAL);
		
		//animation.setEmpty();
		
		moveSpeed = orgMoveSpeed = 1;
		damage = 1;
		
		shield = new ArrayList<EnemyProjectileCircle>();
		
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(animations[curAction]);
		
		//ändra hitbox
		if(curAction == ANIMATION_NORMAL){
			setHitbox(47, 50);
		}else if(curAction == ANIMATION_FIST){
			setHitbox(35, 34);
		}
		
	}
	
	public boolean canChange(){
		return canChange;
	}
	
	public boolean doesNothing(){
		return curPhase == PHASE_NOTHING;
	}
	
	public void begin(){
		setFloat(true);
	}
	
	public void changePhase(int phase){
		if(canChange){
			curPhase = phase;
			counter = 0;
			
			if(phase == PHASE_RAND_ATTACK){
				rand_cur = -1;
			}
			
			//can change
			switch(phase){
			case 2: case 3: case 4: case 5: case 6: case 10: case 12:
				canChange = false;
				break;
			}
		}
	}
	
	public void setPhaseNothing(){
		changePhase(PHASE_NOTHING);
	}
	
	public void stop(){
		setFloat(false);
	}
	
	public void setDefeat(){
		setPosition(-500, -500);
		setFloat(false);
		curPhase = PHASE_NOTHING;
		counter = 0;
		canChange = false;
	}
	
	@Override
	public void update(Section s) {
		super.update(s);
		
		counter++;
		
		if(curPhase == PHASE_NOTHING){
			
		}else if(curPhase == PHASE_RAND_ATTACK){
			if(rand_cur == -1){
				new_rand_attack();
			}else{//should reset
				int d = 300;
				if(counter >= d){
					double p = (counter-d)*(1.0/(600-d)); //på tick 600 är det 1%
					if((Math.random()*100) <= p){
						counter = 0;
						rand_cur = -1;
					}
				}
			}
			
			if(rand_cur == 0){
				if(counter % 80 == 0){
					shootAtPlayer();
				}
			}else if(rand_cur == 1){
				if(counter % 80 == 0){
					shootBow();
				}
			}else if(rand_cur == 2){
				if((counter-1) % 90 == 0 || (counter-1) % 90 == 5 || (counter-1) % 90 == 10){
					shootWobb();
				}
			}
		}else if(curPhase == PHASE_SMASH_PLAYER){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 2.7;
				setAnimation(ANIMATION_FIST);
			}else if(counter >= 30 && counter <= 300){
				gotoDestination(player.getx(), 465); //player.gety()-120
			}else if(counter == 301){
				stopShootDestination();
				stop();
				dy = -0.5;
				dx = 0;
			}else if(counter == 330){
				moveSpeed = 6;
				gotoDestination(x, (11+9)*30-cheight/2);
			}else if(counter >= 352 && counter <= 352+5*5 && (counter-352) % 5 == 0){ //5*x : x=antal
				shootSinusSidus();
			}else if(counter == 610){//åk hem
				moveSpeed = 2.5;
				dy = -1.5;
			}else if(counter == 680){
				goHome();
			}else if(counter == 681){
				if(shootDestination){
					counter--;
				}
			}else if(counter == 690){
				setFloat(true);
				moveSpeed = orgMoveSpeed;
				canChange = true;
				changePhase(PHASE_NOTHING);
				setAnimation(ANIMATION_NORMAL);
			}
		}else if(curPhase == PHASE_STRAFE_SHOOT){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 2;
			}else if(counter == 20){
				gotoDestination(pointStrafeShoot1);
			}else if(counter == 80){
				moveSpeed = 0.9;
				gotoDestination(pointStrafeShoot2);//åk till andra sidan
			}else if(counter >= 81 && counter <= 481){ //tog fram dessa värden genom experiment
				if(counter % 60 == 0){ //35
					//shootStrafe(1);
					shootBow();
				}
			}else if(counter == 500){
				gotoDestination(pointStrafeShoot1);
			}else if(counter >= 501 && counter <= 901){//experiment
				if(counter % 60 == 0){ //35
					//shootStrafe(-1);
					shootBow();
				}
			}else if(counter == 950){
				moveSpeed = 1.5;
				goHome();
			}else if(counter == 951){
				if(shootDestination)
					counter--;
			}else if(counter == 952){
				moveSpeed = orgMoveSpeed;
				canChange = true;
				setFloat(true);
				changePhase(PHASE_NOTHING);
			}
		}else if(curPhase == PHASE_SLAP){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 1;
				setAnimation(ANIMATION_FIST);
			}else if(counter == 18){
				gotoDestination(restingPos.getX(), restingPos.getY()-40);
			}else if(counter == 19){
				if(shootDestination)
					counter--;
			}else if(counter == 20){
				moveSpeed = 4;
				gotoDestination(restingPos.getX(), (11+9)*30-cheight/2.0);
			}else if(counter == 21){
				if(shootDestination)
					counter--;
			}else if(counter == 22){
				shootSide(1);
				shootSide(-1);
			}else if(counter == 100){
				moveSpeed = 2.6;
				gotoDestination(pointStrafeShoot2.getX(), (11+9)*30-cheight/2.0);
			}else if(counter >= 101 && counter <= 200){ //experiment. till andra sidan
				if(counter % 20 == 0){
					slapShoot();
				}
			}else if(counter == 400){
				moveSpeed = 1.5;
				gotoDestination(x, y-60);
			}else if(counter == 401){
				if(shootDestination)
					counter--;
			}else if(counter == 402){
				moveSpeed = 2.6;
				goHome();
				setAnimation(ANIMATION_NORMAL);
			}else if(counter == 403){
				if(shootDestination)
					counter--;
			}else if(counter == 404){
				moveSpeed = orgMoveSpeed;
				canChange = true;
				setFloat(true);
				changePhase(PHASE_NOTHING);
			}
				
		}else if(curPhase == PHASE_CORNER_SHIELD){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 1;
				setAnimation(ANIMATION_FIST);
			}else if(counter == 2){
				gotoDestination(restingPos.getX(), restingPos.getY()-40);
			}else if(counter == 3){
				if(shootDestination)
					counter--;
				else
					moveSpeed = 4;
			}else if(counter == 20){
				gotoDestination(pointStrafeShoot1.getX(), (11+9)*30-cheight/2);
			}else if(counter == 21){
				if(shootDestination)
					counter--;
			}else if(counter == 40){
				spawnShield();
			}/*else if(counter >= 41 && counter <= 700){
				if(counter % 70 == 0){
					int delta = (int)(Math.random()*(14*30-25));
					spawnBlixt(delta+18*30+12.5);
					//shootSplittRing();
				}
			}*/else if(counter == 501){
				removeShield();
			}else if(counter == 560){
				moveSpeed = 2;
				goHome();
			}else if(counter == 561){
				if(shootDestination) counter--;
			}else if(counter == 562){
				moveSpeed = orgMoveSpeed;
				canChange = true;
				setFloat(true);
				changePhase(PHASE_NOTHING);
				setAnimation(ANIMATION_NORMAL);
			}
		}else if(curPhase == PHASE_PAC_MAN){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 2;
			}else if(counter == 20){
				gotoDestination(pointStrafeShoot1);
			}else if(counter == 21){
				if(shootDestination) counter--;
			}else if(counter >= 22 && counter < 22+106*4){ //en 'runda' tar 106 ticks
				moveSpeed = 3.4;
				//åk fram och tillbaka (inte samtidigt, det vore konstigt)
				if((counter-22) % (106*2) == 0){
					gotoDestination(pointStrafeShoot2);
				}else if((counter-22) % (106*2) == 106){
					gotoDestination(pointStrafeShoot1);
				}
				
				//skjut saker
				if(counter % 12 == 0){
					shootPacMan();
				}
				
			}else if(counter == 22+106*4+20){
				moveSpeed = 1.5;
				goHome();
			}else if(counter == 22+106*4+20+1){
				if(shootDestination){
					counter--;
				}else{
					setFloat(true);
				}
			}else if(counter == 22+106*4+250){
				moveSpeed = orgMoveSpeed;
				canChange = true;
				changePhase(PHASE_NOTHING);
			}
		}else if(curPhase == PHASE_SHOOT_SPLITTA){
			if(counter % 100 == 0){
				shootSplittRing();
			}
		}else if(curPhase == PHASE_SHOOT_FALLING){
			if(counter % 12 == 0){
				shootFalling();
			}
		}else if(curPhase == PHASE_SHOOT_RANDOM){
			if(counter % 26 == 0){
				shootRandom();
			}
		}else if(curPhase == PHASE_SHOOT_RINGAR){
			if(counter == 1){
				setFloat(false);
				moveSpeed = 3;
			}else if(counter == 2){
				gotoDestination(pointStrafeShoot1.getX(), (11+9)*30-height/2-15);
			}else if(counter == 3){
				if(shootDestination)
					counter--;
			}else if(counter == 4){
				setFloat(true);
			}else if(counter >= 5 && counter <= 605 && (counter-5) % 200 == 0){
				shootRing();
			}else if(counter == 848){
				moveSpeed = 2.5;
				setFloat(false);
				gotoDestination(x, y-50);
			}else if(counter == 849){
				if(shootDestination) counter--;
			}else if(counter == 850){
				moveSpeed = 2.5;
				goHome();
			}else if(counter == 851){
				if(shootDestination) counter--;
			}else if(counter == 852){
				moveSpeed = orgMoveSpeed;
				canChange = true;
				setFloat(true);
				changePhase(PHASE_NOTHING);
			}
		}else if(curPhase == PHASE_SHOOT_BEAMS){
			if(counter >= 1 && counter % 119 == 0){
				int ran = (int)(Math.random()*2);
				if(ran == 0){
					shootBeam1();
				}else if(ran == 1){
					shootBeam2();
				}
			}
		}else if(curPhase == PHASE_HELL_CIRCLE){
			if(counter == 1){
			}else if(counter >= 2 && counter % 20 == 0 && counter <= 1500){
				shootSpiralHell();
			}else if(counter >= 1700 && counter % 4 == 0 && counter <= 2000){
				shootSpreadHell();
			}else if(counter >= 2200 && counter % 30 == 0 && counter <= 3700){
				shootBowHell();
			}else if(counter == 3800){
				canChange = true;
				setPhaseNothing();
			}
		}else if(curPhase == PHASE_HELL_COOL){
			if(counter % 159 == 0){
				shootBounce();
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
		
		/*
		setMapPosition();
		g.setColor(Color.BLACK);
		g.fillRect((int)(xmap+x-width/2), (int)(ymap+y-height/2), width, height);
		*/
	}
	
	////////////**ATTACKER**//////////
	private void new_rand_attack(){ //slumpar fram en ny attack till PHASE_RAND_ATTACK
		do{
			rand_cur = (int)(Math.random()*rand_amount);
		}while(rand_cur == rand_last);
		rand_last = rand_cur;
	}
	
	private void shootBounce(){
		EnemyProjectile e = getProj2();
		e.setPosition(x, y);
		e.setBounce = true;
		e.checkCollisionWithMap = true;
		e.setHitOnCollideMap = false;
		double rand = Math.random()*90 + 225;
		e.shootAngle(rand);
	}
	
	private void shootBowHell(){ //mera som shootRing
		PatternRing pr = //new PatternRing(tileMap, Content.laserBoll, -1, 14, 14, 2.4, enemyProjectiles);
				new PatternRing(cloneProj1, enemyProjectiles);
		pr.setHitOnPlayer = false;
		pr.setHitOnCollideMap = false;
		pr.checkCollisionWithMap = false;
		pr.setPosition(x, y);
		pr.shootRing(270, 360, 8);
		enemyProjectiles.add(pr);
	}
	
	private void shootSpreadHell(){
		double ang = Calc.calcDir(player.getx()-x, player.gety()-y, Calc.calcLength(getPosDouble(), player.getPosDouble()));
		double spread = Math.random()*4+2;
		for(int i = -1; i <= 1; i++){
			EnemyProjectile e = getProj1();
			e.setPosition(x, y);
			e.shootAngle(ang + i*spread);
		}
	}
	
	private void shootSpiralHell(){
		hellSpiralCur += 10;
		for(int i = 0; i < 4; i++){
			EnemyProjectile e = getProj1();
			e.setPosition(x, y);
			e.setSpeed(3);
			e.shootAngle(hellSpiralCur + i*90);
		}
		
	}
	
	private void shootBeam1(){
		PatternBeam1 e = new PatternBeam1(cloneProj1, enemyProjectiles);
		e.setPosition(x, y);
		e.shootBeam(player.getx(), player.gety(), 7, 2, 0.3);
		enemyProjectiles.add(e);
	}
	
	private void shootBeam2(){
		PatternBeam2 e = new PatternBeam2(cloneProj1, enemyProjectiles);
		e.setPosition(x, y);
		e.shootBeam(player, 7, 2, 0.3, 4);
		enemyProjectiles.add(e);
	}
	
	private void shootRing(){
		PatternCircle e = new PatternCircle(cloneProj1, enemyProjectiles);
		e.setPosition(x, y);
		e.setSpeed(3);
		e.shootCircle(40, 15, 70, player);
		enemyProjectiles.add(e);
	}
	
	private void shootRandom(){
		EnemyProjectile e = getProj1();
		e.setPosition(x, y);
		double rand = Math.random()*70+randStart;
		e.setSpeed(2.4);
		e.shootAngle(rand);
	}
	
	private void shootFalling(){
		EnemyProjectileGravity e = new EnemyProjectileGravity(cloneProj1);
		e.setPosition(x, y);
		double x = Math.random()*(rightSpeed-leftSpeed)+leftSpeed;
		e.setVector(x, -2.5);
		e.setGravity(0.05, 2.1);
		enemyProjectiles.add(e);
	}
	
	/*
	private void shootWave(){
		PatternBeam2 b = new PatternBeam2(cloneProj1, enemyProjectiles);
		b.setPosition(x, y);
		double a = Math.random()*waveInterval+waveStart;
		double s = Math.random()*7+5;
		s *= (int)(Math.random()*2) == 0 ? 1 : -1;
		b.shootBeamAngle(a, s, 5, 2.4, 0.3, 6);
		System.out.println(s);
		enemyProjectiles.add(b);
	}
	*/
	
	private void shootAtPlayer(){
		for(int i = -90; i <= 90; i+=90){
			EnemyProjectile e = getProj1(); //kanske göra detta till en pattern??
			e.setPosition(x, y);
			e.shootTarget(player.getx(), player.gety());
			if(i != 0){
				double v = e.getDir();
				Point2D.Double p = Calc.getCirclePoint(e.getx(), e.gety(), 10, v+i);
				e.setPosition(p.x, p.y);
			}
			
		}
	}
	
	private void shootBow(){ //mera som shootRing
		PatternRing pr = //new PatternRing(tileMap, Content.laserBoll, -1, 14, 14, 2.4, enemyProjectiles);
				new PatternRing(cloneProj1, enemyProjectiles);
		pr.setHitOnPlayer = false;
		pr.setHitOnCollideMap = false;
		pr.checkCollisionWithMap = false;
		pr.setPosition(x, y);
		int start = (int)(Math.random()*360);
		pr.shootRing(start, 360, 10);
		enemyProjectiles.add(pr);
	}
	
	private void shootWobb(){
		EnemyProjectileWobbly e = new EnemyProjectileWobbly(cloneProj1);
		e.setPosition(x, y);
		double dir = Calc.calcDir(player.getx()-x, player.gety()-y, Calc.calcLength(getPosDouble(), player.getPosDouble()));
		e.shootWobbly2(dir, 50, 5, true);
		enemyProjectiles.add(e);
	}
	
	private void shootSinusSidus(){
		for(double i = 0; i <= 180; i+=180){
			for(int j = -1; j <= 1; j+=2){
				EnemyProjectileSine e = new EnemyProjectileSine(cloneProj1);
				e.setSpeed(2);
				e.setOrgPosition(x, y-8);
				e.shootSine(i, 30, 120*j);
				enemyProjectiles.add(e);
			}
		}
	}
	
	//skräp
	/*
	private void shootStrafe(int dir){ //1 är skjut åt vänster, -1 är höger
		/*
		for(int i = 0; i < 3; i++){
			EnemyProjectile e = getProj1();
			e.setPosition(x, y);
			e.setSpeed(1.4);
			e.shootAngle((dir == 1 ? 130 : 310)+i*50);
		}
		
		PatternRing r = new PatternRing(cloneProj1, enemyProjectiles);
		r.setPosition(x, y);
		r.shootRing(0, 360, 10);
		enemyProjectiles.add(r);
	}
	*/

	private void shootSplittRing(){
		PatternSplittaRing r = new PatternSplittaRing(cloneProj1, new Animation(Content.laserBollStor, -1), enemyProjectiles);
		r.setPosition(x, y);
		double rx = 19*30+Math.random()*(10*30);
		int delay = 20+(int)(Math.random()*30);
		r.shootSplitta(8, delay, rx, (11+4)*30);
		enemyProjectiles.add(r);
	}
	
	private void shootSide(int dir){//1 är skjut åt vänster, -1 är höger
		for(int i = 0; i < 3; i++){
			EnemyProjectile e = getProj1();
			e.setPosition(x, 7+y-height/2+i*16.66);
			e.shootAngle(dir == 1 ? 180 : 0);
		}
	}
	
	private void slapShoot(){
		EnemyProjectile e = getProj1();
		e.setPosition(x, y);
		e.shootAngle(90);
		e.setDelay(60);
	}
	
	private void spawnShield(){
		for(int i = 0; i < 7; i++){
			EnemyProjectileCircle e = new EnemyProjectileCircle(cloneProj1);
			e.setPosition(x, y);
			e.shootCircle(40, -45+15*i);
			e.setVinkelSpeed(2.4 * (isLeft?1:-1));
			enemyProjectiles.add(e);
			shield.add(e);
		}
	}
	
	private void removeShield(){
		for(int i = 0; i < shield.size(); i++){
			EnemyProjectileCircle e = shield.get(i);
			e.stopCircle();
			//e.shootTarget(x+(isLeft ? 200 : -200), y);
			//double ang = Calc.calcDir(getPosDouble(), e.getPosDouble());
			e.shootAngle(90);
			//e.setDelay(40);
		}
		shield.clear();
	}
	
	private void shootPacMan(){
		EnemyProjectile e = getProj1();
		e.setSpeed(2.2);
		double d = Math.random()*31;
		e.setPosition(x, y-15+d);
		d = Math.random()*120;
		e.shootAngle(210+d);
		d = Math.random()*140;
		e.setDelay((int)(d+60));
	}
	///////////**INGA ATTACKER**//////
	
}

package game.entity.level3_boss;

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
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Johannes extends Enemy {
	
	private ArrayList<EnemyProjectile> enemyProjectiles;
	private Player player;
	private TextBoard textBoard;
	private Pekare pekare;
	private Kylare kylare;
	
	private boolean defeated = false;
	private boolean started = false;
	private boolean intro = false;
	
	//animations
	private Animation[] animations;
	private final int ANIMATION_NORMAL = 0;
	private final int ANIMATION_SMOKES = 1;
	
	private int curAction = ANIMATION_NORMAL;
	
	//phases
	private final int PHASE_NOTHING = 0;
	private final int PHASE_1 = 1;
	private final int PHASE_2 = 2;
	private final int PHASE_3 = 3;
	
	private int currentPhase = PHASE_NOTHING;
	
	private int counter = 0;
	
	private Point restingPos;
	
	//random attacks
	private int randomAttack = -1;
	private int lastRandomAttack = -1;
	private final int randomAttackDelay = 80;

	public Johannes(TileMap tm, ArrayList<EnemyProjectile> ep, Player player, ArrayList<Effect> ef) {
		super(tm, ef);
		
		enemyProjectiles = ep;
		this.player = player;
		
		width = 256;
		height = 162;
		cwidth = 256;
		cheight = 162;
		
		facingRight = true;
		
		health = maxHealth = 3;
		
		moveSpeed = maxSpeed = stopSpeed = 1;
		
		//init animationer
		BufferedImage[][] sheet = Content.load("/Resources/Sprites/Enemies/johannes/johannes.png", 256, 162, new int[]{1});
		animations = new Animation[2];
		animations[ANIMATION_NORMAL] = new Animation(sheet[0], -1);
		animations[ANIMATION_SMOKES] = animations[ANIMATION_NORMAL];
		
		setAnimation(ANIMATION_NORMAL);
		
		restingPos = new Point(8*30, 81);
		
		pekare = new Pekare(tileMap, ep, player);
		kylare = new Kylare(tileMap, ep, player);
		
	}
	
	private void setAnimation(int a){
		curAction = a;
	
		//set animation
		setObjectAnimation(animations[curAction]);
		
	}
	
	public void playIntro(){intro = true;}
	public boolean isDonePlayIntro(){return !intro;}
	public boolean isDefeated(){return defeated;}
	
	public void begin(){
		started = true;
		currentPhase = PHASE_1; //PHASE_1
		
		textBoard = new TextBoard(tileMap, player);
		textBoard.setPosition(restingPos.x, restingPos.y-50);
		textBoard.generateNewWord();
		
		pekare.begin();
		kylare.begin();
	}
	
	public void specialEfterSuperReset(){
		enemyProjectiles.add(kylare);
	}
	
	@Override
	public void getNextPosition() {
		
	}
	
	@Override
	public void update(Section s) {
		animation.update();
		
		if(pekare != null){
			pekare.update();
			pekare.setPosition(x-80, y+30);
		}
		
		if(kylare != null){
			kylare.offerPosition(x+84, y+30);
		}
		
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
		
		//update textBoard
		if(textBoard != null){
			textBoard.update(s);
			if(textBoard.wordIsComplete()){
				for(int i = 0; i < 2; i++){
					effects.add(new Effect(tileMap, (int)(x-25+50*i), (int)(y), Content.Explosion2, 8));
				}
				pekare.setIdle();
				health--;
				if(health == 0){
					textBoard.clearBoard();
					setDefeated();
				}else{
					textBoard.advanceInDifficulty();
					textBoard.generateNewWord();
					randomAttack = lastRandomAttack = -3;
					if(health == 2)
						currentPhase = PHASE_2;
					else if(health == 1)
						currentPhase = PHASE_3;
				}
			}
		}
		
		counter++;
		
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
			if(currentPhase == PHASE_1)
				nummer = 4;
			else if(currentPhase == PHASE_2)
				nummer = 4;
			else if(currentPhase == PHASE_3)
				nummer = 4;
			do{
				randomAttack = (int)(Math.random()*nummer);
			}while(randomAttack == lastRandomAttack);
			
			lastRandomAttack = randomAttack;
		}
		
		if(currentPhase == PHASE_1){
			switch(randomAttack){
			case 0:
				attack1_0();
				break;
			case 1:
				attack1_1();
				break;
			case 2:
				attack1_2();
				break;
			case 3:
				attack1_3();
				break;
			}
		}else if(currentPhase == PHASE_2){
			switch(randomAttack){
			case 0:
				attack2_0();
				break;
			case 1:
				attack2_1();
				break;
			case 2:
				attack2_2();
				break;
			case 3:
				attack2_3();
				break;
			}
		}else if(currentPhase == PHASE_3){
			switch(randomAttack){
			case 0:
				attack3_0();
				attack1_0();
				break;
			case 1:
				attack3_1();
				break;
			case 2:
				attack3_2();
				break;
			case 3:
				attack3_3();
				break;
			}
		}
		
	}
	
	/////////////attacks///////////
	private void attack3_3(){
		if(counter % 100 >= 0 && counter % 100 <= 20 && counter % 3 == 0 && counter <= 500){
			kylare.shootSpindel();
		}
		if(counter == 1){
			pekare.shootAtRandom(Pekare.FAST, Pekare.MODE_SINGLE);
		}
		if(counter == 500){
			pekare.setIdle();
		}else if(counter == 560){
			randomAttack = -1;
		}
	}
	
	private void attack3_2(){
		if(counter % 100 >= 0 && counter % 100 <= 20 && counter % 5 == 0){
			kylare.shootBeam2();
		}
		if(counter % 200 == 50){
			kylare.shootCircle();
		}
		if(counter == 500){
			randomAttack = -1;
		}
	}
	
	private void attack3_1(){
		if(counter == 1){
			pekare.shootAtPlayer(Pekare.SLOW, Pekare.MODE_SINGLE);
		}
		if((counter-1) % 40 == 0 && counter <= 540){
			kylare.shootSpindel();
		}
		if(counter % 150 == 0){
			kylare.shootRing();
		}
		if(counter == 540){
			pekare.setIdle();
		}
		if(counter == 600){
			randomAttack = -1;
		}
	}
	
	//pekare skjuter spartan
	private void attack3_0(){
		if(counter == 1){
			pekare.shootSpartan();
		}else if(counter == 560){
			randomAttack = -1;
		}
	}
	
	//pekare skjuter på spelaren och kylare skjuter cirklar
	private void attack2_0(){
		if(counter == 1){
			pekare.shootAtPlayer(Pekare.FAST, Pekare.MODE_SINGLE);
		}
		if(counter % 150 == 0){
			kylare.shootCircle();
		}
		if(counter == 520){
			pekare.setIdle();
			randomAttack = -1;
		}
	}
	
	//pekare skjuter alot
	private void attack2_1(){
		if(counter == 1){
			pekare.shootAlot();
		}
		if(counter == 230){
			randomAttack = -1;
		}
	}
	
	//pekare skjuter random och kylare skjuter sig själv
	private void attack2_2(){
		if(counter == 1){
			pekare.shootAtRandom(Pekare.FAST, Pekare.MODE_TRIPPLE);
		}
		if(counter == 50){
			kylare.shootSigSelf();
		}
		if(counter == 450){
			randomAttack = -1;
			pekare.setIdle();
		}
	}
	
	//kylare skjuter massor av beams och sedan en beam2
	private void attack2_3(){
		if(counter % 100 >= 0 && counter % 100 <= 60 && counter % 8 == 0 && counter < 100){
			kylare.shootBeam();
		}
		if(counter  == 200){
			kylare.shootBeam2();
		}
		if(counter == 290){
			randomAttack = -1;
		}
	}
	
	//kylare skjuter på spelaren
	private void attack1_0(){
		if(counter % 50 == 0 && counter <= 500){
			kylare.shootAtPlayer();
		}
		if(counter == 500){
			randomAttack = -1;
		}
	}
	
	//kylare skjuter en ring neråt
	private void attack1_1(){
		if(counter % 60 == 0 && counter <= 300){
			kylare.shootRing();
		}
		if(counter == 300){
			randomAttack = -1;
		}
	}
	
	//pekare skjuter på spelaren
	private void attack1_2(){
		if(counter == 1){
			pekare.shootAtPlayer(Pekare.FAST, Pekare.MODE_SINGLE);
		}
		if(counter == 500){
			pekare.setIdle();
			randomAttack = -1;
		}
	}
	
	//pekare skjuter random och kylare beam
	private void attack1_3(){
		if(counter == 1){
			pekare.shootAtRandom(Pekare.SLOW, Pekare.MODE_SINGLE);
		}
		if((counter-1) % 100 == 0){
			kylare.shootBeam();
		}
		if(counter == 500){
			pekare.setIdle();
			randomAttack = -1;
		}
	}
	////////////inga fler attacker///////
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		
		super.draw(g);
		
		if(textBoard != null)
			textBoard.draw(g);
		
		if(pekare != null)
			pekare.draw(g);
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
	
	private void setDefeated(){
		defeated = true;
		pekare.unBegin();
		kylare.unBegin();
		currentPhase = PHASE_NOTHING;
		//textBoard.clearBoard();
		started = false;
		pekare = null;
		textBoard = null;
	}
	
	public void kill(){
		dead = true;
		kylare.setRemove();
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
			JukeBox.play("johannes_intro");
			dx = 0;
			dy = 0.45;
		}else if(counter == 361){
			dy = 0;
			setPosition(restingPos);
			intro = false;
			counter = 0;
			
		}
	}

}

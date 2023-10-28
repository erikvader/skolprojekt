package game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.audio.JukeBox;
import game.entity.enemies.Enemy;
import game.entity.items.ThrowableItem;
import game.handlers.Content;
import game.handlers.Saves;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private boolean attacking = false;
	private boolean knockback = false;
	
	// fireball
	/*
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	private int fire;
	private int maxFire;
	*/
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	private Rectangle scratchBox;
	
	// gliding
	//private boolean gliding;
	
	//knockbacks
	private double knockbackY = -3;
	private double knockbackX = 1;
	
	//water controls
	private double waterFallSpeed;
	private double waterUpSpeed;
	//private boolean swim;
	private double waterMaxFallSpeed;
	private double waterStopSpeed;
	private double waterSwimSpeed;
	private double waterMaxSwimSpeed;
	//private int swimDelay;
	//private int swimCounter;
	private boolean swimming;
	private boolean canSwimAgain = true;
	
	private boolean upPropulsion = false;
	
	//double jumping
	private boolean hasDoubleJumped = false;
	private boolean doubleJump;
	private double doubleJumpSpeed;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Spike> spikes;
	private ArrayList<Spring> springs;
	private ArrayList<Platform> platforms;
	private ArrayList<ThrowableItem> throwableItems;
	private ArrayList<Button> buttons;
	
	//throwable items
	private ThrowableItem itemCurHolding;
	private boolean itemInteract = false;
	
	//platform
	private boolean onPlatform = false;
	private Platform curPlatform;
	
	// animations
	private ArrayList<Animation> sprites;
	private final int[] numFrames = {
		1, 4, 1, 1, 1, 4, 13
	};
	
	private final int[] delays = {
		-1, 6, -1, -1, -1, 4, 8
	};
	
	private final int[] widths = {
			30, 30, 30, 30, 30, 60, 30
		};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int KNOCKBACK = 4;
	private static final int SCRATCHING = 5;
	private static final int DEAD = 6;
	//private static final int GLIDING = 4;
	//private static final int FIREBALL = 5;
	//private static final int SWIMMING = 7;
	
	
	//emotes
	private BufferedImage[] emoteSprites;
	private int currentEmote = -1;
	public static final int EMOTE_NONE = -1;
	public static final int EMOTE_CONFUSED = 0;
	public static final int EMOTE_ATTENTION = 1;
	
	public Player(TileMap tm, ArrayList<Enemy> enemies) {
		
		super(tm);
		
		this.enemies = enemies;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 30;
		
		//normal speeds
		moveSpeed = 2; //0.3
		maxSpeed = 2; //1.6
		stopSpeed = 2; //0.4
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.5; //-4.8
		stopJumpSpeed = 0.3;
		doubleJumpSpeed = -3.6; //-3
		
		//swim speeds
		waterSwimSpeed = moveSpeed; //0.6
		waterMaxSwimSpeed = maxSpeed; //0.3
		waterStopSpeed = stopSpeed; //0.05
		waterFallSpeed = fallSpeed; //0.07
		waterMaxFallSpeed = 2; //1.3
		waterUpSpeed = -4.2; //-2
		
		facingRight = true;
		
		if(Saves.isGodmode()){
			health = maxHealth = 5000000; 
		}else{
			health = maxHealth = 5;
		}
		//fire = maxFire = 2500;
		
		//fireCost = 200;
		//fireBallDamage = 5;
		//fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 1;
		scratchRange = 34;
		scratchBox = new Rectangle(0, 0, scratchRange, height);
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Resources/Sprites/Player/playersprites.png"
				)
			);
			
			Animation as;
			sprites = new ArrayList<Animation>();
			for(int i = 0; i < 7; i++) {
				
				as = new Animation();
				
				as.load(spritesheet, widths[i], height, i, numFrames[i]);
				as.setDelay(delays[i]);
				
				sprites.add(as);
				
			}
			
			//load emotes
			emoteSprites = Content.loadRow("/Resources/HUD/Emotes.gif", 14, 18, 0, 2);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		sprites.get(SCRATCHING).setSpecificDelay(0, 7);
		
		animation = new Animation();
		setAnimation(IDLE);
		
		JukeBox.load("/Resources/SFX/scratch.mp3", "scratch");
		JukeBox.load("/Resources/SFX/jump.mp3", "jump");
		
	}
	
	public void setSprings(ArrayList<Spring> s){
		springs = s;
	}
	
	public void setSpikes(ArrayList<Spike> s){
		spikes = s;
	}
	
	public void setPlatforms(ArrayList<Platform> s){
		platforms = s;
	}
	
	public void setThrowableItems(ArrayList<ThrowableItem> s){
		throwableItems = s;
	}
	
	public void setButtons(ArrayList<Button> b){
		buttons = b;
	}
	
	public void setEmote(int e){currentEmote = e;}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	//public int getFire() { return fire; }
	//public int getMaxFire() { return maxFire; }
	/*
	public void setFiring() { 
		firing = true;
	}
	*/
	public void setScratching() {
		if(knockback) return;
		scratching = true;
	}
	
	public void setInteractItem() {
		if(knockback) return;
		itemInteract = true;
	}
	/*
	public void setGliding(boolean b) { 
		gliding = b;
	}
	*/
	public void setSwimming(boolean b){
		if(knockback) return;
		if(inWater && b)
			swimming = true;
		else{
			swimming = false;
			canSwimAgain = true;
		}
	}
	
	@Override
	public void setJumping(boolean b){
		if(knockback) return;
		if(b && !jumping && falling && !hasDoubleJumped){
			doubleJump = true;
		}
		jumping = b;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setDead(){
		health = 0;
		dead = true;
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		dead = false;
		itemCurHolding = null;
		setEmote(EMOTE_NONE);
		stop();
	}
	
	private void setAnimation(int i) {
		currentAction = i;
		Animation as = sprites.get(currentAction);
		setObjectAnimation(as);
	}
	
	public void propellUp(double dy){
		this.dy = dy;
		upPropulsion = true;
		hasDoubleJumped = true;
		falling = true;
	}
	
	public void checkAttack() {
		
		if(facingRight){
			scratchBox.x = (int)x;
			scratchBox.y = (int)(y-height/2);
			//scratchBox.width = scratchRange;
			//scratchBox.height = height;
		}else{
			scratchBox.x = (int)x-scratchRange;
			scratchBox.y = (int)(y-height/2);
			//scratchBox.width = scratchRange;
			//scratchBox.height = height;
		}
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(scratching && animation.getFrame() == 1 && animation.getCount() == 0) {
				if(e.intersects(scratchBox)){
					e.hit(scratchDamage);
				}
			}
			
			// fireballs
			/*
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			*/
			// check enemy collision
			if(e.attacks(this) && !e.isDead()) {
				e.attack(this);
				//hit(e.getDamage());
			}
			
		}
		
		//check buttons
		for(int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			
			if(scratching && animation.getFrame() == 1 && animation.getCount() == 0) {
				if(b.intersects(scratchBox)){
					b.hit();
				}
			}
		}
		
	}
	
	public void checkThrowableItems(){
		//plocka upp elr kasta år valfritt håll
		if(itemInteract) {
			if(itemCurHolding == null){ //håller inte i något
				for(int i = 0; i < throwableItems.size(); i++){
					ThrowableItem ti = throwableItems.get(i);
					if(intersects(ti)){
						itemCurHolding = ti;
						scratching = false;
						itemCurHolding.setBeingHeld(true);
						break;
					}
				}
			}else{ //håller i något
				int dir;
				if(getDown()){
					dir = ThrowableItem.DOWN;
				}else if(getUp()){
					dir = ThrowableItem.UP;
				}else if(facingRight){
					dir = ThrowableItem.RIGHT;
				}else{
					dir = ThrowableItem.LEFT;
				}
				itemCurHolding.launch(dir);
				itemCurHolding.setBeingHeld(false);
				itemCurHolding = null;
				scratching = false;
			}
			
			itemInteract = false;
		}
		
		//uppdatera position
		if(itemCurHolding != null){
			itemCurHolding.setPosition(x, y-cheight/2-itemCurHolding.cheight/2);
		}
	}

	public void stop(){
		super.stop();
		flinching = knockback = attacking = scratching = false;
	}
	
	/**
	 * 
	 * @param damage skada att göra
	 * @param k knockback eller inte
	 * @param kx knockback x (positiv) beror på riktning
	 * @param ky knockback y
	 * @param absolute om kx ska bero på riktningen som spelaren tittar åt
	 */
	public void hit(int damage, boolean k, double kx, double ky, boolean absolute){
		if(flinching) return;
		if(dead) return;
		health -= damage;
		stop();
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = 0;
		
		//knockback
		if(k){
			if(facingRight && !absolute) dx = -kx;//-1
			else dx = kx; //1
			dy = ky; //-3
			knockback = true;
			falling = true;
			jumping = false;
		}
	}
	
	public void hit(int damage, boolean k, double kx, double ky){
		hit(damage, k, kx, ky, false);
	}
	
	public void hit(int damage, boolean k){
		hit(damage, k, knockbackX, knockbackY);
	}
	
	public void hit(int damage) {
		hit(damage, true);
	}
	
	private void getNextWaterPosition(){
		//double waterMaxSwimSpeed = this.waterMaxSwimSpeed;
		
		// swimming
		if(swimming && canSwimAgain) {
			JukeBox.play("jump");
			dy = waterUpSpeed;
			falling = true;
			canSwimAgain = false;
			//swim = true;
			//swimCounter = 0;
			//swimDelay = 3;
		}
		
		// movement
		/*
		if(swim){
			if(swimDelay > 0)
				swimDelay--;
			swimCounter++;
			if(swimCounter >= 25){
				swim = false;
			}else{
				waterMaxSwimSpeed *= (1-((swimCounter-1)/23.0))*5.5+1;
			}
		}
		*/
		
		if(left) {
			dx -= waterSwimSpeed;
			if(dx < -waterMaxSwimSpeed) {
				dx = -waterMaxSwimSpeed;
			}
		}
		else if(right) {
			dx += waterSwimSpeed;
			if(dx > waterMaxSwimSpeed) {
				dx = waterMaxSwimSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= waterStopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += waterStopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// falling
		if(falling) {
			dy += waterFallSpeed;
			if(dy < 0 && !swimming && !upPropulsion){ dy += 0.3;} //stopJumpSpeed
			if(dy > waterMaxFallSpeed) dy = waterMaxFallSpeed;
			
		}
	
	}
	
	private void getNextNormalPosition(){
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
		
		//platform
		if(onPlatform){
			dy = 0;
		}
		
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		/* simon gillade int
		if((attacking) && !(jumping || falling)) {
			dx = 0;
		}
		*/
		
		if(inLava){
			propellUp(-5);
		}
		
		// jumping
		if(jumping && !falling) {
			JukeBox.play("jump");
			dy = jumpStart;
			falling = true;
		}
		
		if(doubleJump){
			dy = doubleJumpSpeed;
			hasDoubleJumped = true;
			doubleJump = false;
			JukeBox.play("jump");
		}
		
		if(!falling){ 
			hasDoubleJumped = false;
			upPropulsion = false;
		}
		
		// falling
		if(falling) {
			
			dy += fallSpeed;
			
			//if(dy > 0) jumping = false;
			if(dy < 0 && !jumping && !upPropulsion){ 
				dy += stopJumpSpeed;
			}
			
			
			//if(gliding && dy > maxFallSpeed *0.15) dy = maxFallSpeed*0.15;
			/*else*/ if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
		//platform del2
		if(onPlatform && dy == 0){
			y = curPlatform.y-curPlatform.cheight/2-cheight/2;
			x = curPlatform.getRelativeX()+curPlatform.x;
			//System.out.println(x+" "+curPlatform.getRelativeX()+" "+curPlatform.x);
			if(down){ 
				curPlatform.fallThrough();
			}
		}
	}
	
	private void getNextPosition() {
		if(inWater){
			getNextWaterPosition();
		}else{
			getNextNormalPosition();
		}
		
		
	}
	
	public void updatePlatforms(Section s){
		onPlatform = false;
		for(int i = 0; i < platforms.size(); i++){
			Platform p = platforms.get(i);
			p.update(s);
			if(p.isOn()){
				onPlatform = true;
				curPlatform = p;
			}
			
		}
	}
	
	public void update(Section s) {
		
		// update position
		checkInWater();
		checkInLava();
		updatePlatforms(s);
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(onPlatform){ 
			falling = false;
		}
		/*System.out.println(y);
		if(y == 150.6){
			doubleJump = true;
		}*/
		//if(dx == 0) x = (int)x; //vet inte varför den är här, fanns i artifact. antalgigen för att fixa springa-in-i-väggen-grejen
		
		if(inLava)
			hit(1, false);
		
		animation.update();
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) {scratching = attacking = false;}
		}
		
		//check death animation stopped, får den att stanna på sista frame:en
		if(currentAction == DEAD){
			if(animation.hasPlayedOnce()){
				animation.setFrame(numFrames[DEAD]-1);
			}
		}
		/*
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = attacking = false;
		}
		*/
		
		// fireball attack
		/*
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update(s);
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		*/
		
		// check done flinching
		if(flinching) {
			flinchTimer++;
			if(flinchTimer > 120) {
				flinching = false;
				flinchTimer = 0;
			}
		}
		
		//throwable items
		checkThrowableItems();
		
		//enemy interaction
		checkAttack();
		
		//check spikes
		for(Spike sp : spikes){
			sp.update(s);
			if(sp.intersectsSpike(this)){
				hit(sp.getDamage());
			}
		}
		
		//check springs
		for(int i = 0; i < springs.size(); i++){
			Spring sp = springs.get(i);
			if(sp.intersects(this)){
				sp.setBouncing();
				dy = sp.getSpeed();
				hasDoubleJumped = true;
				falling = true;
				upPropulsion = true;
			}
			sp.update(s);
		}
		
		// set animation
		if(dead){
			if(currentAction != DEAD){
				setAnimation(DEAD);
			}
		}else if(knockback){
			if(currentAction != KNOCKBACK){
				setAnimation(KNOCKBACK);
			}
		}else if(scratching) {
			if(currentAction != SCRATCHING && !attacking) {
				attacking = true;
				JukeBox.play("scratch");
				setAnimation(SCRATCHING);
			}
		}
		/*else if(firing) {
			if(currentAction != FIREBALL && !attacking) {
				attacking = true;
				if(fire > fireCost) {
					fire -= fireCost;
					FireBall fb = new FireBall(tileMap, facingRight);
					fb.setPosition(x, y);
					fireBalls.add(fb);
				}
				
				setAnimation(FIREBALL);
			}
		}*/
		else if(dy > 0) {
			/*if(gliding) {
				if(currentAction != GLIDING) {
					setAnimation(GLIDING);
				}
			}
			else*/ if(currentAction != FALLING) {
				setAnimation(FALLING);
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				setAnimation(WALKING);
			}
		}
		else {
			if(currentAction != IDLE) {
				setAnimation(IDLE);
			}
		}
		
		//animation.update();
		
		// set direction
		if(!attacking && !knockback/*currentAction != SCRATCHING && currentAction != FIREBALL*/) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		//emote
		if(currentEmote != EMOTE_NONE)
			g.drawImage(emoteSprites[currentEmote], (int)(x-7+xmap), (int)(y-9-cheight/2-9+ymap), null);
		
		/*
		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		*/
		
		// draw player
		if(flinching) {
			if(flinchTimer / 7 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
		//platforms.get(0).asd();
		
		//coords
		//g.drawString(x+" "+y, 370, 50);
		//g.drawString((int)(x/30)+" "+(int)(y/30), 370, 70);

	}
	
	/*
	public void asd(){
		int a = (int)(x + xmap - width / 2.0);
		int b = (int)(y + ymap - height / 2.0);
		System.out.println("mitten: "+x+", "+y+" onScreen: "+a+", "+b);
		//System.out.println(xmap+" "+ymap);
	}
	*/

}


















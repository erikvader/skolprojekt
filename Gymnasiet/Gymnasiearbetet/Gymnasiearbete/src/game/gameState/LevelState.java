package game.gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import game.audio.JukeBox;
import game.entity.Button;
import game.entity.Door;
import game.entity.Effect;
import game.entity.EnergyParticle;
import game.entity.Platform;
import game.entity.Player;
import game.entity.Sign;
import game.entity.Spike;
import game.entity.Spring;
import game.entity.enemies.Calculator;
import game.entity.enemies.Enemy;
import game.entity.enemies.Slugger;
import game.entity.enemies.Spider;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.items.ThrowableItem;
import game.handlers.Keys;
import game.handlers.Saves;
import game.hud.Conversation;
import game.hud.HUD;
import game.main.GamePanel;
import game.tileMap.Background;
import game.tileMap.Section;
import game.tileMap.TileMap;


public abstract class LevelState extends GameState{

	protected TileMap tileMap;
	protected Background bg;
	
	protected Player player;
	
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Effect> effects;
	protected ArrayList<Spike> spikes;
	protected ArrayList<Spring> springs;
	protected ArrayList<Platform> platforms;
	protected ArrayList<Sign> signs;
	protected ArrayList<Door> doors;
	protected ArrayList<EnemyProjectile> enemyProjectiles;
	protected ArrayList<ThrowableItem> throwableItems;
	protected ArrayList<Button> buttons;
	
	protected Conversation curDialog;
	
	protected HUD hud;
	protected BufferedImage hudBufferedImage;
	
	protected boolean blockInput = false;
	
	//event
	protected boolean eventStart;
	protected boolean eventFinish;
	protected boolean eventFinishEnergyParticles;
	protected boolean eventFinishVictory;
	protected boolean eventDead;
	protected int eventCounter = 0;
	
	//door event
	protected boolean eventDoor = false;
	protected Door doorEventDoor;
	protected Door targetDoor;
	
	protected boolean paused = false;
	protected boolean readingSign = false;
	
	//black bars
	protected ArrayList<Rectangle2D.Double> tb;
	protected int tbSpeed = 60; //antal frames som öppning samt stängning ska ske på
	protected int tbCounter = 0;
	
	//sections
	protected ArrayList<Section> sections;
	protected int currentSection = 0;
	protected int startSection;
	
	//godmode
	protected boolean isGodmode;
	protected Font godFont;
	
	//protected boolean plzDeload = false; //sätter deload till true precis innan man ändrar state till hub
	
	public LevelState(GameStateManager gsm) {
		super(gsm);
		isGodmode = Saves.isGodmode();
		godFont = new Font("Arial", Font.BOLD, 10);
	}
	
	public void init() {
		
		effects = new ArrayList<Effect>();
		enemies = new ArrayList<Enemy>();
		spikes = new ArrayList<Spike>();
		springs = new ArrayList<Spring>();
		platforms = new ArrayList<Platform>();
		signs = new ArrayList<Sign>();
		doors = new ArrayList<Door>();
		enemyProjectiles = new ArrayList<EnemyProjectile>();
		throwableItems = new ArrayList<ThrowableItem>(); 
		buttons = new ArrayList<Button>(); 
	
		tb = new ArrayList<Rectangle2D.Double>();
		sections = new ArrayList<Section>();
		
		curDialog = new Conversation();
		curDialog.setEmpty();
		
		hudBufferedImage = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		tileMap = new TileMap(30);
		
		player = new Player(tileMap, enemies);
		player.setSpikes(spikes);
		player.setSprings(springs);
		player.setPlatforms(platforms);
		player.setThrowableItems(throwableItems);
		player.setButtons(buttons);
		
		hud = new HUD(player);
	}
	
	protected void addSign(int x, int y, Conversation c){
		Sign t = new Sign(tileMap, c);
		t.setPosition(x, y);
		signs.add(t);
	}
	
	protected void addSign(int x, int y, Conversation c, BufferedImage[] frames){
		Sign t = new Sign(tileMap, c, frames);
		t.setPosition(x, y);
		signs.add(t);
	}
	
	protected void addPlatform(int x1, int y1, int x2, int y2, double speed){
		Platform s = new Platform(tileMap, player);
		s.setRoute(new Point(x1, y1), new Point(x2, y2), speed);
		platforms.add(s);
		
	}
	
	protected void addSpike(int x, int y, int size, int rot){
		Spike s = new Spike(tileMap, rot, size);
		s.setPosition(x, y);
		spikes.add(s);
	}
	
	protected void generateTriggerLatest(int width, int height){
		Spike s = spikes.get(spikes.size()-1);
		s.generateTrigger(player, width, height);
	}
	
	protected void generateSectionDoor(int sourceSection, int targetDoorIndex, int x, int y){
		Door temp = new Door(tileMap, sourceSection);
		temp.setPosition(x, y);
		temp.setDoorTarget(targetDoorIndex);
		doors.add(temp);
	}
	
	protected void generateStateDoor(int sourceSection, int targetState, int x, int y){
		Door temp = new Door(tileMap, sourceSection);
		temp.setPosition(x, y);
		temp.setStateTarget(targetState);
		doors.add(temp);
	}
	
	protected void generateSpiralDoor(int sourceSection, int targetState, int x, int y, String stage){
		Door temp = new Door(tileMap, sourceSection, Door.DOOR_SPIRAL);
		temp.setPosition(x, y);
		temp.setStateTarget(targetState);
		doors.add(temp);
		boolean lo = Saves.getStageProgress(stage);
		if(!lo){temp.setLocked();}
		int score = Saves.getStageScore(stage);
		if(score != -1) temp.setScore(score);
	}
	
	protected void addSpider(double x, int yTile, double spiderHeight, int width, int height){
		Spider s = new Spider(tileMap, player, effects);
		s.setPosesTile(x, spiderHeight, yTile);
		s.setDetectionbox(width, height);
		enemies.add(s);
	}
	
	protected void addCalculator(int x, int y){
		Calculator c = new Calculator(tileMap, enemyProjectiles, effects, throwableItems, enemies);
		c.setPosition(x, y);
		enemies.add(c);
	}
	
	protected void addSlugger(int x, int y){
		Slugger s = new Slugger(tileMap, effects);
		s.setPosition(x, y);
		enemies.add(s);
	}
	
	@Override
	public void update() {
		
		//events
		if(eventStart) eventStart();
		if(eventDead) eventDead();
		if(eventFinish) eventFinish();
		if(eventDoor) eventDoor();
		
		//input
		handleInput();
		
		//update signs
		if(Keys.isPressed(Keys.ENTER)){
			for(int i = 0; i < signs.size(); i++){
				Sign s = signs.get(i);
				if(s.intersects(player)){
					if(curDialog.getDone() == true){
						curDialog = s.getDialog();
						curDialog.reset();
						readingSign = true;
						paused = true;
					}
				}
			}
		}

		curDialog.update();
		if(readingSign && curDialog.getDone()){
			readingSign = false;
			paused = false;
		}
		if(/*!curDialog.getDone() || */paused){
			return;
		}
		
		// update player
		player.update(sections.get(currentSection));
		
		//update section
		sections.get(currentSection).update(); //tilemap.pos och background
		
		//update map
		tileMap.update();
		
		/*
		tileMap.setPosition(
			GamePanel.WIDTH / 2.0 - (int)player.getx(),
			GamePanel.HEIGHT / 2.0 - (int)player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		*/
		
		//player dead
		if(player.isDead()/* || player.gety() > tileMap.getHeight()+player.getHeight()/2*/){ //kollas med section
			eventDead = true;
		}
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update(sections.get(currentSection));
			if(e.isDead()) {
				e.die();
				enemies.remove(i);
				i--;
				//effects.add(new Effect(tileMap, (int)e.getx(), (int)e.gety()));
			}
		}
		
		// update effects
		for(int i = 0; i < effects.size(); i++) {
			effects.get(i).update();
			if(effects.get(i).shouldRemove()) {
				effects.remove(i);
				i--;
			}
		}
		
		// update enemyProjectiles
		for(int i = 0; i < enemyProjectiles.size(); i++) {
			EnemyProjectile ep = enemyProjectiles.get(i);
			ep.update(sections.get(currentSection));
			
			if(ep.hits(player)){
				player.hit(ep.getDamage());
			}
			
			if(ep.shouldRemove()) {
				enemyProjectiles.remove(i);
				i--;
			}
		}
		
		//update doors
		for(int i = 0; i < doors.size(); i++){
			Door d = doors.get(i);
			d.update();
			if(!eventDoor){
				if(player.intersects(d) && player.getUp() && !d.isLocked()){
					eventDoor = true;
					doorEventDoor = d;
					//break;
				}
			}
		}
		
		//update throwable items
		for(int i = 0; i < throwableItems.size(); i++){
			ThrowableItem ti = throwableItems.get(i);
			ti.update(sections.get(currentSection));
			if(ti.plzRemove()){
				throwableItems.remove(i);
				i--;
			}
		}
		
		//update buttons
		for(int i = 0; i < buttons.size(); i++){
			Button b = buttons.get(i);
			b.update(sections.get(currentSection));
			if(b.plzRemove()){
				buttons.remove(i);
				i--;
			}
		}
	}
	
	protected void drawLayer1(Graphics2D g){
		// draw bg
		//bg.draw(g);
		sections.get(currentSection).draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		//springs
		for(Spring s : springs) s.draw(g);
		
		//draw spikes
		for(Spike s : spikes) s.draw(g);
		
		//draw spikes
		for(Platform s : platforms) s.draw(g);
		
		//draw signs
		for(Sign s : signs) s.draw(g);
		
		//draw doors
		for(Door d : doors) d.draw(g);
		
		for(Button b : buttons) b.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw throwable items
		for(int i = 0; i < throwableItems.size(); i++) {
			throwableItems.get(i).draw(g);
		}
		
		// draw player
		player.draw(g);
		//spikes.get(0).asd();
	}
	
	protected void drawLayer2(Graphics2D g){
		//draw projectiles
		for(int i = 0; i < enemyProjectiles.size(); i++){
			enemyProjectiles.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < effects.size(); i++) {
			effects.get(i).draw(g);
		}
		
		// draw hud
		drawHud(g);
		
		//godmode
		if(isGodmode){
			g.setColor(Color.ORANGE);
			g.setFont(godFont);
			g.drawString("damage: "+Integer.toString(player.getMaxHealth()-player.getHealth()), 10, 290);
		}
		
	}
	
	protected void drawHud(Graphics2D g){
		Graphics2D hudG = (Graphics2D)hudBufferedImage.createGraphics();
		
		hudG.setBackground(new Color(255, 255, 255, 0));
		hudG.clearRect(0, 0, hudBufferedImage.getWidth(), hudBufferedImage.getHeight());
		
		hud.draw(hudG);
		curDialog.draw(hudG);
		
		//draw tb
		hudG.setColor(Color.BLACK);
		for(Rectangle2D.Double r : tb) hudG.fillRect((int)r.x, (int)r.y, (int)r.width, (int)r.height);
		
		g.drawImage(hudBufferedImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
	}
	
	public void draw(Graphics2D g) {
		
	}
	
	private void handleInput(){
		curDialog.setEnter(Keys.isPressed(Keys.ENTER));
		if(Keys.isPressed(Keys.DELETE)) curDialog.setEmpty();
		if(Keys.isPressed(Keys.ESCAPE)) gsm.pauseAndSet(true, GameStateManager.PAUSED_SCREEN);
		if(Keys.isPressed(Keys.RESET)){ 
			reset();
			addToDeathCounter();
		}
		if(blockInput || player.isDead()) return;
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setUp(Keys.keyState[Keys.UP]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		//player.setGliding(Keys.keyState[Keys.BUTTON2]);
		//if(Keys.keyState[Keys.BUTTON3]) player.setScratching();
		if(Keys.isPressed(Keys.BUTTON3)) player.setScratching();
		if(Keys.isPressed(Keys.BUTTON3)) player.setInteractItem();
		//else if(Keys.keyState[Keys.BUTTON4]) player.setFiring();
		//if(Keys.isPressed(Keys.BUTTON1)) player.setSwim();
		player.setSwimming(Keys.keyState[Keys.BUTTON1]);
		
	}
	
	/////////////////////events///////////
	
	protected void reset() {
		//reset events
		eventCounter = 0;
		eventStart = eventFinish = eventDead = eventDoor = false;
		
		player.reset();
		enemyProjectiles.clear();
		effects.clear();
		
		readingSign = false;
		paused = false;
		curDialog.setEmpty();
		
		blockInput = true;
		eventCounter = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		/*
		title = new Title(hageonText.getSubimage(0, 0, 178, 20));
		title.sety(60);
		subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
		subtitle.sety(85);
		*/
	}
	
	protected void eventStart(){
		eventCounter++;
		if(eventCounter == 1) {
			blackBarsReset(60);
		}
		if(eventCounter >= 1 && eventCounter <= 60) {
			blackBarsOpen();
		}
		if(eventCounter == 60) {
			eventStart = blockInput = false;
			eventCounter = 0;
			blackBarsReset(60);
		}
	}
	
	
	protected void eventDead() {
		eventCounter++;
		if(eventCounter == 1) {
			addToDeathCounter();
			player.setDead();
			player.stop();
		}
		if(eventCounter == 60) {
			blackBarsReset(58);
		}
		if(eventCounter >= 60) {
			blackBarsClose();
		}
		if(eventCounter > 160) {
			if(/*player.getLives()*/eventCounter == 0) { //bara för att få bort varningnen
				gsm.setState(GameStateManager.MAIN_MENU);
			}
			else {
				eventDead = blockInput = false;
				eventCounter = 0;
				//player.loseLife();
				reset();
			}
		}
	}
	
	private void addToDeathCounter(){
		try {
			Saves.addOneToDeath();
			Saves.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// finished level
	protected void eventFinish() {
		eventCounter++;
		if(eventFinishEnergyParticles && eventCounter % 10 == 0){
			effects.add(new EnergyParticle(tileMap, player.getx(), player.gety(), EnergyParticle.UP));
		}
		
		if(eventCounter == 1) {
			player.stop();
			blockInput = true;
			if(eventFinishVictory)
				JukeBox.play("seger");
		}
		else if(eventCounter == 60) {
			blackBarsReset(57);
		}
		else if((eventFinishVictory ? eventCounter > 260 : eventCounter > 60)) {
			blackBarsClose();
		}
		if((eventFinishVictory ? eventCounter == 330 : eventCounter == 180)) {
			//PlayerSave.setHealth(player.getHealth());
			//PlayerSave.setLives(player.getLives());
			//PlayerSave.setTime(player.getTime());
			setDeload(true);
			gsm.setState(GameStateManager.LEVEL_HUB);
		}
		
	}
	
	protected void setStageAsCleared(String currentStage, String nextStage, int score, int hubDoorIndex){
		try{
			Saves.setStageProgress(nextStage, true);
			if(score > 5) score = 5; //godmode
			Saves.setStageScoreIfHigher(currentStage, score);
			Saves.setHubSpawn(hubDoorIndex);
			Saves.writeFile();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//plzDeload = true;
	}
	
	protected void eventDoor(){
		eventCounter++;
		if(eventCounter == 1){
			player.stop();
			blockInput = true;
			//player enter door animation
			doorEventDoor.setOpened();
			blackBarsReset(30);
		}else if(eventCounter >= 10 && eventCounter <= 40) {
			blackBarsClose();
		}else if(eventCounter == 41) {
			doorEventDoor.setClosed();
			if(doorEventDoor.getMode() == Door.MODE_SECTION){
				targetDoor = doors.get(doorEventDoor.getTarget());
				player.setPosition(targetDoor.getPos());
				currentSection = targetDoor.getSourceSection();
				targetDoor.setOpened();
			}else{
				gsm.setState(doorEventDoor.getTarget());
			}
			
		}else if(eventCounter == 71){
			blackBarsReset(30);
			blackBarsOpen();
		}else if(eventCounter > 71 && eventCounter <= 101){
			blackBarsOpen();
		}else if(eventCounter == 102){
			blockInput = false;
		}else if(eventCounter == 122){
			targetDoor.setClosed();
			eventDoor = false;
			eventCounter = 0;
			blackBarsReset(30);
		}
	}
	
	protected void blackBarsReset(int speed){
		tb.clear();
		tbCounter = 0;
		tbSpeed = speed;
	}
	
	protected void blackBarsOpen(){
		tbCounter++;
		if(tbCounter == 1){
			tb.add(new Rectangle2D.Double(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2.0));
			tb.add(new Rectangle2D.Double(0, 0, GamePanel.WIDTH / 2.0, GamePanel.HEIGHT));
			tb.add(new Rectangle2D.Double(0, GamePanel.HEIGHT / 2.0, GamePanel.WIDTH, GamePanel.HEIGHT / 2.0));
			tb.add(new Rectangle2D.Double(GamePanel.WIDTH / 2.0, 0, GamePanel.WIDTH / 2.0, GamePanel.HEIGHT));
		}
		if(tbCounter >= 1 && tbCounter <= tbSpeed){
			tb.get(0).height -= ((GamePanel.HEIGHT/2.0)/(double)tbSpeed);
			tb.get(1).width -= ((GamePanel.WIDTH/2.0)/(double)tbSpeed);
			tb.get(2).y += ((GamePanel.HEIGHT/2.0)/(double)tbSpeed);
			tb.get(3).x += ((GamePanel.WIDTH/2.0)/(double)tbSpeed);
		}
	}

	protected void blackBarsClose(){
		tbCounter++;
		if(tbCounter == 1){
			tb.add(new Rectangle2D.Double(GamePanel.WIDTH / 2.0, GamePanel.HEIGHT / 2.0, 0, 0));
		}
		if(tbCounter >= 1 && tbCounter <= tbSpeed){
			tb.get(0).height += (2*((GamePanel.HEIGHT/2.0)/(double)tbSpeed));
			tb.get(0).width += (2*((GamePanel.WIDTH/2.0)/(double)tbSpeed));
			tb.get(0).y -= ((GamePanel.HEIGHT/2.0)/(double)tbSpeed);
			tb.get(0).x -= ((GamePanel.WIDTH/2.0)/(double)tbSpeed);
		}
	}
	
}

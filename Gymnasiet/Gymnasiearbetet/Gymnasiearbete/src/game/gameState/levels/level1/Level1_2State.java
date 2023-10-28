package game.gameState.levels.level1;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import game.audio.JukeBox;
import game.entity.Player;
import game.entity.Spring;
import game.entity.Teleport;
import game.entity.enemies.Slugger;
import game.entity.level1_2.Boulder;
import game.functions.Trigger;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.hud.Conversation;
import game.hud.Profile;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level1_2State extends LevelState {
	
	//boulder event
	private Trigger boulderTrigger;
	private boolean isBoulderEvent = false;
	private Boulder boulder;
	private int boulderEventCounter = 0;
	
	//finish
	private Teleport tele;
	
	//sections
	public static final int SECTION_MAIN = 0;

	public Level1_2State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/grass");
		tileMap.loadMap("/Resources/Maps/level1-2");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/grass_background2_shorter.png", 0.1);
		
		//sections
		Section temp = new Section(0, 0, 41*30, 16*30, bg, tileMap, player);
		temp.setSpawn(2*30, 9*30); //2, 9
		sections.add(temp);
		startSection = SECTION_MAIN;
		
		reset();
		
		//teleporter
		tele = new Teleport(tileMap);
		tele.setTilePosition(38, 3);
		
		JukeBox.load("/Resources/Music/default.mp3", "defaultBG");
		JukeBox.load("/Resources/SFX/victory.mp3", "seger");
		//JukeBox.play("level1BG");
		//JukeBox.loop("level1BG");
	}
	
	private void initDialog(){
		
	}
	
	private void populateDoors(){
		doors.clear();
	
		
	}
	
	private void populateSigns(){
		signs.clear();
		Profile p = new Profile("Skylt", 0, "/Resources/HUD/profiles/profile_skylt1.png");
		
		Conversation c;
		c = new Conversation();
		c.addMessage("jag är värdelös", p);
		addSign(3*30+15, 4*30+15, c);
		
		c = new Conversation();
		c.addMessage("Secret:\nDet finns ett godmode, men hur aktiverar man det? Endast gud vet.", p);
		addSign(1*30+15, 15*30+15, c);
	}
	
	private void populatePlatforms(){
		platforms.clear();
		
		addPlatform(29*30+15, 4*30+15, 36*30+15, 4*30+15, 0.4);
		
	}
	
	private void populateSprings(){
		springs.clear();
		
		Spring s;
		Point[] points = new Point[] {
			new Point(37*30+15, 11*30+23)
		};
		
		for(int i = 0; i < points.length; i++) {
			s = new Spring(tileMap);
			s.setPosition(points[i].x, points[i].y);
			springs.add(s);
		}
	}
	
	private void populateSpikes(){
		spikes.clear();
		
		addSpike(19*30+15, 6*30+15, 3, 3);
		addSpike(20*30+15, 9*30+15, 3, 0);
		addSpike(19*30+15, 11*30+15, 3, 3);
		addSpike(23*30+15, 11*30+15, 3, 2);
		addSpike(27*30+15, 15*30+15, 3, 0);
		generateTriggerLatest(10, 180);
		addSpike(29*30+15, 15*30+15, 3, 0);
		generateTriggerLatest(10, 180);
		addSpike(31*30+15, 15*30+15, 3, 0);
		generateTriggerLatest(10, 180);
		addSpike(33*30+15, 15*30+15, 3, 0);
		generateTriggerLatest(10, 180);
		addSpike(35*30+15, 15*30+15, 3, 0);
		generateTriggerLatest(10, 180);
		
		for(int i = 0; i < 24; i++){
			addSpike(29*30+5+10*i, 6*30+25, 1, 0);
		}
		
		
	}
	
	private void populateEnemies() {
		
		enemies.clear();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(7, 9),
			new Point(20, 3),
			//new Point(50, 6),
			//new Point(56, 6),
			//new Point(60, 6)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap, effects);
			s.setTilePosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		addSpider(28*30+15, 10, 50, 90, 90);
		addSpider(30*30+15, 10, 50, 90, 90);
		addSpider(32*30+15, 10, 50, 90, 90);
		addSpider(34*30+15, 10, 50, 90, 90);
		
	}
	
	@Override
	public void update(){
		//events
		//boulder
		boulderTrigger.update(player);
		if(boulderTrigger.hasTriggered()){
			isBoulderEvent = true;
		}
		if(isBoulderEvent){
			boulderEvent();
			boulder.update(sections.get(currentSection));
		}
		if(boulder.isLaunching() && player.intersects(boulder)){
			player.hit(10);
		}
		
		//finish
		tele.update();
		if(tele.contains(player) && !eventFinish){
			eventFinish = true;
			eventFinishEnergyParticles = true;
			eventFinishVictory = true;
			setStageAsCleared("1_2", "1_boss", player.getHealth(), LevelHub.DOOR_STAGE_1_2);
			JukeBox.stop("defaultBG");
		}
		
		super.update();
		
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		
		//draw boulder
		boulder.draw(g);
		
		tele.draw(g);
		
		super.drawLayer2(g);
	}
	
	////////events/////
	@Override
	protected void reset(){
		populateEnemies();
		populateSpikes();
		populateSprings();
		populatePlatforms();
		populateSigns();
		populateDoors();
		initDialog();
		
		//section
		currentSection = startSection;
		player.setPosition(sections.get(currentSection).getSpawn());
		
		//boulder event
		boulder = new Boulder(tileMap, Boulder.RIGHT);
		boulder.setTilePosition(24, 8);
		boulderTrigger = new Trigger(new Rectangle(33*30, 8*30, 30, 30));
		
		super.reset();
		
		JukeBox.stop("seger");
		isBoulderEvent = false;
		boulderEventCounter = 0;
	}
	
	private void boulderEvent(){
		boulderEventCounter++;
		if(boulderEventCounter == 1){
			blockInput = true;
			player.stop();
		}else if(boulderEventCounter == 15){
			player.setEmote(Player.EMOTE_CONFUSED);
		}else if(boulderEventCounter == 75){
			tileMap.setShaking(true, 7);
			player.setEmote(Player.EMOTE_ATTENTION);
		}else if(boulderEventCounter == 125){
			blockInput = false;
			boulder.launch();
			player.setEmote(Player.EMOTE_NONE);
		}else if(boulderEventCounter == 125+210){
			boulder.stopLaunch();
			tileMap.setShaking(true, 77);
		}else if(boulderEventCounter == 365){
			tileMap.setShaking(false, 0);
			boulderEventCounter = 0;
			isBoulderEvent = false;
		}
		
	}

	@Override
	public void activate() {
		JukeBox.resumeLoop("defaultBG");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("defaultBG");
	}
	
	@Override
	public void deload() {
		JukeBox.close("defaultBG");
		JukeBox.close("seger");
	}
	
}













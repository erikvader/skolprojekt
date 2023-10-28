package game.gameState.levels.level2;

import java.awt.Graphics2D;
import java.awt.Point;

import game.audio.JukeBox;
import game.entity.Platform;
import game.entity.Spike;
import game.entity.Teleport;
import game.entity.enemies.Calculator;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.hud.Conversation;
import game.hud.Profile;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level2_1State extends LevelState {
	
	//finish
	private Teleport tele;
	
	//sections
	public static final int SECTION_MAIN = 0;

	public Level2_1State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/grass");
		tileMap.loadMap("/Resources/Maps/level2-1");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/grass_background2_shorter.png", 0.1);
		
		//sections
		Section temp = new Section(0, 0, 102*30, 10*30, bg, tileMap, player);
		temp.setSpawn(2*30, 7*30); //2, 7
		sections.add(temp);
		startSection = SECTION_MAIN;
		
		reset();
		
		//teleporter
		tele = new Teleport(tileMap);
		tele.setTilePosition(98, 7);
		
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
		c.addMessage("Akta så att miniräknarna inte deriverar dig! Då kan de lista ut dina extrempunkter.", p);
		addSign(4*30+15, 4*30+15, c);
		
	}
	
	private void populatePlatforms(){
		platforms.clear();
		
		Platform p = new Platform(tileMap, player);
		p.setRoute(new Point(52*30+15, 9*30+15), new Point(57*30+15, 6*30+15), 1);
		platforms.add(p);
		
		p = new Platform(tileMap, player);
		p.setRoute(new Point(59*30+15, 6*30+15), new Point(67*30+15, 6*30+15), 1);
		platforms.add(p);
		
		p = new Platform(tileMap, player);
		p.setRoute(new Point(69*30+15, 8*30+15), new Point(69*30+15, 2*30+15), 1);
		platforms.add(p);
		
		p = new Platform(tileMap, player);
		p.setRoute(new Point(76*30+15, 2*30+15), new Point(82*30+15, 9*30+15), 1);
		platforms.add(p);
		
	}
	
	private void populateSprings(){
		springs.clear();
		
	}
	
	private void populateSpikes(){
		spikes.clear();
		
		addSpike(24*30+15, 8*30+15, 3, Spike.UP);
		generateTriggerLatest(30, 9*30);
		spikes.get(spikes.size()-1).setSpeed(8);
		
		addSpike(20*30+15, 6*30+15, 3, Spike.LEFT);
		addSpike(31*30+15, 6*30+15, 3, Spike.RIGHT);
		addSpike(36*30+15, 9*30+15, 3, Spike.LEFT);
		addSpike(37*30+15, 4*30+15, 3, Spike.RIGHT);
		addSpike(39*30+15, 4*30+15, 3, Spike.LEFT);
		
		addSpike(43*30+15, 8*30+20, 2, Spike.UP);
		addSpike(43*30+15, 6*30+10, 2, Spike.DOWN);
		
		addSpike(47*30+15, 7*30+20, 2, Spike.UP);
		addSpike(47*30+15, 5*30+10, 2, Spike.DOWN);
		
		addSpike(74*30+15, 2*30+15, 3, Spike.LEFT);
		
		addSpike(89*30+15, 7*30+15, 3, Spike.UP);
		addSpike(91*30+15, 7*30+15, 3, Spike.UP);
		
	}
	
	private void populateEnemies() {
		
		enemies.clear();
		
		Calculator c;
		Point[] points = new Point[] {
			new Point(10, 8),
			new Point(11, 3),
			new Point(75, 5),
			new Point(65, 2),
		};
		for(int i = 0; i < points.length; i++) {
			c = new Calculator(tileMap, enemyProjectiles, effects, throwableItems, enemies);
			c.setTilePosition(points[i].x, points[i].y);
			enemies.add(c);
		}
		
		addSlugger(58*30+15, 5*30+15);
		addSlugger(89*30+15, 3*30+15);
		addSlugger(91*30+15, 1*30+15);
		
		addSpider(65*30+15, 4, 2*30, 90, 4*30);
		addSpider(75*30+15, 3, 2*30, 90, 4*30);
		
	}
	
	private void populateThrowableItems(){
		throwableItems.clear();
	}
	
	@Override
	public void update(){
		//events
		
		//finish
		tele.update();
		if(tele.contains(player) && !eventFinish){
			eventFinish = true;
			eventFinishEnergyParticles = true;
			setStageAsCleared("2_1", "2_2", player.getHealth(), LevelHub.DOOR_STAGE_2_1);
			JukeBox.stop("defaultBG");
			eventFinishVictory = true;
		}
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		
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
		populateThrowableItems();
		initDialog();

		//section
		currentSection = startSection;
		player.setPosition(sections.get(currentSection).getSpawn());
		
		super.reset();
		
		JukeBox.stop("seger");
		
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













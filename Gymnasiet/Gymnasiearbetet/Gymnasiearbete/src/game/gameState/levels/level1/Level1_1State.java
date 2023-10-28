package game.gameState.levels.level1;

import java.awt.Graphics2D;
import java.awt.Point;

import game.audio.JukeBox;
import game.entity.Teleport;
import game.entity.enemies.Slugger;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.hud.Conversation;
import game.hud.Profile;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level1_1State extends LevelState {
	
	//finish
	private Teleport tele;
	
	//sections
	public static final int SECTION_MAIN = 0;

	public Level1_1State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/grass");
		tileMap.loadMap("/Resources/Maps/level1-1");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/grass_background2_shorter.png", 0.1);
		
		//sections
		Section temp = new Section(0, 0, 158*30, 10*30, bg, tileMap, player);
		temp.setSpawn(2*30, 6*30); //2, 9
		sections.add(temp);
		startSection = SECTION_MAIN;
		
		reset();
		
		//teleporter
		tele = new Teleport(tileMap);
		tele.setTilePosition(154, 8);
		
		JukeBox.load("/Resources/Music/super_mario.mp3", "superMario");
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
		c.addMessage("Ifall du håller in hoppknappen så kan du hoppa högre, samt så kan du dubbelhoppa genom att hoppa två gånger.", p);
		addSign(31*30+15, 5*30+15, c);
		
		c = new Conversation();
		c.addMessage("Detta är en magisk värld", p);
		addSign(4*30+15, 8*30+15, c);
		
		c = new Conversation();
		c.addMessage("Du trodde att det skulle stå någonting\nvärdefullt här va?", p);
		addSign(66*30+15, 2*30+15, c);
		
		c = new Conversation();
		c.addMessage("The cake is a lie!", p);
		addSign(99*30+15, 2*30+15, c);
	}
	
	private void populatePlatforms(){
		platforms.clear();
		
		
	}
	
	private void populateSprings(){
		springs.clear();
		
	}
	
	private void populateSpikes(){
		spikes.clear();
		
		
	}
	
	private void populateEnemies() {
		
		enemies.clear();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(13, 2),
			new Point(15, 8),
			new Point(23, 8),
			new Point(35, 8),
			new Point(52, 8),
			new Point(64, 2),
			new Point(75, 8),
			new Point(76, 8),
			new Point(71, 5),
			new Point(98, 2),
			new Point(98, 8),
			new Point(139, 8),
			new Point(138, 6),
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap, effects);
			s.setTilePosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
	}
	
	@Override
	public void update(){
		//events
		
		//finish
		tele.update();
		if(tele.contains(player) && !eventFinish){
			eventFinish = true;
			eventFinishEnergyParticles = true;
			eventFinishVictory = true;
			setStageAsCleared("1_1", "1_2", player.getHealth(), LevelHub.DOOR_STAGE_1_1);
			JukeBox.stop("superMario");
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
		initDialog();

		//section
		currentSection = startSection;
		player.setPosition(sections.get(currentSection).getSpawn());
		
		JukeBox.stop("seger");
		
		super.reset();
	}

	@Override
	public void activate() {
		JukeBox.resumeLoop("superMario");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("superMario");
	}
	
	@Override
	public void deload() {
		JukeBox.close("superMario");
		JukeBox.close("seger");
	}
	
}













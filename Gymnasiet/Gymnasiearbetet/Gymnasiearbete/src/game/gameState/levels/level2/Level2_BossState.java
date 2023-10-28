package game.gameState.levels.level2;

import java.awt.Graphics2D;

import game.audio.JukeBox;
import game.entity.Button;
import game.entity.Effect;
import game.entity.level2_boss.Mattias;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.handlers.Content;
import game.hud.Conversation;
import game.hud.Profile;
import game.main.GamePanel;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level2_BossState extends LevelState{
	
	//sections
	public static final int SECTION_MAIN = 0;
	
	//boss
	private Mattias mattias;
	
	//events
	//intro
	private Button startButton;
	private boolean introEvent = false;
	private boolean defeatedEvent = false;
	private Conversation mattiasIntroDialog;
	private Conversation defeatedDialog;
	
	//music
	public static boolean playMusic = false;
	public static boolean playLast = false;
	
	public Level2_BossState(GameStateManager gsm) {
		super(gsm);
		init();
	}	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/castle");
		tileMap.loadMap("/Resources/Maps/level2-boss");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/grid_background.png", 1);
		
		//sections
		Section temp = new Section(10, 0, GamePanel.WIDTH, 10*30, bg, tileMap, player);
		temp.setSpawn(7*30, 8*30);
		sections.add(temp);
		
		startSection = SECTION_MAIN;
		
		reset();
		
		//JukeBox.load("/Resources/Music/Bowserholt.mp3", "level1BossBG");
		JukeBox.load("/Resources/Music/victory.mp3", "victory");
		JukeBox.load("/Resources/SFX/mattias_laugh.mp3", "mattias_laugh");
		JukeBox.load("/Resources/Music/mattias_background.mp3", "mattias_background");
		JukeBox.load("/Resources/Music/mattias_last.mp3", "mattias_last");
		//JukeBox.play("level1BG");
		//JukeBox.loop("level1BG");
		
	}
	
	private void initDialog(){
		Profile pla = new Profile("Erik", 0, "/Resources/HUD/profiles/profile_erik.png");
		Profile bos = new Profile("MK-82", 1, "/Resources/HUD/profiles/profile_mattias.png");
		
		mattiasIntroDialog = new Conversation();
		mattiasIntroDialog.addMessage("Vill du höra en gåta?", bos);
		mattiasIntroDialog.addMessage("Nej?", pla);
		mattiasIntroDialog.addMessage("vad har du och roten ur minus ett gemensamt?", bos);
		mattiasIntroDialog.addMessage("vet inte", pla);
		mattiasIntroDialog.addMessage("Ni existerar inte!", bos);
		
		defeatedDialog = new Conversation();
		defeatedDialog.addMessage("På något sätt gjorde du mig odefinierad", bos);
		defeatedDialog.addMessage("Vet du vart prinsessan Moa är?", pla);
		defeatedDialog.addMessage("Inte här i alla fall, testa nästa slott!", bos);
		defeatedDialog.addMessage("Vad träligt! Har hon inte en GPS på sig eller nåt?", pla);
		
	}
	
	private void populateButtons(){
		buttons.clear();
		
		//start btn
		Button b = new Button(tileMap);
		b.setPosition(8*30, 6*30+15);
		buttons.add(b);
		startButton = b;
		
	}
	
	private void populateDoors(){
		doors.clear();
	
		
	}
	
	private void populateSigns(){
		signs.clear();
		
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
		
		//boss
		mattias = new Mattias(tileMap, enemyProjectiles, player, throwableItems, effects);
		mattias.setPosition(-8*30, 2*30+15);
		enemies.add(mattias);
		
	}
	
	private void populateThrowableItems(){
		throwableItems.clear();
	}
	
	@Override
	public void update(){
		//events
		//intro
		if(startButton != null && startButton.isPressed()){
			introEvent = true;
		}
		if(introEvent) introEvent();
		
		if(mattias != null && mattias.isDefeated() && !mattias.isDead() && !player.isDead()){
			defeatedEvent = true;
		}
		
		if(defeatedEvent) defeatedEvent();
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
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
		populateButtons();
		populateThrowableItems();
		initDialog();

		//section
		currentSection = startSection;
		player.setPosition(sections.get(currentSection).getSpawn());
		
		//events
		playMusic = false;
		playLast = false;
		JukeBox.stop("mattias_background");
		JukeBox.stop("mattias_last");
		JukeBox.stop("victory");
		JukeBox.stop("mattias_laugh");
		
		super.reset();
		
		introEvent = false;
		defeatedEvent = false;
		
	}

	@Override
	public void activate() {
		if(playMusic)
			JukeBox.resumeLoop("mattias_background");
		
		if(playLast)
			JukeBox.resume("mattias_last");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("mattias_background");
		JukeBox.stop("mattias_last");
	}
	
	@Override
	public void deload() {
		JukeBox.close("mattias_background");
		JukeBox.close("mattias_laugh");
		JukeBox.close("victory");
		JukeBox.close("mattias_last");
	}
	
	////////EVENTS///////
	
	private void introEvent(){
		eventCounter++;
		if(eventCounter == 1){
			player.stop();
			blockInput = true;
		}else if(eventCounter == 30){
			effects.add(new Effect(tileMap, (int)startButton.getx(), (int)startButton.gety()-15, Content.Smoke1, 7));
			buttons.remove(startButton);
			startButton = null;
			mattias.playIntro();
		}else if(eventCounter == 31){
			if(!mattias.isDonePlayIntro())
				eventCounter--;
		}else if(eventCounter == 60){
			curDialog = mattiasIntroDialog;
		}else if(eventCounter == 61){
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 100){
			blockInput = false;
			introEvent = false;
			eventCounter = 0;
			mattias.begin();
			JukeBox.loop("mattias_background");
			playMusic = true;
		}
	}
	
	private void defeatedEvent(){
		eventCounter++;
		if(eventCounter == 1){
			enemyProjectiles.clear();
			blockInput = true;
			player.stop();
			playMusic = false;
			playLast = false;
			//JukeBox.stop("grej");
		}else if(eventCounter == 60){
			curDialog = defeatedDialog;
		}else if(eventCounter == 61){
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 120){
			mattias.kill();
			JukeBox.play("victory");
		}else if(eventCounter == 540){
			setStageAsCleared("2_boss", "3_boss", player.getHealth(), LevelHub.DOOR_HUB_TO_ROOM_2);
			eventCounter = 0;
			defeatedEvent = false;
			eventFinish = true;
		}
	}
	
}

package game.gameState.levels.level3;

import java.awt.Graphics2D;

import game.audio.JukeBox;
import game.entity.Button;
import game.entity.Effect;
import game.entity.level3_boss.Johannes;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.handlers.Content;
import game.hud.Conversation;
import game.hud.Profile;
import game.main.GamePanel;
import game.tileMap.AnimatedBackground;
import game.tileMap.Section;

public class Level3_BossState extends LevelState{
	
	//sections
	public static final int SECTION_MAIN = 0;
	
	//boss
	private Johannes johannes;
	
	//events
	//intro
	private Button startButton;
	private boolean introEvent = false;
	private boolean defeatedEvent = false;
	private Conversation introDialog;
	private Conversation defeatedDialog;
	private boolean playMusic = false;
	
	public Level3_BossState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/castle");
		tileMap.loadMap("/Resources/Maps/level3-boss");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new AnimatedBackground(Content.loadGrid("/Resources/Backgrounds/johannes_background.png", 256, 128, 5, 3, 13, 16), 5, 1, 102, 0);
		bg.setVector(0, -0.3);
		
		//sections
		Section temp = new Section(10, 0, GamePanel.WIDTH, 10*30, bg, tileMap, player);
		temp.setSpawn(7*30, 8*30);
		sections.add(temp);
		
		startSection = SECTION_MAIN;
		
		reset();
		
		//JukeBox.load("/Resources/Music/Bowserholt.mp3", "level1BossBG");
		JukeBox.load("/Resources/Music/victory.mp3", "victory");
		JukeBox.load("/Resources/Music/johannes_background.mp3", "johannes_background");
		JukeBox.load("/Resources/SFX/johannes_intro.mp3", "johannes_intro");
		//JukeBox.play("level1BG");
		//JukeBox.loop("level1BG");
		
	}
	
	private void initDialog(){
		Profile pla = new Profile("Erik", 0, "/Resources/HUD/profiles/profile_erik.png");
		Profile bos = new Profile("Johannes", 1, "/Resources/HUD/profiles/profile_johannes.png");
		
		introDialog = new Conversation();
		introDialog.addMessage("Är det inte du som är svenska läraren?", pla);
		introDialog.addMessage("Det heter svensklärare din promiskuösa\nF-elev!", bos);
		introDialog.addMessage("Va? Vad är en primo...\npromuski...\npromöski...", pla);
		introDialog.addMessage("Sådana som du som inte kan de\nsimplaste av svenska, ariska ord\nhar ingen rätt att leva!", bos);
		introDialog.addMessage("Jag ska domesticera alla er F-elever till en\nny ras som jag ska kalla för\nEffus Elevus Efterblivus.", bos);
		introDialog.addMessage("Va? Vad är en domes...\ndimostec...\ndommesti...", pla);
		introDialog.addMessage("TYSTNAD, framtida F-kreatur! Jag ska\nslänga ner dig i min källare och fodra dig\nmed F, ty jag är en sadist.", bos);
		introDialog.addMessage("Du pratar roligt bror", pla);
		introDialog.addMessage("Skälsord! Jag tolererar inte din\nimpertinenta attityd!", bos);
		introDialog.addMessage("inpär...\ninpretä...", pla);
		
		defeatedDialog = new Conversation();
		defeatedDialog.addMessage("Dessa.. felstavade ord..\njag.. står inte ut..\n", bos);
		
	}
	
	private void populateButtons(){
		buttons.clear();
		
		//start btn
		Button b = new Button(tileMap);
		b.setPosition(8*30, 7*30+15);
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
		johannes = new Johannes(tileMap, enemyProjectiles, player, effects);
		johannes.setPosition(8*30, -81);
		enemies.add(johannes);
		
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
		
		if(johannes != null && johannes.isDefeated() && !johannes.isDead() && !player.isDead()){
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
		JukeBox.stop("johannes_background");
		JukeBox.stop("victory");
		JukeBox.stop("johannes_intro");
		
		super.reset();
		
		///johannes kylare
		johannes.specialEfterSuperReset();
		
		defeatedEvent = false;
		introEvent = false;
	}

	@Override
	public void activate() {
		if(playMusic)
			JukeBox.resumeLoop("johannes_background");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("johannes_background");
	}
	
	@Override
	public void deload() {
		JukeBox.close("johannes_background");
		JukeBox.close("victory");
		JukeBox.close("johannes_intro");
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
			johannes.playIntro();
		}else if(eventCounter == 31){
			if(!johannes.isDonePlayIntro())
				eventCounter--;
		}else if(eventCounter == 60){
			JukeBox.loop("johannes_background");
			curDialog = introDialog;
		}else if(eventCounter == 61){
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 100){
			blockInput = false;
			introEvent = false;
			eventCounter = 0;
			johannes.begin();
			playMusic = true;
		}
	}
	
	private void defeatedEvent(){
		eventCounter++;
		if(eventCounter == 1){
			blockInput = true;
			enemyProjectiles.clear();
			player.stop();
			JukeBox.stop("johannes_background");
		}else if(eventCounter == 60){
			curDialog = defeatedDialog;
		}else if(eventCounter == 61){
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 120){
			johannes.kill();
			JukeBox.play("victory");
		}else if(eventCounter == 540){
			setStageAsCleared("3_boss", "4_boss", player.getHealth(), LevelHub.DOOR_HUB_TO_ROOM_3);
			eventCounter = 0;
			defeatedEvent = false;
			eventFinish = true;
		}
		
	}
	
}

package game.gameState.levels.level1;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import game.audio.JukeBox;
import game.entity.Effect;
import game.entity.Scenery;
import game.entity.level1_boss.alsenholt.Drake;
import game.functions.Trigger;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.handlers.Content;
import game.hud.Conversation;
import game.hud.Profile;
import game.main.GamePanel;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level1_BossState extends LevelState{
	
	//finish event //hamnar på knappen
	private Scenery toad;
	private Scenery button;
	private boolean finishEvent;
	private Conversation toadCon;
	
	//boss introduction event 
	private Conversation bossIntroCon;
	private Trigger bossIntroTrigger;
	private boolean bossIntroEvent;
	
	//boss dead
	private Conversation deathDialog;

	//sections
	public static final int SECTION_MAIN = 0;
	public static final int SECTION_BOSS = 1;
	
	//boss
	private Drake drake;
	
	private boolean wall = true;

	public Level1_BossState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/castle");
		tileMap.loadMap("/Resources/Maps/level1-boss");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/castle_background.png", 0.5);
		
		//sections
		Section temp = new Section(0, 0, 51*30, 10*30, bg, tileMap, player);
		temp.setSpawn(3*30, 6*30); //3, 6
		sections.add(temp);
		
		temp = new Section(18*30-4, 0, GamePanel.WIDTH, 10*30, bg, tileMap, player);
		sections.add(temp);
		
		startSection = SECTION_MAIN;
		
		reset();
		
		JukeBox.load("/Resources/Music/alexander_background.mp3", "level1BossBG");
		JukeBox.load("/Resources/Music/victory.mp3", "victory");
		//JukeBox.play("level1BG");
		//JukeBox.loop("level1BG");
		
	}
	
	private void initDialog(){
		Profile pla = new Profile("Erik", 0, "/Resources/HUD/profiles/profile_erik.png");
		Profile bos = new Profile("Bowserholt", 1, "/Resources/HUD/profiles/profile_bowserholt.png");
		Profile voi = new Profile("Berättarröst", 1, "/Resources/HUD/profiles/profile_morgan.png");
		Profile to = new Profile("Toad", 1, "/Resources/HUD/profiles/profile_toad.png");
		
		bossIntroCon = new Conversation();
		bossIntroCon.addMessage("Vem är du som kliver in i min domän?", bos);
		bossIntroCon.addMessage("Det är bara lilla jag", pla);
		bossIntroCon.addMessage("You shall not pass! RAWR!", bos);
		bossIntroCon.addMessage("Ne nu jävlar!", pla);
		bossIntroCon.addMessage("Bara så att du vet så är mitt skal\ngjort av ununoktium! Det är oförstörbart!\nFörsök inte ens att slå mig! RAWR!", bos);
		bossIntroCon.addMessage("Har inte ununoktium en halveringstid på\nungefär 0.89 millisekunder?", pla);
		bossIntroCon.addMessage("RAWR! Håll tyst!\ndet enda som kan skada mig är att\nramla ner i lavan under oss!", bos);
		bossIntroCon.addMessage("Det ska jag komma ihåg...", pla);
	
		toadCon = new Conversation();
		toadCon.addMessage("Thank you Erik!\nBut our princess is in another castle!", to);
		toadCon.addMessage("Vafan snackar du om?", pla);
		toadCon.addMessage("Har du inte hört? Prinsessan Moa, ordförande i elevkåren 2016, har kidnappats av lärarna.", to);
		toadCon.addMessage("Va? Detta är ju inte bra!", pla);
		toadCon.addMessage("Rädda henne unge padawan, \ndu är vårt enda hopp!", to);
		toadCon.addMessage("Vetandes att du antagligen kommer att få\nminst ett tack av henne fyller \ndig med beslutsamhet!", voi);
		
		deathDialog = new Conversation();
		deathDialog.addMessage("Hur fan lyckades du med det där?\nJag är ju odödlig!\nRAWR!", bos);
		deathDialog.addMessage("Du borde skaffa ett bättre skal!", pla);
		deathDialog.addMessage("Jag ska hämnaaaaaaaas!", bos);
	}
	
	private void populateDoors(){
		doors.clear();
	
		
	}
	
	private void populateSigns(){
		signs.clear();
		Profile p = new Profile("Skylt", 0, "/Resources/HUD/profiles/profile_skylt1.png");
		
		Conversation c;
		c = new Conversation();
		c.addMessage("Legenderna säger att den\nstygga Bowserholt lever i \ndenna mansgrotta", p);
		c.addMessage("Gå in på egen risk!", p);
		addSign(6*30+15, 6*30+15, c);
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
		drake = new Drake(tileMap, player, enemyProjectiles);
		drake.setPosition(28*30+20, 7*30-70);
		enemies.add(drake);
		
	}
	
	@Override
	public void update(){
		//events
		//intro
		bossIntroTrigger.update(player);
		if(bossIntroTrigger.hasTriggered()){
			bossIntroEvent = true;
		}
		if(bossIntroEvent) bossIntroEvent();
		
		//finish event
		if(button.intersects(player)){
			effects.add(new Effect(tileMap, (int)button.getx(), (int)button.gety(), Content.Smoke1, 10));
			button.setPosition(-30, -30);
			finishEvent = true;
			setStageAsCleared("1_boss", "2_1", player.getHealth(), LevelHub.DOOR_HUB_TO_ROOM_1);
		}
		if(finishEvent) finishEvent();
		
		//wall
		if(drake.isFalling() && wall){
			setWall(false);
		}else if(!drake.isFalling() && !wall && player.getx()+player.getCWidth()/2 < 28*30){
			setWall(true);
		}
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		toad.draw(g);
		button.draw(g);
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
		
		tileMap.setTile(17, 6, new Point(0, 0));
		
		for(int i = 18; i <= 31; i++){
			tileMap.setTile(i, 7, new Point(14, 1));
		}
		
		tileMap.setTile(33, 5, new Point(7, 1));
		tileMap.setTile(34, 5, new Point(7, 1));
		
		tileMap.setTween(1);
		
		setWall(true);
		
		//events
		bossIntroTrigger = new Trigger(new Rectangle(19*30, 0, 30, 7*30));
		toad = new Scenery(tileMap, 41*30, 7*30+11, "/Resources/Sprites/Enemies/alsenholt/toad.png", 28, 38);
		button = new Scenery(tileMap, 32*30+15, 5*30+15, "/Resources/Sprites/Enemies/alsenholt/switch.png", 30, 30);
		button.setHitbox(26, 26);
		
		JukeBox.stop("level1BossBG");
		JukeBox.stop("victory");
		
		super.reset();
		
		finishEvent = false;
		bossIntroEvent = false;
		
	}
	
	private void setWall(boolean b){
		for(int y = 0; y <= 5; y++){
			tileMap.setTile(32, y, 0, b?1:0);
		}
		wall = b;
	}

	@Override
	public void activate() {
		if(drake.hasBegun())
			JukeBox.resumeLoop("level1BossBG");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("level1BossBG");
	}
	
	@Override
	public void deload() {
		JukeBox.close("level1BossBG");
		JukeBox.close("victory");
	}

	public void bossIntroEvent(){
		eventCounter++;
		if(eventCounter == 1){
			player.stop();
			blockInput = true;
			JukeBox.loop("level1BossBG");
		}else if(eventCounter == 20){
			tileMap.setTile(17, 6, new Point(7, 1));
			
		}else if(eventCounter == 39){
			currentSection = SECTION_BOSS;
		}else if(eventCounter == 40){
			curDialog = bossIntroCon;
		}else if(eventCounter == 41){
			if(!curDialog.getDone()){
				eventCounter--;
			}
		}else if(eventCounter == 42){
			blockInput = false;
			eventCounter = 0;
			bossIntroEvent = false;
			drake.begin();
		}
	}
	
	public void finishEvent(){
		eventCounter++;
		if(eventCounter == 1){
			player.setLeft(true);
			blockInput = true;
		}else if(eventCounter == 2){
			player.stop();
			JukeBox.stop("level1BossBG");
			drake.setLastTalk();
		}else if(eventCounter == 3){
			curDialog = deathDialog;
		}else if(eventCounter == 4){
			if(!curDialog.getDone()){
				eventCounter--;
			}
		}
		if(eventCounter <= 200 && eventCounter > 4 && (eventCounter-5) % 15 == 0){
			tileMap.setTile(31-(eventCounter-1)/15, 7, new Point(0, 0));
		}else if(eventCounter == 260){
			JukeBox.play("victory");
		}else if(eventCounter == 600){
			tileMap.setTile(33, 5, new Point(0, 0));
			tileMap.setTile(34, 5, new Point(0, 0));
			tileMap.setTween(0.05);
			currentSection = SECTION_MAIN;
			
		}else if(eventCounter == 601){
			if(player.intersects(toad)){
				player.setRight(false);
				player.stop();
				curDialog = toadCon;
				toadCon.reset();
			}else{
				player.setRight(true);
				eventCounter--;
			}
		}else if(eventCounter == 602){
			if(!toadCon.getDone()) eventCounter--;
		}else if(eventCounter == 603){
			tileMap.setTween(1);
			player.setRight(true);
		}else if(eventCounter == 900){
			eventCounter = 0;
			finishEvent = false;
			eventFinish = true;
		}
	}
	
}

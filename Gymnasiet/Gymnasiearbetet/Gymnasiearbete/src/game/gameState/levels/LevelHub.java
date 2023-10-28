package game.gameState.levels;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import game.audio.JukeBox;
import game.entity.Door;
import game.entity.Scenery;
import game.functions.Trigger;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.handlers.Content;
import game.handlers.Saves;
import game.hud.Conversation;
import game.hud.Profile;
import game.tileMap.Background;
import game.tileMap.Section;

public class LevelHub extends LevelState {
	
	//sections
	public static final int SECTION_MAIN = 0;
	public static final int SECTION_ROOM_1 = 1;
	public static final int SECTION_ROOM_2 = 2;
	public static final int SECTION_ROOM_3 = 3;
	public static final int SECTION_ROOM_4 = 4;
	
	//doors
	public static final int DOOR_HUB_TO_ROOM_1 = 0;
	public static final int DOOR_ROOM_1_TO_HUB = 1;
	public static final int DOOR_STAGE_1_1 = 2;
	public static final int DOOR_STAGE_1_2 = 3;
	public static final int DOOR_STAGE_1_BOSS = 4;
	public static final int DOOR_HUB_TO_ROOM_2 = 5;
	public static final int DOOR_ROOM_2_TO_HUB = 6;
	public static final int DOOR_STAGE_2_1 = 7;
	public static final int DOOR_STAGE_2_2 = 8;
	public static final int DOOR_STAGE_2_BOSS = 9;
	public static final int DOOR_HUB_TO_ROOM_3 = 10;
	public static final int DOOR_ROOM_3_TO_HUB = 11;
	public static final int DOOR_STAGE_3_boss = 12;
	public static final int DOOR_HUB_TO_ROOM_4 = 13;
	public static final int DOOR_ROOM_4_TO_HUB = 14;
	public static final int DOOR_STAGE_4_boss = 15;
	
	private Scenery[] fejkDoors;
	
	//intro
	private Conversation introDialog1;
	private Trigger introTrigger1;
	private Conversation introDialog2;
	private Trigger introTrigger2;

	public LevelHub(GameStateManager gsm) {
		super(gsm);
		init();
		
		JukeBox.load("/Resources/Music/ruins.mp3", "ruins");
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/skola_inne");
		tileMap.loadMap("/Resources/Maps/hub");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/svart.gif", 0.1);
		
		//sections
		Section temp = new Section(0, 0, 72*30, 10*30, bg, tileMap, player);
		temp.setSpawn(2*30, 5*30); 
		sections.add(temp);
		startSection = SECTION_MAIN;
		
		temp = new Section(0, 12*30, 20*30, 10*30, bg, tileMap, player);//room for level1_x
		sections.add(temp);
		
		temp = new Section(0, 24*30, 24*30, 10*30, bg, tileMap, player);//room for level2_x
		sections.add(temp);
		
		temp = new Section(0, 36*30, 24*30, 10*30, bg, tileMap, player);//room for level3_x
		sections.add(temp);
		
		temp = new Section(0, 48*30, 18*30, 10*30, bg, tileMap, player);//room for level4_x
		sections.add(temp);
		
		reset();
		
		//dialogs
		initDialog();
		
		//JukeBox.load("/Resources/Music/default.mp3", "defaultBG");
		//JukeBox.play("level1BG");
		//JukeBox.loop("level1BG");
	}
	
	private void initDialog(){
		Profile voi = new Profile("Berättarröst", 1, "/Resources/HUD/profiles/profile_morgan.png");
		
		introDialog1 = new Conversation();
		introDialog1.addMessage("Du har nu hamnat på den översta våningen av Grillska Gymnasiet Uppsala.", voi);
		introDialog1.addMessage("Anledningarna är för komplicerade och för långa för att de ska få plats här på denna lilla ruta.", voi);
		introDialog1.addMessage("Lärarna löper amok och endast DU kan stoppa dem från att exekvera sina hemska planer!", voi);
		introDialog1.addMessage("(Du har inte glömt att läsa kontrollerna i options, va?)", voi);
		
		introDialog2 = new Conversation();
		introDialog2.addMessage("\"Vad är detta? Någon som har klottrat på dörrarna?\" tänker du", voi);
		introDialog2.addMessage("Nej det är det inte, detta är magiska dörrar som leder dig till alternativa dimensioner där lärarna befinner sig.", voi);
		introDialog2.addMessage("De dömer dina prestationer med betyg som visas överst på dörrarna. Ju mer skada du tar, desto lägre ditt betyg.", voi);
		introDialog2.addMessage("Om du tar för mycket skada kan du hamna på F och därmed dö (precis som i verkligheten).", voi);
	}
	
	private void populateDoors(){
		doors.clear();
		//till room 1 från hub
		generateSectionDoor(SECTION_MAIN, 1, 15*30+15, 6*30);
		
		//till hub från room 1
		generateSectionDoor(SECTION_ROOM_1, 0, 16*30+15, 18*30);
		
		//stage 1_1
		generateSpiralDoor(SECTION_ROOM_1, GameStateManager.LEVEL1_1STATE, 9*30+15, 18*30, "1_1");
		
		///stage 1_2
		generateSpiralDoor(SECTION_ROOM_1, GameStateManager.LEVEL1_2STATE, 6*30+15, 18*30, "1_2");
		
		//stage 1_boss
		generateSpiralDoor(SECTION_ROOM_1, GameStateManager.LEVEL1_BOSS, 3*30+15, 18*30, "1_boss");
		
		//från hub till room 2
		generateSectionDoor(SECTION_MAIN, 6, 25*30+15, 6*30);
		
		//till hub från room 2
		generateSectionDoor(SECTION_ROOM_2, 5, 20*30+15, 30*30);
		
		//stage 2_1
		generateSpiralDoor(SECTION_ROOM_2, GameStateManager.LEVEL2_1STATE, 11*30+15, 30*30, "2_1");
		
		//stage 2_2
		generateSpiralDoor(SECTION_ROOM_2, GameStateManager.LEVEL2_2STATE, 8*30+15, 30*30, "2_2");
		
		//stage 2_boss
		generateSpiralDoor(SECTION_ROOM_2, GameStateManager.LEVEL2_BOSS, 3*30+15, 30*30, "2_boss");
		
		//från hub till room 3
		generateSectionDoor(SECTION_MAIN, 11, 33*30+15, 6*30);
		
		//till hub från room 3
		generateSectionDoor(SECTION_ROOM_3, 10, 20*30+15, 42*30);
		
		//stage3_boss
		generateSpiralDoor(SECTION_ROOM_3, GameStateManager.LEVEL3_BOSS, 3*30+15, 42*30, "3_boss");
		
		//från hub till room 4
		generateSectionDoor(SECTION_MAIN, DOOR_ROOM_4_TO_HUB, 42*30+15, 6*30);
		
		//till hub från room 4
		generateSectionDoor(SECTION_ROOM_4, DOOR_HUB_TO_ROOM_4, 15*30+15, 54*30);
		
		//stage4_boss
		generateSpiralDoor(SECTION_ROOM_4, GameStateManager.LEVEL4_BOSS, 2*30+15, 54*30, "4_boss");
		
		
		//fejk doors//
		fejkDoors = new Scenery[3];
		fejkDoors[0] = new Scenery(tileMap, 3*30+15, 6*30, "/Resources/Sprites/Doors/do_not_open.png", 34, 60);
		fejkDoors[1] = new Scenery(tileMap, 9*30+15, 6*30, "/Resources/Sprites/Doors/do_not_open.png", 34, 60);
		fejkDoors[2] = new Scenery(tileMap, 51*30+15, 6*30, "/Resources/Sprites/Doors/do_not_open.png", 34, 60);
		
	}
	
	private void populateSigns(){
		signs.clear();
		Profile p = new Profile("Skylt", 0, "/Resources/HUD/profiles/profile_skylt2.png");
		Profile nil = new Profile("Nils", 1, "/Resources/HUD/profiles/profile_nils.png");
		Profile ras = new Profile("Rasmus", 1, "/Resources/HUD/profiles/profile_rasmus.png");
		
		Conversation temp = new Conversation();
		temp.addMessage("Lärar rummet\n\n-skriven av Johannes Hultin-", p);
		addSign(2*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("405\nGrupprum Pascal", p);
		addSign(14*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("Rum med kopiator o grejer i.\n(Det finns ett X på dörren, så gud har gjort det omöjligt att gå in i detta rum)", p);
		addSign(8*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("404\nGrupprum Gauss\n(hittades inte, fniss!)", p);
		addSign(24*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("403\nGrupprum Nobel", p);
		addSign(32*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("402\nGrupprum Ling", p);
		addSign(41*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("Musikrummet", p);
		addSign(50*30+15, 6*30+15, temp, Content.sign2);
		
		temp = new Conversation();
		temp.addMessage("Jag går här o nilsar runt.", nil);
		temp.addMessage("Å hjälp! jag måste \"Kihla\" iväg!", nil);
		addSign(60*30+15, 6*30+15, temp, Content.loadRow("/Resources/Sprites/Other/nils.png", 25, 30, 0, 1));
		
		temp = new Conversation();
		temp.addMessage("ZZZzzZZzZZz...", ras);
		temp.addMessage("zZzzZZzzZz...", ras);
		temp.addMessage("ZZzzzzzZzzZZzZ...", ras);
		temp.addMessage("ZZzzZZZZZzzZ...", ras);
		temp.addMessage("zzzzzzzzzzzzZZZzzzzzzzz...", ras);
		addSign(67*30+15, 6*30+15, temp, Content.loadRow("/Resources/Sprites/Other/rasmus.png", 25, 30, 0, 1));
		
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
		
	}
	
	@Override
	public void update(){
		//intron
		introTrigger1.update(player);
		if(introTrigger1.hasTriggered()){
			curDialog = introDialog1;
			readingSign = true;
			paused = true;
		}
		
		introTrigger2.update(player);
		if(introTrigger2.hasTriggered()){
			curDialog = introDialog2;
			readingSign = true;
			paused = true;
			
			try{
				Saves.setData("experienced_intro", "true");
				Saves.writeFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		
		//blodiga dörrar
		for(Scenery s : fejkDoors)
			s.draw(g);
		
		//player ritas innan fejkdörrarna, så då gör vi den fulaste lösningen någonsin och ritar player en gång till.
		player.draw(g);
		
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

		//section
		int hubSpawn = Saves.getHubSpawn();
		if(hubSpawn == -1){
			currentSection = startSection;
			player.setPosition(sections.get(currentSection).getSpawn());
		}else{
			Door d = doors.get(hubSpawn);
			currentSection = d.getSourceSection();
			player.setPosition(d.getPos());
		}
		
		//intron
		introTrigger1 = new Trigger(new Rectangle(7*30, 4*30, 30, 90));
		introTrigger2 = new Trigger(new Rectangle(11*30, 16*30, 30, 90));
		if(Saves.hasExperiencesIntro()){
			introTrigger1.disable();
			introTrigger2.disable();
		}
		
		super.reset();
	}

	@Override
	public void activate() {
		JukeBox.resumeLoop("ruins");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("ruins");
	}
	
	@Override
	public void deload() {
		
	}
}













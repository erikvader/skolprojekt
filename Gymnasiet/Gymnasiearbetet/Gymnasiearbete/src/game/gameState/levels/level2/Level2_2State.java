package game.gameState.levels.level2;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.audio.JukeBox;
import game.entity.Scenery;
import game.entity.Teleport;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.gameState.levels.LevelHub;
import game.handlers.Content;
import game.hud.Conversation;
import game.hud.Profile;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level2_2State extends LevelState {
	
	//finish
	private Teleport tele;
	
	//sections
	public static final int SECTION_MAIN = 0;
	
	//mattegrejer
	private ArrayList<Scenery> mattegrejer = new ArrayList<Scenery>();

	public Level2_2State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/grass");
		tileMap.loadMap("/Resources/Maps/level2-2");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/grass_background2_shorter.png", 0.1);
		
		//sections
		Section temp = new Section(0, 0, 63*30, 37*30, bg, tileMap, player);
		temp.setSpawn(2*30, 7*30); //2, 7
		sections.add(temp);
		startSection = SECTION_MAIN;
		
		reset();
		
		//teleporter
		tele = new Teleport(tileMap);
		tele.setTilePosition(59, 35);
		
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
	
	private void populateScenary(){
		mattegrejer.clear();
		
		//Q1
		Scenery s = new Scenery(tileMap, 20*30, 5*30+20, "/Resources/Sprites/Scenery/matte/Q1.png", 240, 100);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 17*30, 8*30, "/Resources/Sprites/Scenery/matte/Q1_A1.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 20*30, 8*30, "/Resources/Sprites/Scenery/matte/Q1_A2.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 23*30, 8*30, "/Resources/Sprites/Scenery/matte/Q1_A3.png", 60, 60);
		mattegrejer.add(s);
		
		//Q2
		s = new Scenery(tileMap, 36*30, 14*30+20, "/Resources/Sprites/Scenery/matte/Q2.png", 240, 100);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 33*30, 17*30, "/Resources/Sprites/Scenery/matte/Q2_A1.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 36*30, 17*30, "/Resources/Sprites/Scenery/matte/Q2_A2.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 39*30, 17*30, "/Resources/Sprites/Scenery/matte/Q2_A3.png", 60, 60);
		mattegrejer.add(s);
		
		//Q3
		s = new Scenery(tileMap, 51*30, 23*30+20, "/Resources/Sprites/Scenery/matte/Q3.png", 240, 100);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 48*30, 26*30, "/Resources/Sprites/Scenery/matte/Q3_A1.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 51*30, 26*30, "/Resources/Sprites/Scenery/matte/Q3_A2.png", 60, 60);
		mattegrejer.add(s);
		s = new Scenery(tileMap, 54*30, 26*30, "/Resources/Sprites/Scenery/matte/Q3_A3.png", 60, 60);
		mattegrejer.add(s);
	}
	
	private void populateSigns(){
		signs.clear();
		Profile p = new Profile("MK-82", 0, "/Resources/HUD/profiles/profile_mattias.png");
		Profile vik = new Profile("Viktor", 1, "/Resources/HUD/profiles/profile_viktor.png");
		Profile pla = new Profile("Erik", 0, "/Resources/HUD/profiles/profile_erik.png");
		
		Conversation c;
		//Q1
		c = new Conversation();
		c.addMessage("Det �r jag! Mattias Karlsson AKA MK-82!\nF�r att ens ha en chans att se mig\nm�ste du klara 3 tester!", p);
		c.addMessage("Vi b�rjar enkelt, t�nk efter noga vad svaret\nkan vara. \nESC f�r att b�rja om N�R du g�r fel", p);
		addSign(12*30+15, 8*30+15, c);
		
		c = new Conversation();
		c.addMessage("va? det �r ett additionstecken d�r!\nInte ett subtraktionstecken! Vi jobbar inte\ni modulus 2 heller!", p);
		addSign(17*30, 15*30+15, c);
		
		c = new Conversation();
		c.addMessage("Trodde du att vi r�knade i sandh�gar\neller n�got?", p);
		addSign(23*30, 15*30+15, c);
		
		//Q2
		c = new Conversation();
		c.addMessage("Du �r smartare �n vad du ser ut,\nvi hoppar direkt till matte fr�n 9:an\nkommer du ih�g detta?", p);
		addSign(28*30+15, 17*30+15, c);
		
		c = new Conversation();
		c.addMessage("Jag vet inte ens vad jag ska s�ga,\ninte ens n�ra. 2^2 betyder 2*2\nsom �r 4. G�r om g�r r�tt!", p);
		addSign(36*30, 24*30+15, c);
		
		c = new Conversation();
		c.addMessage("nej! 2*4 = 8, men 2^4 = 16\nkommer du inte ih�g sakerna du har\nl�rt dig?", p);
		addSign(39*30, 24*30+15, c);
		
		c = new Conversation();
		c.addMessage("D�r ser man, du kom ih�g enkel matte,\nnu hoppar vi till matte 4 med den\nsnyggaste formeln inom matematiken.", p);
		c.addMessage("Du kommer aldrig att kunna detta!\nOm du inte �r en SMES-elev kan\ndu trycka alt+F4 f�r att skippa detta hinder.", p);
		c.addMessage("(Man har en Mac d� och d�r finns inte det kortkommandot)", p);
		addSign(44*30+15, 26*30+15, c);
		
		c = new Conversation();
		c.addMessage("ha! Jag sa ju att du skulle ta fel svar!\nDet r�tta svaret �r enklare �n vad du tror.", p);
		addSign(48*30, 33*30+15, c);
		
		c = new Conversation();
		c.addMessage("Tusan!", vik);
		c.addMessage("Vad g�r du h�r?", pla);
		c.addMessage("Jag f�rs�kte r�dda Moa s� att jag kunde f� �tminstone en handskakning, men jag har fastnat h�r i denna grop!", vik);
		c.addMessage("Ska det inte vara en skylt h�r?", pla);
		c.addMessage("Jag br�t s�nder den med mina gigantiska, sexiga muskler och �t sedan upp den.", vik);
		c.addMessage("...", pla);
		c.addMessage("Du skulle ha tagit ingenj�rssp�ret ist�llet!", pla);
		c.addMessage("Akta s� att jag inte g�r en T-perm p� dig!", vik);
		addSign(54*30, 33*30+15, c, Content.loadRow("/Resources/Sprites/Other/sign_viktor.png", 25, 30, 0, 1));
		
		c = new Conversation();
		c.addMessage("Vafalls?\nHur kunde du klara den sv�ra fr�gan?\nJag antar att du �r v�rdig att m�ta mig.", p);
		addSign(55*30+15, 35*30+15, c);
		
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
			setStageAsCleared("2_2", "2_boss", player.getHealth(), LevelHub.DOOR_STAGE_2_2);
			JukeBox.stop("defaultBG");
			eventFinishVictory = true;
		}
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		
		tele.draw(g);
		
		for(Scenery s : mattegrejer)
			s.draw(g);
		
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
		populateScenary();
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













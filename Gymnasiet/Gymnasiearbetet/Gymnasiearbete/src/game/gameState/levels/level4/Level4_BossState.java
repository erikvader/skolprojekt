package game.gameState.levels.level4;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import game.audio.JukeBox;
import game.entity.Effect;
import game.entity.Scenery;
import game.entity.Spring;
import game.entity.level4_boss.alsenholt.Drake;
import game.entity.level4_boss.fackverk.Fackholt;
import game.entity.level4_boss.fackverk.TransitionBoss;
import game.functions.Trigger;
import game.gameState.GameStateManager;
import game.gameState.LevelState;
import game.handlers.Content;
import game.handlers.Saves;
import game.hud.Conversation;
import game.hud.Profile;
import game.main.GamePanel;
import game.tileMap.Background;
import game.tileMap.Section;

public class Level4_BossState extends LevelState{
	
	//phases
	public static final int PHASE_1 = 0;
	public static final int PHASE_2 = 1;
	
	private int curPhase = PHASE_1;
	
	//music
	private boolean playMusic = false;
	
	//sections
	public static final int SECTION_MAIN = 0;
	public static final int SECTION_BOSS = 1;
	public static final int SECTION_BOSS_2 = 2;
	
	////////PHASE 1////////
	//boss
	private Drake drake;
	private boolean spawnedTrampoline = false;
	
	//boss krockat event
	private boolean krockatEvent = false;
	private int krockatCounter = 0;
	private boolean krockatPlayed = false;
	
	//boss introduction event 
	private Conversation bossIntroCon1;
	private Trigger bossIntroTrigger1;
	private boolean bossIntroEvent1;
	
	//finish event //hamnar på knappen
	private Scenery button;
	private boolean transitionEvent1;
	private Conversation transitionCon;
	
	////////PHASE 2/////////
	//boss
	private Fackholt fackholt;
	
	//boss intro event
	private Conversation bossIntroCon2;
	private boolean bossIntroEvent2;
	private Trigger bossIntroTrigger2;
	private TransitionBoss transitionBoss;
	
	//finish event
	private boolean defeatedEvent;
	private Conversation defeatedDialog;
	private Scenery prinsessa;
	private boolean moaEvent = false;
	private Conversation moaDialog;
	private Scenery cage;
	private Scenery heart;
	private Trigger moaTrigger;
	
	private Background abg;
	
	public Level4_BossState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		//GamePanel.zoom(1);
		
		tileMap.loadTiles("/Resources/Tilesets/castle");
		tileMap.loadMap("/Resources/Maps/level4-boss");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Resources/Backgrounds/castle_background.png", 0.5);
		
		abg = new Background("/Resources/Backgrounds/hell_background.png", 1);
		
		//sections
		Section temp = new Section(0, 0, 51*30, 10*30, bg, tileMap, player);
		temp.setSpawn(3*30, 6*30); //3, 6
		sections.add(temp);
		
		temp = new Section(18*30-4, 0, GamePanel.WIDTH, 10*30, bg, tileMap, player);
		temp.setFallOutside(false);
		sections.add(temp);
		
		temp = new Section(17*30+10, 11*30, GamePanel.WIDTH, 10*30, abg, tileMap, player);
		temp.setSpawn(30*30, 10*30);
		sections.add(temp);
		
		JukeBox.load("/Resources/Music/alexander_background.mp3", "bg1");
		JukeBox.load("/Resources/Music/alexander_background_2.mp3", "bg2");
		JukeBox.load("/Resources/Music/victory.mp3", "victory");
		
		reset();
		
	}
	
	private void initDialog(){
		Profile pla = new Profile("Erik", 0, "/Resources/HUD/profiles/profile_erik.png");
		Profile bos = new Profile("Bowserholt", 1, "/Resources/HUD/profiles/profile_bowserholt2.png");
		Profile bos2 = new Profile("Bowserholt", 1, "/Resources/HUD/profiles/profile_bowserholt3.png");
		Profile moa = new Profile("Moa", 1, "/Resources/HUD/profiles/profile_prinsessa.png");
		
		bossIntroCon1 = new Conversation();
		if(Saves.getAlsenholt2() == 0){
			bossIntroCon1.addMessage("Det är jag, den stora stygga Bowserholt, tillbaka bättre och snyggare än aldrig förr", bos);
			bossIntroCon1.addMessage("Fryser inte du? Du har ganska lite fett på kroppen. Hur kan du prata? Dessutom har väl dina stämband brunnit bort?", pla);
			bossIntroCon1.addMessage("Sluta! Ifrågasätt inte!", bos);
		}else if(Saves.getAlsenholt2() == 1){
			bossIntroCon1.addMessage("Är du tillbaka för att döda mig igen? Hur orkar du? Gå ut istället eller gör någonting produktivt.", bos);
		}else if(Saves.getAlsenholt2() == 2){
			bossIntroCon1.addMessage("En tredje gång? Skaffa ett liv! Jag trodde att jag var en svår boss, du måste fuska!", bos);
		}else if(Saves.getAlsenholt2() >= 3){
			bossIntroCon1.addMessage("Njuter du av att döda mig eller, sadist? Eller vill du bara se vad jag kommer att säga härnäst? Gör som du vill, jag har slutat att bry mig.", bos);
		}
		
		transitionCon = new Conversation();
		if(Saves.getAlsenholt2() >= 0 && Saves.getAlsenholt2() <= 2){
			transitionCon.addMessage("Det är inte över än...\nThis isn't even my final form...", bos);
		}else if(Saves.getAlsenholt2() >= 3){
			transitionCon.addMessage("Och så var det dags igen...", bos);
		}
		
		bossIntroCon2 = new Conversation();
		if(Saves.getAlsenholt2() == 0){
			bossIntroCon2.addMessage("Du tror att du har besegrat mig va?", bos);
			bossIntroCon2.addMessage("FEL!", bos);
			bossIntroCon2.addMessage("Jag har endast använt 20% av min totala kraft, nu ska jag visa dig min fulla styrka!", bos);
			bossIntroCon2.addMessage("Skåda, min ultimata, sanna och teknikinspirerande form!", bos);
		}else if(Saves.getAlsenholt2() == 1){
			bossIntroCon2.addMessage("Du tror att du har besegrat mig va?", bos);
			bossIntroCon2.addMessage("FEL!", bos);
			bossIntroCon2.addMessage("Jag har endast använt 20% av min totala... eller ja, du vet ju redan.", bos);
		}else if(Saves.getAlsenholt2() == 2){
			bossIntroCon2.addMessage("Dina andra två gånger var bara tur, nu jävlar!", bos);
		}else if(Saves.getAlsenholt2() >= 3){
			bossIntroCon2.addMessage("A, du vet redan vad som kommer att hända. Du har besegrat mig "+Saves.getAlsenholt2()+" gånger redan trots allt.", bos);
			bossIntroCon2.addMessage("Så, blah blah blah nu min fulla styrka blah blah blah etc. ", bos);
		}
		
		defeatedDialog = new Conversation();
		if(Saves.getAlsenholt2() == 0){
			defeatedDialog.addMessage("Detta är omöjligt... jag ska ju vara odödlig... kommer jag att dö på riktigt nu?", bos2);
		}else if(Saves.getAlsenholt2() == 1){
			defeatedDialog.addMessage("En andra gång?! Omöjligt!!", bos2);
		}else if(Saves.getAlsenholt2() == 2){
			defeatedDialog.addMessage("AJ! Det gör lika ont varje gång!", bos2);
			defeatedDialog.addMessage("Snälla inget mer! Gå och spela något annat!", bos2);
		}else if(Saves.getAlsenholt2() >= 3){
			defeatedDialog.addMessage("Jag börjar att bli van med den här känslan...", bos2);
		}
		
		moaDialog = new Conversation();
		if(Saves.getAlsenholt2() == 0){
			moaDialog.addMessage("Det är jag, å fagra sköna, den oslagbara hjälten som har räddat dig, ty jag är oslagbar.", pla);
			moaDialog.addMessage("Min hjälte! Låt oss åtminstone skaka hand för att sedan leva lyckliga i alla våra dagar!", moa);
		}else if(Saves.getAlsenholt2() == 1){
			moaDialog.addMessage("Det är jag, å fagra sköna, din kamrat som räddar dig ännu en gång!", pla);
			moaDialog.addMessage("Min hjälte! Låt oss åtminstone kramas för att sedan gå på en dejt!", moa);
			moaDialog.addMessage("Låter som en bra plan!", pla);
		}else if(Saves.getAlsenholt2() == 2){
			moaDialog.addMessage("Det är jag, å fagra sköna, din pojkvän som är här för en andra dejt!", pla);
			moaDialog.addMessage("Min hjälte! Låt oss åtminstone kyssas för att sedan gå på en dejt i solnedgången och kyssas lite till!", moa);
		}else if(Saves.getAlsenholt2() >= 3){
			moaDialog.addMessage("Det är jag, å fagra sköna, din make som är här för att åter igen rädda dig!", pla);
			int grej = Saves.getAlsenholt2()-3;
			String a;
			if(grej == 0)
				a = "första";
			else if(grej == 1)
				a = "andra";
			else if(grej == 2)
				a = "tredje";
			else
				a = (grej+1)+":e";
			moaDialog.addMessage("Min älskling! Låt oss skaffa vårt "+a+" barn för att sedan leva lyckliga tills döden skiljer oss åt!", moa);
			moaDialog.addMessage("Till sängkammaren!", pla);
		}
		
	
	}
	
	private void populateDoors(){
		doors.clear();
	}
	
	private void populateSigns(){
		signs.clear();
		Profile p = new Profile("Skylt", 0, "/Resources/HUD/profiles/profile_skylt1.png");
		
		Conversation c;
		c = new Conversation();
		c.addMessage("Den nyrenoverade mansgrottan! Stiligare än någonsin.", p);
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
		drake = null;
		
		if(curPhase == PHASE_1){
			//boss
			drake = new Drake(tileMap, player, enemyProjectiles, enemies, effects);
			drake.setPosition(29*30, 7*30-70);
			enemies.add(drake);
		}else if(curPhase == PHASE_2){
			//spawna den andra
			fackholt = new Fackholt(tileMap, enemyProjectiles, player, effects);
			fackholt.setPosition(-500, -500);
			enemies.add(fackholt);
			
			//transition
			transitionBoss = new TransitionBoss(tileMap);
			transitionBoss.setPosition(20*30+15, 19*30-10);
		}
		
	}
	
	@Override
	public void update(){
		if(curPhase == PHASE_1){
			//events
			//intro
			bossIntroTrigger1.update(player);
			if(bossIntroTrigger1.hasTriggered()){
				bossIntroEvent1 = true;
			}
			if(bossIntroEvent1) bossIntroEvent1();
			
			//boss
			if(drake.isLast_attack() && !spawnedTrampoline){
				spawnedTrampoline = true;
				Spring s = new Spring(tileMap);
				s.setPosition(19*30+15, 6*30+21);
				s.setSpeed(-9);
				springs.add(s);
				effects.add(new Effect(tileMap, 19*30+15, 6*30, Content.Smoke1, 10));
				setWall(false);
			}
			
			//engångs use
			for(int i = 0; i < springs.size(); i++){
				if(springs.get(i).isBouncing()){
					springs.remove(i);
					i--;
				}
			}
			
			//krockat event
			if(drake.hasKrockat() && !krockatPlayed){
				krockatEvent = true;
			}
			if(krockatEvent) krockatEvent();
			
			//finish event
			if(button.intersects(player)){
				effects.add(new Effect(tileMap, (int)button.getx(), (int)button.gety(), Content.Smoke1, 10));
				button.setPosition(-30, -30);
				transitionEvent1 = true;
			}
			
			if(transitionEvent1) transitionEvent1();
			
		}else if(curPhase == PHASE_2){
			//intro
			bossIntroTrigger2.update(player);
			if(bossIntroTrigger2.hasTriggered()){
				bossIntroEvent2 = true;
			}
			if(bossIntroEvent2) bossIntroEvent2();
			
			if(transitionBoss != null) transitionBoss.update();
			
			if(fackholt != null && fackholt.isDefeated() && !fackholt.isDead() && !player.isDead()){
				defeatedEvent = true;
			}
			
			if(defeatedEvent) defeatedEvent();
			if(moaEvent) moaEvent();
		}
		
		super.update();
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.drawLayer1(g);
		button.draw(g);
		prinsessa.draw(g);
		cage.draw(g);
		heart.draw(g);
		if(transitionBoss != null) transitionBoss.draw(g);
		super.drawLayer2(g);
	}
	
	////////events/////
	@Override
	protected void reset(){
		//kolla vilken phase det är
		checkPhase();
		
		populateEnemies();
		populateSpikes();
		populateSprings();
		populatePlatforms();
		populateSigns();
		populateDoors();
		initDialog();

		tileMap.setTween(1);
		spawnedTrampoline = false;
		
		//events
		bossIntroTrigger1 = new Trigger(new Rectangle(19*30, 0, 30, 7*30));
		transitionEvent1 = false;
		button = new Scenery(tileMap, 32*30+15, 5*30+15, "/Resources/Sprites/Enemies/alsenholt/switch.png", 30, 30);
		button.setHitbox(26, 26);
		
		bossIntroTrigger2 = new Trigger(new Rectangle(18*30, 19*30, 30*14, 30));
		bossIntroEvent2 = false;
		
		//krockat
		tileMap.setShaking(false, 0);
		krockatEvent = false;
		krockatCounter = 0;
		krockatPlayed = false;
		
		//set korrekt background music
		playMusic = false;
		JukeBox.stop("bg1");
		JukeBox.stop("bg2");
		JukeBox.stop("bg");
		if(curPhase == PHASE_1){
			JukeBox.setEqual("bg", "bg1");
		}else if(curPhase == PHASE_2){
			JukeBox.setEqual("bg", "bg2");
		}
		
		//section
		if(curPhase == PHASE_1){
			currentSection = startSection;
			setMapPhase1();
		}else if(curPhase == PHASE_2){
			currentSection = SECTION_BOSS_2;
			setMapPhase2();
		}
		
		player.setPosition(sections.get(currentSection).getSpawn());
		
		//moa
		moaTrigger = new Trigger(new Rectangle(42*30+5, 5*30, 30-5, 90));
		prinsessa = new Scenery(tileMap, 43*30, 5*30-2-16, "/Resources/Sprites/prinsessa/prinsessa.png", 30, 32);
		cage = new Scenery(tileMap, 43*30, 2*30+45, "/Resources/Sprites/prinsessa/cage.png", 23, 90);
		heart = new Scenery(tileMap, 42*30+15, 6*30+15, "/Resources/Sprites/prinsessa/heart.png", 15, 13);
		heart.hide();
		
		super.reset();
		
		JukeBox.stop("victory");
		
		bossIntroEvent1 = false;
		transitionEvent1 = false;
		bossIntroEvent2 = false;
		defeatedEvent = false;
		moaEvent = false;
		
	}
	
	private void checkPhase(){
		String data = Saves.getData("level_4_boss_phase");
		if(data == null || data.equals("1")){
			curPhase = PHASE_1;
		}else if(data.equals("2")){
			curPhase = PHASE_2;
		}
	}
	
	private void setPhaseTo2(){
		Saves.setData("level_4_boss_phase", "2");
		try {
			Saves.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void setPhaseTo1(){
		Saves.setData("level_4_boss_phase", "1");
		try {
			Saves.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setWall(boolean b){
		for(int y = 0; y <= 5; y++){
			tileMap.setTile(32, y, 0, b?1:0);
		}
	}
	
	private void setMapPhase1(){
		//sätter ingången öppen
		tileMap.setTile(17, 6, new Point(0, 0));
		
		//fixar bron och lavan
		for(int i = 18; i <= 31; i++){
			tileMap.setTile(i, 7, new Point(14, 1));
			tileMap.setTile(i, 8, new Point(0, 3));
			tileMap.setTile(i, 9, new Point(1, 3));
		}
		
		//stänger ingången till toad
		tileMap.setTile(33, 5, new Point(7, 1));
		tileMap.setTile(34, 5, new Point(7, 1));
		
		//fixar platformen till höger
		tileMap.setTile(32, 6, 8, 1);
		tileMap.setTile(32, 7, 24, 1);
		tileMap.setTile(32, 8, 24, 1);
		tileMap.setTile(32, 9, 24, 1);
		
		//fixar marken på fas 2
		for(int i = 18; i <= 31; i++){
			tileMap.setTile(i, 20, 20, 1);
		}
		
		//lägger till osynliga väggen
		setWall(true);
	}
	
	private void setMapPhase2(){
		button.setPosition(-30, -30);
		
		//sätter ingången stängd
		tileMap.setTile(17, 6, new Point(7, 1));
		
		//fixar bron och lavan
		for(int i = 18; i <= 31; i++){
			tileMap.setTile(i, 7, 0, 0);
			tileMap.setTile(i, 8, 0, 0);
			tileMap.setTile(i, 9, 0, 0);
		}
		
		//fixar platformen till höger
		tileMap.setTile(32, 6, 0, 0);
		tileMap.setTile(32, 7, 0, 0);
		tileMap.setTile(32, 8, 0, 0);
		tileMap.setTile(32, 9, 0, 0);
		
		//fixar marken på fas 2
		for(int i = 18; i <= 31; i++){
			tileMap.setTile(i, 20, 20, 1);
		}
		
		//tar bort wall
		setWall(false);
	}

	@Override
	public void activate() {
		if(playMusic)
			JukeBox.resumeLoop("bg");
	}

	@Override
	public void deactivate() {
		JukeBox.stop("bg");
	}
	
	@Override
	public void deload() {
		JukeBox.close("bg2");
		JukeBox.close("bg1");
		JukeBox.close("bg");
		JukeBox.close("victory");
	}

	public void bossIntroEvent1(){
		eventCounter++;
		if(eventCounter == 1){
			player.stop();
			blockInput = true;
			JukeBox.loop("bg");
			playMusic = true;
		}else if(eventCounter == 20){
			//stäng ingången
			tileMap.setTile(17, 6, new Point(7, 1));
		}else if(eventCounter == 39){
			currentSection = SECTION_BOSS;
		}else if(eventCounter == 40){
			curDialog = bossIntroCon1;
		}else if(eventCounter == 41){
			if(!curDialog.getDone()){
				eventCounter--;
			}
		}else if(eventCounter == 42){
			blockInput = false;
			eventCounter = 0;
			bossIntroEvent1 = false;
			drake.begin();
		}
	}
	
	public void transitionEvent1(){
		eventCounter++;
		if(eventCounter == 1){
			player.setLeft(true);
			blockInput = true;
		}else if(eventCounter == 2){
			player.stop();
			playMusic = false;
			JukeBox.stop("bg");
			curDialog = transitionCon;
		}else if(eventCounter == 3){
			if(!curDialog.getDone())
				eventCounter--;
		}
		if(eventCounter <= 199 && eventCounter >= 3 && (eventCounter-3) % 15 == 0){
			tileMap.setTile(31-(eventCounter-3)/15, 7, new Point(0, 0));
		}else if(eventCounter == 450){
			setMapPhase2();
		}else if(eventCounter == 510) {
			blackBarsReset(58);
		}
		if(eventCounter >= 510) {
			blackBarsClose();
		}
		if(eventCounter == 580){
			eventCounter = 0;
			transitionEvent1 = false;
			setPhaseTo2();
			reset();
		}
	}
	
	private void krockatEvent(){
		krockatCounter++;
		if(krockatCounter == 1){
			tileMap.setShaking(true, 35);
		}else if(krockatCounter == 91){
			tileMap.setShaking(false, 0);
			krockatEvent = false;
			krockatCounter = 0;
			krockatPlayed = true;
		}
	}
	
	private void bossIntroEvent2(){
		eventCounter++;
		if(eventCounter == 1){
			player.stop();
			blockInput = true;
		}else if(eventCounter == 2){
			player.setLeft(true);
		}else if(eventCounter == 3){
			player.setLeft(false);
		}/*else if(eventCounter == 30){
			transitionBoss.setPratar();
		}*/else if(eventCounter == 60){
			curDialog = bossIntroCon2;
		}else if(eventCounter == 62){
			if(!curDialog.getDone()){
				eventCounter--;
			}
		}else if(eventCounter == 200){ //120
			//transitionBoss.setRiver();
			for(double i = 19*30; i <= 22*30; i+=30){
				effects.add(new Effect(tileMap, (int)i, 19*30, Content.Smoke1, 9));
			}
		}else if(eventCounter == 220){
			transitionBoss = null;
			fackholt.setPosition(20*30, 17*30);
			playMusic = true;
			JukeBox.loop("bg");
			fackholt.playIntro();
		}else if(eventCounter == 221){
			if(!fackholt.isDonePlayIntro()){
			  	eventCounter--;
			}
		}else if(eventCounter == 260){
			blockInput = false;
			eventCounter = 0;
			bossIntroEvent2 = false;
			fackholt.begin();
		}
	}
	
	private void defeatedEvent(){
		eventCounter++;
		if(eventCounter == 1){
			blockInput = true;
			enemyProjectiles.clear();
			player.stop();
			JukeBox.stop("bg");
			playMusic = false;
		}else if(eventCounter == 60){
			curDialog = defeatedDialog;
		}else if(eventCounter == 61){
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 120){
			fackholt.kill();
			JukeBox.play("victory");
		}else if(eventCounter == 540){
			player.propellUp(-12);
		}else if(eventCounter == 600){
			blackBarsReset(60);
		}
		if(eventCounter >= 600 && eventCounter < 670){
			blackBarsClose();
		}
		if(eventCounter == 740){
			blackBarsReset(60);
			player.setPosition(17*30+400, 9*30);
			player.setRight(true);
			player.propellUp(-7);
			tileMap.setTile(33, 5, new Point(0, 0));
			tileMap.setTile(34, 5, new Point(0, 0));
			tileMap.setTile(33, 4, new Point(0, 0));
			tileMap.setTile(34, 4, new Point(0, 0));
			currentSection = SECTION_MAIN;
		}
		if(eventCounter >= 740 && eventCounter < 810){
			blackBarsOpen();
		}
		//prinsessa
		moaTrigger.update(player);
		if(moaTrigger.hasTriggered()){
			defeatedEvent = false;
			player.stop();
			moaEvent = true;
			eventCounter = 0;
		}
		
	}
	
	private void moaEvent(){
		eventCounter++;
		if(eventCounter == 120){
			cage.hide();
		}else if(eventCounter >= 140 && eventCounter < 140+92){ //ska färdas 92 px ner
			prinsessa.setPosition(prinsessa.getx(), prinsessa.gety()+1);
		}else if(eventCounter == 140+92+55){
			curDialog = moaDialog;
		}else if(eventCounter == 140+92+55+1){ //288
			if(!curDialog.getDone())
				eventCounter--;
		}else if(eventCounter == 288+60){ //346
			heart.fadeIn();
		}else if(eventCounter == 346+180){
			blackBarsReset(60);
		}else if(eventCounter > 546 && eventCounter <= 546+100){
			blackBarsClose();
		}else if(eventCounter == 646+1){
			Saves.addOneToAlsenholt2();
			setPhaseTo1(); //sparar också
			setDeload(true);
			gsm.setState(GameStateManager.CREDITS_SCREEN);
		}
	}
	
	
}

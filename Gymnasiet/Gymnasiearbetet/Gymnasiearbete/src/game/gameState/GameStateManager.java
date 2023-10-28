package game.gameState;

import game.gameState.levels.LevelHub;
import game.gameState.levels.level1.Level1_1State;
import game.gameState.levels.level1.Level1_2State;
import game.gameState.levels.level1.Level1_BossState;
import game.gameState.levels.level2.Level2_1State;
import game.gameState.levels.level2.Level2_2State;
import game.gameState.levels.level2.Level2_BossState;
import game.gameState.levels.level3.Level3_BossState;
import game.gameState.levels.level4.Level4_BossState;
import game.gameState.menus.CreditsState;
import game.gameState.menus.IntroScreen;
import game.gameState.menus.KeysMenu;
import game.gameState.menus.MainMenu;
import game.gameState.menus.SaveSelect;

public class GameStateManager {
	
	private GameState gameState;
	private GameState pausedState;
	private int currentIndex;
	//private int pausedIndex;
	
	//menus
	public static final int MAIN_MENU = 6;
	public static final int SAVE_SELECT = 7;
	public static final int INTRO_SCREEN = 8;
	public static final int PAUSED_SCREEN = 9;
	public static final int CREDITS_SCREEN = 13;
	public static final int OPTIONS_MENU = 14;
	
	//levels
	public static final int LEVEL_HUB = 0;
	
	public static final int LEVEL1_1STATE = 1;
	public static final int LEVEL1_2STATE = 2;
	public static final int LEVEL1_BOSS = 3;
	
	public static final int LEVEL2_1STATE = 4;
	public static final int LEVEL2_2STATE = 11;
	public static final int LEVEL2_BOSS = 10;
	
	public static final int LEVEL3_BOSS = 12;
	
	public static final int LEVEL4_BOSS = 5;
	
	public GameStateManager() {
		
		setState(INTRO_SCREEN);
		
	}
	
	private void loadState(int state) {
		if(state == MAIN_MENU)
			gameState = new MainMenu(this);
		else if(state == LEVEL1_1STATE)
			gameState = new Level1_1State(this);
		else if(state == LEVEL_HUB)
			gameState = new LevelHub(this);
		else if(state == LEVEL1_2STATE)
			gameState = new Level1_2State(this);
		else if(state == LEVEL1_BOSS)
			gameState = new Level1_BossState(this);
		else if(state == SAVE_SELECT)
			gameState = new SaveSelect(this);
		else if(state == INTRO_SCREEN)
			gameState = new IntroScreen(this);
		else if(state == PAUSED_SCREEN)
			gameState = new PausedState(this);
		else if(state == LEVEL2_1STATE)
			gameState = new Level2_1State(this);
		else if(state == LEVEL4_BOSS)
			gameState = new Level4_BossState(this);
		else if(state == LEVEL2_BOSS)
			gameState = new Level2_BossState(this);
		else if(state == LEVEL2_2STATE)
			gameState = new Level2_2State(this);
		else if(state == LEVEL3_BOSS)
			gameState = new Level3_BossState(this);
		else if(state == CREDITS_SCREEN)
			gameState = new CreditsState(this);
		else if(state == OPTIONS_MENU)
			gameState = new KeysMenu(this);
	}
	
	private void pauseState(){
		pausedState = gameState;
		//pausedIndex = currentIndex;
	}
	
	private void unpauseState(){
		gameState.deactivate();
		gameState = pausedState;
		pausedState = null;
		gameState.activate();
		//currentIndex = pausedIndex;
		//pausedIndex = -1;
	}
	
	public void removePaused(){
		pausedState.deload();
		pausedState = null;
	}
	
	public void pauseAndSet(boolean b, int i){
		if(b){
			pauseState();
			setState(i);
		}else{
			unpauseState();
		}
	}
	
	public void setState(int state) {
		if(gameState != null){ 
			gameState.deactivate();
			if(gameState.deloadPlease())
				gameState.deload();
		}
		currentIndex = state;
		loadState(currentIndex);
		gameState.activate();
	}
	
	public void update() {
		gameState.update();
	}
	
	public void draw(java.awt.Graphics2D g) {
		gameState.draw(g);
	}
	/*
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	*/
}










package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.gameState.GameState;
import game.gameState.GameStateManager;
import game.handlers.Keys;
import game.main.GamePanel;
import game.tileMap.Background;

public abstract class MenuState extends GameState{
	
	protected String[] choices = {};
	protected int currentChoice = 0;
	
	protected String keyPrompts = "";
	protected Font promptFont;
	
	protected static Background bg;
	
	public MenuState(GameStateManager gsm){
		super(gsm);
		if(bg == null){
			bg = new Background("/Resources/Backgrounds/himmel.png", 1);
			bg.setVector(-0.1, 0);
		}
		promptFont = new Font("Arial", Font.PLAIN, 10);
	}

	protected void handleInput(){
		if(choices.length > 0){
			if(Keys.isPressed(Keys.UP)) {
				currentChoice--;
				if(currentChoice == -1) {
					currentChoice = choices.length - 1;
				}
			}
			if(Keys.isPressed(Keys.DOWN)) {
				currentChoice++;
				if(currentChoice == choices.length) {
					currentChoice = 0;
				}
			}
		}
		
	}
	
	protected void generateKeyPrompts(int[] keys, String[] func){ //id på keys
		keyPrompts = "";
		for(int i = 0; i < keys.length; i++){
			keyPrompts += "["+Keys.getBindName(keys[i])+"]: "+func[i]+"     ";
		}
	}
	
	protected void drawCentered(Graphics2D g, String s, Font f, FontMetrics fm, double y){
		g.setFont(f);
		double w = fm.stringWidth(s);
		g.drawString(s, (float)(GamePanel.WIDTH/2.0 - w/2.0), (float)y);
	}
	
	@Override
	public void update() {
		handleInput();
		bg.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(Color.BLACK);
		drawCentered(g, keyPrompts, promptFont, g.getFontMetrics(promptFont), 280);
	}
	
	@Override
	public void activate() {
		
	}

	@Override
	public void deactivate() {
		
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void deload() {
		
	}
}

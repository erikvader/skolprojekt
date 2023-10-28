package game.gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.handlers.Keys;
import game.main.GamePanel;

public class PausedState extends GameState{
	
	private Font titleFont;
	private Font font;
	
	private int currentChoice = 0;
	private String[] options = {
		"Resume",
		"Quit to main menu"
	};
	
	public PausedState(GameStateManager gsm){
		super(gsm);
		try {
			
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					32);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw title
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("Paused", 150, 100);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			g.drawString(options[i], 145, 140 + i * 15);
		}
		
		g.drawString("*", 140, 140+currentChoice*15);
	}

	public void select() {
		if(currentChoice == 0) {
			gsm.pauseAndSet(false, -1);
		}else if(currentChoice == 1) {
			gsm.removePaused();
			gsm.setState(GameStateManager.MAIN_MENU);
		}
		
	}

	@Override
	public void update() {
		handleInput();
	}
	
	public void handleInput(){
		if(Keys.isPressed(Keys.ENTER)){
			select();
		}
		if(Keys.isPressed(Keys.UP)) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
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

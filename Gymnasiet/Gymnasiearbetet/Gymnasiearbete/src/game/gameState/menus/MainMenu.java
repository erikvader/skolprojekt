package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;

import game.gameState.GameStateManager;
import game.handlers.Keys;

public class MainMenu extends MenuState{
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	private String[] options = {
		"Start",
		"Select another profile",
		"Options",
		"Credits",
		"Quit"
	};
	
	public MainMenu(GameStateManager gsm){
		super(gsm);
		
		choices = Arrays.copyOf(options, options.length);
		
		try {
			
			//bg = new Background("/Resources/Backgrounds/himmel.png", 1);
			//bg.setVector(-0.1, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(	
					"Century Gothic",
					Font.PLAIN,
					28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		// draw bg
		//bg.draw(g);
		super.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Main Menu", 80, 70);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}

	public void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL_HUB); //LEVEL_HUB
		}else if(currentChoice == 1) {
			gsm.setState(GameStateManager.SAVE_SELECT);
		}else if(currentChoice == 2) {
			gsm.setState(GameStateManager.OPTIONS_MENU);
		}else if(currentChoice == 3) {
			gsm.setState(GameStateManager.CREDITS_SCREEN);
		}else if(currentChoice == 4) {
			System.exit(0);
		}
	}
	
	@Override
	public void handleInput(){
		super.handleInput();
		
		if(Keys.isPressed(Keys.ENTER)){
			select();
		}
	}

}

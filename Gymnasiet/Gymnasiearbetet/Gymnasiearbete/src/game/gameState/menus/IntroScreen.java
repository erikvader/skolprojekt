package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.gameState.GameStateManager;
import game.handlers.Keys;

public class IntroScreen extends MenuState{

	private Color titleColor;
	private Font titleFont;
	
	//private Font font;
	private Font continueFont;
	private Font versionFont;
	
	public IntroScreen(GameStateManager gsm) {
		super(gsm);
		
		try {
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			
			//font = new Font("Arial", Font.PLAIN, 12);
			
			continueFont = new Font("Arial", Font.BOLD, 16);
			versionFont = new Font("Arial", Font.PLAIN, 10);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		g.setColor(titleColor);
		//g.setFont(titleFont);
		//g.drawString("The Legend of Grillska", 100, 100);
		drawCentered(g, "The Legend of Grillska", titleFont, g.getFontMetrics(titleFont), 100);
		//g.setColor(Color.BLUE);
		//g.setFont(font);
		//g.drawString("Omar's Awakening", 175, 125);
		
		g.setColor(Color.YELLOW);
		//g.setFont(continueFont);
		//g.drawString("Press almost any button to continue", 115, 200);
		drawCentered(g, "Press almost any button to continue", continueFont, g.getFontMetrics(continueFont), 200);
		
		g.setColor(Color.BLACK);
		g.setFont(versionFont);
		g.drawString("V1.0.1.23d", 10, 290);
	}

	@Override
	protected void handleInput() {
		super.handleInput();
		
		if(Keys.anyKeyPressed()){
			gsm.setState(GameStateManager.SAVE_SELECT);
		}
	}

}

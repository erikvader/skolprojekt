package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.gameState.GameStateManager;
import game.handlers.Keys;
import game.main.GamePanel;

public class CreditsState extends MenuState{

	private String[] text = {")The Legend of Grillska", ")", 
			"Gjordes som gymnasiearbete 2016 av Erik Rimskog TE13A", 
			"Teknikprogrammet Informations- och medieteknik",
			"Grillska Gymnasiet Uppsala",
			")",
			"Bowserholt - Alexander Alsenholt",
			"MK-82 - Mattias Karlsson",
			"Johannes - Johannes Hultin Knutas",
			"Spelgubbe - Erik Rimskog",
			"Prinsessan - Moa Marklund",
			"Viktor - Viktor Strindell",
			"Nils - Nils Kihl",
			"Rasmus - Rasmus Nyström",
			")Main Programmer", "Erik Rimskog",
			")Secondary Programmer", "Erik Rimskog",
			")Ärke Namngivare", "Hampus Nilsson Viberg",
			")Original Story", "Erik Rimskog",
			")Ärke HUD Designer", "Viktor Strindell",
			")Game Design", "Erik Rimskog", "Viktor Strindell", "Simon Ernofsson Spengs",
			")Ärke Rådgivare", "Simon Ernofsson Spengs",
			")Speltestare", "Erik Rimskog", "Simon Ernofsson Spengs", "Viktor Strindell", "Nils Kihl", "Hampus Nilsson Viberg",
			")Music", "Erik Rimskog", "Jesper Pettersson",
			")Art Designer", "Erik Rimskog", "Viktor Strindell", "Josephine Eriksson",
			")Special Thanks", "Erik Rimskog", "Viktor Strindell", "Massor av random personer från internet"};
	
	private Font stor, liten;
	private double curY = 320;
	
	public CreditsState(GameStateManager gsm) {
		super(gsm);
		
		stor = new Font("Arial", Font.BOLD, 18);
		liten = new Font("Arial", Font.PLAIN, 12);
	}

	@Override
	public void update() {
		//super.update();
		handleInput();
		
		curY -= 0.45;
	}
	
	@Override
	protected void handleInput() {
		//super.handleInput();
		if(Keys.isPressed(Keys.ESCAPE)){
			gsm.setState(GameStateManager.INTRO_SCREEN);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		//super.draw(g);
		
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//text
		FontMetrics fms = g.getFontMetrics(stor);
		FontMetrics fml = g.getFontMetrics(liten);
		g.setColor(Color.WHITE);
		double dy = curY;
		
		for(int i = 0; i < text.length; i++){
			if(i == text.length-1 && dy < -50) gsm.setState(GameStateManager.INTRO_SCREEN);
			if(dy > 320) break;
			if(text[i].startsWith(")")){
				dy += 40;
				drawCentered(g, text[i].substring(1), stor, fms, dy);
			}else{
				dy += 15;
				drawCentered(g, text[i], liten, fml, dy);
			}
		}
		
	}
	
	
}

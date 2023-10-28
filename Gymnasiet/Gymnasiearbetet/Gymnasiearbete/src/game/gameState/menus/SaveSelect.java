package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.gameState.GameStateManager;
import game.handlers.Keys;
import game.handlers.Saves;


public class SaveSelect extends MenuState{

	private Font font;
	
	private boolean[] exists = new boolean[3];
	
	public SaveSelect(GameStateManager gsm) {
		super(gsm);
		
		choices = new String[3];
		choices[0] = "save 1";
		choices[1] = "save 2";
		choices[2] = "save 3";
		
		try {
			//bg = new Background("/Resources/Backgrounds/menubg.gif", 1);
			//bg.setVector(-0.1, 0);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
			//kolla exists
			for(int i = 0; i < exists.length; i++){
				exists[i] = Saves.exists(i);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		//choices
		g.setFont(font);
		for(int i = 0; i < choices.length; i++){
			if(currentChoice == i){
				g.setColor(Color.PINK);
			}else{
				g.setColor(Color.BLACK);
			}
			g.drawString(choices[i], 50, 100+i*25);
			if(!exists[i]){
				g.drawString("[empty]", 90, 100+i*25);
			}
		}
	}
	
	@Override
	protected void handleInput() {
		super.handleInput();
		
		if(Keys.isPressed(Keys.ENTER)){
			select();
		}
		if(Keys.isPressed(Keys.DELETE)){
			if(exists[currentChoice]){
				Saves.removeSave(currentChoice);
				exists[currentChoice] = false;
			}
		}
	}

	private void select(){
		try{
			Saves.load(currentChoice);
			gsm.setState(GameStateManager.MAIN_MENU);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void activate() {
		super.activate();
		generateKeyPrompts(new int[]{Keys.DELETE}, new String[]{"delete save"});
	}

}

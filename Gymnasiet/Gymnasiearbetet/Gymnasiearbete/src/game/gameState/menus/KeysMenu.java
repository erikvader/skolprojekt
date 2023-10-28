package game.gameState.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.gameState.GameStateManager;
import game.handlers.Keys;

public class KeysMenu extends MenuState{

	private String[] names = {"up/gå in genom dörr", "left", "down", "right", "jump/swim", "attack/pick up item", "enter/läs skylt/nästa", "escape/paus", "delete/skippa dialog", "reset"};
	//private int[] index = {0, 1, 2, 3, 4, 6, 8, 9, 10};
	private Font font;
	private boolean changeNext = false;
	private int[] changes;
	
	public KeysMenu(GameStateManager gsm) {
		super(gsm);
		
		choices = new String[names.length];
		font = new Font("Arial", Font.PLAIN, 12);
		
		changes = Keys.getBinds();
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		//alla options
		g.setFont(font);
		for(int i = 0; i < names.length; i++){
			if(currentChoice == i){
				if(changeNext){
					g.setColor(Color.ORANGE);
				}else{
					g.setColor(Color.BLACK);
				}
			}else{
				g.setColor(Color.RED);
			}
			
			g.drawString(names[i]+":", 50, 50+18*i);
			g.drawString(KeyEvent.getKeyText(changes[i]), 170, 50+18*i); //changes[index[i]]
		}
		
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	protected void handleInput() {
		if(changeNext){
			if(Keys.getOtherButton() != -1){
				changeNext = false;
				changes[currentChoice] = Keys.getOtherButton(); //index[currentChoice]
			}
		}else{
			super.handleInput();
			if(Keys.isPressed(Keys.ENTER)){
				changeNext = true;
			}
			if(Keys.isPressed(Keys.ESCAPE)){
				for(int i = 0; i < changes.length; i++){
					Keys.changeBind(i, changes[i]);
				}
				Keys.writeFile();
				gsm.setState(GameStateManager.MAIN_MENU);
			}
			if(Keys.isPressed(Keys.DELETE)){
				gsm.setState(GameStateManager.MAIN_MENU);
			}
		}
	}
	
	@Override
	public void activate() {
		super.activate();
		generateKeyPrompts(new int[]{Keys.ENTER, Keys.ESCAPE, Keys.DELETE}, new String[]{"Börja att ändra", "Spara", "Avbryt utan att spara"});
	}
}

package game.entity.level1_2;

import java.awt.Graphics2D;

import game.entity.Animation;
import game.entity.Spike;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;


public class Boulder extends Spike{

	public Boulder(TileMap tm, int orientation) {
		super(tm, orientation, 3);
		
		width = height = 30;
		cheight = cwidth = 21;
		facingRight = true;
		
		animation = new Animation();
		
		animation.setFrames(Content.boulder);
		animation.setDelay(5);
		
		setSpeed(2);
	}
	
	@Override
	public void update(Section s){
		super.update(s);
		
		if(!(dx == 0 && dy == 0))
			animation.update();
	}

	@Override
	public void draw(Graphics2D g){
		//setMapPosition();
		//if(notOnScreen()) return;
		super.draw(g);
	}
}

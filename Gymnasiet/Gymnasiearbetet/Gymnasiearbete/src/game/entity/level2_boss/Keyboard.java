package game.entity.level2_boss;

import game.entity.Animation;
import game.entity.MapObject;
import game.handlers.Content;
import game.tileMap.TileMap;

public class Keyboard extends MapObject{

	public Keyboard(TileMap tm) {
		super(tm);
		
		width = cwidth = 180;
		height = cheight = 100;
		
		animation = new Animation(Content.loadRow("/Resources/Sprites/Enemies/mattias/keyboard.png", 180, 100, 0, 6), 30);
		setObjectAnimation(animation);
		
		facingRight = true;
		
	}
	
	public void update(){
		animation.update();
	}

}

package game.entity.level2_boss;

import java.awt.image.BufferedImage;

import game.entity.Animation;
import game.handlers.Content;
import game.tileMap.TileMap;

public class EnkelnotProjectile extends EProjectile{

	protected EnkelnotProjectile(TileMap tm) {
		super(tm);
		
		int rand = (int)(Math.random()*7);
		
		sprites = new Animation(new BufferedImage[]{Content.enkelnot[rand]}, -1);
		setObjectAnimation(sprites);
		
		cwidth = 10;//12
	}

}

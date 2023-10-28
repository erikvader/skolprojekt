package game.entity.level2_boss;

import java.awt.image.BufferedImage;

import game.entity.Animation;
import game.handlers.Content;
import game.tileMap.TileMap;

public class DubbelnotProjectile extends EProjectile {

	protected DubbelnotProjectile(TileMap tm) {
		super(tm);
		
		int rand = (int)(Math.random()*7);
		
		sprites = new Animation(new BufferedImage[]{Content.dubbelnot[rand]}, -1);
		setObjectAnimation(sprites);
		
	}

}

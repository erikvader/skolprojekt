package game.entity.items.mattias;

import java.awt.Rectangle;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.MapObject;
import game.entity.items.ThrowableItem;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class DelatMedNollItem extends ThrowableItem{

	private ArrayList<Effect> effects;
	private double lastSpeed;
	
	public DelatMedNollItem(TileMap tm, ArrayList<Effect> effects) {
		super(tm);
		this.effects = effects;
		
		animation = new Animation(Content.dividerat_noll_item, -1);
		setObjectAnimation(animation);
		
		facingRight = true;
		
		cwidth = 23;
		cheight = 28;
		
		fallSpeed = 0.15;
		maxFallSpeed = 1;
		moveSpeed = 2.5;
		moveSpeedUp = -8;
		xFriction = 0;
		
		falling = true;
		
	}
	
	@Override
	public void update(Section s) {
		if(s.fellOutside(this)) {
			remove = true;
			return;
		}
		/*
		if(!isInsideSection(s)){
			remove = true;
			return;
		}
		*/
		
		// update position
		if(!isBeingHeld()){
			getNextPosition();
			lastSpeed = dx;
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		}
		
		// update animation
		animation.update();
		
		if(!falling){
			if(dx < 0)
				dx = -moveSpeed;
			else if(dx > 0)
				dx = moveSpeed;
		}
		
		//krocka på väggar
		if(dx == 0){ //krockade på sidan
			dx = -lastSpeed;
		}
		
	}
	
	@Override
	public void remove() {
		effects.add(new Effect(tileMap, (int)x, (int)y, Content.Explosion2, 8));
		super.remove();
	}
	
	public boolean damages(MapObject mo){
		Rectangle rect = mo.getRectangle();
		rect.height -= 25;
		if(wasThrown && intersects(rect)) return true;
		else return false;
	}
	
	@Override
	public void shootAngle(double angle) {
		super.shootAngle(angle);
		dy = 0;
	}

}

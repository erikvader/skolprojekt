package game.entity.items;

import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Effect;
import game.entity.enemies.Enemy;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class CalculatorItem extends ThrowableItem{

	private ArrayList<Enemy> enemies;
	private ArrayList<Effect> effects;
	private int damage;
	
	public CalculatorItem(TileMap tm, ArrayList<Enemy> enemies, ArrayList<Effect> effects) {
		super(tm);
		animation = new Animation(Content.calculator_Item, -1);
		setObjectAnimation(animation);
		
		facingRight = true;
		
		//width = height = 30;
		cwidth = 18;
		cheight = 21;
		
		fallSpeed = 0.15;
		moveSpeed = 4;
		moveSpeedUp = -4;
		xFriction = 0.05;
		maxFallSpeed = 4;
		
		damage = 3;
		
		this.enemies = enemies;
		this.effects = effects;
	}

	@Override
	public void launch(int dir) {
		super.launch(dir);
		if(dir == DOWN){
			wasThrown = false;
		}
	}
	
	@Override
	public void update(Section s) {
		super.update(s);
		
		//kanske sätta som en egen klass? ThrowableDamageItem
		//kolla if skadar enemies
		if(wasThrown){
			for(int i = 0; i < enemies.size(); i++){
				Enemy e = enemies.get(i);
				if(e.intersects(this)){
					dy = dx = 0;
					collidedWithMap = true; //collidedWithEnemy
					e.hit(damage);
				}
			}
		}
		
		//kolla if krockat någonstans
		if(wasThrown && collidedWithMap){
			remove = true;
			effects.add(new Effect(tileMap, (int)x, (int)(y-15), Content.Explosion2, 10));
		}
		
	}
}

package game.functions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.entity.MapObject;

public class Trigger {
	
	private Rectangle hitbox;
	private boolean triggered = false;
	private int triggerMode = 1;
	
	public static final int TRIGGER_ONCE = 1;
	public static final int TRIGGER_MULTIPLE = 2;
	
	//trigger_ONCE
	private boolean triggeredOnce = false;
	
	//TRIGGER_MULTIPLE
	private boolean canTrigger = true;
	
	public Trigger(Rectangle h){
		hitbox = h;
	}
	
	public Trigger(Rectangle h, int t){
		hitbox = h;
		triggerMode = t;
	}
	
	public boolean hasTriggered(){
		return triggered;
	}
	
	public void disable(){
		triggerMode = -1;
	}
	
	public void update(MapObject mo){
		if(triggerMode == TRIGGER_ONCE){
			if(triggeredOnce == false){
				if(mo.intersects(hitbox)){
					triggered = true;
					triggeredOnce = true;
				}
			}else{
				if(triggered) triggered = false;
			}
		}else if(triggerMode == TRIGGER_MULTIPLE){
			if(canTrigger){
				if(mo.intersects(hitbox)){
					triggered = true;
					canTrigger = false;
				}
			}else{
				if(triggered) triggered = false;
				if(!mo.intersects(hitbox)){
					canTrigger = true;
				}
			}
		}
	}
	
	public void draw(Graphics2D g, double xmap, double ymap){
		g.setColor(Color.BLUE);
		g.drawRect(hitbox.x+(int)xmap, hitbox.y+(int)ymap, hitbox.width, hitbox.height);
	}
	
}

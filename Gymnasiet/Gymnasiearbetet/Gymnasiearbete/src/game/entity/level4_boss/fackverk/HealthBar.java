package game.entity.level4_boss.fackverk;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar {

	private int maxHealth, health;
	private double changeSpeed = 0.5, cur = 460; //pixlar
	
	public HealthBar(int maxHealth){
		this.health = this.maxHealth = maxHealth;
	}
	
	public void setHealth(int h){health = h;}
	
	public void draw(Graphics2D g){
		double target = 460*(double)health/maxHealth;
		if(cur > target){
			cur -= changeSpeed;
		}
		g.setColor(Color.RED);
		g.fillRect(0, 30*10-10, (int)(cur), 10);
		
	}
	
}

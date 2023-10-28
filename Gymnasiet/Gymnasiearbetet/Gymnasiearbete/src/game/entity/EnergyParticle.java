package game.entity;

import java.awt.Graphics2D;

import game.handlers.Content;
import game.tileMap.TileMap;

public class EnergyParticle extends Effect {
	
	private int count;
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	
	public EnergyParticle(TileMap tm, double x, double y, int dir) {
		super(tm, (int)x, (int)y, Content.EnergyParticle, -1);

		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8; 
		if(dir == UP) {
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFT) {
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWN) {
			dx = d1;
			dy = -d2;
		}
		else {
			dx = -d2;
			dy = d1;
		}
		
		count = 0;
		
	}
	
	@Override
	public void update() {
		x += dx;
		y += dy;
		count++;
		if(count == 60) remove = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}

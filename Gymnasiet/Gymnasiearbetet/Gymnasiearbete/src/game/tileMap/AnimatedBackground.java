package game.tileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.entity.Animation;
import game.main.GamePanel;

//Har en animerad bild och repeatar åt alla håll
//lite specialanpassat till johannes
public class AnimatedBackground extends Background{
	
	private Animation animation;
	private int width, height;
	
	public AnimatedBackground(BufferedImage[] sprites, int delay, double ms, double x, double y) {
		moveScale = ms;
		animation = new Animation();
		animation.setFrames(sprites, delay);
		width = animation.getWidth();
		height = animation.getHeight();
		
		animation.setSpecificDelay(11, 3);
		animation.setSpecificDelay(10, 2);
		animation.setSpecificDelay(9, 3);
		animation.setSpecificDelay(8, 2);
		
		this.x = x;
		this.y = y;
		
	}
	
	@Override
	public void update() {
		//super.update();
		x += dx;
		y += dy;
		setPos(x, y);
		animation.update();
	}
	
	@Override
	public void setPosition(double x, double y) {
		//så att tileMap inte flyttar på den
	}
	
	public void setPos(double x, double y){
		this.x = (x * moveScale) % width;
		this.y = (y * moveScale) % height;
	}

	
	@Override
	public void draw(Graphics2D g) {
		//find left and top bounds
		double xBound = x, yBound = y;
		while(xBound > 0){
			xBound -= width;
		}
		while(yBound > 0){
			yBound -= height;
		}
		
		//rita
		double drawY = yBound;
		while(xBound < GamePanel.WIDTH){
			drawY = yBound;
			while(drawY < GamePanel.HEIGHT){
				g.drawImage(animation.getImage(), (int)xBound, (int)drawY, null);
				drawY += height;
			}
			xBound += width;
		}
	}
}

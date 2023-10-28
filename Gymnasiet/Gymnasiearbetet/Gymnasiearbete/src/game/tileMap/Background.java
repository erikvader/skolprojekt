package game.tileMap;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import game.main.GamePanel;

public class Background {
	
	private BufferedImage image;
	
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	protected double moveScale;
	
	protected Background(){};
	
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % image.getWidth(); //GamePanel.WIDTH;
		this.y = (y * moveScale) % image.getHeight(); //GamePanel.HEIGHT;
		
		//bounds
		if(this.y > 0) this.y = 0;
		if(this.y < GamePanel.HEIGHT - image.getHeight()) this.y = GamePanel.HEIGHT - image.getHeight();
		
		//System.out.println("map: "+x+", "+y+" bg: "+this.x+", "+this.y);
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
		setPosition(x, y);
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);
		
		if(x <= 0) {
			g.drawImage(
				image,
				(int)x + /*GamePanel.WIDTH*/image.getWidth(),
				(int)y,
				null
			);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x - /*GamePanel.WIDTH*/image.getWidth(),
				(int)y,
				null
			);
		}
	}
	
}








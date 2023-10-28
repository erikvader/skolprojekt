package game.tileMap;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private int type;
	
	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int WATER = 2;
	public static final int LAVA = 3;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	public void setType(int t){type = t;}
	
	public BufferedImage getImage() { return image; }
	public int getType() { return type; }
	
	public void update(){};
	
}

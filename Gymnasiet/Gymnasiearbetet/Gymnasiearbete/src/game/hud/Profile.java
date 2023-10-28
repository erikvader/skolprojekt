package game.hud;

import java.awt.image.BufferedImage;

import game.handlers.Content;


public class Profile {

	private String name;
	private int side;
	private BufferedImage pic;
	
	public Profile(String name, int side, String picPath){
		this.name = name;
		this.side = side;
		this.pic = Content.loadRow(picPath, 30, 30, 0, 1)[0];
	}
	
	public int getSide(){return side;}
	public String getName(){return name;}
	public BufferedImage getPic(){return pic;}
	
	public boolean equals(Profile other){
		return other.name.equals(this.name);
	}
	
}

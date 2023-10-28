package game.hud;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import game.entity.Player;

public class HUD {
	
	private Player player;
	
	private BufferedImage[] image;
	//private Font font;
	
	private int x, y;
	
	public HUD(Player p) {
		player = p;
		try {
			BufferedImage temp = ImageIO.read(getClass().getResourceAsStream("/Resources/HUD/hud.png"));
			image = new BufferedImage[6];
			for(int i = 0; i < 6; i++){
				image[i] = temp.getSubimage(0, 77*i, 80, 77);
			}
			//font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		x = 5; 
		y = 5;
	}
	
	public void draw(Graphics2D g) {
		
		if(player.getHealth() > 5){
			g.drawImage(image[0], x, y, null);
		}else{
			g.drawImage(image[5-player.getHealth()], x, y, null);
		}
		
		/*
		g.drawString(
			player.getFire() / 100 + "/" + player.getMaxFire() / 100,
			30,
			45
		);
		*/
		
		//g.drawString((int)(player.getx()/30)+", "+(int)(player.gety()/30), 200, 30);
		
	}
	
}














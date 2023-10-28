package game.handlers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// this class loads resources on boot.
// spritesheets are taken from here.

public class Content {
	
	//[ROW][COL]
	
	public static BufferedImage[] EnergyParticle = loadRow("/Resources/Sprites/Player/EnergyParticle.gif", 5, 5, 0, 1);
	public static BufferedImage[] blackParticle = loadRow("/Resources/Sprites/Enemies/black_projectile.png", 5, 5, 0, 1);
	public static BufferedImage[] Explosion1 = load("/Resources/Sprites/Explosions/explosion1.gif", 30, 30, new int[]{6})[0];//4
	public static BufferedImage[] Explosion2 = load("/Resources/Sprites/Explosions/explosion2.gif", 30, 60, new int[]{18})[0]; //delay 8
	public static BufferedImage[] Explosion3 = loadGrid("/Resources/Sprites/Explosions/explosion3.png", 30, 30, 4, 4, 14, 0);
	public static BufferedImage[] Smoke1 = loadRow("/Resources/Sprites/Explosions/smoke1.png", 30, 60, 0, 9);
	public static BufferedImage[][] FireBall = load("/Resources/Sprites/Player/fireball.gif", 30, 30, new int[]{4, 3});
	public static BufferedImage[] boulder = loadRow("/Resources/Sprites/Other/boulder.gif", 30, 30, 0, 8);
	public static BufferedImage[] platform1 = loadRow("/Resources/Sprites/Other/platform.gif", 30, 30, 0, 3);
	
	public static BufferedImage[] calculator_Item = loadRow("/Resources/Sprites/Throwable_Items/calculator.png", 30, 30, 0, 1);
	public static BufferedImage[] dividerat_noll_item = loadRow("/Resources/Sprites/Throwable_Items/dividerat_med_0.png", 30, 30, 0, 1);
	
	//public static BufferedImage[][] Gazer = load("/Sprites/Enemies/Gazer.gif", 39, 20);
	//public static BufferedImage[][] Tengu = load("/Sprites/Enemies/Tengu.gif", 30, 30);
	//public static BufferedImage[][] GelPop = load("/Sprites/Enemies/GelPop.gif", 25, 25);
	//public static BufferedImage[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);
	
	public static BufferedImage[][] button1 = load("/Resources/Sprites/button/button1_small.png", 25, 27, new int[]{1, 1});
	
	public static BufferedImage[][] Slugger = load("/Resources/Sprites/Enemies/slugger.gif", 30, 30, new int[]{3});
	public static BufferedImage[] Spider = loadRow("/Resources/Sprites/Enemies/arachnik.gif", 30, 30, 0, 1);
	public static BufferedImage[] Calculator = loadRow("/Resources/Sprites/Enemies/calculator.png", 30, 30, 0, 4);
	
	public static BufferedImage[][] spike1; //size 3, 2, 1; index 0, 1, 2, 3
	public static BufferedImage[] spring1 = loadRow("/Resources/Sprites/Spring/spring1.gif", 30, 30, 0, 3);
	public static BufferedImage conversation1 = loadRow("/Resources/HUD/conversation.png", 293, 59, 0, 1)[0];
	public static BufferedImage[] sign1 = loadRow("/Resources/Sprites/Other/sign1.gif", 30, 30, 0, 1);
	public static BufferedImage[] sign2 = loadRow("/Resources/Sprites/Other/sign2.png", 30, 30, 0, 1);
	public static BufferedImage[][] door1 = load("/Resources/Sprites/Doors/door1.png", 90, 60, new int[]{1, 1, 1});
	public static BufferedImage[][] doorSpiral = load("/Resources/Sprites/Doors/door_spiral.png", 90, 60, new int[]{1, 1, 1});
	
	public static BufferedImage[] alsenholtFire = loadRow("/Resources/Sprites/Enemies/alsenholt/fire.png", 30, 40, 0, 3);
	public static BufferedImage[] alsenholtMini = loadRow("/Resources/Sprites/Enemies/alsenholt/mini_drake.png", 30, 30, 0, 4);
	
	public static BufferedImage[] sineWave = loadRow("/Resources/Sprites/Enemies/mattias/sin.png", 199, 100, 0, 1);
	public static BufferedImage[] pi = loadRow("/Resources/Sprites/Enemies/mattias/pi.png", 15, 15, 0, 1);
	public static BufferedImage[] tau = loadRow("/Resources/Sprites/Enemies/mattias/tau.png", 15, 15, 0, 1);
	public static BufferedImage[] e = loadRow("/Resources/Sprites/Enemies/mattias/e.png", 15, 15, 0, 1);
	public static BufferedImage[] gklav = loadRow("/Resources/Sprites/Enemies/mattias/gklav.png", 60, 60, 0, 1);
	public static BufferedImage[] dubbelnot = loadRow("/Resources/Sprites/Enemies/mattias/dubbelnot.png", 15, 15, 0, 7);
	public static BufferedImage[] enkelnot = loadRow("/Resources/Sprites/Enemies/mattias/enkelnot.png", 15, 15, 0, 7);
	public static BufferedImage[] cokeZero = loadRow("/Resources/Sprites/Enemies/johannes/coke2.png", 15, 15, 0, 6);
	
	public static BufferedImage[] blixt = loadRow("/Resources/Sprites/Enemies/alsenholt2/blixt.png", 50, 270, 0, 6);
	public static BufferedImage[] varning = loadRow("/Resources/Sprites/Enemies/alsenholt2/varning.png", 25, 25, 0, 1);
	public static BufferedImage[] arrowsv = loadRow("/Resources/Sprites/Enemies/alsenholt2/arrows.png", 15, 270, 0, 1);
	public static BufferedImage[] arrowsh = loadRow("/Resources/Sprites/Enemies/alsenholt2/arrowsh.png", 480, 15, 0, 1);
	public static BufferedImage[] laserv = loadRow("/Resources/Sprites/Enemies/alsenholt2/laser.png", 15, 270, 0, 1);
	public static BufferedImage[] laserh = loadRow("/Resources/Sprites/Enemies/alsenholt2/laserh.png", 480, 15, 0, 1);
	public static BufferedImage[] laserBoll = loadRow("/Resources/Sprites/Enemies/alsenholt2/laser_boll.png", 14, 14, 0, 1);
	public static BufferedImage[] laserBollYellow = loadRow("/Resources/Sprites/Enemies/alsenholt2/laser_boll_yellow.png", 14, 14, 0, 1);
	public static BufferedImage[] laserBollStor = loadRow("/Resources/Sprites/Enemies/alsenholt2/laser_boll_stor.png", 20, 20, 0, 1);
	
	public static BufferedImage[][] load(String s, int w, int h, int[] lengths) { //load everything
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			//int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][];
			for(int i = 0; i < height; i++) {
				if(i >= lengths.length) break;
				ret[i] = new BufferedImage[lengths[i]];
				for(int j = 0; j < lengths[i]; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage[] loadRow(BufferedImage spritesheet, int w, int h, int row, int length){
		BufferedImage[] ret;
		try {
			ret = new BufferedImage[length];
			for(int i = 0; i < length; i++) {
				ret[i] = spritesheet.getSubimage(i * w, row * h, w, h);
				
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage[] loadRow(String s, int w, int h, int row, int length){
		try{
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			return loadRow(spritesheet, w, h, row, length);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage[] loadGrid(String s, int w, int h, int rows, int cols, int length, int offset){
		BufferedImage[] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			//int width = spritesheet.getWidth() / w;
			ret = new BufferedImage[length];
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++){
					if(i*cols+j >= length) break;
					ret[i*cols+j] = spritesheet.getSubimage(j*w + offset*(j+1), i*h + offset*(i+1), w, h);
				}
			}
			return ret;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage resize(final BufferedImage img, int width, int height){
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = resized.getGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		
		return resized;
	}
	
	public static BufferedImage rotate(final BufferedImage img, double rad){
		BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)temp.getGraphics();
		AffineTransform at = new AffineTransform();
	
		at.translate(temp.getWidth()/2.0, temp.getHeight()/2.0);
		at.rotate(rad);
		at.translate(-temp.getWidth()/2.0, -temp.getHeight()/2.0);
		g2d.drawImage(img, at, null);
		g2d.dispose();
		
		return temp;
	}
	
	  /////////////////////////////////////////////////////////////////////////
	 ////////////////////load-saker///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public static void loadSpikes(){
		if(spike1 != null) return;
		BufferedImage sp = load("/Resources/Sprites/Spikes/spike1.gif", 30, 30, new int[]{1})[0][0];
		spike1 = new BufferedImage[3][4];
		
		BufferedImage temp;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 3; j++){
				temp = resize(sp, 30-10*j, 30-10*j);
				spike1[j][i] = rotate(temp, (Math.PI/2.0)*i);
			}
		}
	}
	
	
}

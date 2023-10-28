package main;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class SpriteHandler{
	
	private ImageIcon[] snakeIco;
	
	private Point fixPoint(Point p){
		if(p.x > 1){
			p.x = -1;
		}else if(p.x < -1){
			p.x = 1;
		}
		
		if(p.y > 1){
			p.y = -1;
		}else if(p.y < -1){
			p.y = 1;
		}
		return p;
	}

	private ImageIcon drawSingle(Point delta, int offset){
		fixPoint(delta);
		
		if(delta.x == 1 && delta.y == 0){//kroppen är till höger
			return snakeIco[offset+3];
		}else if(delta.x == 0 && delta.y == 1){
			return snakeIco[offset+0];
		}else if(delta.x == -1 && delta.y == 0){
			return snakeIco[offset+1];
		}else if(delta.x == 0 && delta.y == -1){
			return snakeIco[offset+2];
		}
		
		return null;
	}
	
	public ImageIcon drawHead(Point delta){
		return drawSingle(delta, 0);
	}
	
	public ImageIcon drawTail(Point delta){
		return drawSingle(delta, 8);
	}
	
	public ImageIcon drawBody(Point delta, Point delta2){//returnar rätt sprite.
		fixPoint(delta);
		fixPoint(delta2);
		
		if(delta.x == 1 && delta.y == 0){
			if(delta2.x == 0 && delta2.y == 1){
				return snakeIco[12+0];
			}else if(delta2.x == -1 && delta2.y == 0){
				return snakeIco[4+1];
			}else if(delta2.x == 0 && delta2.y == -1){
				return snakeIco[12+6];
			}
		}else if(delta.x == 0 && delta.y == 1){
			if(delta2.x == 1 && delta2.y == 0){
				return snakeIco[12+5];
			}else if(delta2.x == -1 && delta2.y == 0){
				return snakeIco[12+1];
			}else if(delta2.x == 0 && delta2.y == -1){
				return snakeIco[4+2];
			}
		}else if(delta.x == -1 && delta.y == 0){
			if(delta2.x == 1 && delta2.y == 0){
				return snakeIco[4+3];
			}else if(delta2.x == 0 && delta2.y == 1){
				return snakeIco[12+4];
			}else if(delta2.x == 0 && delta2.y == -1){
				return snakeIco[12+2];
			}
		}else if(delta.x == 0 && delta.y == -1){
			if(delta2.x == 1 && delta2.y == 0){
				return snakeIco[12+3];
			}else if(delta2.x == 0 && delta2.y == 1){
				return snakeIco[4+0];
			}else if(delta2.x == -1 && delta2.y == 0){
				return snakeIco[12+7];
			}
		}
		
		return new ImageIcon();
	}

	public void loadImages(String skin){
		try{
			snakeIco = new ImageIcon[20];
			
			BufferedImage[] temp = {
					ImageIO.read(new File("snake/skins/"+skin+"/textures/snake_head.png")),
					ImageIO.read(new File("snake/skins/"+skin+"/textures/snake_body.png")),
					ImageIO.read(new File("snake/skins/"+skin+"/textures/snake_tail.png")),
					ImageIO.read(new File("snake/skins/"+skin+"/textures/snake_turn.png"))
			};
			
			int tempIndex = 0;
			for(int i = 0; i < temp.length; i++){
				for(double j = 0; j < Math.PI*2; j += Math.PI/2){
					snakeIco[tempIndex++] = new ImageIcon(rotate(temp[i], j));
				}
			}
			for(int i = 0; i < 4; i++){
				snakeIco[tempIndex++] = new ImageIcon(flip((BufferedImage)snakeIco[12+i].getImage()));
			}
			
			
			//grassIco = snakeIco[12+5];
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private BufferedImage flip(final BufferedImage img){
		BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)temp.getGraphics();
		AffineTransform at = new AffineTransform();
	
		at.translate(img.getWidth(), 0);
		at.scale(-1, 1);
		g2d.drawImage(img, at, null);
		g2d.dispose();
		
		return temp;
	}
	
	private BufferedImage rotate(final BufferedImage img, double rad){
		BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)temp.getGraphics();
		AffineTransform at = new AffineTransform();
	
		at.translate(temp.getWidth()/2, temp.getHeight()/2);
		at.rotate(rad);
		at.translate(-temp.getWidth()/2, -temp.getHeight()/2);
		g2d.drawImage(img, at, null);
		g2d.dispose();
		
		return temp;
	}
	
}

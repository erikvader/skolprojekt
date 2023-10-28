package game.tileMap;

import java.awt.image.BufferedImage;

import game.entity.Animation;


public class AnimatedTile extends Tile{

	private Animation animation;
	private BufferedImage[] sprites;
	
	public AnimatedTile(BufferedImage image, int type, int delay) {
		super(null, type);
		
		fixSprites(image);
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(delay);
	}
	
	private void fixSprites(BufferedImage image){
		int tilesAcross = image.getWidth()/30;
		sprites = new BufferedImage[tilesAcross];
		for(int i = 0; i < tilesAcross; i++){
			sprites[i] = image.getSubimage(i*30, 0, 30, 30);
		}
	}

	@Override
	public void update(){
		animation.update();
	}
	
	@Override
	public BufferedImage getImage() {
		return animation.getImage();
	}
	
}

package game.sprites;

import game.chat.TextBox;
import game.map.Sign;
import game.map.TileObject;
import game.map.tileMap;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import main.TextureLocations;

public class character extends sprite {

	BufferedImage[] spriteImages;
	tileMap map;
	boolean busy = false;
	boolean walking = false;
	int totalFramesToWalk;
	int walkingFramesPassed = 0;
	int walkSpeedFrames;
	String walkingDir = "down";
	int framesPerAnimation;
	int curTileX;
	int curTileY;
	boolean attacking = false;
	int attackSpeed; // total frames man håller fram svärdet
	int attackCoolDown = 0;
	int acd = 0;
	
	Animation rWalk;
	Animation lWalk;
	Animation uWalk;
	Animation dWalk;
	
	TextBox signBox;

	public character(String name, BufferedImage spriteSheet, tileMap m) {
		super(name, m.startX, m.startY, false);
		splitSpriteSheet(spriteSheet);
		map = m;
		curTileX = map.startTilex;
		curTileY = map.startTiley;
	}

	public void splitSpriteSheet(BufferedImage s) {
		// BufferedImage sheet = ;
		spriteImages = new BufferedImage[20];
		int indexCount = 0;
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 4; x++) {
				spriteImages[indexCount++] = s.getSubimage(60 * x, 60 * y, 60, 60);
			}
		}

		super.setCurImage(new ImageIcon(spriteImages[0]));
	}

	public void setWalkSpeed(int speed) {
		walkSpeedFrames = speed;
		framesPerAnimation = walkSpeedFrames / 4;
	}

	public void setAttackSpeed(int s) {
		attackSpeed = s;
	}
	
	public void setAttackCoolDown(int a){
		acd = a;
	}
	
	public void loadAnimations(){
		rWalk = new Animation();
		
		rWalk.addAnim(framesPerAnimation, spriteImages[9]);
		rWalk.addAnim(framesPerAnimation, spriteImages[10]);
		rWalk.addAnim(framesPerAnimation, spriteImages[11]);
		rWalk.addAnim(framesPerAnimation, spriteImages[8]);
		rWalk.setDefaultPic(3);
		
		lWalk = new Animation();
		lWalk.addAnim(framesPerAnimation, spriteImages[5]);
		lWalk.addAnim(framesPerAnimation, spriteImages[6]);
		lWalk.addAnim(framesPerAnimation, spriteImages[7]);
		lWalk.addAnim(framesPerAnimation, spriteImages[4]);
		
		uWalk = new Animation();
		uWalk.addAnim(framesPerAnimation, spriteImages[13]);
		uWalk.addAnim(framesPerAnimation, spriteImages[14]);
		uWalk.addAnim(framesPerAnimation, spriteImages[15]);
		uWalk.addAnim(framesPerAnimation, spriteImages[12]);
		
		dWalk = new Animation();
		dWalk.addAnim(framesPerAnimation, spriteImages[1]);
		dWalk.addAnim(framesPerAnimation, spriteImages[2]);
		dWalk.addAnim(framesPerAnimation, spriteImages[3]);
		dWalk.addAnim(framesPerAnimation, spriteImages[0]);
	}

	public void walk(String dir/* , int frames */) {
		if (!busy) {
			resetPicture(dir);
			if (checkWalkable(dir)) {
				busy = true;
				walking = true;
				float moveAmount = (float) 60 / walkSpeedFrames;
				totalFramesToWalk = walkSpeedFrames;
				if (dir.equals("right")) {
					super.setVX(moveAmount);
					super.setVY(0f);
					walkingDir = dir;
				} else if (dir.equals("left")) {
					super.setVX(-moveAmount);
					super.setVY(0f);
					walkingDir = dir;
				} else if (dir.equals("up")) {
					super.setVX(0f);
					super.setVY(-moveAmount);
					walkingDir = dir;
				} else if (dir.equals("down")) {
					super.setVX(0f);
					super.setVY(moveAmount);
					walkingDir = dir;
				}
			}
		}
	}

	private void resetPicture(String dir) {// change image to the correct
													// direction if you walk
													// into something unwalkable
													// resets
		if (dir != null) {
			if (dir.equals("down")) {
				super.setCurImage(new ImageIcon(spriteImages[0]));
			} else if (dir.equals("left")) {
				super.setCurImage(new ImageIcon(spriteImages[4]));
			} else if (dir.equals("right")) {
				super.setCurImage(new ImageIcon(spriteImages[8]));
			} else if (dir.equals("up")) {
				super.setCurImage(new ImageIcon(spriteImages[12]));
			}
			walkingDir = dir;
		} else {
			if (walkingDir.equals("down")) {
				super.setCurImage(new ImageIcon(spriteImages[0]));
			} else if (walkingDir.equals("left")) {
				super.setCurImage(new ImageIcon(spriteImages[4]));
			} else if (walkingDir.equals("right")) {
				super.setCurImage(new ImageIcon(spriteImages[8]));
			} else if (walkingDir.equals("up")) {
				super.setCurImage(new ImageIcon(spriteImages[12]));
			}
		}
	}

	private void updateMovement() {
		if (walking) {
//			updateAnim();
			if (walkingFramesPassed < totalFramesToWalk) {
				super.move();
				walkingFramesPassed++;
				updateAnim();
			} else {
				// snapCharacterToClosestTile
				totalFramesToWalk = 0;
				walkingFramesPassed = 0;
				walking = false;
				busy = false;
//				resetPicture(null);
				if (walkingDir.equals("down")) {
					curTileY++;
				} else if (walkingDir.equals("up")) {
					curTileY--;
				} else if (walkingDir.equals("right")) {
					curTileX++;
				} else if (walkingDir.equals("left")) {
					curTileX--;
				}
			}
		}
	}
	
	private void updateAnim(){
		if(walkingDir.equals("right")){
			super.setCurImage(new ImageIcon(rWalk.playLoop()));
		}else if(walkingDir.equals("left")){
			super.setCurImage(new ImageIcon(lWalk.playLoop()));
		}else if(walkingDir.equals("up")){
			super.setCurImage(new ImageIcon(uWalk.playLoop()));
		}
	}

	/*private void updateAnim() {
		if (walkingDir.equals("down")) {
			checkAnimState(1);
		} else if (walkingDir.equals("left")) {
			checkAnimState(5);
		} else if (walkingDir.equals("right")) {
			checkAnimState(9);
		} else if (walkingDir.equals("up")) {
			checkAnimState(13);
		}
	}

	private void checkAnimState(int row) {
		if (walkingFramesPassed <= framesPerAnimation * 1) {
			super.setCurImage(new ImageIcon(spriteImages[0 + row]));
		} else if (walkingFramesPassed <= framesPerAnimation * 2) {
			super.setCurImage(new ImageIcon(spriteImages[1 + row]));
		} else if (walkingFramesPassed <= framesPerAnimation * 3) {
			super.setCurImage(new ImageIcon(spriteImages[2 + row]));
		} else if (walkingFramesPassed > framesPerAnimation * 3) {
			super.setCurImage(new ImageIcon(spriteImages[-1 + row]));
		}

	}*/

	private Boolean checkWalkable(String dir) {
		int newX = -2;
		int newY = -2;

		if (dir.equals("down")) {
			newX = curTileX;
			newY = curTileY + 1;
		} else if (dir.equals("left")) {
			newX = curTileX - 1;
			newY = curTileY;
		} else if (dir.equals("right")) {
			newX = curTileX + 1;
			newY = curTileY;
		} else if (dir.equals("up")) {
			newX = curTileX;
			newY = curTileY - 1;
		}

		if (newX >= 0 && newY >= 0 && newX <= map.endTilex && newY <= map.endTiley) {
			return map.getTile(newX, newY).getWalkable();
		}
		/*
		 * if(newX == -1 || newY == -1 || newX == map.endTilex+1 || newY ==
		 * map.endTiley+1){ return true; }
		 */
		return false;
	}

	public void update() {
		updateMovement();
		updateAttack();
	}

	public void attack() {
		if (!busy && attackCoolDown == 0) {
			attacking = true;
			busy = true;
			attackCoolDown = acd;
		}
	}

	private void updateAttack() {
		if (attacking) {
			if (walkingFramesPassed < attackSpeed) {
				walkingFramesPassed++;
				if (walkingDir.equals("down")) {
					super.setCurImage(new ImageIcon(spriteImages[16]));
				} else if (walkingDir.equals("left")) {
					super.setCurImage(new ImageIcon(spriteImages[17]));
				} else if (walkingDir.equals("right")) {
					super.setCurImage(new ImageIcon(spriteImages[18]));
				} else if (walkingDir.equals("up")) {
					super.setCurImage(new ImageIcon(spriteImages[19]));
				}
			} else {
				walkingFramesPassed = 0;
				attacking = false;
				busy = false;
				resetPicture(null);
			}
		}else{
			if(attackCoolDown > 0){
				attackCoolDown--;
			}
		}
	}
	
	public void readSign(){
		TileObject tempSign = null;
		tempSign = (Sign) map.getTile(curTileX, curTileY-1).getObject("sign");
		if(tempSign != null){
			signBox = new TextBox(new TextureLocations().chatBoxTexture, );//sätter in bilden istället för URL:en
		}
		
	}

}

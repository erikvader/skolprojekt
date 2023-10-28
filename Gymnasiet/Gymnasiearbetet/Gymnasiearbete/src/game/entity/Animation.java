package game.entity;

import java.awt.image.BufferedImage;

import game.handlers.Content;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private int count;
	//private int delay;
	private int[] delays;
	
	private int width, height;
	
	private int timesPlayed;
	
	private boolean empty = false;
	
	private int dir = 1;
	private boolean alternate = false;
	
	public Animation(BufferedImage[] frames, int delay) {
		//timesPlayed = 0;
		//delay = 2;
		setFrames(frames, delay);
	}
	
	public Animation(){
		
	}
	
	public Animation(Animation a){
		setFrames(a.getFrames(), a.getDelay());
		this.delays = a.delays.clone();
		if(a.empty){
			setEmpty();
		}
	}
	
	public void setAlternating(){alternate = true;}
	
	public void setEmpty(){
		timesPlayed = 1; 
		delays = new int[1];
		setDelay(-1);
		empty = true;
		width = height = 1;
		frames = new BufferedImage[1];
		frames[0] = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void reset(){
		if(empty) return;
		count = 0;
		timesPlayed = 0;
		currentFrame = 0;
	}
	
	public void setFrames(BufferedImage[] frames, int delay) {
		this.frames = frames;
		width = frames[0].getWidth();
		height = frames[0].getHeight();
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		numFrames = frames.length;
		delays = new int[numFrames];
		setDelay(delay);
	}
	
	public void setFrames(BufferedImage[] frames){
		setFrames(frames, -1);
	}
	
	public void setDelay(int delay){
		for(int i = 0; i < delays.length; i++){
			delays[i] = delay;
		}
	}
	
	public void setSpecificDelay(int index, int delay){
		delays[index] = delay;
	}
	
	public void setFrame(int i) { currentFrame = i; }
	public void setNumFrames(int i) { numFrames = i; }
	public void resetTimesPlayed(){timesPlayed = 0;}
	public BufferedImage[] getFrames(){return frames;}
	
	public int getDelay(){
		return getDelay(0);
	}
	
	public int getDelay(int i){
		return delays[i];
	}
	
	/*
	public void setFrames(AnimationSet frames){
		this.frames = frames.getSprites();
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		numFrames = this.frames.length;
		delays = frames.getDelays();
	}
	*/
	
	public void load(String s, int w, int h, int row, int length){
		frames = Content.loadRow(s, w, h, row, length);
		setFrames(frames);
	}
	
	public void load(BufferedImage bi, int w, int h, int row, int length){
		frames = Content.loadRow(bi, w, h, row, length);
		setFrames(frames);
	}
	
	public void update() {
		
		if(delays[currentFrame] == -1) return;
		
		count++;
		
		if(count == delays[currentFrame]) {
			currentFrame += dir;
			count = 0;
		}
		if(dir > 0){
			if(currentFrame == numFrames) {
				currentFrame = 0;
				timesPlayed++;
				if(alternate){
					currentFrame = numFrames-2;
					dir = -1;
				}
			}
		}else if(dir < 0){
			if(currentFrame == -1) {
				currentFrame = numFrames-1;
				timesPlayed++;
				if(alternate){
					currentFrame = 1;
					dir = 1;
				}
			}
		}
		
	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getFrame() { return currentFrame; }
	public int getCount() { return count; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return timesPlayed > 0; }
	public boolean hasPlayed(int i) { return timesPlayed == i; }
	
}
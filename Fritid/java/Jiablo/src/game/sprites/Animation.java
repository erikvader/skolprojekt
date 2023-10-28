package game.sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {

	List<Integer> keySequence;
	List<BufferedImage> animImages;
	
	int currentFrame = 0;
	int defaultPic = 0;
	
	public Animation(){
		keySequence = new ArrayList<>();
		animImages = new ArrayList<>();
	}
	
	public void setDefaultPic(int index){
		defaultPic = index;
	}
	
	public BufferedImage play(){
		BufferedImage tempImg = null;
		try{
			tempImg = animImages.get(keySequence.get(currentFrame++));
		}catch(Exception e){
			tempImg = animImages.get(defaultPic);
		}
		return tempImg;
	}
	
	public BufferedImage playLoop(){
		BufferedImage tempImg = null;
		try{
			tempImg = animImages.get(keySequence.get(currentFrame++));
		}catch(Exception e){
			reset();
			tempImg = animImages.get(keySequence.get(currentFrame++));
		}
		return tempImg;
	}
	
	public void reset(){
		currentFrame = 0;
	}
	
	public void addAnim(int frames, BufferedImage img){
		animImages.add(img);
		
		for(int i = 0; i < frames; i++){
			keySequence.add(animImages.size()-1);
		}
	}
	
}

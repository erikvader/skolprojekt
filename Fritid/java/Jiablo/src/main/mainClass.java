package main;

import game.sprites.character;
import game.sprites.spriteCollection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import main.maps.startArea.startArea;


public class mainClass implements KeyListener{
	
	spriteCollection col = new spriteCollection();
	startArea map = new startArea();
	
	BufferedImage mainCharacterImage;
	character mainChar;
	
	JFrame mainWindow = new JFrame();
	screen s = new screen(col, map);
	JScrollPane pane = new JScrollPane(s, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	boolean running = true;
	Dimension resolution = new Dimension(1920, 1080);
	
	
	public mainClass(){
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane.setBorder(null);
		pane.setPreferredSize(resolution);
		s.setPreferredSize(map.imgSize);
		mainWindow.add(pane, BorderLayout.CENTER);
//		mainWindow.setPreferredSize(resolution);
		mainWindow.setUndecorated(true);
		mainWindow.addKeyListener(this);
		mainWindow.setVisible(true);
		mainWindow.pack();
		
		initGame();
		mainLoop();
	}
	
	private void initGame(){
		try {
			mainCharacterImage = ImageIO.read(getClass().getResource("npc/mainCharacter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mainChar = new character("mainChar", mainCharacterImage, map);
		mainChar.setWalkSpeed(20);//20
		mainChar.setAttackSpeed(20);
		mainChar.setAttackCoolDown(20);
		mainChar.loadAnimations();
		mainChar.setHitboxFromImageDimension();
		col.add(mainChar);
	}
	
	private void mainLoop(){
		final int optimalFPS = 60;
		final long frameTime = 1000000000/optimalFPS;
		long oldTime = System.nanoTime();
		long totalTime = 0;
		int fps = 0;
		long plusTime;
		while(running){
			long curTime = System.nanoTime();
			plusTime = curTime - oldTime;
			totalTime += plusTime;
			oldTime = curTime;
//			double Delta = plusTime/((double)frameTime);
			
			fps++;
			
			if(totalTime >= 1000000000){
				System.out.println("FPS: "+fps);
				fps = 0;
				totalTime = 0;
			}
			
			
			gameUpdates();
			render();
			
			
			while(curTime - oldTime <= frameTime){
				
				try{
					Thread.sleep(1);
				}catch(Exception e){}
				curTime = System.nanoTime();
				
			}
			
			/*
			try{
				Thread.sleep((oldTime - System.nanoTime() + frameTime)/1000000);
			}catch(Exception e){}
			*/
			
		}
		mainWindow.dispose();
	}
	
	public void gameUpdates(){
		mainChar.update();
		
	}
	
	public void render(){
		updateCamera();
		s.repaint();
	}
	
	/*public void nextAction(){
		
	}*/

	public static void main(String[] args) {
		new mainClass();
	}

	public void keyPressed(KeyEvent event) {
		int pressedKey = event.getKeyCode();
	
		if(pressedKey == KeyEvent.VK_ESCAPE){
			running = false;
		}else if(pressedKey == KeyEvent.VK_RIGHT){
			mainChar.walk("right");
		}else if(pressedKey == KeyEvent.VK_LEFT){
			mainChar.walk("left");
		}else if(pressedKey == KeyEvent.VK_UP){
			mainChar.walk("up");
		}else if(pressedKey == KeyEvent.VK_DOWN){
			mainChar.walk("down");
		}else if(pressedKey == KeyEvent.VK_SPACE){
			mainChar.readSign();
			mainChar.attack();
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	public void updateCamera(){
		int charX = mainChar.getX();
		int charY = mainChar.getY();
		
		pane.getVerticalScrollBar().setValue( charY - resolution.height/2);
		pane.getHorizontalScrollBar().setValue(charX - resolution.width/2);
	}
	
}

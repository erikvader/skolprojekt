package game.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.audio.JukeBox;
import game.gameState.GameStateManager;
import game.handlers.Keys;
import game.handlers.Saves;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// dimensions
	public static final int WIDTH = 460; //320
	public static final int HEIGHT = 300; //240
	public static final double SCALE = 2; //2
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// image
	private static BufferedImage image;
	public static Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	private JFrame window;
	
	public GamePanel(JFrame window) {
		super();
		setPreferredSize(
			new Dimension((int)(WIDTH * SCALE), (int)(HEIGHT * SCALE)));
		setFocusable(true);
		requestFocus();
		
		this.window = window;
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		
		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
		JukeBox.init();
		Saves.init();
		Keys.init();
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		int fps = 0;
		long timer = 0;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 0;
			
			fps++;
			timer += elapsed/1000000+wait;
			if(timer >= 1000){
				timer = 0;
				//System.out.println("fps: "+fps);
				updateTitle(fps);
				fps = 0;
			}
			
			try {
				Thread.sleep(wait);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void updateTitle(int fps){
		String title = window.getTitle();
		//fps
		int i = title.indexOf("FPS: ");
		title = title.substring(0, i).concat("FPS: "+fps);
		
		//deaths
		int dea = Saves.getDeaths();
		if(dea != -1){
			title = title.concat(" - Deaths: "+dea);
		}
		window.setTitle(title);
	}
	
	private void update() {
		gsm.update();
		Keys.update();
	}
	private void draw() {
		gsm.draw(g);
	}
	private void drawToScreen() {
		Graphics2D g2 = (Graphics2D)getGraphics();
		//g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(image, 0, 0,
				(int)(WIDTH * SCALE), (int)(HEIGHT * SCALE),
				null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}


}

















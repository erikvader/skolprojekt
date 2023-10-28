package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import javazoom.jl.player.Player;
import main.bot.HundraprocentBot;
import main.bot.ShortestBot;
import main.bot.SnakeBot;
import main.menu.Menyu;

public class Game implements KeyListener{

	/*
	public static void main(String[] args) {
		new Main().run();
	}
	*/
	
	//TODO write in english
	//TODO private och public är inkonsistent
	
	JFrame frame;
	int size = 30; //30
	int width = 20; //20
	int height = 20; //20
	int windowWidth = width*size;
	int windowHeight = width*size;
	ArrayList<Ormdel> orm = new ArrayList<>();
	int ormDir = 1; //0: norr, 1: öster, 2: syd, 3: väst
	Point newTail = new Point(-1, -1);
	Point snakeStart = new Point(width/2, height/2);
	boolean canChange = true;
	boolean unibody = false;
	int multigrass = 1;
	boolean bigBackground = false;
	boolean solidWalls = false;
	boolean gameOver = false;
	boolean pause = false;
	String latestName = "";
	int score = 0;
	Apple apple;
	panel p;
	Timer time;
	Color scoreColor = Color.BLUE;
	Color backgroundColor = Color.BLACK;
	SnakeBot bot;
	
	//timer-grejer
	int timerStart = 200;//per minute
	int timerEnd = 80; 
	int timerStep = 5;
	int timerCur = timerStart;
	
	BufferedImage appleIco;
	BufferedImage wallIco;
	BufferedImage[] grassIco;
	String skin = "hampus";
	SpriteHandler handler;
	BufferedImage uniPicture;
	
	//musik
	Player background;
	String background_music;
	String death_music;
	String left_music;
	String right_music;
	String down_music;
	String up_music;
	String eat_music;
	
	//keybinds
	int upKey = KeyEvent.VK_UP;
	int downKey = KeyEvent.VK_DOWN;
	int rightKey = KeyEvent.VK_RIGHT;
	int leftKey = KeyEvent.VK_LEFT;
	int pauseKey = KeyEvent.VK_P;
	int restartKey = KeyEvent.VK_R;
	int exitKey = KeyEvent.VK_ESCAPE;
	
	public void run(){
		loadSettings();
		
		frame = new JFrame();
		p = new panel();
		p.setPreferredSize(new Dimension(windowWidth, windowHeight));
		p.setMinimumSize(new Dimension(windowWidth, windowHeight));
		frame.getContentPane().setLayout(new GridBagLayout());
		frame.getContentPane().setBackground(backgroundColor);
		frame.add(p);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		//frame.setPreferredSize(new Dimension(width*size, height*size));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(this);
		frame.addComponentListener(resizeListener);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		try{
			frame.setIconImage(ImageIO.read(getClass().getResourceAsStream("/main/snake-icon.png")));
		}catch (IOException e){
			e.printStackTrace();
		}
		
		apple = new Apple(-1, -1);
		time = new Timer(timerStart, al);
		
		loadImages();
		loadMusic();
		
		start();
				
		frame.setVisible(true);
		
		//create empty cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		frame.getContentPane().setCursor(blank);
		
	}
	
	public void start(){
		orm.clear();
		//init orm
		Ormdel del = new Ormdel(snakeStart.x, snakeStart.y);
		orm.add(del);
		del = new Ormdel(snakeStart.x, snakeStart.y+1);
		orm.add(del);
		del = new Ormdel(snakeStart.x, snakeStart.y+2);
		orm.add(del);
		
		//init apple
		putRandomApple();
		
		ormDir = 0;
		score = 0;
		gameOver = false;
		pause = false;
		
		bot = null;
		
		timerCur = timerStart;
		time.setDelay((int)(1000.0/(timerStart/60.0)));
		time.start();
		
		startBackgroundMusic(background_music);
		
	}
	
	public void stop(){//game over
		time.stop();
		gameOver = true;
		startBackgroundMusic(death_music);
		p.repaint();
		addNewHighscore();
	}
	
	class panel extends JPanel{
		private static final long serialVersionUID = 1L;
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			//super.setBackground(backgroundColor);
		
			//rita vägger, om man ska
			if(solidWalls){
				for(int x = 0; x < width+2; x+= 1){
					for(int y = 0; y < height+2; y++){
						if(y == 0 || x == 0 || y == height+1 || x == width+1){
							g.drawImage(wallIco, x*size, y*size, size, size, null);
						}
						
					}
				}
				g.translate(size, size);
			}
			
			//rita marken
			if(bigBackground){
				g.drawImage(grassIco[0], 0, 0, super.getWidth(), super.getHeight(), null);
			}else{
				int grassIndex = 0;
				for(int x = 0; x < width; x++){
					for(int y = 0; y < height; y++){
						g.drawImage(grassIco[grassIndex], x*size, y*size, size, size, null);
						if(grassIndex < multigrass-1)
							grassIndex++;
						else
							grassIndex = 0;
					}
			}
			}
			
			//rita apple
			g.drawImage(appleIco, apple.getX()*size, apple.getY()*size, size, size, null);
	
			if(unibody)
				drawUniSnake(g);
			else
				drawSnake(g);
			
			//rita score
			g.setColor(scoreColor);
			g.setFont(new Font("Monospaced", Font.PLAIN, 60));
			g.drawString(Integer.toString(score), 30, 60);
			
			//if paused
			if(solidWalls) g.translate(-size, -size);
			
			if(pause || gameOver){
				g.setColor(new Color(0F, 0F, 0F, 0.5F));
				g.fillRect(0, 0, p.getWidth(), p.getHeight());
			}
			
			if(solidWalls) g.translate(size, size);
			
			if(pause){
				g.setColor(Color.WHITE);
				g.setFont(new Font("Monospaced", Font.PLAIN, 80));
				FontMetrics fm = g.getFontMetrics();
				g.drawString("Pause", width*size/2 - fm.stringWidth("Pause")/2, height*size/2 - fm.getHeight()/2);
				//System.out.println(width + " " + height);
				//System.out.println("Width: " + (width*size/2 - fm.stringWidth("Pause")/2) + " height: "+(height*size/2 - fm.getHeight()/2));
			}
			
			if(gameOver){
				g.setColor(Color.WHITE);
				g.setFont(new Font("Monospaced", Font.PLAIN, 80));
				FontMetrics fm = g.getFontMetrics();
				g.drawString("Game Over", width*size/2 - fm.stringWidth("Game Over")/2, height*size/2 - fm.getHeight()/2);
				g.setFont(new Font("Monospaced", Font.PLAIN, 40));
				fm = g.getFontMetrics();
				g.drawString("press R to retry", width*size/2 - fm.stringWidth("press R to retry")/2, height*size/2 - fm.getHeight()/2 + 40);
				
			}
		}
	}
	
	public void drawSnake(Graphics g){
		/*
		//rita kropp
		g.setColor(Color.BLACK);
		for(int i = 1; i < orm.size(); i++){
			g.fillRect(orm.get(i).getX()*size, orm.get(i).getY()*size, size, size);
		}
		
		//rita huvud
		g.setColor(Color.GREEN);
		g.fillRect(orm.get(0).getX()*size, orm.get(0).getY()*size, size, size);
		*/
		//är inte tillräckligt smart för att hitta en snyggare lösning
		
		Point delta = new Point(0, 0);
		
		ImageIcon toDraw = null;
		//svans
		delta = getDelta(orm.get(orm.size()-1).getPoint(), orm.get(orm.size()-2).getPoint());
		toDraw = handler.drawTail(delta);
		g.drawImage(toDraw.getImage(), orm.get(orm.size()-1).getX()*size, orm.get(orm.size()-1).getY()*size, size, size, null);
		
		Point delta2 = new Point(0, 0);
		
		//kropp
		for(int i = 1; i < orm.size()-1; i++){
			delta = getDelta(orm.get(i).getPoint(), orm.get(i-1).getPoint());
			delta2 = getDelta(orm.get(i).getPoint(), orm.get(i+1).getPoint());
			
			toDraw = handler.drawBody(delta, delta2);
			g.drawImage(toDraw.getImage(), orm.get(i).getX()*size, orm.get(i).getY()*size, size, size, null);
		}
		
		//huvud
		delta = getDelta(orm.get(0).getPoint(), orm.get(1).getPoint());
		toDraw = handler.drawHead(delta);
		g.drawImage(toDraw.getImage(), orm.get(0).getX()*size, orm.get(0).getY()*size, size, size, null);
		
	}
	
	
	public void drawUniSnake(Graphics g){
		for(int i = 0; i < orm.size(); i++){
			g.drawImage(uniPicture, orm.get(i).getX()*size, orm.get(i).getY()*size, size, size, null);
		}
	}
	
	public Point getDelta(Point p1, Point p2){
		return new Point(p2.x-p1.x, p2.y-p1.y);
	}
	
	public void moveOrm(){
		Ormdel tail = orm.get(orm.size()-1);
		newTail.setLocation(tail.getX(), tail.getY());
		
		//flytta resten
		for(int i = orm.size()-1; i > 0; i--){
			orm.get(i).move(orm.get(i-1));
		}
		//flytta huvud
		int dX = orm.get(0).getX(); int dY = orm.get(0).getY();
		switch(ormDir){
		case 0:
			dY += -1;
			break;
		case 1:
			dX += 1;	
			break;
		case 2:
			dY += 1;
			break;
		case 3:
			dX += -1;
			break;
		}
		
		if(!solidWalls){
			if(dX < 0){
				dX = width-1;
			}else if(dX > width-1){
				dX = 0;
			}
			
			if(dY < 0){
				dY = height-1;
			}else if(dY > height-1){
				dY = 0;
			}
		}
		
		orm.get(0).move(dX, dY);
	}
	
	public void putRandomApple(){
		if(orm.size() < width*height){
			int randomX = -1;
			int randomY  = -1;
			boolean leta = true;
			while(leta){
				//System.out.println("a");
				leta = false;
				randomX = (int)(Math.random()*width);
				randomY = (int)(Math.random()*height);
				for(Ormdel o : orm){
					if(o.getX() == randomX && o.getY() == randomY){
						leta = true;
						break;
					}
				}
				
			}
			apple.setPos(randomX, randomY);
		}else{
			stop();
		}
		
	}
	
	public void kollaOmKrockar(){
		int x = orm.get(0).getX(), y = orm.get(0).getY();
		for(int i = 1; i < orm.size(); i++){
			if(x == orm.get(i).getX() && y == orm.get(i).getY()){
				stop();
			}
		}
		
		if(solidWalls){
			if(orm.get(0).getX() > width-1 || orm.get(0).getX() < 0 || orm.get(0).getY() > height-1 || orm.get(0).getY() < 0){
				stop();
			}
		}
	}
	
	public void kollaOmOnApple(){
		if(apple.getX() == orm.get(0).getX() && apple.getY() == orm.get(0).getY()){
			playSound(eat_music);
			//Ormdel svans = orm.get(orm.size()-1);
			score++;
			orm.add(new Ormdel(newTail.x, newTail.y));
			putRandomApple(); 
			timerCur += timerStep;
			if(timerCur > timerEnd)
				timerCur = timerEnd;
			time.setDelay((int)(1000.0/(timerCur/60.0)));
			//System.out.println((int)(timerA+old*timerPercent));
		}
	}
	
	ActionListener al = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//System.out.println("tick");
			
			if(bot != null){
				ormDir = bot.play(orm, apple, ormDir);
			}
			
			moveOrm();
			kollaOmOnApple();
			kollaOmKrockar();
			
			p.repaint();
			canChange = true;
			
		}};

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		//System.out.println(code);
		if(canChange){
			if(code == downKey){
				if(ormDir != 0){
					ormDir = 2;
				}
				playSound(down_music);
			}
			else if(code == rightKey){
				if(ormDir != 3){
					ormDir = 1;
				}
				playSound(right_music);
			}else if(code == upKey){
				if(ormDir != 2){
					ormDir = 0;
				}
				playSound(up_music);
			}else if(code == leftKey){
				if(ormDir != 1){
					ormDir = 3;
				}
				playSound(left_music);
			}
			canChange = false;
		}
		if(code == restartKey){
			start();
		}else if(code == pauseKey){
			togglePause();
			
		}else if(code == exitKey){
			stopPlayBackground();
			time.stop();
			frame.dispose();
			new Menyu().run();
		}else if(ke.isShiftDown() && code == KeyEvent.VK_1){
			bot = new HundraprocentBot(width, height);
		}else if(ke.isShiftDown() && code == KeyEvent.VK_2){
			bot = new ShortestBot(width, height, solidWalls);
		}else if(ke.isShiftDown() && code == KeyEvent.VK_0){
			bot = null;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	/*
	 * Hädanefter finns det ingenting som påverkar själva "mekaniken" av spelet, bara settings o grejer. 
	 */
	
	ComponentListener resizeListener = new ComponentListener(){

		@Override
		public void componentHidden(ComponentEvent arg0) {
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			fitSize();
			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			
		}
		
	};
	
	public void fitSize(){
		int deltaWidth = frame.getContentPane().getWidth()-p.getWidth();
		int deltaHeight = frame.getContentPane().getHeight()-p.getHeight();
		
		if(deltaWidth < deltaHeight){
			size = frame.getContentPane().getWidth()/(width + (solidWalls?2:0));
		}else{
			size = frame.getContentPane().getHeight()/(height + (solidWalls?2:0));
		}
		fixWindowSize();
		Dimension temp = new Dimension(windowWidth, windowHeight);
		p.setPreferredSize(temp);
		p.setMinimumSize(temp);
		p.revalidate();
		p.repaint();
	}
	
	public void fixWindowSize(){
		windowWidth = width*size;
		windowHeight = height*size;
		if(solidWalls){
			windowWidth += size*2;
			windowHeight += size*2;
		}
	}
	
	public void loadSettings(){
		try{
			SettingsReader sr = new SettingsReader(new File("snake/settings/settings.txt"));
			width = (int)Double.parseDouble(sr.get("width"));
			height = (int)Double.parseDouble(sr.get("height"));
			timerStart = (int)Double.parseDouble(sr.get("timerStart"));
			timerEnd = (int)Double.parseDouble(sr.get("timerEnd"));
			timerStep = (int)Double.parseDouble(sr.get("timerStep"));
			skin = sr.get("skin");
			upKey = Integer.parseInt(sr.get("up"));
			downKey = Integer.parseInt(sr.get("down"));
			leftKey = Integer.parseInt(sr.get("left"));
			rightKey = Integer.parseInt(sr.get("right"));
			solidWalls = Boolean.parseBoolean(sr.get("solidWalls"));
			
			snakeStart = new Point(width/2, height/2);
			fixWindowSize();
			
			//get texture flags
			File f = new File("snake/skins/"+skin+"/textures/texture_settings.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String input = "";
			while((input = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(input);
				String temp = st.nextToken();
				if(temp.equals("UNIBODY")){
					unibody = true;
				}else if(temp.startsWith("MULTIGRASS")){
					multigrass = Integer.parseInt(st.nextToken());
				}else if(temp.startsWith("SCORECOLOR")){
					String color = st.nextToken();
					scoreColor = new Color(Integer.parseInt(color.substring(0, 2), 16), Integer.parseInt(color.substring(2, 4), 16), Integer.parseInt(color.substring(4, 6), 16));
				}else if(temp.startsWith("BACKGROUND")){
					String color = st.nextToken();
					backgroundColor = new Color(Integer.parseInt(color.substring(0, 2), 16), Integer.parseInt(color.substring(2, 4), 16), Integer.parseInt(color.substring(4, 6), 16));
				}else if(temp.startsWith("BIGBACKGROUND")){
					bigBackground = true;
				}
			}
			br.close();
		}catch (Exception e){
			//e.printStackTrace();
			System.out.println("förmodligen så har inte skin:et någon settings-fil.");
		}
	}
	
	public void loadMusic(){
		background_music = "snake/skins/"+skin+"/sound/background.mp3";
		death_music = "snake/skins/"+skin+"/sound/death.mp3";
		left_music = "snake/skins/"+skin+"/sound/movement/left_snd.wav";
		right_music = "snake/skins/"+skin+"/sound/movement/right_snd.wav";
		down_music = "snake/skins/"+skin+"/sound/movement/down_snd.wav";
		up_music = "snake/skins/"+skin+"/sound/movement/up_snd.wav";
		eat_music = "snake/skins/"+skin+"/sound/eat.wav";
	}
	
	public void startBackgroundMusic(final String is){
		stopPlayBackground();
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					do{
						background = new Player(new FileInputStream(is));
						background.play();
					}while(background != null);
				}catch(Exception e){
					System.out.println("Bakgrundmusiken existerar inte");
				}
			}}).start();
		
	}
	
	public void stopPlayBackground(){
		if(background != null){
			background.close();
			background = null;
		}
	}
	
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(url)));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        //e.printStackTrace();
		    	  System.out.println("låten fanns inte");
		      }
		    }
		  }).start();
		}

	public void addNewHighscore(){
		if(HighscoreClass.isInTop10(score, timerStart, timerEnd, timerStep, solidWalls, width, height)){
			//String name = JOptionPane.showInputDialog(null, "Your score is "+score+"!\n"+"Enter your name with a maximum of 4 characters:", "Yatta!", JOptionPane.PLAIN_MESSAGE, null, null, latestName);
			String name = (String)JOptionPane.showInputDialog(null, "Your score is "+score+"!\n"+"Enter your name with a maximum of 4 characters:", "Yatta!", JOptionPane.QUESTION_MESSAGE,null,null, latestName);
			
			if(name == null || name.equals("")){
				//name = "dumhuvud";
			}else{
				name.replace(" ", "");
				if(name.length() > 4)
					name = name.substring(0, 4);
				latestName = name;
				HighscoreClass.addScore(name, score, timerStart, timerEnd, timerStep, solidWalls, width, height);
			}
		}
	}
	
	public void togglePause(){
		if(time.isRunning() ){
			time.stop();
			pause = true;
		}else if(!time.isRunning() && !gameOver){
			time.start();
			pause = false;
		}
		p.repaint();
	}
	
	public void loadImages(){
		try{
			appleIco = ImageIO.read(new File("snake/skins/"+skin+"/textures/tile_apple.png"));
			wallIco = ImageIO.read(new File("snake/skins/"+skin+"/textures/wall.png"));
			
			grassIco = new BufferedImage[multigrass];
			for(int i = 0; i < multigrass; i++){
				String temp = "";
				if(i != 0)
					temp = Integer.toString(i);
				
				grassIco[i] = ImageIO.read(new File("snake/skins/"+skin+"/textures/grass"+temp+".png"));
			}
			
			if(unibody){
				uniPicture = ImageIO.read(new File("snake/skins/"+skin+"/textures/snake_body.png"));
			}else{
				handler = new SpriteHandler();
				handler.loadImages(skin);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}

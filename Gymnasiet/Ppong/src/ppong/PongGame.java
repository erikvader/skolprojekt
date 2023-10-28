package ppong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PongGame extends JPanel implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 460;
	public static final int HEIGHT = 300;
	public static final double SCALE = 2;
	
	private Timer timer;
	private PinneBot left;
	private Pinne right;
	private ArrayList<Ball> balls;
	
	private int numBalls = 5;
	
	//private JFrame window;
	
	private int scoreLeft, scoreRight;
	
	private Font scoreFont = new Font("Arial", Font.BOLD, (int)(30*SCALE));
	
	/**
	 * spawnar och initierar allt.
	 * 
	 * @param window
	 */
	public PongGame(JFrame window){
		super();
		
		timer = new Timer(16, this);
		
		balls = new ArrayList<Ball>();
		spawnBalls();
		
		left = new PinneBot("lefty", 0+25, HEIGHT/2-25, 10, 50, Color.WHITE, true, balls);
		right = new PinneBot("righty", WIDTH-25-10, HEIGHT/2-25, 10, 50, Color.WHITE, false, balls);
		
		setPreferredSize(new Dimension((int)(WIDTH*SCALE), (int)(HEIGHT*SCALE)));
		setFocusable(true);
		addKeyListener(this);
		
		//this.window = window;
		
		scoreLeft = 0;
		scoreRight = 0;
		
	}
	
	/**
	 * startar timern.
	 */
	public void setUp(){
		requestFocus();
		timer.start();
		//setFocusTraversalKeysEnabled(false);
	}
	
	/**
	 * spawnar alla bollar och kör resetball(b) på alla
	 */
	private void spawnBalls(){
		for(int i = balls.size(); i < numBalls; i++){
			Ball ball = new Ball("ball"+i, WIDTH/2-5, HEIGHT/2-5, 10, 10, Color.WHITE);
			balls.add(ball);
			resetBall(ball);
		}
	}
	
	/**
	 * sätter bollen i mitten och skjuter iväg den åt ett slumpmässigt håll.
	 * 
	 * @param b
	 */
	private void resetBall(Ball b){
		//shoot random
		double grader = 15+(Math.random()*60)+(int)(Math.random()*4+1)*90;
		double speed = Math.random()*1.75+2;
		
		b.shootDir(grader, speed);
		b.setPos(WIDTH/2-5, HEIGHT/2-5);
		b.reset();
	}

	/**
	 * hanterar tangenttryckningar.
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		
		if(code == KeyEvent.VK_W){
			left.setUp(true);
		}
		if(code == KeyEvent.VK_S){
			left.setDown(true);
		}
		if(code == KeyEvent.VK_UP){
			right.setUp(true);
		}
		if(code == KeyEvent.VK_DOWN){
			right.setDown(true);
		}
	}

	/**
	 * hanterar tangenttryckningar.
	 */
	@Override
	public void keyReleased(KeyEvent ke) {
		int code = ke.getKeyCode();
		
		if(code == KeyEvent.VK_W){
			left.setUp(false);
		}
		if(code == KeyEvent.VK_S){
			left.setDown(false);
		}
		if(code == KeyEvent.VK_UP){
			right.setUp(false);
		}
		if(code == KeyEvent.VK_DOWN){
			right.setDown(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		
	}

	/**
	 * Uppdaterar hela spelet ett tick. 
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		//update
		left.tick();
		right.tick();
		
		//loppa bollarna
		Ball b;
		for(int i = 0; i < balls.size(); i++){
			b = balls.get(i);
			b.tick();
		
			//collide with varandra
			b.bouncePinne(left);
			b.bouncePinne(right);
			
			//ball outside
			if(b.isOutside()){
				//kolla vilket håll
				if(b.leftSide()){
					scoreRight++;
				}else{
					scoreLeft++;
				}
				resetBall(b);
			}
		
		}
		
		repaint();
	}
	
	/**
	 * ritar ut hela spelet.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)(WIDTH*SCALE), (int)(HEIGHT*SCALE));
		
		//objekten
		left.draw(g);
		right.draw(g);
		for(Ball b : balls)
			b.draw(g);
		
		//scores
		g.setColor(Color.WHITE);//Color.WHITE
		g.setFont(scoreFont);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(Integer.toString(scoreLeft), (int)(170*SCALE), (int)(40*SCALE));
		g.drawString(Integer.toString(scoreRight), (int)(290*SCALE) - fm.stringWidth(Integer.toString(scoreRight)), (int)(40*SCALE));
		
		g.drawLine((int)(230*SCALE), 0, (int)(230*SCALE), (int)(HEIGHT*SCALE));
		
	}
	
}

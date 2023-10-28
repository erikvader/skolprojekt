import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Multi {

	public static void main(String[] args) {
		new Multi().run();
	}
	
	JFrame window;
	public void run(){
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(new Rita(), BorderLayout.CENTER);
		window.setSize(500, 500);
		window.setResizable(true);
		window.setLocationRelativeTo(null);
		window.setTitle("ritar saker o shit");
		window.setVisible(true);
		
	}
	
	public class Rita extends JPanel{
		private static final long serialVersionUID = 1L;
		
		final double circleRadius = 0.9; //ska ju vara i caps
		final int pointsOnCircle = 100;
		final double radPerPoint = 2*Math.PI/pointsOnCircle;
		double mult = 1;
		
		Timer tim;
		double animDir = 0.01;
		boolean displayMult = false;
		boolean showHelp = true;
		boolean night = false;
		Color c1 = Color.BLACK;
		
		final String[] helpText = {"D: toggla info", "space: starta animation", "pilar u/d: steppa", "pilar l/r: ändra anim speed", "A: ändra anim dir", "N: night mode"};
		int timDelay = 40;
		
		public Rita(){
			setFocusable(true);
			requestFocus();
			setBackground(Color.WHITE);
			
			addKeyListener(new KeyListener(){
				@Override
				public void keyTyped(KeyEvent e) {
				}
				@Override
				public void keyPressed(KeyEvent e) {
					int keycode = e.getKeyCode();
					if(keycode == KeyEvent.VK_UP){
						mult += 0.01;
						repaint();
					}else if(keycode == KeyEvent.VK_DOWN && mult > 0){
						mult -= 0.01;
						repaint();
					}else if(keycode == KeyEvent.VK_SPACE){
						if(tim.isRunning()){
							tim.stop();
						}else{
							tim.start();
						}
						
					}else if(keycode == KeyEvent.VK_D){
						if(showHelp){
							showHelp = false;
							displayMult = true;
						}else if(displayMult){
							displayMult = false;
						}else{
							showHelp = true;
						}
						repaint();
					}else if(keycode == KeyEvent.VK_A){
						animDir = -animDir;
					}else if(keycode == KeyEvent.VK_RIGHT && timDelay > 0){
						timDelay -= 1;
						tim.setDelay(timDelay);
					}else if(keycode == KeyEvent.VK_LEFT){
						timDelay += 1;
						tim.setDelay(timDelay);
					}else if(keycode == KeyEvent.VK_N){
						night = !night;
						if(night){
							setBackground(Color.BLACK);
							c1 = Color.WHITE;
						}else{
							setBackground(Color.WHITE);
							c1 = Color.BLACK;
						}
						repaint();
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
				}
			});
			
			tim = new Timer(timDelay, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					mult += animDir;
					repaint();
				}
			});
		
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			double circleRadius = this.circleRadius*Math.min(getWidth(), getHeight())/2;
			
			//yttre cirkel
			g2d.setColor(c1);
			g2d.drawOval((int)(getWidth()/2 - circleRadius), (int)(getHeight()/2 - circleRadius), (int)(circleRadius*2), (int)(circleRadius*2));
			
			//fyll grejer
			g2d.setColor(c1);
			for(int i = 0; i < pointsOnCircle; i++){
				g2d.drawLine((int)(-Math.cos(radPerPoint*i)*circleRadius+getWidth()/2), (int)(-Math.sin(radPerPoint*i)*circleRadius+getHeight()/2), (int)(-Math.cos(radPerPoint*((i*mult) % pointsOnCircle))*circleRadius+getWidth()/2), (int)(-Math.sin(radPerPoint*((i*mult) % pointsOnCircle))*circleRadius+getHeight()/2));
			}
			
			//infotext
			if(displayMult)
				g2d.drawString("multiplier: "+Double.toString(mult), 10, 30);
			
			//help
			if(showHelp)
				for(int i = 0; i < helpText.length; i++)
					g2d.drawString(helpText[i], 10, 30+15*i);
			
			g2d.dispose();
		}
	}

}

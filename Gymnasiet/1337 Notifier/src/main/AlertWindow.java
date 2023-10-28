package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javazoom.jl.player.Player;

public class AlertWindow extends JFrame implements WindowListener{

	private static final long serialVersionUID = 1L;
	
	//JLabel text = new JLabel();
	String text = "";
	Player playMP3;
	Thread MP3Player;
	MainPanel mainPane = new MainPanel();

	public AlertWindow(){
		super("Leeeeet!");
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setSize(500, 500);
		super.setResizable(false);
		super.setLayout(new BorderLayout());
		super.add(mainPane, BorderLayout.CENTER);
		super.addWindowListener(this);
		super.setIconImage(new ImageIcon(getClass().getResource("icon1.png")).getImage());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//System.out.println(dim);
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
	}
	
	public void start(){
		super.setVisible(true);
		super.requestFocus();
		super.toFront();
		playSound("alert.wav");
		
	}
	
	public synchronized void playSound(final String url) {
		MP3Player = new Thread(new Runnable() {
		    public void run() {
		      try {
		    	  do{
		    		  InputStream fis = getClass().getResourceAsStream("alert.mp3");
			    	  playMP3 = new Player(fis);
			    	  playMP3.play();
		    	  }while(true);
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  });
		MP3Player.start();
	}

	
	public void setMessage(String msg){
		//text.setText(msg);
		text = msg;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void windowClosing(WindowEvent arg0) {
		MP3Player.stop();
		playMP3.close();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
	
	private class MainPanel extends JPanel{
		
		private static final long serialVersionUID = 1L;
		
		int fps = 120;
		long sleepTime = 1000/fps;
		Color[] colors = {new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255)};
		int colorCounter = 0;
		int colorCounter2 = 0;
		int colorChangePerFrame = 40;
		
		Font storFont = new Font("Arial", Font.BOLD, 80);
		Point textPos = new Point(0, 0);
		int offsetX = 0;
		int speed = 4;
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//background
			super.setBackground(colors[colorCounter]);
			// /background
			//text
			FontMetrics fm = g.getFontMetrics(storFont);
			//int hgt = fm.getAscent();
			int JPhgt = this.getHeight();
			int JPwidth = this.getWidth();
			textPos.y = JPhgt/2;
			
			if(offsetX < fm.stringWidth(text)+JPwidth){
				offsetX += speed;
			}else{
				offsetX = 0;
			}
			
			textPos.x = JPwidth - offsetX;
			
			g.setFont(storFont);
			g.setColor(Color.BLACK);
			g.drawString(text, textPos.x, textPos.y);
			
			// /text
		}
		
		public MainPanel(){
			new Thread(new Runnable(){
				public void run(){
					while(true){
						
						if(colorCounter2 == 0){
							colorCounter = 0;
						}else if(colorCounter2 == 1*colorChangePerFrame){
							colorCounter = 1;
						}else if(colorCounter2 == 2*colorChangePerFrame){
							colorCounter = 2;
						}
						
						if(colorCounter2 >= fps )
							colorCounter2 = -1;
						
							
						colorCounter2++;
						mainPane.repaint();
						
						try{
							Thread.sleep(sleepTime);
						}catch(Exception e){
							System.out.println("meh!");
						}
					}
				}
			}).start();
			
		}
	}
	
}

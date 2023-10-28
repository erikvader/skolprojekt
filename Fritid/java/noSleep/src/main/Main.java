package main;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Main {
	
	static Robot rob;

	public static void main(String[] args) throws Exception {
		JFrame window = new JFrame("/AFK");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b = new JButton("I'm no longer AFK");
		b.setPreferredSize(new Dimension(200, 50));
		window.add(b);
		window.pack();
		window.setVisible(true);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);
			}
		});
		window.requestFocus();
		Point grej = MouseInfo.getPointerInfo().getLocation();
		window.setLocation(grej.x, grej.y);
		window.setResizable(false);
		Dimension ws = window.getSize();
		rob = new Robot();
		while(true){
			Point p = window.getLocationOnScreen();
			move(p.x, p.y, ws.width, 0, 1000);
			move(p.x + ws.width, p.y, 0, ws.height, 500);
			move(p.x +ws.width, p.y+ws.height, -ws.width, 0, 1000);
			move(p.x, p.y+ws.height, 0, -ws.height, 500);
		}
	}
	
	public static void move(int fromX, int fromY, int offsetX, int offsetY, int speed){
		rob.mouseMove(fromX, fromY);
		double tickX = ((double)offsetX/speed);
		double tickY = ((double)offsetY/speed);
		for(int i = 1; i <= speed; i+=10){
			rob.mouseMove(fromX + (int)(tickX * i), fromY + (int)(tickY*i));
			rob.delay(10);
		}
		
		
	}

}

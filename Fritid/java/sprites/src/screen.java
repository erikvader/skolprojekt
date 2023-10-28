import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class screen extends JPanel{

	spriteCollection s;
	
	public screen(spriteCollection ss){
		s = ss;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		s.printAll(g);
		s.printAllHitbox(g);
		
	}
}

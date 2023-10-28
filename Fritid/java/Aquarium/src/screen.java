import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class screen extends JPanel{

	spriteCollection s;
	Image bgImage = new ImageIcon(getClass().getResource("textures/backgrounds/fishtank1.jpg")).getImage();
	
	public screen(spriteCollection ss){
		s = ss;
	}
	
	public void setBG(ImageIcon bg){
		bgImage = bg.getImage();
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		g.drawImage(bgImage, 0, 0, null);
		s.printAll(g);
		//s.printAllHitbox(g);
	}
	
}

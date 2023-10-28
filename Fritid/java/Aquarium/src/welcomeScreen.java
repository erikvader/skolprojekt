import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



public class welcomeScreen extends JPanel{
	
	Image bild = new ImageIcon(getClass().getResource("textures/menus/home.jpg")).getImage();
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		g.drawImage(bild, 0, 0, null);
	}

}

package game.chat;

import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;


public class TextBox {

	URL textBoxTexturePath;
	ImageIcon textBoxTexture;
	int xpos;
	int ypos;
	
	public TextBox(URL u, int x, int y, String mes){
		setTextBoxTexture(u);
		setTextBox(x, y, mes);
	}
	
	public void setTextBoxTexture(URL url){
		textBoxTexturePath = url;
		textBoxTexture = new ImageIcon(textBoxTexturePath);
	}
	
	public void print(Boolean b, Graphics g){
//		g.drawImage(textBoxTexture, , arg2, arg3)
	}
	
	public void setTextBox(int x, int y, String mes){
		
	}
	
	
}

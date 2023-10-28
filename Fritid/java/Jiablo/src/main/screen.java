package main;

import game.map.tileMap;
import game.sprites.spriteCollection;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


public class screen extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	spriteCollection col;
	tileMap map;
	
	public screen(spriteCollection c, tileMap m){
		col = c;
		map = m;
//		super.setSize(map.imgSize);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		map.printMap(g);
		col.printAll(g);
//		col.printAllHitbox(g);
		
//		g.drawImage(new ImageIcon(getClass().getResource("textures/testMap/grid60.png")).getImage(), 0, 0, null);
	}
	

}

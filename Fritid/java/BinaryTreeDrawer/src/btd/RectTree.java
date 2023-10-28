package btd;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class RectTree {
	public static final int nodew = 20, nodeh = 20;
	public int x, y, w, h, nx;
	public RectTree left, right;
	public String label;
	
	public RectTree(){
		
	}
	
	public RectTree(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void draw(Graphics g, int dx, int dy){
		if(w == 0 || h == 0) return;
		dx += x;
		dy += y;
		
		g.setColor(Color.RED);
		g.fillRect(dx+nx, dy, nodew, nodeh);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString(label, dx+nx, dy+nodeh/2);
		
		if(left != null){
			left.draw(g, dx, dy);
			g.setColor(Color.BLACK);
			g.drawLine(dx+nx+nodew/2, dy+nodeh, dx+left.x+left.w/2, dy+left.y);
		}
		
		if(right != null){
			right.draw(g, dx, dy);
			g.setColor(Color.BLACK);
			g.drawLine(dx+nx+nodew/2, dy+nodeh, dx+right.x+right.w/2, dy+right.y);
		}
		
		
	}
}

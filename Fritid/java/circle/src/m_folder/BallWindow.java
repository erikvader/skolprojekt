package m_folder;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


public class BallWindow extends JPanel{

	
	private static final long serialVersionUID = 1L;
	
	int circleSize = 0;
	
	public void setCircleSize(int size){
		circleSize = size;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		super.setBackground(Color.WHITE);
		g.setColor(Color.BLUE);
		drawCircle(g);
	}
	
	private void drawCircle(Graphics g){
		int x = this.getWidth()/2 - circleSize/2;;
		int y = this.getHeight()/2 - circleSize/2;;
	
		g.fillOval(x, y, circleSize, circleSize);
	}

}

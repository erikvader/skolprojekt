import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class mainTest {

	JFrame window = new JFrame("sprites");
	sprite sp1 = new sprite("sp1", 50, 50, 2, 4, 10, 10);
	sprite sp2 = new sprite("sp2", 60, 60, -2, 2, 200, 200);
	sprite sp3 = new sprite("sp3", 50, 50, 5, -1, 400, 200);
	sprite sp4 = new sprite("sp4", 50, 50, 1, 1, 100, 100);
	spriteCollection col1 = new spriteCollection();
	screen s = new screen(col1);
	
	public static void main(String[] args) {
		mainTest t = new mainTest();
		t.init();
		t.run();
	}
	
	public void init(){
		sp1.setImage(new ImageIcon(getClass().getResource("evil.png")));
		sp2.setImage(new ImageIcon(getClass().getResource("heart.png")));
		sp3.setImage(new ImageIcon(getClass().getResource("fish1.png")));
		sp4.setImage(new ImageIcon(getClass().getResource("fish2.png")));
		
		col1.add(sp1);
		col1.add(sp2);
		col1.add(sp3);
		col1.add(sp4);
		
		window.setSize(700, 500);
		window.add(s, BorderLayout.CENTER);
		window.setVisible(true);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*window.createBufferStrategy(2);
		window.setIgnoreRepaint(true);*/
		
		
	}
	
	public void run(){
		while(true){
			col1.moveAll();
			s.repaint();
			col1.checkAllCollisionAutoBounce();
			col1.checkAllWallCollisionAutoBounce(s.getWidth(), s.getHeight());
			try{
				Thread.sleep(10);
			}catch(Exception e){};
			
		}
	}

}

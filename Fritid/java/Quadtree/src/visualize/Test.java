package visualize;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Test {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Panel p = new Panel();
		p.setPreferredSize(new Dimension(1000, 600));
		//p.setSize(new Dimension(500, 300));
		//window.setSize(500, 300);
		window.add(p);
		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		p.init();
		p.requestFocus();
		
	}
	
}

package ppong;

import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame("2 pads 1 ball");
		PongGame game = new PongGame(window);
		window.setContentPane(game);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		game.setUp();
	}
	
	private static Color[] colors = {Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.YELLOW};
	
	public static Color randomColor(){
		return colors[(int)(Math.random()*colors.length)];
	}

}

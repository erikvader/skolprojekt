package m_folder;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main implements ChangeListener{

	int circleSize = 50;
	JFrame mainFrame;
	BallWindow BallWindow;
	JSlider slider;

	public Main() {
		mainFrame = new JFrame("WINDOW!");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		slider = new JSlider(JSlider.HORIZONTAL, 0, 450, 50);
		slider.addChangeListener(this);
		mainFrame.add(slider, BorderLayout.SOUTH);
		slider.setMajorTickSpacing(50);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(5);
		slider.setPaintLabels(true);
		BallWindow = new BallWindow();
		BallWindow.setCircleSize(circleSize);
		mainFrame.add(BallWindow, BorderLayout.CENTER);
		
		mainFrame.setSize(500, 500);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new Main().start();
	}

	public void start() {

	}

	@Override
	public void stateChanged(ChangeEvent event) {
		circleSize = slider.getValue();
		BallWindow.setCircleSize(circleSize);
		BallWindow.repaint();
	}

}

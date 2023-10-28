import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class aquarium implements Runnable, MouseListener, MouseWheelListener, KeyListener /*
																						 * ,
																						 * MouseMotionListener
																						 */{

	spriteCollection col = new spriteCollection();
	/*
	 * sprite f1 = new sprite("fish1", 50, 50, -1, 1.5f, 10, 10, false); sprite
	 * f2 = new sprite("fish2", 50, 50, -2.5f, 1, 100, 100, false); sprite s1 =
	 * new sprite("seaweed1", 13, 31, 1, 3.5f, 200, 200, true);
	 */

	screen s = new screen(col);
	JFrame window = new JFrame("Aquarium");

	Random rand = new Random();
	sprite lastSprite;

	public aquarium(/* spriteCollection ss, screen sss */) {
		// col = ss;
		// s = sss;

	}

	int[] borders = new int[4];

	public void initialize(int w) {
		/*
		 * f1.setImage1(new
		 * ImageIcon(getClass().getResource("textures\\fishes\\fish1.png")));
		 * f1.setImage2(new
		 * ImageIcon(getClass().getResource("textures\\fishes\\fish1r.png")));
		 * f1.setImage(1); f2.setImage1(new
		 * ImageIcon(getClass().getResource("textures\\fishes\\fish2.png")));
		 * f2.setImage2(new
		 * ImageIcon(getClass().getResource("textures\\fishes\\fish2r.png")));
		 * f2.setImage(1); s1.setImage1(new
		 * ImageIcon(getClass().getResource("textures\\seaweeds\\seaweed1.png"
		 * ))); s1.setImage(1);
		 * 
		 * col.add(f1); col.add(f2); col.add(s1);
		 */

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(s, BorderLayout.CENTER);
		s.addMouseListener(this);
		window.addMouseWheelListener(this);
		window.addKeyListener(this);
		// s.addMouseMotionListener(this);
		// window.setSize(1920, 1080);
		if (w == 0) {
			borders[0] = window.getInsets().top;
			borders[1] = window.getInsets().bottom;
			borders[2] = window.getInsets().left;
			borders[3] = window.getInsets().right;
			window.setSize(1024 + borders[2] + borders[3], 768 + borders[0] + borders[1]);
		}else if(w == 1){
			window.setSize(1920, 1080);
			window.setUndecorated(true);
		}

		window.setVisible(true);
	}

	public static void main(String[] args) {
		aquarium a = new aquarium();
		initW w = new initW(a.s, a.col);
		do {
			// System.out.println("checking");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
		} while (w.getGo() == false);

		// System.out.println(w.selIn);
		if (w.selIn == 6 | w.selIn == 7) {
			a.initialize(1);
		} else {
			a.initialize(0);
		}
		Thread ta = new Thread(a);
		ta.run();

	}

	public void run() {
		while (true) {
			// System.out.println("ran!");
			checkDir();
			col.checkAllWallCollisionAutoBounce(s.getWidth(), s.getHeight());
			col.checkAllCollisionAutoBounce();
			col.moveAll();
			s.repaint();

			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		}

	}

	public void checkDir() {
		List<sprite> spriteList = col.getList();

		for (int i = 0; i < spriteList.size(); i++) {
			if (spriteList.get(i).name.contains("fish")) {
				double oldx = spriteList.get(i).getVX();
				if (spriteList.get(i).getCurImageIndex() == 0 && oldx < 0) {
					spriteList.get(i).switchImage(1);
				} else if (spriteList.get(i).getCurImageIndex() == 1 && oldx > 0) {
					spriteList.get(i).switchImage(0);
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		sprite clickedSprite = col.checkAllMouseClick(e.getX(), e.getY());
		// System.out.println("X "+e.getX()+" Y "+e.getY());
		if (clickedSprite != null) {
			lastSprite = clickedSprite;
			if (e.getButton() == 1) {
				int ranDir = rand.nextInt(3);
				if (ranDir == 0) {
					clickedSprite.setVX(clickedSprite.vx *= -1);
					clickedSprite.setVY(clickedSprite.vy *= -1);
				} else if (ranDir == 1) {
					clickedSprite.setVY(clickedSprite.vy *= -1);
				} else if (ranDir == 2) {
					clickedSprite.setVX(clickedSprite.vx *= -1);
				}
			} else if (e.getButton() == 3) {
				if (clickedSprite.going == false) {
					clickedSprite.setGoing(true);
				} else {
					clickedSprite.setGoing(false);
				}
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mouseClicked(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent arg0) {}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (lastSprite != null) {
			double scrollAmount = e.getPreciseWheelRotation() / 4;
			if (e.isControlDown()) {
				changeX(scrollAmount);
			} else if (e.isShiftDown()) {
				changeY(scrollAmount);
			} else if (e.isControlDown() == false && e.isShiftDown() == false) {
				changeX(scrollAmount);
				changeY(scrollAmount);
			}

		}
	}

	private void changeX(double scrollAmount) {
		if (lastSprite.vx <= 0) {
			lastSprite.setVX(lastSprite.vx += (float) scrollAmount);
			if (lastSprite.vx > 0) {
				lastSprite.vx = 0;
			}
		} else {
			lastSprite.setVX(lastSprite.vx -= (float) scrollAmount);
			if (lastSprite.vx < 0) {
				lastSprite.vx = 0;
			}
		}
	}

	private void changeY(double scrollAmount) {
		if (lastSprite.vy <= 0) {
			lastSprite.setVY(lastSprite.vy += (float) scrollAmount);
			if (lastSprite.vy > 0) {
				lastSprite.vy = 0;
			}
		} else {
			lastSprite.setVY(lastSprite.vy -= (float) scrollAmount);
			if (lastSprite.vy < 0) {
				lastSprite.vy = 0;
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_S) {
			if (lastSprite.going == false) {
				lastSprite.setGoing(true);
			} else {
				lastSprite.setGoing(false);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			changeY(0.25);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			changeY(-0.25);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			changeX(-0.25);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			changeX(0.25);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			List<sprite> tempList = col.getList();
			for (int i = 0; i < tempList.size(); i++) {
				if (tempList.get(i).going) {
					tempList.get(i).setGoing(false);
				} else {
					tempList.get(i).setGoing(true);
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

	/*
	 * public void mouseDragged(MouseEvent e) { try{ //vart för mycket fel
	 * lastSprite.setGoing(false); lastSprite.setX(e.getX());
	 * lastSprite.setY(e.getY()); //lastSprite.setGoing(true); }catch(Exception
	 * ex){} }
	 * 
	 * public void mouseMoved(MouseEvent arg0) {}
	 */

}

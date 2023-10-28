package visualize;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import quadtree.QuadObject;
import quadtree.Quadtree;
import quadtree.QuadtreeDraw;

public class Panel extends JPanel implements MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	public Quadtree qt;
	public Rectangle.Double retRect;
	public ArrayList<TestClass> allObjects;
	public Rectangle totalRect;
	
	public Timer timer;
	
	public Panel(){
		addMouseListener(this);
		addKeyListener(this);
		allObjects = new ArrayList<TestClass>();
	}
	
	public void init(){
		qt = new Quadtree(0, 0, getWidth(), getHeight(), 10, 1);
		System.out.println(super.getWidth() + " " + super.getHeight());
		totalRect = super.getBounds();
		
		/*
		qt.add(new TestClass(10, 10, 10, 10));
		qt.add(new TestClass(400, 10, 10, 10));
		qt.add(new TestClass(500, 300, 10, 10));
		qt.add(new TestClass(450, 300, 10, 10));
		qt.add(new TestClass(470, 300, 10, 10));
		*/
		
		for(int i = 0; i < 1000; i++){
			int rx = (int)(Math.random()*(getWidth()-10));
			int ry = (int)(Math.random()*(getHeight()-10));
			TestClass tc = new TestClass(rx, ry, 10, 10);
			qt.add(tc);
			allObjects.add(tc);
		}
		
		timer = new Timer(10, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				for(TestClass tc : allObjects){
					tc.update();
					qt.updateObject(tc);
				}
				repaint();
				//System.out.println(qt.getTotalElements());
			}
		});
		timer.start();
		
		System.out.println(qt.getTotalElements());
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(qt != null)
			QuadtreeDraw.painTree(g, qt);
		
		if(retRect != null){
			g.setColor(Color.BLUE);
			g.drawRect((int)retRect.x, (int)retRect.y, (int)retRect.width, (int)retRect.height);
		}
	}
	
	public class TestClass extends QuadObject{
		double velx, vely;
		public boolean marked = false;
		public TestClass(double x, double y, double w, double h) {
			super(x, y, w, h);
			velx = Math.random()+0.5;
			velx *= (int)(Math.random()*2) == 0 ? 1 : -1;
			vely = Math.random()+0.5;
			vely *= (int)(Math.random()*2) == 0 ? 1 : -1;
		}
		public void update(){
			super.translate(velx, vely);
			if(getBounds().getX() < 0 || getBounds().getMaxX() >= totalRect.getMaxX()){
				velx *= -1;
			}
			if(getBounds().getY() < 0 || getBounds().getMaxY() >= totalRect.getMaxY()){
				vely *= -1;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/*
		int w = 30;
		int h = 30;
		retRect = new Rectangle2D.Double(e.getX()-w/2, e.getY()-h/2, w, h);
		ArrayList<QuadObject> ret = new ArrayList<QuadObject>();
		qt.retrieve(retRect, ret);
		for(TestClass tc : allObjects) tc.marked = false;
		for(QuadObject tc : ret) ((TestClass)tc).marked = true;
		*/
		
		for(int i = 0; i < allObjects.size(); i++){
			if(allObjects.get(i).getBounds().contains(e.getX(), e.getY())){
				qt.removeObject(allObjects.get(i));
				allObjects.remove(i);
				break;
			}
		}
		
		System.out.println(qt.getTotalElements());
		
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("released!");
		if(e.getKeyCode() == KeyEvent.VK_1){
			qt.removeObject(allObjects.get(0));
			allObjects.remove(0);
		}
		
		repaint();
	}

}

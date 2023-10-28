package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main implements MouseMotionListener, KeyListener{

	public static void main(String[] args) {
		new Main().run();
	}
	
	JFrame window;
	ritruta rr;
	JButton goBtn;
	Color goBtnColor;
	JSlider slid;
	
	boolean[][] someoneHasDied;
	boolean[][] organism;
	boolean running = false;
	
	public int brushSize = 0;
	public int tilesX = 400;
	public int tilesY = 200;
	public int size = 2;
	public int width = tilesX*(size+1)-1;
	public int height = tilesY*(size+1)-1;
	public double updatePerSecond = 5;
	
	public void run(){
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Game of Life");
		window.setLayout(new BorderLayout());
		window.setResizable(true);
		
		rr = new ritruta();
		rr.setPreferredSize(new Dimension(width, height));
		rr.setMaximumSize(new Dimension(width, height));
		rr.setMinimumSize(new Dimension(width, height));
		window.add(rr, BorderLayout.CENTER);
		
		JPanel tools = new JPanel(new FlowLayout());
		//tools.setMaximumSize(new Dimension(width, 0));
		//tools.setPreferredSize(new Dimension(width, 90));
		//tools.setMinimumSize(new Dimension(width, 50));
		tools.setBackground(Color.GRAY);
		
		goBtn = new JButton("321 kör!");
		goBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!running){
					start();
				}else{
					stop();
				}
			}
			
		});
		goBtnColor = goBtn.getBackground();
		tools.add(goBtn);
		
		JButton resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(running)
					stop();
				
				reset();
			}
			
		});
		tools.add(resetBtn);
		
		JButton options = new JButton("Options");
		//saker
		tools.add(options);
		
		JButton preBtn = new JButton("Predefined");
		preBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] possi = {"Line", "cannon"};
				String val = (String)JOptionPane.showInputDialog(window, "Välj en: ", "Predefined", JOptionPane.PLAIN_MESSAGE, null, possi, "Line");
				fixPreset(val);
			}
		});
		tools.add(preBtn);
		
		slid = new JSlider(25, 100100, 300);
		slid.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent ce) {
				updatePerSecond = slid.getValue()/100;
			}
			
		});
		slid.setPreferredSize(new Dimension(700, 20));
		tools.add(slid);
		
		window.add(tools, BorderLayout.SOUTH);
		
		window.pack();
		window.setLocationRelativeTo(null);
		rr.addMouseMotionListener(this);
		rr.addKeyListener(this);
		window.setVisible(true);
		
		organism = new boolean[tilesX][tilesY];
		someoneHasDied = new boolean[tilesX][tilesY];
		
		reset();
		
		rr.requestFocus();
	}
	
	public void start(){
		running = true;
		goBtn.setBackground(Color.RED);
		mainLoop();
	}
	
	public void stop(){
		goBtn.setBackground(goBtnColor);
		running = false;
	}
	
	public void fixPreset(String val){
		reset();
		if(val.equalsIgnoreCase("line")){
			int middle = tilesY/2;
			for(int x = 0; x < tilesX; x++){
				addOrganism(x, middle);
			}
		}else if(val.equalsIgnoreCase("cannon")){
			addOrganism(5, 5);
			addOrganism(5, 6);
			addOrganism(6, 5);
			addOrganism(6, 6);
			addOrganism(15, 5);
			addOrganism(15, 6);
			addOrganism(15, 7);
			addOrganism(16, 4);
			addOrganism(16, 8);
			addOrganism(17, 9);
			addOrganism(17, 3);
			addOrganism(18, 3);
			addOrganism(18, 9);
			addOrganism(19, 6);
			addOrganism(20, 4);
			addOrganism(20, 8);
			addOrganism(21, 5);
			addOrganism(21, 6);
			addOrganism(21, 7);
			addOrganism(22, 6);
			addOrganism(25, 5);
			addOrganism(25, 4);
			addOrganism(25, 3);
			addOrganism(26, 5);
			addOrganism(26, 4);
			addOrganism(26, 3);
			addOrganism(27, 2);
			addOrganism(27, 6);
			addOrganism(29, 2);
			addOrganism(29, 1);
			addOrganism(29, 6);
			addOrganism(29, 7);
			addOrganism(39, 4);
			addOrganism(39, 5);
			addOrganism(40, 4);
			addOrganism(40, 5);
			
		}
	}
	
	private void mainLoop(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(running){
					doUpdates();
					rr.repaint();
					try{
						Thread.sleep((int)(1000/updatePerSecond));
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}}).start();
	}
	
	public void doUpdates(){
		int[][] tempOrga = new int[tilesX][tilesY];
		for(int x = 0; x < tilesX; x++){
			for(int y = 0; y < tilesY; y++){
				int grannar = kollaGrannar(x, y);
				if(!organism[x][y]){//kolla om icke-organismen ska bli en organism
					if(grannar == 3){
						tempOrga[x][y] = 1;
					}
				}else{//kolla om ska dö
					if(grannar < 2 || grannar > 3){
						tempOrga[x][y] = -1;
					}
				}
			}
		}
		
		for(int i2 = 0; i2 < tempOrga.length; i2++){
			for(int i = 0; i < tempOrga[0].length; i++){
				if(tempOrga[i2][i] == 1){
					organism[i2][i] = true;
				}else if(tempOrga[i2][i] == -1){
					organism[i2][i] = false;
					someoneHasDied[i2][i] = true;
				}
			}
		}
		
	}
	
	public int kollaGrannar(int x, int y){
		int gran = 0;
		
		for(int dx = -1; dx <= 1; dx++){
			for(int dy = -1; dy <= 1; dy++){
				if(dx != 0 || dy != 0){
					if(x+dx >= 0 && x+dx < tilesX){
						if(y+dy >= 0 && y+dy < tilesY){
							if(organism[x+dx][y+dy]){
								gran++;
							}
						}
					}
				}
			}
		}
		
		return gran;
	}
	
	public void reset(){
		for(int x = 0; x < tilesX; x++){
			for(int y = 0; y < tilesY; y++){
				someoneHasDied[x][y] = false;
				organism[x][y] = false;
				rr.repaint();
			}
		}
	}
	
	public void addOrganism(int x, int y){
		organism[x][y] = true;
		someoneHasDied[x][y] = true;
	}
	
	class ritruta extends JPanel{
		private static final long serialVersionUID = 1L;
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			super.setBackground(Color.WHITE);
			
			for(int x = 0; x < tilesX; x++){
				for(int y = 0; y < tilesY; y++){
					if(someoneHasDied[x][y]){
						g.setColor(Color.GREEN);
						g.fillRect(x*(size+1), y*(size+1), size, size);
					}
					if(organism[x][y]){
						g.setColor(Color.BLACK);
						g.fillRect(x*(size+1), y*(size+1), size, size);
					}
				}
			}
			
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		//System.out.println(me.getX() + " " + me.getY());
		int x = me.getX()/(size+1);
		int y = me.getY()/(size+1);
		//System.out.println(me.getX()+" "+me.getY());
		if(x-(brushSize) >= 0 && x+brushSize < tilesX && y+brushSize >= 0 && y-brushSize < tilesY){
			for(int i = x-brushSize; i <= x+brushSize; i++){
				for(int i2 = y-brushSize; i2 <= y+brushSize; i2++){
					addOrganism(i, i2);
				}
			}
			rr.repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(!rr.hasFocus())
			rr.requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_UP){
			brushSize++;
		}else if(ke.getKeyCode() == KeyEvent.VK_DOWN){
			if(brushSize > 0)
				brushSize--;
		}
		//System.out.println(brushSize);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}

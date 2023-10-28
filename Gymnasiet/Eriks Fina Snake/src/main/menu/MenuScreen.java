package main.menu;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MenuScreen extends JPanel/* implements KeyListener*/{

	private static final long serialVersionUID = 1L;
	
	protected String title = "HAHAHAHSH";
	protected ArrayList<Option> options = new ArrayList<>();
	protected int cur = 0;
	protected Color marked = Color.RED;
	protected int scrollOffset = 0;
	protected Font titleFont = new Font("Arial", Font.PLAIN, 50);
	protected Font optionsFont = new Font("Arial", Font.PLAIN, 25);
	
	public MenuScreen(String title){
		//super.setFocusable(true);
		//super.addKeyListener(this);
		this.title = title;
		
	}
	/*
	public void test(){
		super.requestFocus();
		//super.addKeyListener(this);
	}
	*/
	public void addOption(String name, MenyuAction ma){
		options.add(new Option(name, ma));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		super.setBackground(Color.GREEN);
		g.setColor(Color.BLACK);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(title, this.getWidth()/2-fm.stringWidth(title)/2, 60);
		
		g.setFont(optionsFont);
		fm = g.getFontMetrics();
		for(int i = 0; i < options.size(); i++){
			if(i == cur){
				g.setColor(marked);
				g.fillRect(this.getWidth()/2-fm.stringWidth(options.get(i).getName())/2, (i*30+120)-fm.getAscent()-scrollOffset, fm.stringWidth(options.get(i).getName()), fm.getHeight());
				g.setColor(Color.BLACK);
			}
			if((i*30+120-scrollOffset) >= 120)
				g.drawString(options.get(i).getName(), this.getWidth()/2-fm.stringWidth(options.get(i).getName())/2, i*30+120-scrollOffset);
		}
	}
	
	private void checkOffset(){
		Canvas temp = new Canvas();
		FontMetrics fm = temp.getFontMetrics(optionsFont);
		
		int ypos = (cur*30+120)-fm.getAscent();
		int height = fm.getHeight();
		if(ypos+height > this.getHeight()){
			scrollOffset = (ypos+height) - this.getHeight() +10;
		}else{
			scrollOffset = 0;
		}
				
	}

	public void processPress(KeyEvent e){
		int code = e.getKeyCode();
		//System.out.println(code);
		if(code == KeyEvent.VK_UP){
			if(cur > 0){
				cur--;
			}else{
				cur = options.size()-1;
			}
		}else if(code == KeyEvent.VK_DOWN){
			if(cur < options.size()-1){
				cur++;
			}else{
				cur = 0;
			}
		}else if(code == KeyEvent.VK_ENTER){
			options.get(cur).click();
		}
		checkOffset();
		repaint();
	}
	
	public void changedTo(){
		cur = 0;
	}
	
}

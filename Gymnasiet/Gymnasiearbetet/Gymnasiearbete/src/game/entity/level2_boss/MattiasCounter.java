package game.entity.level2_boss;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.entity.MapObject;
import game.tileMap.TileMap;

public class MattiasCounter extends MapObject {

	private int time = 60*63; //63 ticks per sekund. avrundningsfel vid mina loop
	private Font daFont = new Font("monospace", Font.BOLD, 40);
	
	private boolean started = false;
	
	private int xpos = -500, ypos = -500;
	
	public MattiasCounter(TileMap tm) {
		super(tm);
	}
	
	public int getTime(){return time;}
	
	public void update(){
		if(!started) return;
		
		if(time > 0){
			time--;
		}
	}
	
	public void start(){
		started = true;
		xpos = 13*30;
		ypos = 2*30;
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		g.setColor(Color.RED);
		g.setFont(daFont);
		String daString = Integer.toString((int)Math.floor(time/63.0));
		if(daString.length() == 1){
			daString = "0"+daString;
		}
		g.drawString(daString, (int)(xmap+xpos), (int)(ypos+ymap));
	}

}

package game.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;


public class Platform extends MapObject{

	private Player player;
	private Point p1, p2, comingFrom;
	private double distance;
	private boolean playerOn = false;
	private boolean comingFromAbove = false;
	private boolean fallingThrough = false;
	private double relativeX;
	
	public Platform(TileMap tm, Player player) {
		super(tm);
		this.player = player;
		
		height = width = 30;
		cheight = 4;
		cwidth = 20;
		
		facingRight = true;
		
		animation = new Animation();
		animation.setFrames(Content.platform1);
		animation.setDelay(5);
	}
	
	public void setRoute(Point p1, Point p2, double speed){
		this.p1 = p1;
		this.p2 = p2;
		comingFrom = this.p1;
		setPosition(p1.getX(), p1.getY());
		
		distance = Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2)+Math.pow(p1.getY()-p2.getY(), 2));
		double ratio = speed/distance;
		
		dx = (p2.getX()-p1.getX())*ratio;
		dy = (p2.getY()-p1.getY())*ratio;
	}
	
	public double getRelativeX(){return relativeX;}
	
	public boolean isOn(){return playerOn;}
	
	public void update(Section s){
		if(!isInsideSection(s)) return;
		
		animation.update();
		
		relativeX = Math.round(player.x-x);
		
		//player på???
		if(playerOn){
			Rectangle pRect = player.getRectangle();
			pRect.y += 1;
			if(!intersects(pRect)){
				playerOn = false;
				
			}
		}
		
		if(!playerOn && comingFromAbove && player.intersects(this)){
			playerOn = true;
		}
		
		if((player.gety()+player.cheight/2) < y-cheight/2+1){
			comingFromAbove = true;
		}else{
			comingFromAbove = false;
		}
		
		if(fallingThrough){
			comingFromAbove = false;
			if((player.gety()+player.cheight/2) > y-cheight/2+1){
				fallingThrough = false;
			}
		}
		
		//position
		x += dx;
		y += dy;
		double dist = Math.sqrt(Math.pow(comingFrom.getX()-x, 2)+Math.pow(comingFrom.getY()-y, 2));
		if(dist >= distance){
			if(comingFrom == p1) comingFrom = p2;
			else comingFrom = p1;
			
			dx *= -1;
			dy *= -1;
			
			setPosition(comingFrom.getX(), comingFrom.getY());
		}
		
	}
	
	@Override
	public void draw(Graphics2D g){
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
	}
	
	public void fallThrough(){
		comingFromAbove = false;
		playerOn = false;
		fallingThrough = true;
	}
	
	public void asd(){
		int a = (int)(x + xmap - width / 2.0);
		int b = (int)(y + ymap - height / 2.0);
		System.out.println("platform mitten: "+x+", "+y+" onScreen: "+a+", "+b);
		//System.out.println(xmap+" "+ymap);
	}

}

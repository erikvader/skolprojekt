package game.entity;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.functions.Trigger;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;


public class Spike extends MapObject{

	//orientation
	private int orientation;
	private BufferedImage[][] sprites;
	private int size = 3;
	private double speed;
	
	private Trigger trigger;
	private Player player;
	private boolean launching;
	
	public static int UP = 0;
	public static int RIGHT = 1;
	public static int DOWN = 2;
	public static int LEFT = 3;
	
	public static final double DEFAULT_SPEED = 3.5;
	
	//intersection
	private Polygon hitbox;
	private int lastX, lastY;
	
	//damage
	private int damage;
	
	public Spike(TileMap tm, int orientation, int size) {
		super(tm);
		
		this.orientation = orientation;
		facingRight = true;
		
		Content.loadSpikes();
		
		width = 30;
		height = 30;
		cwidth = 1;
		cheight = 1;
		
		setSize(size);
		
		sprites = Content.spike1;
		animation = new Animation();
		animation.setFrames(sprites[3-size]);
		animation.setFrame(this.orientation);
		animation.setDelay(-1);
		
		speed = DEFAULT_SPEED;
		
		if(orientation == 0 || orientation == 2){
			
		}else if(orientation == 1 || orientation == 3){
			/*int temp = cwidth;
			cwidth = cheight;
			cheight = temp;*/
			
		}
		
		damage = 1;
		
	}
	
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		setHitbox();
		lastX = (int)x;
		lastY = (int)y;
	}
	
	public boolean intersectsSpike(MapObject o) {
		if(!((int)x == lastX && (int)y == lastY)){
			setHitbox();
			lastX = (int)x;
			lastY = (int)y;
		}
		
		return hitbox.intersects(o.getRectangle());
	}
	
	public void setTrigger(Trigger t, Player player){
		trigger = t;
		this.player = player;
	}
	
	public void generateTrigger(Player player, int width, int height){
		this.player = player;
		
		Rectangle rect = new Rectangle();
		if(orientation == UP){
			rect.width = width;
			rect.height = height;
			rect.x = (int)(this.x-width/2.0);
			rect.y = (int)(this.y -(height-this.height/2.0));
		}else if(orientation == DOWN){
			rect.width = width;
			rect.height = height;
			rect.x = (int)(this.x-width/2.0);
			rect.y = (int)(this.y -this.height/2.0);
		}else if(orientation == RIGHT){
			rect.width = height;
			rect.height = width;
			rect.x = (int)(this.x-this.width/2.0);
			rect.y = (int)(this.y-height/2.0);
		}else if(orientation == LEFT){
			rect.width = height;
			rect.height = width;
			rect.x = (int)(this.x-height+this.height/2);
			rect.y = (int)(this.y-width/2.0);
		}
		trigger = new Trigger(rect);
		
	}
	
	private void setSize(int s){
		if(size == s) return;
		size = s;
		if(size == 3){
			width = 30;
			height = 30;
			//cwidth = 20;
			//cheight = 28;
		}else if(size == 2){
			width = height = 20;
			//cwidth = 13;
			//cheight = 19;
		}else if(size == 1){
			width = height = 10;
			//cwidth = 6;
			//cheight = 9;
		}
		
		
	}
	
	private void setHitbox(){
		if(orientation == UP){
			hitbox = new Polygon(new int[]{(int)x, (int)(x-width/2), (int)(x+width/2)}, new int[]{(int)(y-height/2), (int)(y+height/2), (int)(y+height/2)}, 3);
		}else if(orientation == DOWN){
			hitbox = new Polygon(new int[]{(int)x, (int)(x-width/2), (int)(x+width/2)}, new int[]{(int)(y+height/2), (int)(y-height/2), (int)(y-height/2)}, 3);
		}else if(orientation == LEFT){
			hitbox = new Polygon(new int[]{(int)(x-width/2), (int)(x+width/2), (int)(x+width/2)}, new int[]{(int)(y), (int)(y-height/2), (int)(y+height/2)}, 3);
		}else if(orientation == RIGHT){
			hitbox = new Polygon(new int[]{(int)(x+width/2), (int)(x-width/2), (int)(x-width/2)}, new int[]{(int)(y), (int)(y-height/2), (int)(y+height/2)}, 3);
		}
		
	}
	
	public void setSpeed(double s){speed = s;}
	
	public int getDamage(){
		return damage;
	}
	
	public boolean isLaunching(){return launching;}
	
	public void update(Section s){
		if(!isInsideSection(s)) return;
		
		updateMovement();
		
		if(trigger == null) return;
		
		trigger.update(player);
		
		if(trigger.hasTriggered()){
			launch();
		}
	}
	
	public void launch(){
		if(orientation == 0){
			dy = -speed;
		}else if(orientation == 1){
			dx = speed;
		}else if(orientation == 2){
			dy = speed;
		}else if(orientation == 3){
			dx = -speed;
		}
		launching = true;
	}
	
	public void stopLaunch(){
		launching = false;
		dx = dy = 0;
	}
	
	private void updateMovement(){
		if(launching){
			//checkTileMapCollision(); //falling kommer att bli true
			//setPosition(xtemp, ytemp);
			
			x += dx;
			y += dy;
			
			if(dx == 0 && dy == 0){
				launching = false;
			}
			
		}
	}
	
	/*
	public void asd(){
		int a = (int)Math.round(x + xmap - width / 2.0);
		int b = (int)Math.round(y + ymap - height / 2.0);
		System.out.println("mitten: "+x+", "+y+" onScreen: "+a+", "+b);
		System.out.println(xmap+" "+ymap);
	}
	*/
	
	@Override
	public void draw(Graphics2D g){
		setMapPosition();
		if(notOnScreen()) return;
		super.draw(g);
		/*
		if(hitbox == null) return;
		g.setColor(Color.BLUE);
		g.translate(xmap, ymap);
		g.drawPolygon(hitbox);
		g.translate(-xmap, -ymap);
		
		if(trigger != null) trigger.draw(g, xmap, ymap);
		g.setColor(Color.RED);
		g.drawRect((int)(x+xmap-cwidth/2), (int)(y+ymap-cheight/2), cwidth, cheight);
		*/
	}

	
}

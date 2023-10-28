package game.tileMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import game.entity.MapObject;
import game.entity.Player;
import game.main.GamePanel;


public class Section {

	private Point spawn;
	private Background background;
	private TileMap tileMap;
	private Player player;
	
	private boolean canFallOutside = true;
	
	//sizes
	private int x, y, width, height;
	private int xmin, ymin, xmax, ymax;
	
	
	public Section(int x, int y, int width, int height, Background bg, TileMap map, Player player){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.background = bg;
		this.tileMap = map;
		this.player = player;

		spawn = new Point(0, 0);
		
		xmin = GamePanel.WIDTH - x - width;
		xmax = -x;
		ymin = GamePanel.HEIGHT - y - height;
		ymax = -y;
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(x, y, width, height);
	}
	
	public void setSpawn(int x, int y){spawn.x = x; spawn.y = y;}
	public Point getSpawn(){return spawn;}
	public int getSpawnX(){return spawn.x;}
	public int getSpawnY(){return spawn.y;}
	public void setFallOutside(boolean b){canFallOutside = b;}
	
	public boolean fellOutside(MapObject mo){ //ramlade ner
		if(mo.gety() >= height+y+mo.getHeight()/2){
			return true;
		}
		return false;
	}
	
	public void update(){
		//tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2.0 - (int)player.getx(),
			GamePanel.HEIGHT / 2.0 - (int)player.gety(),
			xmin, xmax, ymin, ymax
		);
		
		//background
		background.setPosition(tileMap.getx()+x, tileMap.gety()+y);
		background.update();
		
		//player outside
		if(canFallOutside && fellOutside(player)){
			player.setDead();
		}
	}
	
	/**
	 * ritar typ bara backgrounden
	 * @param g
	 */
	public void draw(Graphics2D g){
		background.draw(g);
	}

	
}












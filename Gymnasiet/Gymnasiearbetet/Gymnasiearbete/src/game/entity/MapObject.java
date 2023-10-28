package game.entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import game.handlers.Calc;
import game.main.GamePanel;
import game.tileMap.Section;
import game.tileMap.Tile;
import game.tileMap.TileMap;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	//protected boolean topLeft;
	//protected boolean topRight;
	//protected boolean bottomLeft;
	//protected boolean bottomRight;
	protected boolean leftSide;
	protected boolean rightSide;
	protected boolean bottomSide;
	protected boolean topSide;
	
	protected boolean collidedWithMap = false;
	protected boolean collidedWithMapTop = false;
	protected boolean collidedWithMapBottom = false;
	protected boolean collidedWithMapLeft = false;
	protected boolean collidedWithMapRight = false;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//water
	protected boolean inWater;
	
	//lava
	protected boolean inLava;
	
	
	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize(); 
		facingRight = true;
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}
	
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth/2,
				(int)y - cheight/2,
				cwidth,
				cheight
		);
		
	}
	
	public Rectangle getSpriteRectangle() {
		return new Rectangle((int)x - width/2,(int)y - height/2,width,height);
		
	}
	
	public void checkInWater(){
		inWater = false;
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                return;
        }
        
        for(int i = leftTile; i <= rightTile; i++){
        	for(int j = topTile; j <= bottomTile; j++){
        		if(tileMap.getType(j, i) == Tile.WATER){
        			inWater = true;
        			return;
        		}
        	}
        }
		
	}
	
	public void checkInLava(){
		inLava = false;
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                return;
        }
        
        for(int i = leftTile; i <= rightTile; i++){
        	for(int j = topTile; j <= bottomTile; j++){
        		if(tileMap.getType(j, i) == Tile.LAVA){
        			inLava = true;
        			return;
        		}
        	}
        }
		
	}
	
	/*
	public void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
	}
	*/
	
	public void calculateSides(double x, double y){
		topSide = bottomSide = leftSide = rightSide = false;
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topSide = bottomSide = leftSide = rightSide = false;
                return;
        }
        
        for(int i = leftTile; i <= rightTile; i++){
        	for(int j = topTile; j <= bottomTile; j++){
        		if(tileMap.getType(j, i) == Tile.BLOCKED){
        			if(i == leftTile){
        				leftSide = true;
        			}
        			if(i == rightTile){
        				rightSide = true;
        			}
        			if(j == topTile){
        				topSide = true;
        			}
        			if(j == bottomTile){
        				bottomSide = true;
        			}
        		}
        	}
        }
        
        
	}
	
	public void checkTileMapCollision() {
		collidedWithMap = false;
		collidedWithMapTop = false;
		collidedWithMapBottom = false;
		collidedWithMapLeft = false;
		collidedWithMapRight = false;
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateSides(x, ydest);
		if(dy < 0) {
			if(topSide) {
				dy = 0;
				//ytemp = currRow * tileSize + cheight / 2;
				ytemp = (currRow - (int)((cheight/2/*-1*/)/tileSize))*tileSize + cheight/2;
				collidedWithMap = true;
				collidedWithMapTop = true;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomSide) {
				dy = 0;
				falling = false;
				ytemp = (currRow + (int)((cheight/2/*-1*/)/tileSize) + 1)*tileSize - cheight/2;
				collidedWithMap = true;
				collidedWithMapBottom = true;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateSides(xdest, y);
		if(dx <= 0) {
			if(leftSide) {
				dx = 0;
				//xtemp = currCol * tileSize + cwidth / 2;
				xtemp = (currCol - (int)((cwidth/2/*-1*/)/tileSize))*tileSize + cwidth/2;
				collidedWithMap = true;
				collidedWithMapLeft = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx >= 0) {
			if(rightSide) {
				dx = 0;
				//xtemp = (currCol + 1) * tileSize - cwidth / 2;
				xtemp = (currCol + (int)((cwidth/2/*-1*/)/tileSize) + 1)*tileSize - cwidth/2;
				collidedWithMap = true;
				collidedWithMapRight = true;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateSides(x, ydest + 1);
			if(!bottomSide) {
				falling = true;
			}
		}
		
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	public Point getPos() {return new Point((int)x, (int)y);}
	public Point2D.Double getPosDouble(){return new Point2D.Double(x, y);}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	public boolean isFalling(){return falling;}
	
	public void setHitbox(int cwidth, int cheight){
		this.cwidth = cwidth;
		this.cheight = cheight;
	}
	
	public void setPosition(Point p){
		setPosition(p.getX(), p.getY());
	}
	
	public void setPosition(Point2D.Double p){
		setPosition(p.getX(), p.getY());
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setTilePosition(int x, int y){
		this.x = x*tileSize+tileSize/2.0;
		this.y = y*tileSize+tileSize/2.0;
	}
	
	/*
	public void setToWallPosition(int x, int y, int dir){ //0 = up, 1 = right etc.
		setTilePosition(x, y);
		double temp;
		if(dir == 2){
			temp = (30/2.0) - (cheight/2.0);
			setPosition(this.x, this.y+temp);
		}else if(dir == 0){
			temp = (30/2.0) - (cheight/2.0);
			setPosition(this.x, this.y-temp);
		}else if(dir == 1){
			temp = (30/2.0) - (cwidth/2.0);
			setPosition(this.x+temp, this.y);
		}else if(dir == 0){
			temp = (30/2.0) - (cwidth/2.0);
			setPosition(this.x-temp, this.y);
		}
		
	}
	*/
	
	public double calcDir(MapObject mo){
		double dx = mo.getx() - x;
		double dy = mo.gety() - y;
		return Calc.calcDir(dx, dy, Math.sqrt(dx*dx+dy*dy));
	}
	
	public void setObjectAnimation(Animation as){
		animation = as;
		width = as.getWidth();
		height = as.getHeight();
		animation.reset();
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public boolean isInsideSection(Section s){
		Rectangle r = s.getRectangle();
		r.x -= 30; //säkerhetsmarginaler
		r.y -= 90;
		r.width += 60;
		r.height += 120;
		//return intersects(r);
		
		return r.intersects(getSpriteRectangle());
	}
	
	public void setMapPosition() {
		xmap = (int)tileMap.getx();
		ymap = (int)tileMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean getUp(){return up;}
	public boolean getDown(){return down;}
	
	public void setDX(double dx){this.dx = dx;}
	public void setDY(double dy){this.dy = dy;}
	public double getDY(){return dy;}
	public double getDX(){return dx;}
	
	public void stop() {
		left = right = up = down = jumping = false;
		
		dx = 0;
		
	}
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void drawRotate(java.awt.Graphics2D g, double grader){//0 är normalt, 90 är roterad 90 grader motsols
		AffineTransform at = new AffineTransform();
		at.translate(x+xmap, y+ymap);
		at.rotate(Math.toRadians(-grader));
		at.translate(-width/2, -height/2);
		g.drawImage(animation.getImage(), at, null);
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)x + (int)xmap - width / 2,
				(int)y + (int)ymap - height / 2,
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)x + (int)xmap - width / 2 + width,
				(int)y + (int)ymap - height / 2,
				-width,
				height,
				null
			);
		}
		
		/*
		setMapPosition();
		Rectangle rect = getRectangle();
		g.setColor(Color.BLUE);
		g.drawRect(rect.x+(int)xmap, rect.y+(int)ymap, rect.width, rect.height);
		*/
		
	}
	
	
	
}

















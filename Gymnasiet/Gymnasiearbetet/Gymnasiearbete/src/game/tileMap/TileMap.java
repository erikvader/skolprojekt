package game.tileMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

import game.main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	private int intensity;
	private boolean shaking = false;
	
	// map
	private Point[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private int numTilesUpDown;
	private Tile[][] tiles;
	private int tileTypes[] = {Tile.NORMAL, Tile.BLOCKED, Tile.WATER, Tile.LAVA};
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s) {
		try {

			tileset = ImageIO.read(
				getClass().getResourceAsStream(s+"/tileset.png")
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			numTilesUpDown = tileset.getHeight() / tileSize;
			tiles = new Tile[numTilesUpDown][numTilesAcross];
			
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(s+"/links.txt")));
			
			String input = "";
			while((input = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(input);
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				int delay = Integer.parseInt(st.nextToken());
				
				tiles[y][x] = new AnimatedTile(ImageIO.read(getClass().getResourceAsStream(s+"/"+name)), tileTypes[0], delay);
			}
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				for(int row = 0; row < numTilesUpDown; row++){
					if(tiles[row][col] == null){
						subimage = tileset.getSubimage(
								col * tileSize,
								row * tileSize,
								tileSize,
								tileSize
							);
						tiles[row][col] = new Tile(subimage, tileTypes[row]);
					}else{
						tiles[row][col].setType(tileTypes[row]);
					}
					
				}
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String s) {
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new Point[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = new Point(Integer.parseInt(tokens[2*col]), Integer.parseInt(tokens[2*col+1]));
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setShaking(boolean b, int i) { shaking = b; intensity = i; }
	public boolean isShaking() { return shaking; }
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public int getType(int row, int col) {
		Point rc = map[row][col];
		return tiles[rc.y][rc.x].getType();
	}
	
	public void setTween(double d) { tween = d; }
	
	public void setPosition(double x, double y){
		setPosition(x, y, xmin, xmax, ymin, ymax);
	}
	
	public void setPosition(double x, double y, int xmin, int xmax, int ymin, int ymax) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds(xmin, xmax, ymin, ymax);
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	
	private void fixBounds(int xmin, int xmax, int ymin, int ymax) {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	
	public void setTile(int tx, int ty, Point to){
		map[ty][tx] = to;
	}
	
	public void setTile(int tx, int ty, int sx, int sy){
		setTile(tx, ty, new Point(sx, sy));
	}
	
	public void update(){
		//animations
		for(int row = 0; row < numTilesUpDown; row++){
			for(int col = 0; col < numTilesAcross; col++){
				tiles[row][col].update();
			}
		}
		
		//shaking
		if(shaking) {
			this.x += Math.random() * intensity - intensity / 2;
			this.y += Math.random() * intensity - intensity / 2;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				/*
				g.setColor(Color.RED);
				g.drawRect((int)x + col * tileSize, (int)y + row * tileSize, tileSize, tileSize);
				*/
				if(map[row][col].x == 0 && map[row][col].y == 0) continue;
				
				Point rc = map[row][col];
				
				g.drawImage(
					tiles[rc.y][rc.x].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				/*
				g.setColor(Color.RED);
				g.drawRect((int)x + col * tileSize, (int)y + row * tileSize, tileSize, tileSize);
				*/
				
			}
			
		}
		
	}
	 /*
	public int getXmin(){return xmin;}
	public void setXmin(int xmin){this.xmin = xmin;}
	public int getYmin(){return ymin;}
	public void setYmin(int ymin){this.ymin = ymin;}
	public int getXmax(){return xmax;}
	public void setXmax(int xmax){this.xmax = xmax;}
	public int getYmax(){return ymax;}
	public void setYmax(int ymax){this.ymax = ymax;}
	*/
	
}




















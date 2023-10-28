package game.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class tileMap {

	Image mapImage;
	public tileInfo[][] tiles;
	public int startTilex;
	public int startTiley;
	public int startX;
	public int startY;
	public int endTilex;
	public int endTiley;
	public Dimension imgSize;

	public void init(Image m, int sTileX, int sTileY) {
		setMapImage(m);
		setStartTile(sTileX, sTileY);
		getMapImgSize();
		initTiles();
	}

	public void printMap(Graphics g) {
		g.drawImage(mapImage, 0, 0, null);
	}

	public void setMapImage(Image i) {
		mapImage = i;
	}

	public void setStartTile(int tileX, int tileY) {
		startTilex = tileX;
		startTiley = tileY;

		startX = tileX * 60;
		startY = tileY * 60;
	}

	public void getMapImgSize() {
		ImageIcon tempImg = new ImageIcon(mapImage);
		imgSize = new Dimension(tempImg.getIconWidth(), tempImg.getIconHeight());

		endTilex = (imgSize.width / 60) - 1;
		endTiley = (imgSize.height / 60) - 1;
	}

	public void initTiles() {
		tiles = new tileInfo[endTilex + 1][endTiley + 1];
	}

	public tileInfo createTile(Boolean walkable) {
		return new tileInfo(walkable);
	}

	public void fillTiles(tileInfo t) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				setTile(x, y, t);
			}
		}
	}

	public void setTile(int x, int y, tileInfo t) {
		tiles[x][y] = t;
	}

	public tileInfo getTile(int x, int y) {
		return tiles[x][y];
	}

	public void fillSquare(int xpos, int ypos, int width, int height, tileInfo t) {
		for (int x = xpos; x < width+xpos; x++) {
			for(int y = ypos; y< height+ypos; y++){
				setTile(x, y, t);
			}
		}
	}

}

package main.maps.testMap;

import game.map.tileInfo;
import game.map.tileMap;
import java.awt.Image;
import javax.swing.ImageIcon;


public class testMap extends tileMap{
	
	Image mImage = new ImageIcon(getClass().getResource("testMap.png")).getImage();
	tileInfo grassTile;
	tileInfo rockTile;
	tileInfo waterTile;
	
	public testMap(){
		super.init(mImage, 15, 9);
		createTiles();
		setTiles();
	}
	
	public void createTiles(){
		grassTile = super.createTile(true);
		rockTile = super.createTile(false);
	}
	
	public void setTiles(){
		super.fillTiles(grassTile);
		super.setTile(16, 9, rockTile);
	}
}

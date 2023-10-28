package main.maps.startArea;

import game.map.Sign;
import game.map.tileInfo;
import game.map.tileMap;
import javax.swing.ImageIcon;


public class startArea extends tileMap{
	
	tileInfo grassTile;
	tileInfo unwalkableTile;
	tileInfo waterTile;
	tileInfo welcomeSignTile;
	Sign welcomeSign = new Sign();

	public startArea(){
		super.init(new ImageIcon(getClass().getResource("startArea.png")).getImage(), 21, 24);
		createTiles();
		setTiles();
	}

	private void setTiles() {
		super.fillTiles(grassTile);
		//sätter skyltar
		super.setTile(23, 22, welcomeSignTile);
		welcomeSign.setMessage("WELCOME!");
		welcomeSignTile.addObject(welcomeSign, "sign");
		
	}

	private void createTiles() {
		grassTile = super.createTile(true);
		unwalkableTile = super.createTile(false);
		welcomeSignTile = new tileInfo(false);
	}
	
}

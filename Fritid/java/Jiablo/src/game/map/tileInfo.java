package game.map;

import java.util.HashMap;


public class tileInfo {

	public Boolean walkable;
	HashMap<String,TileObject> objects = new HashMap<>();
	
	public tileInfo(Boolean w){
		walkable = w;
	}
	
	public Boolean getWalkable(){
		return walkable;
	}
	
	public void setWalkable(Boolean w){
		walkable = w;
	}
	
	public void addObject(TileObject obj, String name){
		objects.put(name, obj);
	}
	
	public TileObject getObject(String name){
		return objects.get(name);
	}
	
}

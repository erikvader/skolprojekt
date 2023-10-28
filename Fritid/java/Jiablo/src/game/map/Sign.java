package game.map;


public class Sign extends TileObject{

	String message;

	public void setMessage(String m){
		message = m;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}

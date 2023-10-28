package game.map;

public class TileObject {

	String name;

	public void setName(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

	// functions to be overridden by sign
	public String getMessage() {
		return "";
	}

}

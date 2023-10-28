package game.gameState;

public abstract class GameState {
	
	protected GameStateManager gsm;
	private boolean deload = false;
	
	public GameState(GameStateManager gsm){
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void activate();
	public abstract void deactivate();
	public abstract void draw(java.awt.Graphics2D g);
	//public abstract void keyPressed(int k);
	//public abstract void keyReleased(int k);
	public abstract void deload(); //rensa saker
	
	public boolean deloadPlease(){return deload;}
	protected void setDeload(boolean b){deload = b;}
	
}

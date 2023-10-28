package game.entity.level3_boss;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import game.entity.MapObject;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.tileMap.TileMap;

public class BokstavProjectile extends EnemyProjectile{

	private String character;
	private Font font;
	private int rectSize = 20;
	private Color colorBackground, colorText;
	
	private boolean isCorrect = false; //är en bokstav man behöver, roaming
	private int boardIndex = -1; //för att veta vilket index man är i på board
	private boolean intersected_traveling = false;
	
	//states
	public static final int CORRECT = 0;
	public static final int WRONG = 1;
	public static final int ROAMING = 2;
	public static final int INTERSECTED = 3;
	public static final int TO_BE_DESTROYED = 4;
	public static final int PLACEHOLDER = 5;
	public static final int TO_BE_DESTROYED_TRIGGER = 6;
	
	private int curState = ROAMING;
	
	public BokstavProjectile(TileMap tm, char character) {
		super(tm);
		animation.resetTimesPlayed(); //så att den inte försvinner direkt när hit = true
		this.character = Character.toString(character);
		font = new Font("Arial", Font.BOLD, 18);
		setColor(Color.WHITE, Color.BLACK);
		
		cwidth = width = rectSize;
		cheight = height = rectSize;
		moveSpeed = 1;
		setBounce = true;
		setHitOnCollideMap = false;
		setHitOnPlayer = false;
	}
	
	public void setColor(Color background, Color text){
		colorBackground = background;
		colorText = text;
	}
	
	public void setState(int state){
		curState = state;
		if(curState == CORRECT){
			setColor(Color.GREEN, Color.BLACK);
		}else if(curState == WRONG){
			setColor(Color.RED, Color.BLACK);
		}else if(curState == PLACEHOLDER){
			setColor(Color.DARK_GRAY, Color.LIGHT_GRAY);
		}else if(curState == TO_BE_DESTROYED || curState == TO_BE_DESTROYED_TRIGGER){
			//byt inte färg
		}else{
			setColor(Color.WHITE, Color.BLACK);
		}
	}
	
	public int getState(){return curState;}
	public char getChar(){return character.charAt(0);}
	public void setChar(char c){character = Character.toString(c);}
	public boolean isCorrect(){return isCorrect;}
	public void setCorrect(){isCorrect = true;}
	public void setBoardIndex(int i){boardIndex = i;}
	public int getBoardIndex(){return boardIndex;}
	public boolean isIntersectedTraveling(){return intersected_traveling;}
	public void setIntersectedTraveling(boolean b){intersected_traveling = b;}
	
	public void shootAtBoard(double x, double y, int index){
		shootDestination(x, y);
		setBoardIndex(index);
		intersected_traveling = true;
	}
	
	@Override
	public boolean hits(MapObject mo) {
		if(intersects(mo) && !hit){
			stop();
			setState(INTERSECTED);
			hit = true;
		}
		
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		
		//rita bakgrund
		g.setColor(colorBackground);
		g.fillRect((int)(xmap+x-width/2), (int)(ymap+y-height/2), rectSize, rectSize);
		
		g.setFont(font);
		g.setColor(colorText);
		
		//rita centrerad text
		FontMetrics metrics = g.getFontMetrics(font);
	    int x = (rectSize - metrics.stringWidth(character)) / 2;
	    int y = ((rectSize - (metrics.getHeight() - metrics.getDescent())) / 2);
	    y += metrics.getAscent() - metrics.getDescent() + 3/*lite extra*/;
	    g.drawString(character, (int)(x+this.x-rectSize/2+xmap), (int)(y+this.y-rectSize/2+ymap));
	    
	    //rita rutan
	    Stroke oldStroke = g.getStroke();
	    g.setStroke(new BasicStroke(2));
	    g.drawRect((int)(this.x-rectSize/2+xmap), (int)(this.y-rectSize/2+ymap), rectSize, rectSize);
	    g.setStroke(oldStroke);
	}
	

}

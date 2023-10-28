package game.entity.level3_boss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.entity.MapObject;
import game.entity.Player;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class TextBoard extends MapObject{
	
	private ArrayList<BokstavProjectile> ep;
	private Player player;

	private String[][] words = {
								{"OBROR", "YOLOA", "ABORRE", "USIE", "VÄLL", "SVAJPA"}, 
								{"GLAS ÖGON", "PLATT FILM", "EMBREJSA", "PARALELL"},
								{"KASSA PERSONAL", "SKUM TOMTE", "RÖK FRITT HÄR", "HERR TOALETT"},
								{"DOM DOM DOM DOM DOM DOM"}
							   };
	private int difficulty = 0, curWord = 0;
	private int curWordLength;
	
	private int bokstavSize = 20;
	
	private final int maxRoaming = 5; //hur många som ska vara ute
	private final int maxCorrect = 2; //hur många som måste vara en man vill ha
	private int curCorrect = 0; //hur många som sitter rätt _PÅ_ board
	private int curCorrectRoaming = 0; //hur många som är korrekta just nu som åker runt
	private int numPlaced = 0; //antal som sitter på board
	
	private int counter = 0;
	
	public TextBoard(TileMap tm, Player p) {
		super(tm);
		
		player = p;
		ep = new ArrayList<BokstavProjectile>();
		
		height = bokstavSize; //bokstavProjectile height
		width = bokstavSize; //-----||-------
	}
	
	private void shootBokstav(){
		int rand = ((int)(Math.random()*30)+65);
		char c;
		if(rand == 93){
			c = 'Ö';
		}else if(rand == 92){
			c = 'Ä';
		}else if(rand == 91){
			c = 'Å';
		}else if(rand == 94){
			c = ' ';
		}else{
			c = (char)rand;
		}
		
		int randAngle = (int)(Math.random()*160+190);
		
		BokstavProjectile bv = new BokstavProjectile(tileMap, c);
		bv.setPosition(x, y);
		bv.shootAngle(randAngle);
		if(curCorrectRoaming < maxCorrect){
			bv.setCorrect();
			curCorrectRoaming++;
			setCorrectLetter(bv);
		}
		ep.add(bv);
		
	}
	
	public void update(Section s){
		counter++;
		
		//System.out.println("curCorrect: "+curCorrect+" curCorrectRoaming: "+curCorrectRoaming+" numPlaced: "+numPlaced);
		
		//skjuta ut mer
		if(counter % 60 == 0){
			if(ep.size()-curWordLength-numPlaced < maxRoaming){
				shootBokstav();
			}
		}
		
		//uppdatera dem
		BokstavProjectile b;
		for(int i = 0; i < ep.size(); i++){
			b = ep.get(i);
			b.update(s);
			b.hits(player); //kollar if träffar
			
			if(b.getState() == BokstavProjectile.INTERSECTED){
				handleBokstavIntersected(b);
			}else if(b.isAtDestination() && b.isIntersectedTraveling()){ //är framme och har inte uppdaterat allt som ska tas bort
				b.setIntersectedTraveling(false);
				if(b.getState() == BokstavProjectile.CORRECT){
					curCorrect++;
				}
				if(b.getState() == BokstavProjectile.TO_BE_DESTROYED_TRIGGER){
					for(BokstavProjectile k : ep){
						if(k.getBoardIndex() == b.getBoardIndex() && (k.getState() == BokstavProjectile.TO_BE_DESTROYED || k.getState() == BokstavProjectile.TO_BE_DESTROYED_TRIGGER)){
							k.setRemove();
							numPlaced--;
						}
					}
				}
			}
			
		}
		
		//ta bort de som ska tas bort
		for(int i = 0; i < ep.size(); i++){
			b = ep.get(i);
			if(b.shouldRemove()){
				if(b.isCorrect()){
					curCorrectRoaming--;
				}
				ep.remove(i);
				i--;
			}
		}
	}
	
	private void setCorrectLetter(BokstavProjectile bp){
		int[] board = getBoardState();
		//kolla om det ens finns ledigt på board
		boolean works = false;
		for(int i = 0; i < board.length; i++){
			if(board[i] != 1){
				works = true;
				break;
			}
		}
		
		if(!works) return;
		
		//hitta en random bokstav som funkar
		int rand;
		do{
			rand = (int)(Math.random()*board.length);
		}while(board[rand] == 1);
		bp.setChar(words[difficulty][curWord].charAt(rand));
	}
	
	private void handleBokstavIntersected(BokstavProjectile bp){
		//kolla state på den nuvarande boarden
		int[] board = getBoardState();
		
		//kolla vart bp ska åka
		//vilken state bp är
		boolean bpCorrect = false;
		bp.setState(BokstavProjectile.WRONG);
		for(char c : words[difficulty][curWord].toCharArray()){
			if(bp.getChar() == c){
				bpCorrect = true;
				bp.setState(BokstavProjectile.CORRECT);
				break;
			}
		}
		
		int target = -1; //platsen vi vill till
		int prio = -1; //prio för platsen, högre är bättre
		for(int i = 0; i < board.length; i++){
			if(bpCorrect){
				if(board[i] == 0 && bp.getChar() == words[difficulty][curWord].charAt(i)){ //tom
					if(prio < 3){
						target = i;
						prio = 3;
					}
				}else if(board[i] == 2){ //wrong
					if(prio < 2){
						target = i;
						prio = 2;
					}
				}
			}else{
				if(board[i] == 0){
					if(prio < 1){
						prio = 1;
						target = i;
					}
				}
			}
		}
		
		//skjut den
		bp.setSpeed(2);
		if(target == -1){
			bp.setState(BokstavProjectile.PLACEHOLDER);
			bp.shootAngle(90);
		}else{
			numPlaced++;
			bp.shootAtBoard(getBoardX(target), y, target);
			if(prio == 2){ //båda ska förstöras
				for(BokstavProjectile j : ep){//hitta den andra
					if(j.getBoardIndex() == target && j.getState() == BokstavProjectile.WRONG/* && j.getState() != BokstavProjectile.PLACEHOLDER*/){
						j.setState(BokstavProjectile.TO_BE_DESTROYED);
					}
				}
				bp.setState(BokstavProjectile.TO_BE_DESTROYED_TRIGGER);
			}else if(prio == 3){
				curCorrectRoaming--;
			}
		}
		
	}
	
	private int[] getBoardState(){
		int[] board = new int[curWordLength]; //0 = empty, 1 = correct, 2 = wrong 
		BokstavProjectile b;
		for(int i = 0; i < ep.size(); i++){
			b = ep.get(i);
			if(b.getBoardIndex() != -1){
				if(b.getState() != BokstavProjectile.PLACEHOLDER && b.getState() != BokstavProjectile.TO_BE_DESTROYED && b.getState() != BokstavProjectile.TO_BE_DESTROYED_TRIGGER){
					board[b.getBoardIndex()] = b.getState() == BokstavProjectile.CORRECT ? 1 : 2;
				}
			}
		}
		return board;
	}
	
	public void clearBoard(){
		ep.clear();
		curCorrect = 0;
		curCorrectRoaming = 0;
		numPlaced = 0;
	}
	
	public void generateNewWord(){
		clearBoard();
		
		curWord = (int)(Math.random()*words[difficulty].length);
		curWordLength = words[difficulty][curWord].length();
		
		width = bokstavSize * curWordLength;
		
		BokstavProjectile b;
		for(int i = 0; i < curWordLength; i++){
			b = new BokstavProjectile(tileMap, words[difficulty][curWord].charAt(i));
			b.setPosition(getBoardX(i), y);
			b.setState(BokstavProjectile.PLACEHOLDER);
			b.setBoardIndex(i);
			ep.add(b);
		}
	}
	
	private double getBoardX(int i){
		return x-width/2+10+i*bokstavSize;
	}
	
	public void advanceInDifficulty(){
		difficulty++;
	}
	
	public boolean wordIsComplete(){
		return curCorrect == curWordLength;
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		
		//rita omringande ruta
		g.setColor(Color.BLACK);
		
		g.drawRect((int)(xmap+x-width/2-1), (int)(ymap+y-height/2-1), width+2, height+2);
		
		//rita bokstäverna
		for(int i = 0; i < ep.size(); i++){
			ep.get(i).draw(g);
		}
		
	}

}

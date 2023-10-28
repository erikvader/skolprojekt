
public class Card {
	
	public static final int HEARTS = 0;
	public static final int DIAMONDS = 1;
	public static final int SPADES = 2;
	public static final int CLUBS = 3;
	
	public static final int ACE = 14;
	public static final int KNIGHT = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	
	private int value, suit;
	
	public Card(int value, int suit){
		this.value = value;
		this.suit = suit;
	}
	
	public int getValue(){return value;}
	public int getSuit(){return suit;}
	
	public String toString(){
		String name = "", val;
		if(suit == HEARTS){
			name = "H";
		}else if(suit == DIAMONDS){
			name = "D";
		}else if(suit == SPADES){
			name = "S";
		}else if(suit == CLUBS){
			name = "C";
		}
		
		if(value == ACE){
			val = "A";
		}else if(value == KNIGHT){
			val = "K";
		}else if(value == QUEEN){
			val = "Q";
		}else if(value == KING){
			val = "K";
		}else{
			val = Integer.toString(value);
		}
		
		return name+""+val;
	}
	
	public String toDisplayString(){
		String name = "", val;
		if(suit == HEARTS){
			name = "Hearts";
		}else if(suit == DIAMONDS){
			name = "Diamonds";
		}else if(suit == SPADES){
			name = "Spades";
		}else if(suit == CLUBS){
			name = "Clubs";
		}
		
		if(value == ACE){
			val = "Ace";
		}else if(value == KNIGHT){
			val = "Knight";
		}else if(value == QUEEN){
			val = "Queen";
		}else if(value == KING){
			val = "King";
		}else{
			val = Integer.toString(value);
		}
		
		return val+" of "+name;
	}
}

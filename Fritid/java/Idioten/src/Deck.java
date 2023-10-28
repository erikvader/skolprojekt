public class Deck {

	private Card[] deck;
	private int cur;
	
	public Deck(){
		deck = new Card[52];
		resetDeck(true);
		
	}
	
	public void resetDeck(boolean order){
		cur = 51;
		if(order){
			for(int suit = 0; suit < 4; suit++){
				for(int val = 2; val <= 14; val++){
					deck[suit*13 + (val-2)] = new Card(val, suit);
				}
			}
		}
	}
	
	public void shuffleDeck(){
		if(cur == -1) return;
		for(int i = cur; i > 0; i--){
			int r = (int)(Math.random()*(i+1));
			Card temp = deck[r];
			deck[r] = deck[i];
			deck[i] = temp;
		}
	}
	
	public Card peek(){
		if(cur == -1) return null;
		return deck[cur];
	}
	
	public Card pop(){
		if(cur == -1) return null;
		if(cur >= 0)
			cur--;
		return deck[cur+1];
	}
	
}

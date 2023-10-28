import java.util.ArrayList;
import java.util.Stack;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	private ArrayList<Stack<Card>> piles;
	private Deck deck;
	
	private int win = 0, lose = 0;
	
	public void run(){
		piles = new ArrayList<Stack<Card> >(4);
		for(int i = 0; i < 4; i++){
			piles.add(new Stack<Card>());
		}
		deck = new Deck();
		
		
		for(int i = 0; i < 1000000; i++){
			playGame();
			System.out.println("won: "+win+", lost: "+lose);
		}
		System.out.println("win percentage: "+(double)(100*win)/(win+lose)+"%");
	}
	
	public void playGame(){
		//init spel
		for(int i = 0; i < 4; i++){
			piles.get(i).clear();
		}
		
		deck.resetDeck(false);
		deck.shuffleDeck();
		
		for(int i = 0; i < 13; i++){
			newRound();
			
			eliminateAll(piles);
			fillHoles(); //fyller alla hål så att flest kort försvinner samt applyar det.
			
		}
		
		//kolla vinst
		int count = 0;
		for(int i = 0; i < 4; i++){
			if(piles.get(i).size() == 1 && piles.get(i).peek().getValue() == Card.ACE){
				count++;
			}
		}
		
		if(count == 4){
			win++;
		}else{
			lose++;
		}
		
	}
	
	public void fillHoles(){
		ArrayList<Stack<Card>> copy = new ArrayList<Stack<Card>>(piles);
		ArrayList<Stack<Card>> best = copy;
		holeRecurs(copy, best);
		piles = best;
	}
	
	public void holeRecurs(ArrayList<Stack<Card>> copy, ArrayList<Stack<Card>> best){
		for(int i = 0; i < 4; i++){ //hitta alla hål
			if(copy.get(i).size() == 0){
				for(int j = 0; j < 4; j++){ //hitta en hög att ta ifrån
					if(copy.get(j).size() > 1){
						ArrayList<Stack<Card>> copyCopy = new ArrayList<Stack<Card>>(copy);
						copyCopy.get(i).push(copyCopy.get(j).pop());
						eliminateAll(copyCopy);
						if(getPileSize(copyCopy) < getPileSize(best)){
							best = copyCopy;
						}
						holeRecurs(copyCopy, best);
					}
				}
			}
		}
	}
	
	public boolean hasHoles(){
		for(int i = 0; i < 4; i++){
			if(piles.get(i).size() == 0) return true;
		}
		
		return false;
	}
	
	public void eliminateAll(ArrayList<Stack<Card>> piles){
		boolean running = true;
		while(running){
			int spades = 0, hearts = 0, clubs = 0, diamonds = 0;
			//hitta högsta av varje
			for(int i = 0; i < 4; i++){
				if(piles.get(i).size() == 0) continue;
				Card c = piles.get(i).peek();
				if(c.getSuit() == Card.HEARTS && c.getValue() > hearts){
					hearts = c.getValue();
				}else if(c.getSuit() == Card.CLUBS && c.getValue() > clubs){
					clubs = c.getValue();
				}else if(c.getSuit() == Card.DIAMONDS && c.getValue() > diamonds){
					diamonds = c.getValue();
				}else if(c.getSuit() == Card.SPADES && c.getValue() > spades){
					spades = c.getValue();
				}
			}
			
			//eliminera
			running = false;
			for(int i = 0; i < 4; i++){
				if(piles.get(i).size() == 0) continue;
				Card c = piles.get(i).peek();
				if((c.getSuit() == Card.HEARTS && c.getValue() < hearts) ||
						(c.getSuit() == Card.CLUBS && c.getValue() < clubs) ||
						(c.getSuit() == Card.DIAMONDS && c.getValue() < diamonds) ||
						(c.getSuit() == Card.SPADES && c.getValue() < spades)
					){
					piles.get(i).pop();
					running = true;
				}
			}
		}
	}
	
	public void newRound(){
		for(int i = 0; i < 4; i++){
			piles.get(i).push(deck.pop());
		}
	}
	
	public int getPileSize(ArrayList<Stack<Card>> piles){
		int result = 0;
		for(int i = 0; i < piles.size(); i++){
			result += piles.get(i).size();
		}
		return result;
	}

}

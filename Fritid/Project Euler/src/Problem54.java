import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Problem54 {

	public static void main(String[] args) {
		new Problem54().run();
	}
	
	public HashMap<Character, Integer> cardValue;
	
	public void run(){
		cardValue = new HashMap<Character, Integer>();
		cardValue.put('2', 2);
		cardValue.put('3', 3);
		cardValue.put('4', 4);
		cardValue.put('5', 5);
		cardValue.put('6', 6);
		cardValue.put('7', 7);
		cardValue.put('8', 8);
		cardValue.put('9', 9);
		cardValue.put('T', 10);
		cardValue.put('J', 11);
		cardValue.put('Q', 12);
		cardValue.put('K', 13);
		cardValue.put('A', 14);
		
		int pla1 = 0;
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < 1000; i++){
			String[] p1 = new String[5];
			String[] p2 = new String[5];
			String line = scan.nextLine();
			for(int j = 0; j < 5; j++){
				p1[j] = line.substring(j*3, j*3+2);
			}
			for(int j = 0; j < 5; j++){
				p2[j] = line.substring(15+j*3, 15+j*3+2);
			}
			
			PokerHand h1 = new PokerHand(p1);
			PokerHand h2 = new PokerHand(p2);
			
			h1.evaluate();
			h2.evaluate();
			
			if(h1.compare(h2) == -1){
				pla1++;
			}
		}
		
		scan.close();
		System.out.println(pla1);

	}
	
	public class PokerHand{
		
		private String[] kort;
		
		/*
		 * 0 = high card
		 * 1 = one pair
		 * 2 = teo pairs
		 * 3 = tripple
		 * 4 = straight
		 * 5 = flush
		 * 6 = hus
		 * 7 = fyrtal
		 * 8 = straight flush
		 * 9 = royal flush
		 */
		private int kind = 0;
		
		private ArrayList<Integer> compareValues;
		
		public PokerHand(String[] kort){
			this.kort = kort;
			compareValues = new ArrayList<Integer>();
			sortHand();
		}
		
		private void sortHand(){
			for(int i = kort.length-1; i > 0; i--){
				int smallestIndex = -1;
				int smallestValue = 15;
				int temp;
				for(int j = 0; j <= i; j++){
					temp = getValue(kort[j]);
					if(temp < smallestValue){
						smallestValue = temp;
						smallestIndex = j;
					}
				}
				String byta = kort[i];
				kort[i] = kort[smallestIndex];
				kort[smallestIndex] = byta;
			}
		}
		
		private int getValue(String c){
			return cardValue.get(c.charAt(0));
		}
		
		private char getSuit(String c){
			return c.charAt(1);
		}
		
		public void evaluate(){
			compareValues.clear();
			boolean flush = true;
			boolean straight = true;
			int[] counts = new int[13];
			int pair1 = -1;
			int pair2 = -1;
			int three = -1;
			int four = -1;
			
			for(int i = 0; i < 5; i++){
				//flush
				if(flush && getSuit(kort[0]) != getSuit(kort[i])){
					flush = false;
				}
				
				//straight
				if(i > 0){
					if(straight && getValue(kort[i-1])-1 != getValue(kort[i])){
						straight = false;
					}
				}
				
				//counts
				counts[getValue(kort[i])-2]++;
			}
			
			for(int i = 0; i < counts.length; i++){
				if(counts[i] == 4){
					four = i+2;
				}else if(counts[i] == 3){
					three = i+2;
				}else if(counts[i] == 2){
					if(pair1 == -1){
						pair1 = i+2;
					}else{
						pair2 = i+2;
					}
				}
			}
			
			if(getValue(kort[0]) == cardValue.get('A') && straight && flush){
				kind = 9;
			}else if(straight & flush){
				kind = 8;
				//compareValues.add(getValue(kort[0]));
			}else if(four >= 2){
				kind = 7;
				compareValues.add(four);
			}else if(three >= 2 && pair1 >= 2){
				kind = 6;
				compareValues.add(three);
				compareValues.add(pair1);
			}else if(flush){
				kind = 5;
			}else if(straight){
				kind = 4;
			}else if(three >= 2){
				kind = 3;
				compareValues.add(three);
			}else if(pair2 >= 2){
				kind = 2;
				compareValues.add(pair2);
				compareValues.add(pair1);
			}else if(pair1 >= 2){
				kind = 1;
				compareValues.add(pair1);
			}
			
			for(int i = counts.length-1; i >= 0; i--){
				if(counts[i] > 0 && !compareValues.contains(i+2)){
					compareValues.add(i+2);
				}
			}
			
		}
		
		/**
		 * -1 ifall denna vinner, 0 ifall det blir oavgjort eller 1 ifall other vinner.
		 * @param other
		 * @return
		 */
		public int compare(PokerHand other){
			if(kind > other.kind){
				return -1;
			}else if(kind < other.kind){
				return 1;
			}else{
				for(int i = 0; i < compareValues.size() && i < other.compareValues.size(); i++){
					if(compareValues.get(i) > other.compareValues.get(i)){
						return -1;
					}else if(compareValues.get(i) < other.compareValues.get(i)){
						return 1;
					}
				}
				
				if(compareValues.size() > other.compareValues.size()){
					return -1;
				}else if(compareValues.size() < other.compareValues.size()){
					return 1;
				}
			}
			
			return 0;
		}
	}

}

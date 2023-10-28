import java.util.Scanner;

public class Bokstavstarningar {

	public static void main(String[] args) {
		new Bokstavstarningar().run();
	}
	
	int numDice, numSides, numWords;
	
	char[][] dices;
	//char[][] words;

	boolean[] used;
	int svar = 0;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		
		numDice = scan.nextInt();
		numSides = scan.nextInt();
		numWords = scan.nextInt();
		scan.nextLine();
		
		dices = new char[numDice][numSides];
		
		String input;
		for(int i = 0; i < numDice; i++){
			input = scan.nextLine();
			for(int j = 0; j < numSides; j++){
				dices[i][j] = input.charAt(j);
			}
		}

		used = new boolean[numDice];
		//words = new char[numWords][numDice];
		for(int i = 0; i < numWords; i++){
			input = scan.nextLine();
			//Arrays.fill(used, false);
			recurs(input, 0);
			/*
			for(int j = 0; j < numDice; j++){
				words[i][j] = input.charAt(j);
			}
			*/
		}
		
		scan.close();
		
		//solvera();
		
		System.out.println(svar);
	}
	
	/*
	public void solvera(){
		for(int i = 0; i < numWords; i++){
			
		}
	}
	*/
	
	public boolean recurs(String word, int dice){
		if(dice == numDice){
			/*
			for(boolean b : used){
				if(!b) return false;
			}
			*/
			
			svar++;
			return true;
		}
		
		boolean works = false;
		for(int i = 0; i < numDice; i++){ //alla chars i ord
			if(works) break;
			if(!used[i]){
				for(int j = 0; j < numSides; j++){ //alla chars i dice
					if(word.charAt(i) == dices[dice][j]){
						used[i] = true;
						works = recurs(word, dice+1);
						used[i] = false;
						break;
					}
				}
			}
		}
		
		return works;
		
	}

}

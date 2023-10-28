package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Main {

	
	public static void main(String[] args) {
		new Main().run();
	}
	
	int[] board;
	int[] ordning;
	boolean searching = true;
	
	public void run(){
		board = new int[81];
		
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < 81; i++)
			board[i] = scan.nextInt();
		
		scan.close();
		
		solve();
	}
	
	public void solve(){
		ArrayList<numberCombinationClass> mojliga = new ArrayList<>();
		
		for(int i = 0; i < 81; i++){
			int combs = 0;
			if(board[i] == 0){
				for(int num = 1; num <= 9; num++){
					if(validPlacement(board, num, i)){
						combs++;
					}
				}
				mojliga.add(new numberCombinationClass(i, combs));
			}
		}
		
		Collections.sort(mojliga);
		
		ordning = new int[mojliga.size()];
		for(int i = 0; i < ordning.length; i++){
			ordning[i] = mojliga.get(i).index;
		}
		
		//börja lösningen
		
		recurs(0);
		
		for(int i = 0; i < 81; i++){
			if(i%9==0)
				System.out.println();
			
			System.out.print(board[i]+" ");
		}
		
	}
	
	public void recurs(int ordningIndex){
		for(int i = 1; i <= 9; i++){
			if(searching){
				if(validPlacement(board, i, ordning[ordningIndex])){
					board[ordning[ordningIndex]] = i;
					if(ordningIndex == ordning.length-1){
						searching = false;
						
					}else{
						recurs(ordningIndex+1);
					}
				}
			}else{
				return;
			}
			
		}
		if(searching) board[ordning[ordningIndex]] = 0;
	}
	
	private class numberCombinationClass implements Comparable<numberCombinationClass>{
		int index;
		int combinations;
		
		public numberCombinationClass(int i, int c){
			index = i;
			combinations = c;
		}

		@Override
		public int compareTo(numberCombinationClass other) {
			if(combinations < other.combinations)
				return -1;
			if(combinations > other.combinations)
				return 1;
			
			return 0;
		}
	}
	
	public boolean validPlacement(int[] board, int number, int index){
		int[] tempBoard = board.clone();
		tempBoard[index] = number;
		
		int row = index / 9;
		int startIndex = row*9;
		for(int i = startIndex; i < startIndex+9; i++){
			if(tempBoard[i] == number && index != i) return false;
		}
		
		
		int col = index%9;
		startIndex = col;
		for(int i = startIndex; i <= 72+startIndex; i+=9){
			if(tempBoard[i] == number && index != i) return false;
		}
		
		
		startIndex = (col/3)*3 + (row/3)*27;
		int i = startIndex;
		int j = 0;
		while(i <= startIndex+20){
			if(tempBoard[i] == number && index != i) return false;
			
			if(j%3 == 2){
				i+=7;
			}else{
				i++;
			}
			
			j++;
		}
		
		
		return true;
		
	}

}

package main;

import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	Item[] allItems;
	int[] avaibleItems;
	int knapsackMax;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		String input;
		System.out.print("Maximum weight: ");
		knapsackMax = scan.nextInt();
		System.out.print("Amount of items: ");
		input = scan.next();
		int temp = Integer.parseInt(input);
		allItems = new Item[temp];
		avaibleItems = new int[temp];
		
		for(int i = 0; i < temp; i++){
			System.out.print((i+1)+" Value, Weight, amount: ");
			allItems[i] = new Item(scan.nextInt(), scan.nextInt());
			avaibleItems[i] = scan.nextInt();
		}
		
		scan.close();
		
		ItemCol best = knapsack();
		
		System.out.println("Den bästa väger " + best.getTotalWeight() + " och är värd " + best.getTotalValue()+".");
		//System.out.println(best);
	}
	
	public ItemCol knapsack(){
		ItemCol[] array = new ItemCol[knapsackMax+1];
		
		array[0] = new ItemCol();
		
		for(int ite = 0; ite < allItems.length; ite++){
			for(int i = 0; i < avaibleItems[ite]; i++){
				for(int j = 0; j < array.length; j++){
					if(array[j] != null){
						int newj = allItems[ite].getWeight()+j;
						if(newj >= array.length) continue;
						ItemCol newCol = new ItemCol(array[j]);
						newCol.add(allItems[ite]);
						if(array[newj] == null){
							array[newj] = new ItemCol(newCol);
						}else{
							if(array[newj].getTotalValue() < newCol.getTotalValue()){
								array[newj] = newCol;
							}
						}
					}
				}
			}
		}
		
		int best = -1;
		for(int i = 0; i < array.length; i++){
			if(array[i] != null){
				if(best == -1) best = i;
				else{
					if(array[best].getTotalValue() < array[i].getTotalValue()){
						best = i;
					}
				}
			}
		}
		
		return array[best];
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

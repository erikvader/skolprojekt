package main;

import java.util.ArrayList;
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
		ItemCol[][] grid = new ItemCol[knapsackMax+1][allItems.length+1]; //x, y
		
		//fyll alla x=0 med 0 och sedan alla y=0 med 0
		ItemCol nolla = new ItemCol(0, new ArrayList<Item>());
		for(int y = 0; y < grid[0].length; y++){
			grid[0][y] = nolla;
			//grid[y][0] = nolla;
		}
		
		for(int i = 0; i < allItems.length; i++){//i = onItem nummer...
			for(int col = 0; col < grid.length; col++){
				if(grid[col][i] != null){
					ItemCol cur = grid[col][i];
					for(int am = 0; am < avaibleItems[i]; am++){//available
						ItemCol newOne = new ItemCol(cur);
						for(int a = 0; a < am+1; a++){
							newOne.add(allItems[i]);
						}
						int newX = col+allItems[i].getWeight()*(am+1);
						if(newX < grid.length){
							if(grid[newX][i+1] != null){
								if(grid[newX][i+1].getTotalValue() < newOne.getTotalValue()){
									grid[newX][i+1] = newOne;
								}
							}else{
								grid[newX][i+1] = newOne;
							}
						}
					}
				}
			}
			//flytta ner värden
			if(i < allItems.length-1){ //inte på den sista
				for(int col = 0; col < grid.length; col++){
					if(grid[col][i] != null){
						if(grid[col][i+1] != null){
							if(grid[col][i+1].getTotalValue() < grid[col][i].getTotalValue()){
								grid[col][i+1] = grid[col][i];
							}
						}else{
							grid[col][i+1] = grid[col][i];
						}
					}
				}
			}
		}
		
		//hitta den bästa
		ItemCol theBest = nolla;
		for(int i = 0; i < grid.length; i++){
			if(grid[i][grid[0].length-1] != null){
				if(grid[i][grid[0].length-1].getTotalValue() > theBest.getTotalValue()){
					theBest = grid[i][grid[0].length-1];
				}
			}
		}
		
		return theBest;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

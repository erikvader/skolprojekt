package longest_increasing_subsequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * stryker tal i en sekvens så att en sån lång stigande sekvens som möjligt är kvar.
 * ex.
 * 5 1 4 7 3 3 6 ger 1 3 3 6
 * 
 * @author Erik Rimskog
 *
 */
public class LongestIncreasing {

	public static void main(String[] args) {
		System.out.println("Skriv en sekvens av heltal på en rad ty:");
		Scanner scan = new Scanner(System.in);
		String[] input = scan.nextLine().split(" ");
		int[] numbers = new int[input.length];
		for(int i = 0; i < input.length; i++){
			numbers[i] = Integer.parseInt(input[i]);
		}
		scan.close();
		
		int[] result = increasing(numbers);
		System.out.println("Längsta ökande sekvensen: ");
		for(int a : result)
			System.out.print(a+" ");
		
	}
	
	public static int[] increasing(int[] tal){
		int best = 0;
		ArrayList<LinkedList<Integer>> sub = new ArrayList<LinkedList<Integer>>();
		for(int i = 0; i < tal.length+1; i++){
			sub.add(new LinkedList<Integer>());
			sub.get(i).add(0);
		}
		
		for(int k = 0; k < tal.length; k++){
			int i = best;
			while(i > 0 && sub.get(i).getLast() > tal[k]){
				i--;
			}
			LinkedList<Integer> temp = new LinkedList<Integer>(sub.get(i));
			temp.add(tal[k]);
			sub.set(i+1, temp);
			if(i == best){
				best++;
			}
		}
		
		int[] re = new int[sub.get(best).size()-1];
		int fittTal = 0;
		Iterator<Integer> ite = sub.get(best).iterator();
		ite.next();
		while(ite.hasNext()){
			re[fittTal] = ite.next();
			fittTal++;
		}
		
		return re;
	}

}

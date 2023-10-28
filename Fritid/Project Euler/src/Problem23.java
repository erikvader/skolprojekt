import java.util.ArrayList;

import isPrime.Prime;

public class Problem23 {

	public static ArrayList<Integer> abundant; 
	public static int limit = 28123;
	
	
	public static void main(String[] args) {
		abundant = new ArrayList<Integer>();
		ArrayList<Integer> primes = Prime.ESieve(limit+1);
		
		//hitta alla abundat till limit
		for(int i = 12; i <= limit; i++){
			ArrayList<Integer> divisors = Prime.getDivisors(Prime.primeFactors(i, primes));
			int sum = 0;
			for(int j = 0; j < divisors.size()-1; j++){
				sum += divisors.get(j);
			}
			if(sum > i)
				abundant.add(i);
		}
		
		//hitta alla som inte kan skrivas som en summa av abundant
		boolean[] marked = new boolean[limit+1];
		int a, b, c;
		for(int i = 0; i < abundant.size(); i++){
			a = abundant.get(i);
			for(int j = i; j < abundant.size(); j++){
				b = abundant.get(j);
				c = a+b;
				if(c > limit) break;
				marked[c] = true;
			}
		}
		
		//summara
		int total = 0;
		for(int i = 1; i < marked.length; i++){
			if(!marked[i])
				total += i;
		}
		System.out.println(total);
	}

}

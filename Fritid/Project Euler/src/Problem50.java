import java.util.ArrayList;

import isPrime.Prime;

public class Problem50 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		ArrayList<Integer> primes = Prime.ESieve(1000000);
		
		int summa = 0;
		int bestLength = 0;
		int bestPrime = 0;
		for(int i = 0; i < primes.size(); i++){
			summa = 0;
			for(int j = i; j >= 0; j--){
				summa += primes.get(j);
				if(summa >= 1000000) break;
				if(Prime.isPrimeDumb(summa, primes)){
					if(i-j+1 >= bestLength){
						bestLength = i-j+1;
						bestPrime = summa;
					}
				}
			}
		}
		
		System.out.println(bestPrime + " " + bestLength);
		System.out.println((System.nanoTime() - t)/1000000.0);
	}

}

import java.util.ArrayList;

import isPrime.Prime;

public class Problem27 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		ArrayList<Integer> primes = Prime.ESieve(10000);

		int bestProdukt = 0, bestLength = 0;
		for(int a = -999; a < 1000; a++){
			for(int b = -999; b < 1000; b++){
				int p, n = -1;
				do{
					n++;
					p = n*n + n*a + b;
				}while(Prime.isPrimeDumb(p, primes));
				
				if(n > bestLength){
					bestLength = n;
					bestProdukt = a*b;
				}
			}	
		}
		
		System.out.println(bestProdukt + " " + bestLength);
		System.out.println((System.nanoTime()-t)/1000000.0);
	}

}

import java.util.ArrayList;

import funktioner.Funktioner;
import isPrime.Prime;

public class Problem37 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		new Problem37().run();
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	ArrayList<Integer> primes;
	
	public void run(){
		primes = Prime.ESieve(1000000);
		int found = 0;
		int sum = 0;
		for(int i = 4; i < primes.size() && found < 11; i++){
			if(isTrunc(primes.get(i))){
				found++;
				sum += primes.get(i);
			}
		}
		System.out.println("summan: "+sum+" av "+found+" tal");
	}
	
	public boolean isTrunc(int a){
		int a2 = 0;
		int pos = 1;
		while(a > 9){
			a2 += pos * (a % 10);
			pos *= 10;
			a /= 10;
			
			if(!Prime.isPrimeDumb(a, primes) || !Prime.isPrimeDumb(a2, primes)){
				return false;
			}
		}
		
		return true;
	}
	
	public ArrayList<Integer> ESieve(int limit){
		boolean[] marked = new boolean[limit];
		ArrayList<Integer> primes = new ArrayList<Integer>();
		for(int i = 2; i < limit; i++){
			if(marked[i]) continue;
			if(i*i <= limit){ //av någon anledning
				for(int j = 2*i; j < limit; j+=i){
					marked[j] = true;
				}
			}
			if(Funktioner.containsOnlyOdd(i) || i == 2)
				primes.add(i);
		}
		return primes;
	}

}

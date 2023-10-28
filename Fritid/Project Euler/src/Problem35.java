import java.util.ArrayList;
import java.util.HashSet;

import isPrime.Prime;

public class Problem35 {

	HashSet<Integer> visited;
	ArrayList<Integer> primes;
	int antal = 0;
	
	public static void main(String[] args) {
		long t = System.nanoTime();
		new Problem35().run();
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	public void run(){
		visited = new HashSet<Integer>();
		primes = ESieve(1000000);
		
		for(int i = 0; i < primes.size(); i++){
			if(!visited.contains(primes.get(i))){
				checkIfRotaryPrime(primes.get(i));
			}
		}
		
		System.out.println(antal);
	}
	
	public void checkIfRotaryPrime(int p){
		HashSet<Integer> rotations = new HashSet<Integer>();
		rotations.add(p);
		int length = 0;
		int pp = p;
		while(pp > 0){
			pp /= 10;
			length++;
		}
		
		for(int i = 0; i < length-1; i++){
			p = rotateNumber(p, length);
			if(Prime.isPrimeDumb(p, primes)){
				rotations.add(p);
			}else{
				return;
			}
		}
		
		antal += rotations.size();
		
		for(Integer a : rotations)
			visited.add(a);
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
			if(containsOnlyOdd(i) || i == 2)
				primes.add(i);
		}
		return primes;
	}
	
	/**
	 * Kollar ifall ett tal endast innehåller udda siffror
	 * @param i
	 * @return
	 */
	public static boolean containsOnlyOdd(int i){
		while(i > 0){
			if((i % 10) % 2 == 0){
				return false;
			}
			i /= 10;
		}
		return true;
	}
	
	/**
	 * roterar ett nummer n åt vänster
	 * <br>
	 * 123 = 231
	 * @param n
	 * @return
	 */
	public static int rotateNumber(int n, int length){
		if(n < 10 || length < 2) return n;
		int left = n;
		int remove = 1;
		for(int i = 0; i < length-1; i++){
			left /= 10;
			remove *= 10;
		}
		n -= remove*left;
		n *= 10;
		return n + left;
	}

}

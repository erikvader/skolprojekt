import java.util.ArrayList;
import java.util.HashSet;

import isPrime.Prime;

public class Problem47 {

	public static void main(String[] args) {
		int upperLimit = 1000000;
		ArrayList<Integer> primes = Prime.ESieve(upperLimit);
		
		int first = 0, count = 0;
		
		for(int i = 1; i <= upperLimit; i++){
			if(primeFactors(i, primes).size() == 4){
				count++;
				if(count == 1){
					first = i;
				}else if(count == 4){
					break;
				}
			}else{
				count = 0;
			}
		}
		
		System.out.println(count+" "+first);
	}
	
	public static HashSet<Integer> primeFactors(int p, ArrayList<Integer> primes){
		HashSet<Integer> facts = new HashSet<Integer>();
		if(p == 1){
			facts.add(p);
			return facts;
		}
		
		int pp = p;
		int index = 0;
		int dela = primes.get(0);
		while(dela*dela <= p){
			if(pp % dela == 0){
				pp /= dela;
				facts.add(dela);
			}else{
				index++;
				dela = primes.get(index);
			}
		}
		
		if(pp != 1)
			facts.add(pp);
		
		return facts;
	}

}

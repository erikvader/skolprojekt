import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import isPrime.Prime;

public class Problem29 {

	public static void main(String[] args) {
		//doubleSolution();
		stringSolution();
	}
	
	/**
	 * Tar exponenten och försöker att dela upp den så mycket som möjligt. Den splittar upp b
	 * till primtal samt försöker ta ut alla primtalsexponenter ur a genom att dra olika sorters roten ur.
	 */
	public static void stringSolution(){
		ArrayList<Integer> primes = Prime.ESieve(151);
		HashSet<String> hash = new HashSet<String>();
		for(int a = 2; a <= 100; a++){
			for(int b = 2; b <= 100; b++){
				hash.add(simplify(a, b, primes));
			}
		}
		System.out.println(hash.size());
	}
	
	public static String simplify(int a, int b, ArrayList<Integer> primes){
		ArrayList<Integer> exponenter = Prime.primeFactors(b, primes);
		
		//hitta grejer från a
		double aa = a;
		int cur = 0;
		int curPrime = primes.get(cur);
		while(curPrime < aa){
			double temp = Math.pow(aa, 1.0/curPrime);
			if(temp % 1 == 0){
				exponenter.add(curPrime);
				aa = temp;
			}else{
				cur++;
				if(cur == primes.size()){
					System.out.println("den failade på a: "+a+", b: "+b+", aa: "+aa+", prime: "+curPrime);
					System.exit(0);
				}
				curPrime = primes.get(cur);
			}
		}
		
		//slå ihop allt till en String
		String ans = "";
		Collections.sort(exponenter);
		ans += aa+"^";
		for(int i = 0; i < exponenter.size(); i++){
			ans += exponenter.get(i)+"*";
		}
		
		return ans;
	}
	
	public static void doubleSolution(){
		HashSet<Double> hash = new HashSet<Double>();
		for(int a = 2; a <= 100; a++){
			for(int b = 2; b <= 100; b++){
				hash.add(Math.pow(a, b));
			}
		}
		System.out.println(hash.size());
	}

	
}

package isPrime;

import java.util.ArrayList;
import java.util.Collections;

import funktioner.Funktioner;

public class Prime {
	
	/**
	 * Hur många gånger isPrime(int) ska testa primtalet.
	 */
	public static int isPrimeCycles = 20;

	/**
	 * använder sig av Fermats lilla sats för att kolla om p är ett primtal.
	 * <p>
	 * Det är som max(?) 0.5^isPrimeCycles att ett oprim tros vara ett primtal.
	 * <p>
	 * För stora tal är det en större risk att den säger att det är ett primtal även fast det inte är det
	 * 
	 * @param p talet att testa
	 * @return ifall p är ett primtal (med stor sannolikhet)
	 */
	public static boolean isPrime(int p){
		
		if(p == 2) return true;
		if(p < 2) return false;
		
		for(int i = 0; i < isPrimeCycles; i++){
			int rand = (int)(Math.random()*(p-1)+1);
			
			if(Funktioner.GCD(rand, p) != 1){
				return false;
			}
			
			if(Funktioner.bigExponentModulos(rand, p-1, p) != 1){
				return false;
			}
			
		}
		
		return true;
		
	}
	
	/**
	 * En lite dummare metod för att kolla ifall något är ett primtal. Denna har dock aldrig fel.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isPrimeDumb(int p){
		return isPrimeDumb((long)p);
	}
	
	/**
	 * En lite dummare metod för att kolla ifall något är ett primtal. Denna har dock aldrig fel.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isPrimeDumb(long p){
		if(p == 2) return true;
		if(p < 2) return false;
		if(p % 2 == 0) return false;
		double sq = Math.sqrt(p);
		for(long i = 3; i <= sq; i+=2){
			if(p % i == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Binärsöker efter primtalet hos en lista med primtal
	 * @param p
	 * @param primes
	 * @return
	 */
	public static boolean isPrimeDumb(int p, ArrayList<Integer> primes){
		if(p < 2) return false;
		int l = 0, r = primes.size()-1, m, k;
		while(l <= r){
			m = (r+l)/2;
			k = primes.get(m);
			if(k == p){
				return true;
			}else if(k < p){
				l = m+1;
			}else{
				r = m-1;
			}
		}
		return false;
	}
	
	/**
	 * En lite dummare metod för att kolla ifall något är ett primtal. Denna har dock aldrig fel.
	 * <br>
	 * Denna använder en lista med primtal för att testa som faktorer.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isPrimeDumbSmarter(long p, ArrayList<Integer> primes){
		if(p == 2) return true;
		if(p < 2) return false;
		double sq = Math.sqrt(p);
		int prime;
		for(int i = 0; i < primes.size(); i++){
			prime = primes.get(i);
			if(prime > sq) break;
			if(p % prime == 0){
				return false;
			}
		}
		return true;
	}
	
	public static ArrayList<Long> primeFactors(long p){
		ArrayList<Long> facts = new ArrayList<Long>();
		if(p == 1){
			facts.add(p);
			return facts;
		}
		
		long pp = p;
		long dela = 2;
		while(dela*dela <= p){
			if(pp % dela == 0){
				pp /= dela;
				facts.add(dela);
			}else{
				dela += dela == 2 ? 1 : 2;
			}
		}
		
		if(pp != 1)
			facts.add(pp);
		
		return facts;
	}
	
	public static ArrayList<Integer> primeFactors(int p){
		ArrayList<Long> temp = primeFactors((long)p);
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(long l : temp)
			res.add((int)l);
		return res;
	}
	
	public static ArrayList<Integer> primeFactors(int p, ArrayList<Integer> primes){
		ArrayList<Integer> facts = new ArrayList<Integer>();
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
	
	/**
	 * Sieve of Eratosthenes
	 * <p>
	 * Hittar primtal MINDRE än limit
	 * @param limit 
	 * @return
	 */
	public static ArrayList<Integer> ESieve(int limit){
		boolean[] marked = new boolean[limit];
		ArrayList<Integer> primes = new ArrayList<Integer>();
		for(int i = 2; i < limit; i++){
			if(marked[i]) continue;
			if(i*i <= limit){ //av någon anledning
				for(int j = 2*i; j < limit; j+=i){
					marked[j] = true;
				}
			}
			primes.add(i);
		}
		return primes;
	}
	
	/**
	 * Genererar alla delare för ett tal utifrån dess primtalfaktorer
	 * @param primeFactors
	 * @return
	 */
	public static ArrayList<Integer> getDivisors(final ArrayList<Integer> primeFactors){
		ArrayList<Integer> sorted = new ArrayList<>(primeFactors);
		Collections.sort(sorted);
		ArrayList<Integer> answer = new ArrayList<Integer>();
		answer.add(1);
		
		int max = 1;
		int last = -1;
		int cur = -1;
		for(int i = 0; i < sorted.size(); i++){
			if(last == sorted.get(i)){
				cur *= sorted.get(i);
			}else{
				max = answer.size();
				cur = sorted.get(i);
			}
			for(int j = 0; j < answer.size() && j < max; j++){
				answer.add(answer.get(j)*cur);
			}
			last = sorted.get(i);
		}
		
		return answer;
	}
	
}

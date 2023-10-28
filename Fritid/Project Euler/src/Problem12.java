import java.util.ArrayList;

import isPrime.Prime;

public class Problem12 {

	public static void main(String[] args) {
		//searchNumbers();
		//searchSmarter();
	}
	
	public static void searchSmarter(){
		ArrayList<Integer> primes = Prime.ESieve(10000);
		boolean running = true;
		int summa = 0;
		int cur = 1;
		while(running){
			summa += cur;
			cur++;
			int d = divisorsSmarter(summa, primes);
			System.out.println("tal: "+summa+", divisors: "+d);
			if(d > 500){
				System.out.println("svaret är: "+summa);
				running = false;
			}
		}
	}
	
	//hittar alla primtalsfaktorer och kollar sen hur många olika unika tal man kan bilda ifrån dem.
	//Om man har n st av ett tal så kan man ha med 0, 1, 2... n olika av det talet när man multiplicerar.
	public static int divisorsSmarter(int tal, ArrayList<Integer> pri){
		ArrayList<Integer> primes = Prime.primeFactors(tal, pri);
		int svar = 1;
		long last = primes.get(0);
		int antal = 1;
		for(int i = 1; i < primes.size(); i++){
			if(primes.get(i) == last){
				antal++;
			}else{
				svar *= antal+1;
				last = primes.get(i);
				antal = 1;
			}
		}
		svar *= antal+1;
		return svar;
	}
	
	//76576500
	public static void searchNumbers(){
		boolean running = true;
		int summa = 0;
		int cur = 1;
		while(running){
			summa += cur;
			cur++;
			int d = divisors(summa);
			System.out.println("tal: "+summa+", divisors: "+d);
			if(d > 500){
				System.out.println("svaret är: "+summa);
				running = false;
			}
		}
	}
	
	//testar alla delare upp till sqrt
	public static int divisors(int s){
		int t = 0;
		for(int i = 1; i*i <= s; i++){
			if(s % i == 0) t+=2; //om mna hittar den delare så finns det ett par till den, så man behöver inte leta feter roten ur
		}
		if(Math.sqrt(s) % 1 == 0){
			t--;
		}
		return t;
	}

}

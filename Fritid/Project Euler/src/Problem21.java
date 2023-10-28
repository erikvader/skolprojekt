import java.util.ArrayList;

import isPrime.Prime;

public class Problem21 {

	public static ArrayList<Integer> primes = Prime.ESieve(10000);
	public static boolean[] checked = new boolean[10000];
	
	public static void main(String[] args) {
		int sum = 0;
		for(int a = 1; a < 10000; a++){
			if(checked[a]) continue;
			int b = d(a);
			int c = d(b); //c = a
			if(c == a && a != b){
				if(a < 10000){
					checked[a] = true;
					sum += a;
				}
				if(b < 10000){
					sum += b;
					checked[b] = true;
				}
			}
		}
		System.out.println(sum);
	}
	
	public static int d(int tal){
		ArrayList<Integer> p = Prime.getDivisors(Prime.primeFactors(tal, primes));
		int sum = 0;
		for(int i : p)
			sum += i;
		sum -= tal;
		return sum;
	}

}

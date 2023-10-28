import java.util.ArrayList;

import isPrime.Prime;

public class Problem51 {

	public static void main(String[] args) {
		new Problem51().run();
	}
	
	ArrayList<Integer> primes;
	int ans = 0;
	
	public void run(){
		primes = Prime.ESieve(1000000);
		for(int i = 0; i < primes.size(); i++){
			if(ans != 0) break;
			int p = primes.get(i);
			int len = (int)Math.log10(p)+1;
			if(len > 1){
				boolean[] marked = new boolean[len];
				for(int j = 1; j < len; j++){
					recurs(marked, len-1, j, 0, p);
				}
			}
		}
		
		System.out.println(ans);
	}
	
	public void recurs(boolean[] marked, int length, int toMark, int start, int prime){
		if(toMark == 0){
			check(marked, prime);
			return;
		}
		
		for(int i = start; i < length; i++){
			if(!marked[i]){
				marked[i] = true;
				recurs(marked, length, toMark-1, i+1, prime);
				marked[i] = false;
			}
		}
	}
	
	public void check(boolean[] marked, int prime){
		int[] split = new int[marked.length];
		int pp = prime;
		for(int i = split.length-1; i >= 0; i--){
			split[i] = pp % 10;
			pp /= 10;
		}
		
		int fel = marked[0] ? 2 : 3;
		int liten = 0;
		for(int replace = 0; replace <= 9; replace++){
			if(replace == 0 && marked[0]) continue;
			pp = prime;
			for(int i = 0; i < marked.length; i++){
				if(marked[i]){
					int shift = (int)Math.pow(10, marked.length-i-1);
					pp -= split[i]*shift;
					pp += replace*shift;
				}
			}
			if(!Prime.isPrimeDumb(pp, primes)){
				fel--;
			}else{
				if(pp < liten || liten == 0) liten = pp;
			}
			if(fel <= 0){
				return;
			}
		}
		
		ans = liten;
	}

}

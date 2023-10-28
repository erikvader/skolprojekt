import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import isPrime.Prime;

public class Problem49 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		new Problem49().run();
		System.out.println((System.nanoTime() - t)/1000000.0);
	}
	
	HashSet<String> visitedPerms;
	ArrayList<String> perms;
	ArrayList<Integer> primes;
	
	public void run(){
		visitedPerms = new HashSet<String>();
		perms = new ArrayList<String>();
		primes = Prime.ESieve(10000);
		
		for(int i = 0; i < primes.size(); i++){
			if((int)Math.log10(primes.get(i)) < 3) continue;
			String s = Integer.toString(primes.get(i));
			if(visitedPerms.add(s)){
				perms.clear();
				recurs("", 4, s, new boolean[4]);
				if(perms.size() < 3) continue;
				Collections.sort(perms);
				int d = -1, l = 0, last = Integer.parseInt(perms.get(0));
				for(int j = 1; j < perms.size(); j++){
					int newTal = Integer.parseInt(perms.get(j));
					if(d == -1 || newTal - last != d){
						l = 1;
						d = newTal - last;
					}else if(newTal - last == d){
						l++;
					}
					
					if(l == 2){
						String ans = "";
						for(int k = j; k >= j-2; k--){
							ans = perms.get(k) + ans;
						}
						System.out.println(ans);
						i = 10000;
						break;
					}
					
					last = newTal;
				}
			}
		}
	}
	
	public void recurs(String prev, int length, String word, boolean[] inUse){
		if(prev.length() == length){
			if(Prime.isPrimeDumb(Integer.parseInt(prev), primes)){
				perms.add(prev);
				visitedPerms.add(prev);
			}
			
			return;
		}
		
		HashSet<Character> visited = new HashSet<>();
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(prev.length() == 0 && c == '0') continue;
			if(!inUse[i]){
				if(visited.add(c)){
					inUse[i] = true;
					recurs(prev+c, length, word, inUse);
					inUse[i] = false;
				}
			}
		}
		
	}

}

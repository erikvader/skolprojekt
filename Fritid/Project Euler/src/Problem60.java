import java.util.ArrayList;
import java.util.HashMap;

import isPrime.Prime;

public class Problem60 {

	public static void main(String[] args) {
		//new Problem60().run();
		new Problem60().run2();
	}
	
	public ArrayList<Integer> primes;
	public int svar = Integer.MAX_VALUE;
	public HashMap<Long, HashMap<Long, Boolean>> hash;
	
	public void run(){
		primes = Prime.ESieve(10000);
		hash = new HashMap<Long, HashMap<Long, Boolean>>();
		
		long[] tal = new long[5];
		/*
		int len = 4;
		while(svar == 0){
			System.out.println(len);
			test(len, 1, 0, tal);
			len++;
		}
		*/
		test2(tal, 1, 0);
		System.out.println("svaret är: "+svar);
	}
	
	public void test2(long[] tal, int start, int ite){
		if(ite == 5){
			int s = (int)(tal[0] + tal[1] + tal[2] + tal[3] + tal[4]);
			if(s < svar) svar = s;
		}else{
			for(int i = start; i < primes.size(); i++){
				tal[ite] = primes.get(i);
				if(worksWithRest(tal, ite)){
					test2(tal, i+1, ite+1);
				}
			}
		}
	}
	
	//för seg. testar samma grejer för många gånger
	public boolean test(int len, int start, int ite, long[] tal){
		if(ite == 4){
			tal[4] = primes.get(len+1);
			if(worksWithRest(tal, 4)){
				svar = (int)(tal[0] + tal[1] + tal[2] + tal[3] + tal[4]);
				return true;
			}
		}else{
			for(int i = start; i <= len - (3-ite); i++){
				tal[ite] = primes.get(i);
				if(worksWithRest(tal, ite)){
					if(test(len, i+1, ite+1, tal)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean worksWithRest(long[] tal, int i){
		for(int j = i-1; j >= 0; j--){
			if(!isConcat(tal[i], tal[j])){
				return false;
			}
		}
		return true;
	}
	
	public boolean isConcat(long a, long b){
		long aa = Math.min(a, b);
		long bb = Math.max(a, b);
		
		if(!hash.containsKey(aa) || !hash.get(aa).containsKey(bb)){
			boolean v = false;
			if(Prime.isPrimeDumbSmarter(concat(a, b),  primes) && 
				Prime.isPrimeDumbSmarter(concat(b, a),  primes)
				){
				v = true;
			}
			
			if(!hash.containsKey(aa)){
				HashMap<Long, Boolean> temp = new HashMap<Long, Boolean>();
				hash.put(aa, temp);
			}
			
			hash.get(aa).put(bb, v);
			
			return v;
		}else{
			return hash.get(aa).get(bb);
		}
	}
	
	public long concat(long a, long b){
		return a * (int)Math.pow(10, ((int)Math.log10(b)+1)) + b;
	}
	
	////////////lösning 2////////
	/*
	 * Generate a boolean matrix of what primes concatinate with what primes to form another prime. This is usefull because we can cram 32 boolean values on every int. 

Use recursion to implement a depth first search to find a list of five primes which concatenate. The boolean matrix can be used to quickly determine potential candidates, using a bitwise logical AND.

Anyways, it's not that much better because it still runs in O(n^5) time.
	 */
	
	public void run2(){
		primes = Prime.ESieve(10000);
		boolean[][] grid = new boolean[primes.size()][primes.size()];
		
		System.out.println("bygger grid");
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < i; j++){
				boolean v = false;
				if(
						Prime.isPrimeDumbSmarter(concat(primes.get(i), primes.get(j)),  primes) && 
						Prime.isPrimeDumbSmarter(concat(primes.get(j), primes.get(i)),  primes)
					){
						v = true;
					}
				grid[i][j] = v;
			}
		}
		
		System.out.println("utför dfs");
		int[] used = new int[5];
		for(int j = 1; j < primes.size(); j++){
			used[0] = j;
			if(dfs(j, grid, 1, used)) break;
		}
		
		System.out.println(svar);
	}
	
	public boolean dfs(int x, boolean[][] grid, int depth, int[] used){
		if(depth == 5){
			svar = 0;
			for(int i = 0; i < used.length; i++){
				svar += primes.get(used[i]);
			}
			return true;
		}
		
		for(int i = 0; i <= x && i < grid.length; i++){
			used[depth] = i;
			if(grid[x][i] && works(grid, depth, used)){
				if(dfs(i, grid, depth+1, used)) return true;
			}
		}
		
		return false;
	}
	
	public boolean works(boolean[][] grid, int depth, int[] used){
		for(int i = 0; i < depth; i++){
			if(!grid[Math.max(used[depth], used[i])][Math.min(used[depth], used[i])]){
				return false;
			}
		}
		return true;
	}

}

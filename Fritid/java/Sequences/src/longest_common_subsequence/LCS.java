package longest_common_subsequence;

import java.util.HashSet;
import java.util.Scanner;

/**
 * tar två sekvenser och letar efter den längsta subsekvensen som båda har. (de behöver inte komma direkt efter varandra)
 * ex.
 * gac och agcat har ac, ga och gc
 * @author Erik Rimskog
 *
 */
public class LCS {

	char[] a;
	char[] b;
	int[][] grid;
	
	public static void main(String[] args) {
		new LCS().run();
	}
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Skriv in en sekvens A:");
		String input = scan.nextLine();
		a = input.toCharArray();
		
		System.out.println("Skriv in en sekvens B:");
		input = scan.nextLine();
		b = input.toCharArray();

		scan.close();
		
		buildGrid();
		
		System.out.println("Den längsta subsekvensen är: "+getLength()+" lång");
		
		String[] all = findAll();
		
		System.out.println("alla gemensamma subsekvenser:");
		for(String s : all){
			System.out.println(s);
		}
		
	}
	
	private void buildGrid(){
		grid = new int[a.length+1][b.length+1]; //row (a), column (b)
		
		for(int r = 1; r < grid.length; r++){
			for(int c = 1; c < grid[r].length; c++){
				if(a[r-1] == b[c-1]){
					grid[r][c] = grid[r-1][c-1] + 1;
				}else{
					grid[r][c] = Math.max(grid[r-1][c], grid[r][c-1]);
				}
			}
		}
	}
	
	private int getLength(){
		int y = grid.length;
		int x = grid[0].length;
		return grid[y-1][x-1];
	}
	
	private String[] findAll(){
		HashSet<String> ans = new HashSet<String>();
		
		int y = grid.length;
		int x = grid[0].length;
		
		recurs(y-1, x-1, ans, "");
		
		return ans.toArray(new String[ans.size()]);
	}
	
	private void recurs(int r, int c, HashSet<String> ans, String cur){
		if(r == 0 || c == 0){
			ans.add(cur);
		}else{
			if(a[r-1] == b[c-1]){ //vi hoppade snett, de är lika
				String curcur = a[r-1] + cur;
				recurs(r-1, c-1, ans, curcur);
			}else{
				int top = grid[r-1][c];
				int left = grid[r][c-1];
				
				if(top == left){
					recurs(r-1, c, ans, cur);
					recurs(r, c-1, ans, cur);
				}else{
					int max = Math.max(top, left);
					if(max == top){
						recurs(r-1, c, ans, cur);
					}else{
						recurs(r, c-1, ans, cur);
					}
				}
			}
		}
	}

}

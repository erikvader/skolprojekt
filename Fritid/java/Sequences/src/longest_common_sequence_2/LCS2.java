package longest_common_sequence_2;

import java.util.HashSet;
import java.util.Scanner;

/**
 * tar två sekvenser och letar efter den längsta subsekvensen som båda har. (varje char måste komma direkt efter varandra)
 * ex.
 * erik och ri ger ri
 * 
 * @author Erik Rimskog
 *
 */
public class LCS2 {

	char[] a;
	char[] b;
	int[][] grid;
	int highest;
	
	public static void main(String[] args) {
		new LCS2().run();
	}
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Skriv in en sekvens A:");
		String input = scan.nextLine();
		a = input.toCharArray();
		
		System.out.println("Skriv in en sekvens B:");
		input = scan.nextLine();
		b =  input.toCharArray();

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
		highest = 0;
		
		for(int r = 1; r < grid.length; r++){
			for(int c = 1; c < grid[r].length; c++){
				if(a[r-1] == b[c-1]){
					grid[r][c] = grid[r-1][c-1] + 1;
				}else{
					//grid[r][c] = Math.max(grid[r-1][c], grid[r][c-1]);
				}
				if(grid[r][c] > highest) highest = grid[r][c];
			}
		}
	}
	
	private int getLength(){
		return highest;
	}
	
	private String[] findAll(){
		HashSet<String> ans = new HashSet<String>();
		
		for(int r = 1; r < grid.length; r++){
			for(int c = 1; c < grid[r].length; c++){
				if(grid[r][c] == highest){
					recurs(r, c, ans, "");
				}
			}
		}
		
		return ans.toArray(new String[ans.size()]);
	}
	
	private void recurs(int r, int c, HashSet<String> ans, String cur){
		if(grid[r][c] == 0){
			ans.add(cur);
		}else{
			String curcur = a[r-1] + cur;
			recurs(r-1, c-1, ans, curcur);
		}
	}

}

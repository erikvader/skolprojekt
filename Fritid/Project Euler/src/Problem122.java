import java.util.ArrayList;
import java.util.Arrays;

public class Problem122 {

	//min egna som inte funkade
	//0, 1, 2, 2, 3, 3, 4, 3, 4, 4, 4, 4, 5, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 
	//sum: 1410
	
	//här implementerar jag lösningen från mathblog. (fast denna funkar inte av ngn anledning, #orka)
	
	public static void main(String[] args) {
		new Problem122().run();
	}
	
	int bigK = 200; //[1, 200], tal att testa
	int[] ms;
	
	public void run(){
		
		ms = new int[bigK];
		Arrays.fill(ms, Integer.MAX_VALUE);
		search(1, new ArrayList<Integer>(), 0, true);
		
		int sum = 0;
		for(int a : ms){
			System.out.print(Integer.toString(a) + ", ");
			sum += a;
		}
		System.out.println();
		System.out.println("sum: "+Integer.toString(sum));
	}
	
	public void search(int cur, ArrayList<Integer> past, int len, boolean power){
		if(cur > bigK) return;
		past.add(cur);
		if(power) search(cur*2, new ArrayList<Integer>(past), len+1, true);
		if(ms[cur-1] > len) ms[cur-1] = len;
		
		for(int i = 0; i < past.size(); i++){
			search(past.get(i) + cur, new ArrayList<Integer>(past), len+1, false);
		}
		
	}

}

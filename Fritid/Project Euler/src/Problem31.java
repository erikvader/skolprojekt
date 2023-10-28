
public class Problem31 {

	public static int[] mynt = {200, 100, 50, 20, 10, 5, 2, 1};
	public static int antal = 0;
	public static int target = 200;
	
	public static void main(String[] args) {
		long t = System.nanoTime();
		minSolution();
		//dynamiskSolutionFromEnKommentar();
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	public static void minSolution(){
		search(0, 0);
		System.out.println(antal);
	}
	
	//2ms
	public static void search(int index, int sum){
		if(index == mynt.length-1){ //ettorna fyller
			//if(sum == target){
				antal++;
			//}
			return;
		}
		
		int cur = 0;
		while(cur <= target - sum){
			search(index+1, sum+cur);
			cur += mynt[index];
		}
	}
	
	//0.3ms
	public static void dynamiskSolutionFromEnKommentar(){
		int[] ways = new int[target + 1];
		ways[0] = 1;

		for(int coin: mynt)
			for(int j = coin; j <= target; j++)
				ways[j] += ways[j - coin];

		System.out.println("Result: " + ways[target]);
	}

}

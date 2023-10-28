import funktioner.Funktioner;

public class Problem53 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		int counter = 0;
		for(int n = 1; n <= 100; n++){
			for(int k = 0; k <= n; k++){
				if(Funktioner.chooseDouble(n, k) > 1000000)
					counter++;
			}
		}
		
		System.out.println(counter);
		System.out.println((System.nanoTime() - t)/1000000.0);
	}

}

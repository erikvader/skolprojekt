import funktioner.Funktioner;

public class Problem33 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		int numerator = 1, denominator = 1;
		
		for(int n = 10; n <= 99; n++){
			for(int d = n+1; d <= 99; d++){
				if(!(n % 10 == 0 && d % 10 == 0)){ //inte är trivial
					if(kanSimplify(n, d)){
						numerator *= n;
						denominator *= d;
					}
				}
			}
		}
		
		int gcd = Funktioner.GCD(numerator, denominator);
		System.out.println(denominator/gcd);
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	public static boolean kanSimplify(int n, int d){
		double original = n/(double)d;
		int[][] split = new int[2][2];
		split[0][0] = n % 10;
		split[0][1] = d % 10;
		d /= 10;
		n /= 10;
		split[1][0] = n % 10;
		split[1][1] = d % 10;
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				if(split[i][0] == split[j][1]){
					if(split[(i+1)%2][0] / (double)split[(j+1)%2][1] == original){
						return true;
					}
				}
			}
		}
		
		return false;
	}

}

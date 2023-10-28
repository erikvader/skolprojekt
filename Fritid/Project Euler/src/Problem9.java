import funktioner.Funktioner;

public class Problem9 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		//old();
		ny();
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	//24 ms
	public static void old(){
		for(int a = 1; a <= 1000; a++){
			for(int b = 1; b <= a; b++){
				double c = Math.sqrt(a*a+b*b);
				if(c % 1 == 0){ //det är inga decimaler
					if(a + b + c == 1000){
						System.out.println(a+" "+b+" "+c);
						System.out.println(a*b*(int)c);
					}
				}
			}
		}
	}
	
	//1.5 ms
	//använder euclids formel
	public static void ny(){
		int m = 1;
		boolean running = true;
		while(running){
			m++;
			for(int n = 1; n < m; n++){
				if((m - n) % 2 == 0) continue;
				if(Funktioner.GCD(m, n) != 1) continue;
				int a = m*m - n*n;
				int b = 2*m*n;
				int c = m*m + n*n;
				int k = 1;
				while(a+b+c < 1000){
					k++;
					a = (a/(k-1)) * k;
					b = (b/(k-1)) * k;
					c = (c/(k-1)) * k;
				}
				if(a+b+c == 1000){
					System.out.println(a*b*c);
					running = false;
					break;
				}
			}
		}
	}

}


public class Problem45 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		long n = 285;
		long start = n*(n+1)/2;
		boolean running = true;
		while(running){
			n++;
			start += n;
			if(isPenta(start) && isHexa(start)){
				System.out.println(start);
				running = false;
			}
		}
		System.out.println((System.nanoTime()-t)/1000000.0);
	}
	
	public static boolean isPenta(long p){
		long n = (long)Math.round(1/6.0 + Math.sqrt(1/36.0+2*p/3.0));
		return p == n*(3*n-1)/2;
	}
	
	public static boolean isHexa(long h){
		long n = (long)Math.round(1/4.0 + Math.sqrt(1/16.0+h/2.0));
		return h == n*(2*n-1);
	}

}

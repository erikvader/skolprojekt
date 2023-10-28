
public class Problem44 {
	
	public static void main(String[] args){
		long t = System.nanoTime();
		int j=1;
		boolean running = true;
		while(running){
			j++;
			for(int k = 1; k < j; k++){
				int pj = penta(j);
				int pk = penta(k);
				if(isPenta(pj+pk) && isPenta(pj-pk)){
					System.out.println(pj-pk);
					running = false;
					break;
				}
			}
		}
		System.out.println((System.nanoTime() - t)/1000000.0);
	}
	
	public static int penta(int n){
		return n*(3*n-1)/2;
	}
	
	public static boolean isPenta(int p){
		int n = (int)Math.round(1/6.0 + Math.sqrt(1/36.0+2*p/3.0));
		return p == n*(3*n-1)/2;
	}
}

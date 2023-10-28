import isPrime.Prime;

public class Problem58 {

	public static void main(String[] args) {
		int total = 1;
		int primes = 0;
		
		int layer = 2;
		int cur = 1;
		while(primes/(double)total >= 0.1 || total == 1){
			for(int i = 0; i < 4; i++){
				cur += layer;
				if(Prime.isPrimeDumb(cur)) primes++;
			}
			layer += 2;
			total += 4;
		}
		System.out.println(layer-1);
	}

}

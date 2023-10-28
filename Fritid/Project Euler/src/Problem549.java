import java.math.BigInteger;
import java.util.HashMap;

public class Problem549 {
	//för seg

	static HashMap<Long, BigInteger> hash = new HashMap<Long, BigInteger>();
	
	public static void main(String[] args) {
		long t = System.nanoTime();
		System.out.println(S(100000000l));
		System.out.println((System.nanoTime()-t)/1000000000.0+"s");
	}
	
	public static long s(long n){
		long index = 1;
		BigInteger m = BigInteger.valueOf(n);
		while(!fac(index).mod(m).equals(BigInteger.ZERO)){
			index++;
		}
		return index;
	}
	
	public static long S(long n){
		long sum = 0;
		for(long i = 2; i <= n; i++){
			sum += s(i);
		}
		return sum;
	}
	
	public static BigInteger fac(long n){
		if(hash.containsKey(n))
			return hash.get(n);
		
		if(n <= 1){
			return BigInteger.valueOf(1);
		}else{
			BigInteger m = fac(n-1).multiply(BigInteger.valueOf(n));
			hash.put(n, m);
			return m;
		}
	}

}

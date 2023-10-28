import java.math.BigInteger;

public class Problem20 {

	public static void main(String[] args) {
		BigInteger bi = fac(BigInteger.valueOf(100));
		String s = bi.toString();
		int tot = 0;
		for(int i = 0; i < s.length(); i++){
			tot += Integer.parseInt(s.substring(i, i+1));
		}
		System.out.println(tot);
	}
	
	public static BigInteger fac(BigInteger bi){
		if(bi.compareTo(BigInteger.valueOf(1)) == 0){
			return BigInteger.valueOf(1);
		}else{
			return bi.multiply(fac(bi.subtract(BigInteger.valueOf(1))));
		}
	}

}

import java.math.BigInteger;

public class Problem48 {

	//använda modulo är mycket smartare
	public static void main(String[] args) {
		BigInteger daTal = new BigInteger("0");
		for(int i = 1; i <= 1000; i++){
			BigInteger power = new BigInteger(Integer.toString(i));
			power = power.pow(i);
			daTal = daTal.add(power);
		}
		String talet = daTal.toString();
		System.out.println(talet.substring(talet.length()-10));
	}

}

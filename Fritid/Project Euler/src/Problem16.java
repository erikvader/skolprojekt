import java.math.BigInteger;

public class Problem16 {

	public static void main(String[] args) {
		BigInteger siffra = new BigInteger("2");
		siffra = siffra.pow(1000);
		String s = siffra.toString();
		int summa = 0;
		for(int i = 0; i < s.length(); i++){
			summa += Integer.parseInt(s.substring(i, i+1));
		}
		System.out.println(summa);
	}

}

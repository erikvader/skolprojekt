import java.math.BigInteger;

public class Problem56 {

	public static void main(String[] args) {
		BigInteger cur;
		int best = 0;
		for(int a = 1; a < 100; a++){
			cur = new BigInteger(Integer.toString(a));
			for(int b = 1; b < 100; b++){
				String tal = cur.pow(b).toString();
				int summa = 0;
				for(int i = 0; i < tal.length(); i++){
					summa += Integer.parseInt(tal.substring(i, i+1));
				}
				if(summa > best) best = summa;
			}
		}
		
		System.out.println(best);
	}

}

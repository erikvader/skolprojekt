import java.math.BigInteger;

public class Problem25 {

	public static void main(String[] args) {
		long tid = System.nanoTime();
		BigInteger a = new BigInteger("1"), b = new BigInteger("1"), c;
		int index = 2;
		while(b.toString().length() < 1000){
			index++;
			c = b;
			b = a.add(b);
			a = c;
		}
		System.out.println(index);
		System.out.println((System.nanoTime()-tid)/1000000.0);
	}
	
	

}

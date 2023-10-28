import java.math.BigInteger;
import java.util.Scanner;

public class Problem13 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		BigInteger sum = new BigInteger("0");
		for(int i = 0; i < 100; i++){
			sum = sum.add(new BigInteger(scan.nextLine()));
		}
		scan.close();
		System.out.println("resultat: "+sum);
	}

}

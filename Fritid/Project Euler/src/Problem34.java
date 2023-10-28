
public class Problem34 {

	public static void main(String[] args) {
		int[] fac = new int[10];
		fac[0] = 1;
		for(int i = 1; i < fac.length; i++){
			fac[i] = i * fac[i-1];
		}
		
		int totalSum = 0;
		for(int i = 3; i < 2500000; i++){
			int sum = 0;
			int ii = i;
			while(ii > 0){
				sum += fac[ii % 10];
				ii /= 10;
			}
			if(sum == i)
				totalSum += i;
		}
		
		System.out.println(totalSum);
	}

}

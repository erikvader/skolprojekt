package subarray;

import java.util.Arrays;

public class Subarray {

	public static void main(String[] args) {
		new Subarray().run();
	}
	
	@SuppressWarnings("unused")
	public void run(){
		int[] in = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7}; //7, 10, 43
		int[] in2 = {-13, -3, -25, -20, -3, -16, -23, -18, -20, -12, -1, -200000, -1};
		int[] out = new int[3];
		maximumSum(in2, out);
		System.out.println(Arrays.toString(out));
	}
	
	/**
	 * returnar en array {i, j} med index för subarrayen [i..j] (i och j inclusive) i a som har störst summa.
	 * <br>
	 * kör i O(n)
	 * <br>
	 * om a är tom händer ingenting med out
	 * <br>
	 * om allt är negativt returnas den största siffran
	 * <br>
	 * om det finns flera subarrayer med samma summa returnas den första.
	 * 
	 * @param a - en array med positiva eller negativa ints
	 * @param out - {i, j, sum} (startindex, slutindex (inclusive) och summan arrayen har)
	 */
	public void maximumSum(int[] a, int[] out){
		if(a.length == 0) return;
		
		int i = 0; //startindex vi börjar att räkna ifrån
		int buf = a[0]; //nuvarande summa
		int maxBuf = a[0]; //bästa summan
		int maxi = 0, maxj = 0; //index där maxBuf förekom
		for(int k = 1; k < a.length; k++){
			buf += a[k];
			
			if(buf < a[k]){ //TODO fatta varför denna funkar. en grej är nog att ifall den nuvarande är större än summan vi har arbetat på så börjar vi om på k för att kunna få en större summa.
				buf = a[k];
				i = k;
			}
			
			if(buf > maxBuf){
				maxBuf = buf;
				maxi = i;
				maxj = k;
			}
		}
		
		//sätter out med resultaten
		out[0] = maxi;
		out[1] = maxj;
		out[2] = maxBuf;
	}

}

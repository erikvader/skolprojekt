package countingSort;

import java.util.Arrays;

public class Counting {

	public static void main(String[] args) {
		new Counting().run();
	}
	
	public void run(){
		int[] in = {2, 5, 3, 0, 2, 3, 0, 3};
		int[] ut = new int[in.length];
		countingSort(in, ut, 5);
		System.out.println(Arrays.toString(ut));
	}
	
	/**
	 * tar en lista med ints som alla �r mellan [0..k] och sorterar dem i output p� O(n+k). Denna alg �r stable. tar O(k) i minne.
	 * @param input
	 * @param output
	 * @param k
	 */
	public void countingSort(int[] input, int[] output, int k){
		//r�knar hur m�nga det �r
		int[] c = new int[k+1];
		for(int i = 0; i < input.length; i++){
			c[input[i]]++;
		}
		
		//r�knar ut hur m�nga tal som �r mindre som �r f�re talet i.
		//om det finns 7 tal mindre �n i s� m�ste i hamna p� plats 8
		for(int i = 1; i < c.length; i++){
			c[i] += c[i-1];
		}
		
		//sorterar
		for(int i = output.length-1; i >= 0; i--){
			output[c[input[i]]-1] = input[i];
			c[input[i]]--; //ifall vi har flera av samma tal s� kommer de att hamna p� en egen plats.
		}
	}

}

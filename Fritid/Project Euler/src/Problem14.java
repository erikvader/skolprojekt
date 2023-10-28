
public class Problem14 {

	public static void main(String[] args) {
		int longestSeq = -1;
		int longestIndex = -1;
		for(int i = 1; i <= 1000000; i++){
			int t = getSequenceLength(i);
			if(t >= longestSeq){ 
				longestSeq = t;
				longestIndex = i;
			}
			System.out.println(i);
		}
		System.out.println(longestIndex + " " + longestSeq);
	}
	
	public static int getSequenceLength(int numba){
		long t = numba;
		int counter = 1;
		while(t != 1){
			if(t % 2 == 0){
				t /= 2;
			}else{
				t = 3*t + 1;
			}
			counter++;
		}
		return counter;
	}

}

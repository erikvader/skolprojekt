
public class Problem39 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		int bestP = 0;
		int bestSolutions = 0;
		for(int p = 3+4+5; p <= 1000; p++){
			int numSolutions = 0;
			for(int a = 1; a <= p; a++){
				for(int b = 1; b <= a; b++){
					int c = p-a-b;
					if(c <= 0) break;
					if(a*a + b*b == c*c){
						//System.out.println("yay "+a+" "+b+" "+c);
						numSolutions++;
					}
				}
			}
			if(numSolutions > bestSolutions){
				bestP = p;
				bestSolutions = numSolutions;
			}
		}
		System.out.println(bestP);
		System.out.println((System.nanoTime()-t)/1000000.0);
	}

}

import funktioner.Funktioner;

public class Problem40 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		int target = 1;
		int finalTarget = 1000000;
		int curSiffra = 0;
		int pos = 1;
		int sum = 1;
		int curSiffraCopy;
		
		while(target <= finalTarget){
			curSiffra++;
			curSiffraCopy = Funktioner.flipNumber(curSiffra*10+1);
			while(curSiffraCopy > 1){
				if(pos == target){
					target *= 10;
					sum *= curSiffraCopy % 10;
				}
				pos++;
				curSiffraCopy /= 10;
			}
		}
		System.out.println(sum);
		System.out.println((System.nanoTime()-t)/1000000.0);
	}

}

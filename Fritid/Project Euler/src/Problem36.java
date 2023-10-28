
public class Problem36 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		int sum = 0;
		for(int i = 1; i < 1000000; i+=2){ //jämna binära tal slutar alltid på 0
			int dec = i;
			int decInv = 0;
			while(dec > 0){
				decInv *= 10;
				decInv += dec % 10;
				dec /= 10;
			}
			
			if(i != decInv) continue;
			
			int bin = i;
			int binInv = 0;
			while(bin > 0){
				binInv = binInv << 1;
				binInv += bin & 1;
				bin = bin >> 1;
			}
			
			if(i == binInv)
				sum += i;
		}
		System.out.println(sum);
		System.out.println((System.nanoTime()-t)/1000000.0);
	}

}

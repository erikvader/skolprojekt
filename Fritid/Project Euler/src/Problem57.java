import java.math.BigInteger;

public class Problem57 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		new Problem57().run();
		System.out.println((System.nanoTime() - t)/1000000.0);
	}
	
	public void run(){
		int svar = 0;
		for(int i = 0; i < 1000; i++){
			BigInteger[] tal = new BigInteger[2];
			re(tal, i, 0);
			if(tal[0].toString().length() > tal[1].toString().length()){
				svar++;
			}
		}
		
		System.out.println(svar);
	}
	
	public void re(BigInteger[] tal, int length, int index){
		if(index == length){
			tal[0] = index == 0 ? new BigInteger("3") : new BigInteger("5");
			tal[1] = new BigInteger("2");
			return;
		}else{
			re(tal, length, index+1);
			BigInteger temp = tal[0];
			tal[0] = tal[1];
			tal[1] = temp;
			tal[0] = tal[0].add(tal[1].multiply((index == 0 ? BigInteger.valueOf(1) : BigInteger.valueOf(2))));
			
		}
	} 

}

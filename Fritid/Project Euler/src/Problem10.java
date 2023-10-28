import isPrime.Prime;

public class Problem10 {

	//142913828922
	public static void main(String[] args) {
		long sum = 2;
		for(int i = 3; i < 2000000; i+=2){
			if(Prime.isPrime(i)){
				if(Prime.isPrimeDumb(i)){ //dubbelkoll
					sum += i;
				}
			}
		}
		System.out.println(sum);
	}

}

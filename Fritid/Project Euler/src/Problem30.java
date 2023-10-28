
public class Problem30 {

	//upper limit 355000
	public static void main(String[] args) {
		int res = 0;
		for(int i = 2; i < 355000; i++){
			int sum = 0;
			int ii = i;
			while(ii > 0){
				int digit = ii % 10;
				ii /= 10;
				int d = 1;
				for(int j = 0; j < 5; j++){
					d *= digit;
				}
				sum += d;
			}
			
			if(sum == i){
				res += i;
			}
		}
		
		System.out.println(res);
	}

}

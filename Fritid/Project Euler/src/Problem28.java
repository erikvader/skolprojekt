
public class Problem28 {

	public static void main(String[] args) {
		int sum = 1;
		int layer = 2;
		int cur = 1;
		while(layer+1 <= 1001){
			for(int i = 0; i < 4; i++){
				cur += layer;
				sum += cur;
			}
			layer += 2;
		}
		System.out.println(sum);
	}

}
